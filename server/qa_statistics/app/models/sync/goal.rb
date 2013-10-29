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
     

    def self.search_invalid_goal
      goals = Sync::Goal.collection.aggregate([{"$project" => {"eq" => {"$cond" => [{"$gt" => ["$st", "$et"]}, 1, 0]}}}, {"$match" => {"eq" => 1}}])
      debugger
      arrayResult = []
      goals.each do |goal|
        arrayResult << goal + "<\/br\>"
      end
      reuslt = arrayResult.join("\n")
    end

  end
end

#var result = db.goals.aggregate([ {$match: {}}, {$project: {eq: { $cond: [ { $gt: [ '$st', '$et' ] }, 1, 0 ] } }}, { $match: { eq: 1 } } ])
#result.result.length