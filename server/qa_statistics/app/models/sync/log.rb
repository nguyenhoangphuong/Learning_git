module Sync

  class Log
    include Mongoid::Document
    store_in collection: 'logs'

    field :st,    as: :start_time, type: Integer
    field :et,    as: :end_time, type: Integer
    field :seq,   as: :sequence_number, type: Integer
    field :seri,  as: :serial_number_string, type: String
    field :fw,    as: :firmware_revision_string, type: String
    field :log, type: String
    field :data, type: Hash
    field :isok,  as: :is_successful, type: Integer
    field :x_st,  as: :extracting_status, type: Integer
    field :s3_p,  as: :s3_folder_path, type: String
    field :ip,    as: :ip_address, type: String

    SYNC_FAILED_ERRORS = {
      -1 => "Unknown",
      -2 => "Disconnected unexpectedly",
      -3 => "Command returned error",
      -4 => "Network request error",
      -5 => "Timeout", # deprecated from MVP19
      -6 => "RSSI out of range",
      -7 => "Serial number null",
      -8 => "Disconnected by user",
      -9 => "No Shine Found", # deprecated from MVP19
      -10 => "User refused all Shine",
      -11 => "No unlinked Shine found",
      -12 => "Not compatiable",
      -13 => "CB fail to connect",
      -14 => "Fail to handshake", # deprecated from MVP19
      -15 => "BG sync: Too many files",
      -16 => "BG sync: Not enough time",
      -17 => "BG sync: User is active",
      -18 => "No Shine with qualified RSSI found",
      -20 => "Timeout: Scanning",
      -21 => "Timeout: Connecting",
      -22 => "Timeout: Handshake",
      -23 => "Timeout: Disconnected",
      -24 => "Timeout: Command",
      -30 => "CB handshake fail: Service discovery",
      -31 => "CB handshake fail: Characteristic discovery",
      -32 => "CB handshake fail: Characteristic update",
      -40 => "OAD: Connecting timeout after OAD reset",
      -41 => "OAD: Waiting for EOF timeout"
    }

    SYNC_INCOMPLETE_ERROR_CODES = [-4, -6, -8, -10, -11, -12, -15, -16, -17, -18]
    SYNC_DISCOVERY_ERROR_CODES = [-9, -20, -30, -31, -32, -40]

    SYNC_MODES = {
      1 => "Manual sync",
      2 => "Quiet sync",
      3 => "Background sync"
    }

    DEVICE_INFOS = {
      "iPod 5" => ["iPod5,1"],
      "iPhone 4S" => ["iPhone4,1"],
      "iPad Mini" => ["iPad2,5", "iPad2,6"],
      "iPhone 5" => ["iPhone5,1", "iPhone5,2"],
      "iPhone 5C" => ["iPhone5,3", "iPhone5,4"],
      "iPhone 5S" => ["iPhone6,1", "iPhone6,2"],
      "iPad" => ["iPad3,1", "iPad3,2", "iPad3,3", "iPad3,4", "iPad3,5", "iPad3,6"]
    }

    IOS_VERSIONS = [
      "6.1.3", "7.0", "7.0.1", "7.0.2" 
    ]

    def self.search_logs_by_criteria(isok, from_time, to_time, app_version, sync_mode, ios_version, failure_reasons, device_infos)
      # build search criteria
      result = self.where(isok: isok)
      # result = self.and(device: serial_number_string) if serial_number_string.present?
      result = result.and(:end_time.gte => from_time) if from_time.present?
      result = result.and(:start_time.lte => to_time) if to_time.present?
      result = result.and(:'data.appVersion' => app_version) if app_version.present?
      result = result.and(:'data.syncMode' => sync_mode) if sync_mode.present?
      result = result.in(:'data.iosVersion' => ios_version) if ios_version.present?
      result = result.in(:'data.failureReason' => failure_reasons) if failure_reasons.present?

      result = result.in(:'data.deviceInfo' => device_infos) if device_infos.present?
      result
    end

    def self.get_statistics_by_criteria(isok, from_time, to_time, app_version, sync_mode, ios_versions, failure_reasons, device_infos)
      # get total number of failures by app version and time range
      total_failure = search_logs_by_criteria(isok, from_time, to_time, app_version, sync_mode, nil, nil, nil).count

     
      ios_versions = ["all"] if ios_versions.nil?
      failure_reasons = ["all"] if failure_reasons.nil?
      device_infos = ["all"] if device_infos.nil?
    
      # build sample message
      result = []
      ios_versions.each do |ios_version|
        str1 = ios_version != "all" ? "Criteria with ios version " + ios_version + ", " : "Criteria with " 
        device_infos.each do |device_info|
          str2 = device_info != "all" ? str1 + "device " + device_info + ", " : str1
          failure_reasons.each do |failure_reason|
            
            str3 = failure_reason != "all" ? str2 + "failure code " + failure_reason.to_s + " and failure reason " + SYNC_FAILED_ERRORS[failure_reason] + ": " : str2
            failure = search_logs_by_criteria(isok, from_time, to_time, app_version, sync_mode, ios_version != "all" ? [ios_version] : nil, failure_reason != "all" ? [failure_reason] : nil, device_info != "all" ? [device_info] : nil).count
            percentage = (failure.to_f / total_failure) * 100
            str3 << failure.to_s + " failures, " + percentage.to_s + " \%" + "\<\/br\>"
            result << str3
          end
        end
      end
      @statistics = result.join("\n")
    end

    def self.calculate_statistics_from_logs(sync_logs)
      json = DEVICE_INFOS.map { |k, v| v.map {|model| {model => k} } }.flatten.inject(&:merge).to_json
    #  debugger
      map = %Q{
        function() {
          var failure_reasons = JSON.parse('#{SYNC_FAILED_ERRORS.to_json}');
          var reason = failure_reasons[this.data.failureReason] || "Unknown reason";
          var failureCode = this.data.failureReason;
          var iosVersion = this.data.iosVersion;
          var device_models = JSON.parse('#{json}');
          var deviceInfo = device_models[this.data.deviceInfo] || "Unknown device info";
          emit({ failureCode: failureCode, failureReason: reason, iosVersion: iosVersion, deviceInfo: deviceInfo}, 1);
        }
      }

      reduce = %Q{
        function(key, values) {
          return Array.sum(values);
        }
      }
      
      mr_result = sync_logs.map_reduce(map, reduce).out(inline: true).to_a
      # each entry of mr_result is a json like this 
      # {"_id"=>{"failureCode" => -8, failureReason"=>"Disconnected by user", "iosVersion"=>"6.1.2", "deviceInfo"=>"iPod 5"}, "value"=>1.0}
    
      result = []
     
      total_failures = sync_logs.count
      mr_result.each do |entry|
        failures = entry["value"].to_i
        percentage = (entry["value"] * 100 / total_failures).round(2)
        result << [entry["_id"]["failureCode"].to_i, entry["_id"]["failureReason"], entry["_id"]["iosVersion"], entry["_id"]["deviceInfo"], failures, percentage]
      end 


      #debugger
      result
    end 

    def self.calculate_statistics_by_criteria(isok, from_time, to_time, app_version, sync_mode, ios_versions, failure_reasons, device_infos)
       total_logs = search_logs_by_criteria(isok, from_time, to_time, app_version, sync_mode, ios_versions, failure_reasons, device_infos)
       statisticsFromLogs = Sync::Log.calculate_statistics_from_logs(total_logs)
       arrayResult = []
       arrayResult << "Total failures: " + total_logs.count.to_s + "\<\/br\>"
       arrayResult << build_statistics_result(statisticsFromLogs)
       result = arrayResult.join("\n")
    end 

    def self.build_statistics_result(statisticsFromLogs)
      # each entry of statisticsFromLogs is an array like this [-8, "Disconnected by user","6.1.2","iPod 5",1,0.28]
      result = []
      statisticsFromLogs.each do |entry|
        temp = "Failure code: " + entry[0].to_s + ", failure reason: \"" + entry[1] + "\", iOS version: " + entry[2] + ", device model: " + 
            entry[3] + ", number of failures: " + entry[4].to_s + ", failure rate: " + entry[5].to_s + "\%" + "\<\/br\>"
        result << temp
      end 
      result
    end
  end
end
