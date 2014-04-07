class GoalFilterController < ApplicationController
  def index
  	 @logs = Sync::Goal.search_invalid_goal
  end

end
