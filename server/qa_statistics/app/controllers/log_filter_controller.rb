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
  	errorCodes = params[:error_codes].presence ? params[:error_codes] : nil
  	deviceInfos = params[:device_infos].presence ? params[:device_infos] : nil
  	iosVersions = params[:ios_versions].presence ? params[:ios_versions] : nil
  	syncMode = params[:syncMode].presence ? params[:syncMode].to_i : nil
  	
  	params.each do |key,value|
  		Rails.logger.warn "Param #{key}: #{value}"
	end

  @logs = Sync::Log.calculate_statistics_by_criteria(0, startTime, endTime, appVersion, syncMode, iosVersions, errorCodes ? errorCodes.map(&:to_i) : nil, deviceInfos ? Sync::Log::DEVICE_INFOS.slice(*deviceInfos).values.flatten : nil)
   end
end
