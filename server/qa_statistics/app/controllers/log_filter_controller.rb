class LogFilterController < ApplicationController
  def index
  	#render text: Sync::Log.last.to_json
  	#render json: Sync::Log.last
  	#render json: Sync::Log.search_logs_by_criteria(0, nil, nil, nil, nil, 2, nil, -9)
  	@logs = Sync::Log.search_logs_by_criteria(0, nil, nil, nil, nil, 2, nil, -9)
  end
end
