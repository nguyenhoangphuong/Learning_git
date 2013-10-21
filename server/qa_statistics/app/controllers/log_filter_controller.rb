class LogFilterController < ApplicationController
  def index
  	#render text: Sync::Log.last.to_json
  	#render json: Sync::Log.last
  	#render json: Sync::Log.search_logs_by_criteria(0, nil, nil, nil, nil, 2, nil, -9)
  	#@logs = Sync::Log.search_logs_by_criteria(0, nil, nil, nil, nil, 2, nil, -9).count
  end

  def post
  	startTime = params[:startTime].presence.try(&:to_i)
  	endTime = params[:endTime].presence.try(&:to_i)
  	appVersion = params[:appVersion].presence.try(&:to_s)
  	errorCodes = params[:error_codes] ? params[:error_codes] : nil
  	deviceInfos = params[:device_infos] ? params[:device_infos] : nil
  	iosVersions = params[:ios_versions] ? params[:ios_versions] : nil
  	syncMode = params[:syncMode] ? params[:syncMode].to_i : nil

  	
  	params.each do |key,value|
  		Rails.logger.warn "Param #{key}: #{value}"
	end

#@logs = [startTime, endTime, appVersion, errorCode[1], deviceInfo]
#  def self.search_logs_by_criteria(isok, from_time, to_time, app_version, sync_mode, ios_version, failure_reasons, device_infos)
#@logs = Sync::Log.search_logs_by_criteria(0, startTime, endTime, appVersion, syncMode, iosVersions, errorCodes ? errorCodes.map(&:to_i) : nil, Sync::Log::DEVICE_INFOS.slice(*deviceInfos).values.flatten)
  @logs = Sync::Log.get_statistics_by_criteria(0, startTime, endTime, appVersion, syncMode, iosVersions, errorCodes ? errorCodes.map(&:to_i) : nil, Sync::Log::DEVICE_INFOS.slice(*deviceInfos).values.flatten)
  end
end
