module Sync
  ##
  #
  # A {Sync::Goal} represents a goal for the user. The user should have one goal per day
  #
  class Goal
    include Mongoid::Document
    store_in collection: 'goals'

    field :val,   as: :goal_value, type: Float
    field :st,    as: :start_time, type: Integer
    field :et,    as: :end_time, type: Integer
    field :lv,    as: :level, type: Integer
    field :tz,    as: :time_zone_offset_in_seconds, type: Integer
    field :prgd,  as: :progress_data, type: Hash
    field :tap3c, as: :triple_tap_type_changes, type: Array
     
    field :uid, as: :user_id
    belongs_to :user, foreign_key: :uid, index: true


    def self.search_invalid_goal
      goalIds = Sync::Goal.collection.aggregate([{"$project" => {"eq" => {"$cond" => [{"$gt" => ["$st", "$et"]}, 1, 0]}}}, {"$match" => {"eq" => 1}}]).to_a

      ids = []
      goalIds.each do |goal|
        ids << goal["_id"]
      end


      goals = search_goals_by_ids(ids)
      map = "function() { var keys = new Object(); "
      map << "keys.uid = this.uid; " 
       map << "emit(keys, 1); }"
    
      reduce = %Q{
        function(key, values) {
          return Array.sum(values);
        }
      }

      mr_result = goals.map_reduce(map, reduce).out(inline: true).to_a
      result = []
      mr_result.each do |entry|
        sum = entry["value"].to_i
        tmp = ""
        tmp << entry["_id"]["uid"].to_s + "\t"
        tmp << sum.to_s 
        result << tmp
      end
      result
    end

    def self.search_goals_by_ids(ids)

      # build search criteria
      result = self.where(:goal_value.gte => 0)
      result = result.in(:_id => ids)
      result
  
    end
  end
end

#var result = db.goals.aggregate([ {$match: {}}, {$project: {eq: { $cond: [ { $gt: [ '$st', '$et' ] }, 1, 0 ] } }}, { $match: { eq: 1 } } ])
#result.result.length