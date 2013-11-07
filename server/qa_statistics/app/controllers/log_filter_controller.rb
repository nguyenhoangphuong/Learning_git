class LogFilterController < ApplicationController
  def index
  	endTime = DateTime.new(Time.now.year, Time.now.month, Time.now.day, 0, 0, 0).to_i #current day, at 12am, timezone GMT+0
  	dt = Time.now - (7 * 24 * 60 * 60) #1 week ago
  	startTime = DateTime.new(dt.year, dt.month, dt.day, 0, 0, 0).to_i #1 week before current day, at 12am, timezone GMT+0
  	@timeStamps = [startTime, endTime]
  end

  def post
  	startTime = params[:startTime].presence.try(&:to_i)
  	endTime = params[:endTime].presence.try(&:to_i)
  	appVersion = params[:appVersion].presence.try(&:to_s)
  	firmware = params[:firmware].presence.try(&:to_s)
  	errorCodes = params[:error_codes].presence ? params[:error_codes] : nil #params[:errorCodes].count == 1 && 
  	deviceInfos = params[:device_infos].presence ? params[:device_infos] : nil
  	iosVersions = params[:ios_versions].presence ? params[:ios_versions] : nil
  	syncMode = params[:syncMode].presence && params[:syncMode].to_i != -1 ? params[:syncMode].to_i : nil
    action = params[:submit_button].presence ? params[:submit_button] : nil

  	# select box includes blank value, so we must filter that blank value
  	errorCodes = !errorCodes.nil? && !(errorCodes.count ==1 && errorCodes.first.empty?) ? errorCodes : nil
  	deviceInfos = !deviceInfos.nil? && !(deviceInfos.count ==1 && deviceInfos.first.empty?) ? deviceInfos : nil
  	iosVersions = !iosVersions.nil? && !(iosVersions.count ==1 && iosVersions.first.empty?) ? iosVersions : nil
  	
  	params.each do |key,value|
  		Rails.logger.warn "Param #{key}: #{value}"
	  end

    statistics_params = {}
    statistics_params["isok"] = 0
    statistics_params["fromTime"] = startTime
    statistics_params["toTime"] = endTime
    statistics_params["appVersion"] = appVersion
    statistics_params["syncMode"] = syncMode
    statistics_params["iosVersions"] = iosVersions
    statistics_params["errorCodes"] = errorCodes ? errorCodes.map(&:to_i) : nil
    statistics_params["deviceInfos"] = deviceInfos ? Sync::Log::DEVICE_INFOS.slice(*deviceInfos).values.flatten : nil
    statistics_params["firmware"] = firmware
    statistics_params["showLastCommand"] = false

    if action == "Search"
      @logs = Sync::Log.calculate_statistics_by_criteria(statistics_params)
    elsif action == "Export"
     Sync::Log.export_statistics_by_criteria(statistics_params)
     @logs = "File is exported!"
    end
  end
end
