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

    def self.search_logs_by_criteria(isok, from_time, to_time, serial_number_string, app_version, sync_mode, ios_version, failure_reason)
      # build search criteria
      result = self.where(isok: isok)
      result = self.and(seri: serial_number_string) if serial_number_string.present?
      result = result.and(:end_time.gte => from_time) if from_time.present?
      result = result.and(:start_time.lte => to_time) if to_time.present?
      result = result.and(:'data.appVersion' => app_version) if app_version.present?
      result = result.and(:'data.syncMode' => sync_mode) if sync_mode.present?
      result = result.and(:'data.iosVersion' => ios_version) if ios_version.present?
      result = result.and(:'data.failureReason' => failure_reason) if failure_reason.present?

      #failureReason":0,"iosVersion" syncMode
      #result.desc(:start_time).limit(max_number_of_objects_for_search).to_a
      result.desc(:start_time).to_a
    end

  end
end
