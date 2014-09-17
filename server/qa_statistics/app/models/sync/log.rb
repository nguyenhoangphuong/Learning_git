require 'csv'
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
    field :plf,   as: :platform, type: String

    field :uid, as: :user_id
    belongs_to :user, foreign_key: :uid, index: true

    PENDING_STATUS = 0
    EXTRACTING_STATUS = 1
    EXTRACTED_STATUS = 2
    FAILED_STATUS = 3
    DEBUG_LOG_FILENAME = 'debug_log.txt'
    HARDWARE_LOG_FILENAME = 'hardware_log'
  
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
      -33 => "Cannot get activity data",
      -34 => "Cannot get configurations",
      -35 => "Cannot set configurations",
      -36 => "Cannot activate Flash device",
      -40 => "OAD: Connecting timeout after OAD reset",
      -41 => "OAD: Waiting for EOF timeout",
      -42 => "OAD failed",
      -50 => "No available serial number to reset",
      -51 => "Cannot load firmware to reset SN",
      -52 => "Cannot reset SN",
      -53 => "Companion app: Cannot launch companion app",
      -54 => "Companion app: Get empty data",
      -55 => "Companion app: Cannot get data",
      -56 => "Companion app: Cannot set point",
      -57 => "Companion app: Cannot set step",
      -58 => "Companion app: Cannot set goal",
      -70 => "Cannot create and push new linked pedometer to server"
    }

    SYNC_FAILED_ERRORS_ANDROID = {
      0 => "DEFAULT",
      1 => "SCAN_FAILED_NO_SHINE_FOUND",
      20 => "SERVER_VERIFY_FAILED_NETWORK",
      24 => "CONNECT_DEVICE_INVALID_SERIAL_NUMBER",
      25 => "CONNECT_DEVICE_FAILED_NO_MORE_SHINE_TO_CONNECT",
      26 => "CONNECT_DEVICE_FAILED_LINKED_TO_ANOTHER_ACCOUNT",
      27 => "CONNECT_DEVICE_FAILED_SHINE_DEACTIVATED",
      28 => "CONNECT_DEVICE_FAILED_MUTIPLE_DETECTED_LINKED_TO_OTHERS",
      29 => "CONNECT_DEVICE_FAILED_TIMEOUT",
      30 => "CONNECT_DEVICE_FAILED",
      31 => "PLAY_ANIMATION_FAILED",
      32 => "PLAY_ANIMATION_FAILED_TIMEOUT",
      35 => "GETTING_CONFIG_FAILED_TIMEOUT",
      36 => "GETTING_CONFIG_FAILED",
      40 => "GETTING_ACTIVITY_FAILED_TIMEOUT",
      41 => "GETTING_ACTIVITY_FAILED",
      45 => "SETTING_DEVICE_CONFIG_FAILED_TIMEOUT",
      46 => "SETTING_DEVICE_CONFIG_FAILED",
      47 => "CONNECT_DEVICE_AFTER_OADING_FAILED_TIMEOUT",
      48 => "CONNECT_DEVICE_AFTER_OADING_FAILED",
      50 => "OAD_FAILED_TIMEOUT",
      51 => "OAD_FAILED",
      52 => "PUSH_PEDOMETER_FAILED",
      55 => "SUCCESSFULLY_COMPLETELY",
      56 => "SUCCESSFULLY_BUT_OAD_FAILED",
      57 => "OAD_TO_SPECIAL_FW_FAILED",
      58 => "OAD_TO_SPECIAL_FW_FAILED_TIMEOUT",
      59 => "GET_NEW_SERIAL_NUMBER_NETWORK_FAILED",
      60 => "GET_NEW_SERIAL_NUMBER_GENERATED_5_SN",
      61 => "GET_NEW_SERIAL_NUMBER_CANNOT_GENERATE",
      62 => "CONNECT_DEVICE_AFTER_SPECIAL_OADING_FAILED_TIMEOUT",
      63 => "CONNECT_DEVICE_AFTER_SPECIAL_OADING_FAILED",
      64 => "RESET_SERIAL_NUMBER_FAILED",
      65 => "RESET_SERIAL_NUMBER_FAILED_TIMEOUT",
      66 => "INFORM_SERVER_NETWORK_FAILED",
      67 => "DUPLICATE_BUT_LINKED",
      68 => "BG_SYNC_TOO_MANY_FILES",
      69 => "BG_SYNC_WHEN_USER_ACTIVE",
      70 => "BG_SYNC_RSSI_TOO_WEAK",
      71 => "VENUS_ACTIVATE_FAILED",
      72 => "VENUS_ACTIVATE_FAILED_TIMEOUT",
      73 => "VENUS_GET_ACTIVATION_STATE_FAILED",
      74 => "VENUS_GET_ACTIVATION_STATE_FAILED_TIMEOUT"
    }

    SYNC_INCOMPLETE_ERROR_CODES = [-4, -6, -8, -10, -11, -12, -15, -16, -17, -18]
    SYNC_DISCOVERY_ERROR_CODES = [-9, -20, -30, -31, -32, -40]

    SYNC_MODES = {
      -1 => "All",
      1 => "Manual sync",
      2 => "Quiet sync",
      3 => "Background sync"
    }

    DEVICE_INFOS = {
      "Unsupported devices" => ["iPhone2,1", "iPhone3,1", "iPhone3,2", "iPhone3,3", "iPod4,1", 
        "iPad2,1", "iPad2,2", "iPad2,3", "iPad2,4"],
      "iPod 5" => ["iPod5,1"],
      "iPhone 4S" => ["iPhone4,1", "iPhone4,2"],
      "iPhone 5" => ["iPhone5,1", "iPhone5,2"],
      "iPhone 5C" => ["iPhone5,3", "iPhone5,4"],
      "iPhone 5S" => ["iPhone6,1", "iPhone6,2"],
      "iPad" => ["iPad3,1", "iPad3,2", "iPad3,3", "iPad3,4", "iPad3,5", "iPad3,6", "iPad4,2", "iPad4,5"],
      "iPad Mini" => ["iPad2,5", "iPad2,6", "iPad2,7"]
    }

    IOS_VERSIONS = [
      "5.0", "5.0.1",
      "5.1", "5.1.1", 
      "6.0", "6.0.1", "6.0.2", 
      "6.1", "6.1.1", "6.1.2", "6.1.3", "6.1.4", 
      "7.0", "7.0.1", "7.0.2", "7.0.3", "7.0.4",
      "7.1"
    ]

    def self.search_logs_by_criteria(isok, from_time, to_time, app_version, sync_mode, ios_version, failure_reasons, device_infos, firmware)
      # build search criteria
      result = self.where(isok: isok)
      result = result.and(:start_time.gte => from_time) if from_time.present?
      result = result.and(:start_time.lte => to_time) if to_time.present?
      result = result.and(:firmware_revision_string => firmware) if firmware.present?
      result = result.and(:'data.appVersion' => app_version) if app_version.present?
      result = result.and(:'data.syncMode' => sync_mode) if sync_mode.present?
      result = result.in(:'data.iosVersion' => ios_version) if ios_version.present?
      result = result.in(:'data.failureReason' => failure_reasons) if failure_reasons.present?
      result = result.in(:'data.deviceInfo' => device_infos) if device_infos.present?
      result
    end

    def self.search_logs_by_criteria_android(isok, from_time, to_time, android_app_version, android_os_version, android_phone_model, android_phone_manufacturer, android_sdk, firmware, failure_reasons)
      # build search criteria
      result = self.where(isok: isok)
      result = result.and(:start_time.gte => from_time) if from_time.present?
      result = result.and(:start_time.lte => to_time) if to_time.present?
      result = result.and(:firmware_revision_string => firmware) if firmware.present?
      result = result.and(:'data.androidAppVersion' => android_app_version) if android_app_version.present?
      result = result.and(:'data.androidOsVersion' => android_os_version) if android_os_version.present?
      result = result.and(:'data.androidPhoneModel' => android_phone_model) if android_phone_model.present?
      result = result.and(:'data.androidPhoneManufacturer' => android_phone_manufacturer) if android_phone_manufacturer.present?
      result = result.and(:'data.androidSdk' => android_sdk) if android_sdk.present?
      result = result.in(:'data.failureReason' => failure_reasons) if failure_reasons.present?
      result
    end

    def self.search_total_logs_ios(startTime, endTime, isok)
      result = self.where(isok: isok)
      result = result.and(:start_time.gte => startTime) if startTime.present?
      result = result.and(:start_time.lte => endTime) if endTime.present?
      result = result.and(:'data.iosVersion'.exists => true)
      result
    end

    def self.search_total_logs_android(startTime, endTime, isok)
      result = self.where(isok: isok)
      result = result.and(:start_time.gte => startTime) if startTime.present?
      result = result.and(:start_time.lte => endTime) if endTime.present?
      result = result.and(:'data.androidOsVersion'.exists => true)
      result
    end

    def self.build_map_reduce_query_android(sync_logs, has_failure_reasons)
      map = "function() { var keys = new Object(); "
      # map << "keys.androidOSVersion = this.data.androidOSVersion; " if has_android_os_version
      # map << "keys.androidPhoneModel = this.data.androidPhoneModel;" if has_android_phone_model
      # map << "keys.androidSdk = this.data.androidSdk;" if has_android_sdk
      map << "keys.failureCode = this.data.failureReason; " if has_failure_reasons
      map << "emit(keys, 1); }"

      reduce = %Q{
        function(key, values) {
          return Array.sum(values);
        }
      }

      mr_result = sync_logs.map_reduce(map, reduce).out(inline: true).to_a
      mr_result
    end

    def self.build_map_reduce_query(sync_logs, has_ios_version, has_failure_reasons, has_device_infos, showLastCommand)
      deviceJson = DEVICE_INFOS.map { |k, v| v.map {|model| {model => k} } }.flatten.inject(&:merge).to_json

      # This is what deviceJson looks like
      # "{\"iPod5,1\":\"iPod 5\",\"iPhone4,1\":\"iPhone 4S\",\"iPad2,5\":\"iPad Mini\"
      # ,\"iPad2,6\":\"iPad Mini\",\"iPhone5,1\":\"iPhone 5\",\"iPhone5,2\":\"iPhone 5\"
      # ,\"iPhone5,3\":\"iPhone 5C\",\"iPhone5,4\":\"iPhone 5C\",\"iPhone6,1\":\"iPhone 5S\"
      # ,\"iPhone6,2\":\"iPhone 5S\",\"iPad3,1\":\"iPad\",\"iPad3,2\":\"iPad\",\"iPad3,3\":\"iPad\"
      # ,\"iPad3,4\":\"iPad\",\"iPad3,5\":\"iPad\",\"iPad3,6\":\"iPad\"}"

      map = "function() { var keys = new Object(); "
      map << "keys.lastCommand = this.data.lastCommand; " if showLastCommand
      map << "keys.failureCode = this.data.failureReason; " if has_failure_reasons
      map << "keys.iosVersion = this.data.iosVersion; " if has_ios_version
      map << %Q{
        var device_models = JSON.parse('#{deviceJson}');
        keys.deviceInfo = device_models[this.data.deviceInfo] || "Unknown device info";
      } if has_device_infos
      map << "emit(keys, 1); }"

      reduce = %Q{
        function(key, values) {
          return Array.sum(values);
        }
      }

      mr_result = sync_logs.map_reduce(map, reduce).out(inline: true).to_a
      mr_result
    end

    def self.calculate_statistics_from_logs_android(sync_logs, has_failure_reasons)
      mr_result = build_map_reduce_query_android(sync_logs, has_failure_reasons)

      result = []
      total_failures = 0
      mr_result.each do |entry|
          total_failures += entry["value"].to_i
      end
      if has_failure_reasons

      #build label
        tmpArray = []
        tmpArray << "Failure code" if has_failure_reasons
        tmpArray << "Failure reason" if has_failure_reasons
        tmpArray << "Number of failures"
        tmpArray << "Failure rate"
        result << tmpArray
        mr_result.each do |entry|
          failures = entry["value"].to_i
          percentage = (entry["value"] * 100 / total_failures).round(2)
          tmpArray = []
          tmpArray << entry["_id"]["failureCode"].to_i if has_failure_reasons
          tmpArray << SYNC_FAILED_ERRORS_ANDROID[entry["_id"]["failureCode"].to_i] || "Unknown reason" if has_failure_reasons
          tmpArray << failures
          tmpArray << percentage
          result << tmpArray
        end        
      end
      return total_failures, result
    end

    def self.calculate_statistics_from_logs(sync_logs, has_ios_version, has_failure_reasons, has_device_infos, showLastCommand)

      mr_result = build_map_reduce_query(sync_logs, has_ios_version, has_failure_reasons, has_device_infos, showLastCommand)

      # each entry of mr_result is a json like this 
      # {"_id"=>{"failureCode" => -8, failureReason"=>"Disconnected by user", "iosVersion"=>"6.1.2", "deviceInfo"=>"iPod 5"}, "value"=>1.0}
    
      result = []
      total_failures = 0

      mr_result.each do |entry|
        total_failures += entry["value"].to_i
      end
      if has_ios_version or has_device_infos or has_failure_reasons or showLastCommand
       

        #build label
        tmpArray = []
        tmpArray << "Failure code" if has_failure_reasons
        tmpArray << "Failure reason" if has_failure_reasons
        tmpArray << "iOS version" if has_ios_version
        tmpArray << "Device model" if has_device_infos
        tmpArray << "Last command" if showLastCommand
        tmpArray << "Number of failures"
        tmpArray << "Failure rate"
        result << tmpArray

        mr_result.each do |entry|
          failures = entry["value"].to_i
          percentage = (entry["value"] * 100 / total_failures).round(2)
          tmpArray = []
          tmpArray << entry["_id"]["failureCode"].to_i if has_failure_reasons
          tmpArray << SYNC_FAILED_ERRORS[entry["_id"]["failureCode"].to_i] || "Unknown reason" if has_failure_reasons
          tmpArray << "\'" + entry["_id"]["iosVersion"] if has_ios_version # the result can be parsed into csv file, so 7.0 will become 7, add "'" to avoid it
          tmpArray << entry["_id"]["deviceInfo"] if has_device_infos
          if showLastCommand
            lastCommand = entry["_id"]["lastCommand"].nil? ? "Last command is nil" : entry["_id"]["lastCommand"].empty? ? "Last command is empty" : entry["_id"]["lastCommand"]
            tmpArray << lastCommand
          end
          tmpArray << failures
          tmpArray << percentage
          result << tmpArray
        end 
      end
      return total_failures, result
    end 

    def self.calculate_statistics_by_criteria(statistics_params)
      total_logs = search_logs_by_criteria(statistics_params["isok"], statistics_params["fromTime"], statistics_params["toTime"], 
        statistics_params["appVersion"], statistics_params["syncMode"], statistics_params["iosVersions"], statistics_params["errorCodes"], 
        statistics_params["deviceInfos"], statistics_params["firmware"])
      total_failures, statisticsFromLogs = Sync::Log.calculate_statistics_from_logs(total_logs, !statistics_params["iosVersions"].nil?, !statistics_params["errorCodes"].nil?, !statistics_params["deviceInfos"].nil?, statistics_params["showLastCommand"])
      arrayResult = []
      arrayResult << "Total failures: " + total_failures.to_s + "\<\/br\>"
      arrayResult << build_statistics_result(statisticsFromLogs)
      result = arrayResult.join("\n")
    end 

    def self.calculate_statistics_by_criteria_android(statistics_params)
      total_logs = search_logs_by_criteria_android(statistics_params["isok"], statistics_params["fromTime"], statistics_params["toTime"], 
        statistics_params["androidAppVersion"], statistics_params["androidOSVersion"], statistics_params["androidPhoneModel"], statistics_params["androidPhoneManufacturer"], 
        statistics_params["androidSdk"], statistics_params["firmware"], statistics_params["errorCodes"])
      total_failures, statisticsFromLogs = Sync::Log.calculate_statistics_from_logs_android(total_logs, !statistics_params["errorCodes"].nil?)
      arrayResult = []
      arrayResult << "Total failures: " + total_failures.to_s + "\<\/br\>"
      arrayResult << build_statistics_result(statisticsFromLogs)
      result = arrayResult.join("\n")
    end 

    def self.export_statistics_by_criteria(statistics_params)
      total_logs = search_logs_by_criteria(statistics_params["isok"], statistics_params["fromTime"], statistics_params["toTime"], 
          statistics_params["appVersion"], statistics_params["syncMode"], statistics_params["iosVersions"], statistics_params["errorCodes"], 
        statistics_params["deviceInfos"], statistics_params["firmware"])
      total_failures, statisticsFromLogs = Sync::Log.calculate_statistics_from_logs(total_logs, !statistics_params["iosVersions"].nil?, !statistics_params["errorCodes"].nil?, !statistics_params["deviceInfos"].nil?, statistics_params["showLastCommand"])
      row = []
      row << "Total failures: "
      row << total_failures.to_s
      fileName = "failed_sync_statistics_" + Time.now.to_i.to_s + ".csv"
      CSV.open(fileName, "wb") do |csv|
        csv << row
        statisticsFromLogs.each do |entry|
          csv << entry
        end
      end 
    end

     def self.export_statistics_by_criteria_android(statistics_params)
       total_logs = search_logs_by_criteria_android(statistics_params["isok"], statistics_params["fromTime"], statistics_params["toTime"], 
        statistics_params["androidAppVersion"], statistics_params["androidOSVersion"], statistics_params["androidPhoneModel"], statistics_params["androidPhoneManufacturer"], 
        statistics_params["androidSdk"], statistics_params["firmware"], statistics_params["errorCodes"])
      total_failures, statisticsFromLogs = Sync::Log.calculate_statistics_from_logs_android(total_logs, !statistics_params["errorCodes"].nil?)
      row = []
      row << "Total failures: "
      row << total_failures.to_s
      fileName = "failed_sync_statistics_android_" + Time.now.to_i.to_s + ".csv"
      CSV.open(fileName, "wb") do |csv|
        csv << row
        statisticsFromLogs.each do |entry|
          csv << entry
        end
      end 
    end

    def self.calculate_total_logs_android(startTime, endTime, isok)
      total_logs = search_total_logs_android(startTime, endTime, isok)
      result = total_logs.count.to_s +  "\<\/br\>"
      result
    end

    def self.calculate_total_logs_ios(startTime, endTime, isok)
      total_logs = search_total_logs_ios(startTime, endTime, isok)
      result = total_logs.count.to_s +  "\<\/br\>"
      result
    end

    def self.build_statistics_result(statisticsFromLogs)
      # each entry of statisticsFromLogs is an array like this [-8, "Disconnected by user","6.1.2","iPod 5",1,0.28], 
      # but the first entry is used to store labels
      result = []
      if statisticsFromLogs.count > 1
        count = statisticsFromLogs.first.count
        i = 0
        statisticsFromLogs.each do |entry|
          temp = entry.join("\t")
          temp += i == 0 ? "<\/br\>" : "\%\<\/br\>"
          i += 1
          result << temp
        end 
        result 
      end
      result
    end
  end
end
