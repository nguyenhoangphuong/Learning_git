class AndroidLogFilterController < ApplicationController
  def index
  	endTime = DateTime.new(Time.now.year, Time.now.month, Time.now.day, 0, 0, 0).to_i #current day, at 12am, timezone GMT+0
  	dt = Time.now - (7 * 24 * 60 * 60) #1 week ago
  	startTime = DateTime.new(dt.year, dt.month, dt.day, 0, 0, 0).to_i #1 week before current day, at 12am, timezone GMT+0
  	@timeStamps = [startTime, endTime]
  end

  def post
  	startTime = params[:startTime].presence.try(&:to_i)
  	endTime = params[:endTime].presence.try(&:to_i)
  	androidAppVersion = params[:androidAppVersion].presence.try(&:to_s)
  	firmware = params[:firmware].presence.try(&:to_s)
  	errorCodes = params[:error_codes].presence ? params[:error_codes] : nil #params[:errorCodes].count == 1 && 
  	androidPhoneModel = params[:androidPhoneModel].presence.try(&:to_s)
  	androidOSVersion = params[:androidOSVersion].presence.try(&:to_s)
  	androidSdk = params[:androidSdk].presence.try(&:to_s)
    androidPhoneManufacturer = params[:androidPhoneManufacturer].presence.try(&:to_s)
    action = params[:submit_button].presence ? params[:submit_button] : nil

  	# select box includes blank value, so we must filter that blank value
  	errorCodes = !errorCodes.nil? && !(errorCodes.count ==1 && errorCodes.first.empty?) ? errorCodes : nil
  	
  	params.each do |key,value|
  		Rails.logger.warn "Param #{key}: #{value}"
	  end

    statistics_params = {}
    statistics_params["isok"] = 0
    statistics_params["fromTime"] = startTime
    statistics_params["toTime"] = endTime
    statistics_params["androidAppVersion"] = androidAppVersion
    statistics_params["androidPhoneModel"] = androidPhoneModel
    statistics_params["androidOSVersion"] = androidOSVersion
    statistics_params["errorCodes"] = errorCodes ? errorCodes.map(&:to_i) : nil
    statistics_params["androidSdk"] = androidSdk 
    statistics_params["firmware"] = firmware
    statistics_params["androidPhoneManufacturer"] = androidPhoneManufacturer

    if action == "Search"
      @logs = Sync::Log.calculate_statistics_by_criteria_android(statistics_params)
    elsif action == "Export"
     Sync::Log.export_statistics_by_criteria_android(statistics_params)
     @logs = "File is exported!"
    elsif action == "Total success"
      @logs = Sync::Log.calculate_total_logs_android(startTime, endTime, 1)
    elsif action == "Total failure"
      @logs = Sync::Log.calculate_total_logs_android(startTime, endTime, 0)
    end
  end
end
