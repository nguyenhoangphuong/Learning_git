package com.misfit.ta.backend.data;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class SyncData 
{
	// static methods
	public static String emptyData = "{\"timeFrames\":[],\"goals\":[],\"activities\":[]}";
    public static String fullData = "{\"profile\":{\"lastUpdated\":1357534714,\"serverId\":\"50c567edcaedd062400001dd\",\"localId\":\"51AE1CBC-BA8D-4D64-9D0E-39BF279BF408\",\"deleted\":0,\"weight\":190.0,\"height\":70.0,\"unit\":0,\"gender\":0,\"dateOfBirth\":345834000,\"name\":null,\"latestVersion\":\"4\",\"goalLevel\":5},\"timeFrames\":[{\"lastUpdated\":1356577020,\"serverId\":\"50dbbfc6caedd055dc000016\",\"localId\":\"C8FD5E3D-7310-4C83-8F20-A3048A1AD912\",\"deleted\":0,\"endTime\":1356627599,\"startTime\":1356541200,\"timeZoneOffsetInSeconds\":25200,\"type\":\"PTDailyTimeFrame\",\"goals\":[{\"serverId\":\"50dbbfc6caedd055dc000018\",\"localId\":\"048C90F4-9BCE-4B82-B051-C826359BC53D\",\"type\":\"PTDailyGoal\"},{\"serverId\":\"50dbbfc6caedd055dc000019\",\"localId\":\"25BE8C4C-A802-4C11-A725-FC0528FDF292\",\"type\":\"PTDailyGoal\"},{\"serverId\":\"50dbbfc6caedd055dc00001a\",\"localId\":\"A626DBB2-C67C-4CC5-9BEA-9C8D6F2B2712\",\"type\":\"PTDailyGoal\"}]}],\"goals\":[{\"lastUpdated\":1356592121,\"serverId\":\"50dbbfc6caedd055dc000018\",\"localId\":\"048C90F4-9BCE-4B82-B051-C826359BC53D\",\"deleted\":0,\"goalValue\":0.0,\"intensityLevel\":4,\"progress\":0.0,\"activitiesCount\":2,\"goalLevel\":0,\"type\":\"PTDailyGoal\",\"timeFrame\":{\"lastUpdated\":1356577020,\"serverId\":\"50dbbfc6caedd055dc000016\",\"localId\":\"C8FD5E3D-7310-4C83-8F20-A3048A1AD912\",\"deleted\":0,\"endTime\":1356627599,\"startTime\":1356541200,\"timeZoneOffsetInSeconds\":25200,\"type\":\"PTDailyTimeFrame\",\"goals\":[{\"serverId\":\"50dbbfc6caedd055dc000018\",\"localId\":\"048C90F4-9BCE-4B82-B051-C826359BC53D\",\"type\":\"PTDailyGoal\"},{\"serverId\":\"50dbbfc6caedd055dc000019\",\"localId\":\"25BE8C4C-A802-4C11-A725-FC0528FDF292\",\"type\":\"PTDailyGoal\"},{\"serverId\":\"50dbbfc6caedd055dc00001a\",\"localId\":\"A626DBB2-C67C-4CC5-9BEA-9C8D6F2B2712\",\"type\":\"PTDailyGoal\"}]},\"activities\":[{\"serverId\":\"50dbbfe6caedd055dc00001c\",\"localId\":\"9673D60D-C024-41F3-A4AD-C2DC5F033D6A\",\"type\":\"PTActivity\"},{\"serverId\":\"50dbf3f9caedd01f22000014\",\"localId\":\"A662D2E8-DF4A-451A-AC0A-9B39D533B3D0\",\"type\":\"PTActivity\"}]},{\"lastUpdated\":1356592090,\"serverId\":\"50dbbfc6caedd055dc000019\",\"localId\":\"25BE8C4C-A802-4C11-A725-FC0528FDF292\",\"deleted\":0,\"goalValue\":2.0,\"intensityLevel\":2,\"progress\":2.0,\"activitiesCount\":3,\"goalLevel\":0,\"type\":\"PTDailyGoal\",\"timeFrame\":{\"lastUpdated\":1356577020,\"serverId\":\"50dbbfc6caedd055dc000016\",\"localId\":\"C8FD5E3D-7310-4C83-8F20-A3048A1AD912\",\"deleted\":0,\"endTime\":1356627599,\"startTime\":1356541200,\"timeZoneOffsetInSeconds\":25200,\"type\":\"PTDailyTimeFrame\",\"goals\":[{\"serverId\":\"50dbbfc6caedd055dc000018\",\"localId\":\"048C90F4-9BCE-4B82-B051-C826359BC53D\",\"type\":\"PTDailyGoal\"},{\"serverId\":\"50dbbfc6caedd055dc000019\",\"localId\":\"25BE8C4C-A802-4C11-A725-FC0528FDF292\",\"type\":\"PTDailyGoal\"},{\"serverId\":\"50dbbfc6caedd055dc00001a\",\"localId\":\"A626DBB2-C67C-4CC5-9BEA-9C8D6F2B2712\",\"type\":\"PTDailyGoal\"}]},\"activities\":[{\"serverId\":\"50dbbfc6caedd055dc000017\",\"localId\":\"05DD862D-78FD-4959-8CC6-CA0568E9BA3F\",\"type\":\"PTActivity\"},{\"serverId\":\"50dbea21caedd00fb8000012\",\"localId\":\"E90B7344-C351-43B8-943C-297B89B669D6\",\"type\":\"PTActivity\"},{\"serverId\":\"50dbf3dbcaedd01f22000012\",\"localId\":\"771C4CE8-2772-461B-8D12-C791DC7DE31C\",\"type\":\"PTActivity\"}]},{\"lastUpdated\":1356578129,\"serverId\":\"50dbbfc6caedd055dc00001a\",\"localId\":\"A626DBB2-C67C-4CC5-9BEA-9C8D6F2B2712\",\"deleted\":0,\"goalValue\":1.0,\"intensityLevel\":3,\"progress\":0.0,\"activitiesCount\":0,\"goalLevel\":null,\"type\":\"PTDailyGoal\",\"timeFrame\":{\"lastUpdated\":1356577020,\"serverId\":\"50dbbfc6caedd055dc000016\",\"localId\":\"C8FD5E3D-7310-4C83-8F20-A3048A1AD912\",\"deleted\":0,\"endTime\":1356627599,\"startTime\":1356541200,\"timeZoneOffsetInSeconds\":25200,\"type\":\"PTDailyTimeFrame\",\"goals\":[{\"serverId\":\"50dbbfc6caedd055dc000018\",\"localId\":\"048C90F4-9BCE-4B82-B051-C826359BC53D\",\"type\":\"PTDailyGoal\"},{\"serverId\":\"50dbbfc6caedd055dc000019\",\"localId\":\"25BE8C4C-A802-4C11-A725-FC0528FDF292\",\"type\":\"PTDailyGoal\"},{\"serverId\":\"50dbbfc6caedd055dc00001a\",\"localId\":\"A626DBB2-C67C-4CC5-9BEA-9C8D6F2B2712\",\"type\":\"PTDailyGoal\"}]},\"activities\":[]}],\"activities\":[{\"lastUpdated\":1356578788,\"serverId\":\"50dbbfe6caedd055dc00001c\",\"localId\":\"9673D60D-C024-41F3-A4AD-C2DC5F033D6A\",\"deleted\":0,\"activityType\":1,\"endTime\":1356609036,\"startTime\":1356607596,\"timeZoneOffset\":25200.0,\"bipedalCount\":9717.0,\"type\":\"PTActivity\",\"dailyGoal\":{\"serverId\":\"50dbbfc6caedd055dc000018\",\"localId\":\"048C90F4-9BCE-4B82-B051-C826359BC53D\",\"type\":\"PTDailyGoal\"}},{\"lastUpdated\":1356592121,\"serverId\":\"50dbf3f9caedd01f22000014\",\"localId\":\"A662D2E8-DF4A-451A-AC0A-9B39D533B3D0\",\"deleted\":0,\"activityType\":2,\"endTime\":1356606853,\"startTime\":1356604093,\"timeZoneOffset\":25200.0,\"bipedalCount\":8540.0,\"type\":\"PTActivity\",\"dailyGoal\":{\"serverId\":\"50dbbfc6caedd055dc000018\",\"localId\":\"048C90F4-9BCE-4B82-B051-C826359BC53D\",\"type\":\"PTDailyGoal\"}},{\"lastUpdated\":1356578756,\"serverId\":\"50dbbfc6caedd055dc000017\",\"localId\":\"05DD862D-78FD-4959-8CC6-CA0568E9BA3F\",\"deleted\":0,\"activityType\":1,\"endTime\":1356598760,\"startTime\":1356594320,\"timeZoneOffset\":25200.0,\"bipedalCount\":897.0,\"type\":\"PTActivity\",\"dailyGoal\":{\"serverId\":\"50dbbfc6caedd055dc000019\",\"localId\":\"25BE8C4C-A802-4C11-A725-FC0528FDF292\",\"type\":\"PTDailyGoal\"}},{\"lastUpdated\":1356589600,\"serverId\":\"50dbea21caedd00fb8000012\",\"localId\":\"E90B7344-C351-43B8-943C-297B89B669D6\",\"deleted\":0,\"activityType\":3,\"endTime\":1356567840,\"startTime\":1356566640,\"timeZoneOffset\":25200.0,\"bipedalCount\":1018.0,\"type\":\"PTActivity\",\"dailyGoal\":{\"serverId\":\"50dbbfc6caedd055dc000019\",\"localId\":\"25BE8C4C-A802-4C11-A725-FC0528FDF292\",\"type\":\"PTDailyGoal\"}},{\"lastUpdated\":1356592090,\"serverId\":\"50dbf3dbcaedd01f22000012\",\"localId\":\"771C4CE8-2772-461B-8D12-C791DC7DE31C\",\"deleted\":0,\"activityType\":2,\"endTime\":1356596014,\"startTime\":1356592654,\"timeZoneOffset\":25200.0,\"bipedalCount\":6809.0,\"type\":\"PTActivity\",\"dailyGoal\":{\"serverId\":\"50dbbfc6caedd055dc000019\",\"localId\":\"25BE8C4C-A802-4C11-A725-FC0528FDF292\",\"type\":\"PTDailyGoal\"}}]}";

    public static SyncData getDefault()
    {
    	return new SyncData(0, fullData);
    }
    
    public static SyncData getEmpty()
    {
    	return new SyncData(0, emptyData);
    }
    
    
    // fields
    public Double timestamp;
    public String objects;
    
    public JSONObject json;
    
    // constructor
    public SyncData(double timestamp, String objects)
    {
    	this.timestamp = timestamp;
    	this.objects = objects;
    }
    
    // methods
    public String getValue(String key)
    {
    	String s = objects;
    	key = "\"" + key + "\":";
    	
    	int index = s.indexOf(key);
    	int start = index + key.length();
    	int end = s.indexOf(",", start);
    	
    	if(index >= 0) {
    		String result = s.substring(start, end);
    		if (result != null) {
    		    result = result.replace("\"", "");
    		}
    		return result;
    	}
    	
    	return null;
    }
    
    public void setValue(String key, Object value)
    {
    	String s = objects;
    	key = "\"" + key + "\":";
    	
    	int index = s.indexOf(key);
    	int start = index + key.length();
    	int end = s.indexOf(",", start);
    	
    	if(index >= 0)
    	{
    		String newStr = s.substring(0, start);
    		newStr += "\"" + value + "\"";
    		newStr += s.substring(end);
    		
    		objects = newStr;
    	}
    }
       
    public String getString()
    {
    	return objects;
    }

    public void changeLastUpdated(boolean increase)
    {
    	String s = objects;
    	String key = "\"" + "lastUpdated" + "\":";
    	
    	int start = -1;
    	int end = -1;
    	
    	while(true)
    	{
	    	int index = s.indexOf(key, end + 1);
	    	start = index + key.length();
	    	end = s.indexOf(",", start);
	    	    	
	    	if(index < 0)
	    		break;

	    	String sub = s.substring(start, end).replace("\"", "");
	    	long num = Long.parseLong(sub);
	    	num = increase ? ++num : --num;

    		String newStr = s.substring(0, start);
    		newStr += "\"" + num + "\"";
    		newStr += s.substring(end);
   		
    		s = newStr;
    	}
    	
    	objects = s;
    }
    
}






















// public static String fullData =
// "{\"activities\":[],\"timeFrames\":[{\"goals\":[{\"localId\":\"22\",\"type\":\"PTWeeklyGoal\"},{\"localId\":\"21\",\"type\":\"PTWeeklyGoal\"},{\"localId\":\"23\",\"type\":\"PTWeeklyGoal\"}],\"timeZoneOffsetInSeconds\":25200,\"lastUpdated\":1352803026,\"type\":\"PTWeeklyTimeFrame\",\"localId\":\"31\",\"serverId\":null,\"deleted\":0,\"startTime\":1352803026,\"childrenTimeFrames\":[{\"localId\":\"28\",\"type\":\"PTDailyTimeFrame\"},{\"localId\":\"27\",\"type\":\"PTDailyTimeFrame\"},{\"localId\":\"25\",\"type\":\"PTDailyTimeFrame\"},{\"localId\":\"26\",\"type\":\"PTDailyTimeFrame\"},{\"localId\":\"29\",\"type\":\"PTDailyTimeFrame\"},{\"localId\":\"30\",\"type\":\"PTDailyTimeFrame\"},{\"localId\":\"24\",\"type\":\"PTDailyTimeFrame\"}],\"endTime\":1353344399,\"needsSync\":1},{\"goals\":[{\"localId\":\"10\",\"type\":\"PTDailyGoal\"},{\"localId\":\"19\",\"type\":\"PTDailyGoal\"},{\"localId\":\"11\",\"type\":\"PTDailyGoal\"}],\"timeZoneOffsetInSeconds\":25200,\"lastUpdated\":1352803026,\"type\":\"PTDailyTimeFrame\",\"localId\":\"25\",\"serverId\":null,\"deleted\":0,\"startTime\":1353258000,\"childrenTimeFrames\":[],\"endTime\":1353344399,\"parentTimeFrame\":{\"localId\":\"31\",\"type\":\"PTWeeklyTimeFrame\"},\"needsSync\":1},{\"goals\":[{\"localId\":\"13\",\"type\":\"PTDailyGoal\"},{\"localId\":\"9\",\"type\":\"PTDailyGoal\"},{\"localId\":\"16\",\"type\":\"PTDailyGoal\"}],\"timeZoneOffsetInSeconds\":25200,\"lastUpdated\":1352803026,\"type\":\"PTDailyTimeFrame\",\"localId\":\"26\",\"serverId\":null,\"deleted\":0,\"startTime\":1353171600,\"childrenTimeFrames\":[],\"endTime\":1353257999,\"parentTimeFrame\":{\"localId\":\"31\",\"type\":\"PTWeeklyTimeFrame\"},\"needsSync\":1},{\"goals\":[{\"localId\":\"1\",\"type\":\"PTDailyGoal\"},{\"localId\":\"14\",\"type\":\"PTDailyGoal\"},{\"localId\":\"12\",\"type\":\"PTDailyGoal\"}],\"timeZoneOffsetInSeconds\":25200,\"lastUpdated\":1352803026,\"type\":\"PTDailyTimeFrame\",\"localId\":\"27\",\"serverId\":null,\"deleted\":0,\"startTime\":1352912400,\"childrenTimeFrames\":[],\"endTime\":1352998799,\"parentTimeFrame\":{\"localId\":\"31\",\"type\":\"PTWeeklyTimeFrame\"},\"needsSync\":1},{\"goals\":[{\"localId\":\"8\",\"type\":\"PTDailyGoal\"},{\"localId\":\"5\",\"type\":\"PTDailyGoal\"},{\"localId\":\"18\",\"type\":\"PTDailyGoal\"}],\"timeZoneOffsetInSeconds\":25200,\"lastUpdated\":1352803026,\"type\":\"PTDailyTimeFrame\",\"localId\":\"28\",\"serverId\":null,\"deleted\":0,\"startTime\":1352826000,\"childrenTimeFrames\":[],\"endTime\":1352912399,\"parentTimeFrame\":{\"localId\":\"31\",\"type\":\"PTWeeklyTimeFrame\"},\"needsSync\":1},{\"goals\":[{\"localId\":\"2\",\"type\":\"PTDailyGoal\"},{\"localId\":\"17\",\"type\":\"PTDailyGoal\"},{\"localId\":\"15\",\"type\":\"PTDailyGoal\"}],\"timeZoneOffsetInSeconds\":25200,\"lastUpdated\":1352803026,\"type\":\"PTDailyTimeFrame\",\"localId\":\"29\",\"serverId\":null,\"deleted\":0,\"startTime\":1353085200,\"childrenTimeFrames\":[],\"endTime\":1353171599,\"parentTimeFrame\":{\"localId\":\"31\",\"type\":\"PTWeeklyTimeFrame\"},\"needsSync\":1},{\"goals\":[{\"localId\":\"3\",\"type\":\"PTDailyGoal\"},{\"localId\":\"7\",\"type\":\"PTDailyGoal\"},{\"localId\":\"4\",\"type\":\"PTDailyGoal\"}],\"timeZoneOffsetInSeconds\":25200,\"lastUpdated\":1352803026,\"type\":\"PTDailyTimeFrame\",\"localId\":\"30\",\"serverId\":null,\"deleted\":0,\"startTime\":1352803026,\"childrenTimeFrames\":[],\"endTime\":1352825999,\"parentTimeFrame\":{\"localId\":\"31\",\"type\":\"PTWeeklyTimeFrame\"},\"needsSync\":1},{\"goals\":[{\"localId\":\"0\",\"type\":\"PTDailyGoal\"},{\"localId\":\"20\",\"type\":\"PTDailyGoal\"},{\"localId\":\"6\",\"type\":\"PTDailyGoal\"}],\"timeZoneOffsetInSeconds\":25200,\"lastUpdated\":1352803026,\"type\":\"PTDailyTimeFrame\",\"localId\":\"24\",\"serverId\":null,\"deleted\":0,\"startTime\":1352998800,\"childrenTimeFrames\":[],\"endTime\":1353085199,\"parentTimeFrame\":{\"localId\":\"31\",\"type\":\"PTWeeklyTimeFrame\"},\"needsSync\":1}],\"profile\":{\"authToken\":\"wUDf1GC1eRqLKJGTDE9w\",\"dateOfBirth\":0,\"email\":\"email@company.com\",\"gender\":0,\"goal\":0,\"height\":0,\"lastSyncTime\":0,\"lastUpdated\":0,\"latestVersion\":\"\",\"name\":\"\",\"needsSynch\":false,\"playMusic\":false,\"playlistName\":\"\",\"seedingTimestamps\":0,\"shuffleMode\":false,\"smartDJMode\":false,\"unit\":0,\"userType\":0,\"weight\":1,\"yearOfBirth\":0},\"goals\":[{\"activities\":[],\"lastUpdated\":1352803026,\"type\":\"PTDailyGoal\",\"localId\":\"18\",\"activityType\":5,\"serverId\":null,\"deleted\":0,\"goalValue\":900,\"timeFrame\":{\"localId\":\"28\",\"type\":\"PTDailyTimeFrame\"},\"fixed\":1,\"needsSync\":1},{\"activities\":[],\"lastUpdated\":1352803026,\"type\":\"PTDailyGoal\",\"localId\":\"10\",\"activityType\":6,\"serverId\":null,\"deleted\":0,\"goalValue\":0,\"timeFrame\":{\"localId\":\"25\",\"type\":\"PTDailyTimeFrame\"},\"fixed\":1,\"needsSync\":1},{\"activities\":[],\"lastUpdated\":1352803026,\"type\":\"PTDailyGoal\",\"localId\":\"19\",\"activityType\":5,\"serverId\":null,\"deleted\":0,\"goalValue\":0,\"timeFrame\":{\"localId\":\"25\",\"type\":\"PTDailyTimeFrame\"},\"fixed\":1,\"needsSync\":1},{\"activities\":[],\"lastUpdated\":1352803026,\"type\":\"PTDailyGoal\",\"localId\":\"11\",\"activityType\":2,\"serverId\":null,\"deleted\":0,\"goalValue\":0,\"timeFrame\":{\"localId\":\"25\",\"type\":\"PTDailyTimeFrame\"},\"fixed\":1,\"needsSync\":1},{\"activities\":[],\"lastUpdated\":1352803026,\"type\":\"PTDailyGoal\",\"localId\":\"0\",\"activityType\":2,\"serverId\":null,\"deleted\":0,\"goalValue\":2,\"timeFrame\":{\"localId\":\"24\",\"type\":\"PTDailyTimeFrame\"},\"fixed\":1,\"needsSync\":1},{\"activities\":[],\"lastUpdated\":1352803026,\"type\":\"PTDailyGoal\",\"localId\":\"12\",\"activityType\":6,\"serverId\":null,\"deleted\":0,\"goalValue\":0,\"timeFrame\":{\"localId\":\"27\",\"type\":\"PTDailyTimeFrame\"},\"fixed\":1,\"needsSync\":1},{\"activities\":[],\"lastUpdated\":1352803026,\"type\":\"PTDailyGoal\",\"localId\":\"1\",\"activityType\":5,\"serverId\":null,\"deleted\":0,\"goalValue\":0,\"timeFrame\":{\"localId\":\"27\",\"type\":\"PTDailyTimeFrame\"},\"fixed\":1,\"needsSync\":1},{\"activities\":[],\"lastUpdated\":1352803026,\"type\":\"PTDailyGoal\",\"localId\":\"20\",\"activityType\":6,\"serverId\":null,\"deleted\":0,\"goalValue\":140,\"timeFrame\":{\"localId\":\"24\",\"type\":\"PTDailyTimeFrame\"},\"fixed\":1,\"needsSync\":1},{\"activities\":[],\"lastUpdated\":1352803026,\"type\":\"PTDailyGoal\",\"localId\":\"2\",\"activityType\":5,\"serverId\":null,\"deleted\":0,\"goalValue\":0,\"timeFrame\":{\"localId\":\"29\",\"type\":\"PTDailyTimeFrame\"},\"fixed\":1,\"needsSync\":1},{\"activities\":[],\"lastUpdated\":1352803026,\"type\":\"PTDailyGoal\",\"localId\":\"13\",\"activityType\":6,\"serverId\":null,\"deleted\":0,\"goalValue\":0,\"timeFrame\":{\"localId\":\"26\",\"type\":\"PTDailyTimeFrame\"},\"fixed\":1,\"needsSync\":1},{\"activities\":[],\"lastUpdated\":1352803026,\"type\":\"PTDailyGoal\",\"localId\":\"3\",\"activityType\":2,\"serverId\":null,\"deleted\":0,\"goalValue\":2,\"timeFrame\":{\"localId\":\"30\",\"type\":\"PTDailyTimeFrame\"},\"fixed\":1,\"needsSync\":1},{\"serverId\":null,\"deleted\":0,\"type\":\"PTWeeklyGoal\",\"goalValue\":1800,\"timeFrame\":{\"localId\":\"31\",\"type\":\"PTWeeklyTimeFrame\"},\"activityType\":5,\"lastUpdated\":1352803026,\"needsSync\":1,\"localId\":\"21\"},{\"activities\":[],\"lastUpdated\":1352803026,\"type\":\"PTDailyGoal\",\"localId\":\"14\",\"activityType\":2,\"serverId\":null,\"deleted\":0,\"goalValue\":3,\"timeFrame\":{\"localId\":\"27\",\"type\":\"PTDailyTimeFrame\"},\"fixed\":1,\"needsSync\":1},{\"activities\":[],\"lastUpdated\":1352803026,\"type\":\"PTDailyGoal\",\"localId\":\"4\",\"activityType\":5,\"serverId\":null,\"deleted\":0,\"goalValue\":0,\"timeFrame\":{\"localId\":\"30\",\"type\":\"PTDailyTimeFrame\"},\"fixed\":1,\"needsSync\":1},{\"activities\":[],\"lastUpdated\":1352803026,\"type\":\"PTDailyGoal\",\"localId\":\"5\",\"activityType\":6,\"serverId\":null,\"deleted\":0,\"goalValue\":0,\"timeFrame\":{\"localId\":\"28\",\"type\":\"PTDailyTimeFrame\"},\"fixed\":1,\"needsSync\":1},{\"activities\":[],\"lastUpdated\":1352803026,\"type\":\"PTDailyGoal\",\"localId\":\"15\",\"activityType\":6,\"serverId\":null,\"deleted\":0,\"goalValue\":0,\"timeFrame\":{\"localId\":\"29\",\"type\":\"PTDailyTimeFrame\"},\"fixed\":1,\"needsSync\":1},{\"serverId\":null,\"deleted\":0,\"type\":\"PTWeeklyGoal\",\"goalValue\":8,\"timeFrame\":{\"localId\":\"31\",\"type\":\"PTWeeklyTimeFrame\"},\"activityType\":2,\"lastUpdated\":1352803026,\"needsSync\":1,\"localId\":\"22\"},{\"activities\":[],\"lastUpdated\":1352803026,\"type\":\"PTDailyGoal\",\"localId\":\"6\",\"activityType\":5,\"serverId\":null,\"deleted\":0,\"goalValue\":0,\"timeFrame\":{\"localId\":\"24\",\"type\":\"PTDailyTimeFrame\"},\"fixed\":1,\"needsSync\":1},{\"serverId\":null,\"deleted\":0,\"type\":\"PTWeeklyGoal\",\"goalValue\":245,\"timeFrame\":{\"localId\":\"31\",\"type\":\"PTWeeklyTimeFrame\"},\"activityType\":6,\"lastUpdated\":1352803026,\"needsSync\":1,\"localId\":\"23\"},{\"activities\":[],\"lastUpdated\":1352803026,\"type\":\"PTDailyGoal\",\"localId\":\"16\",\"activityType\":2,\"serverId\":null,\"deleted\":0,\"goalValue\":1,\"timeFrame\":{\"localId\":\"26\",\"type\":\"PTDailyTimeFrame\"},\"fixed\":1,\"needsSync\":1},{\"activities\":[],\"lastUpdated\":1352803026,\"type\":\"PTDailyGoal\",\"localId\":\"7\",\"activityType\":6,\"serverId\":null,\"deleted\":0,\"goalValue\":105,\"timeFrame\":{\"localId\":\"30\",\"type\":\"PTDailyTimeFrame\"},\"fixed\":1,\"needsSync\":1},{\"activities\":[],\"lastUpdated\":1352803026,\"type\":\"PTDailyGoal\",\"localId\":\"8\",\"activityType\":2,\"serverId\":null,\"deleted\":0,\"goalValue\":0,\"timeFrame\":{\"localId\":\"28\",\"type\":\"PTDailyTimeFrame\"},\"fixed\":1,\"needsSync\":1},{\"activities\":[],\"lastUpdated\":1352803026,\"type\":\"PTDailyGoal\",\"localId\":\"17\",\"activityType\":2,\"serverId\":null,\"deleted\":0,\"goalValue\":0,\"timeFrame\":{\"localId\":\"29\",\"type\":\"PTDailyTimeFrame\"},\"fixed\":1,\"needsSync\":1},{\"activities\":[],\"lastUpdated\":1352803026,\"type\":\"PTDailyGoal\",\"localId\":\"9\",\"activityType\":5,\"serverId\":null,\"deleted\":0,\"goalValue\":900,\"timeFrame\":{\"localId\":\"26\",\"type\":\"PTDailyTimeFrame\"},\"fixed\":1,\"needsSync\":1}],\"personalPlans\":[]} ";
// public static String profile =
// "{\"personalPlans\":[],\"timeFrames\":[],\"goals\":[],\"activities\":[], \"profile\":{\"authToken\":\"wUDf1GC1eRqLKJGTDE9w\",\"dateOfBirth\":0,\"email\":\"email@company.com\",\"gender\":0,\"goal\":0,\"height\":0,\"lastSyncTime\":0,\"lastUpdated\":0,\"latestVersion\":\"\",\"name\":\"\",\"needsSynch\":false,\"playMusic\":false,\"playlistName\":\"\",\"seedingTimestamps\":0,\"shuffleMode\":false,\"smartDJMode\":false,\"unit\":0,\"userType\":0,\"weight\":1,\"yearOfBirth\":0} }";
//
// public static String profile1 =
// "\"profile\":{\"authToken\":\"wUDf1GC1eRqLKJGTDE9w\",\"dateOfBirth\":0,\"email\":\"email@company.com\",\"gender\":0,\"goal\":0,\"height\":0,\"lastSyncTime\":0,\"lastUpdated\":0,\"latestVersion\":\"\",\"name\":\"\",\"needsSynch\":false,\"playMusic\":false,\"playlistName\":\"\",\"seedingTimestamps\":0,\"shuffleMode\":false,\"smartDJMode\":false,\"unit\":0,\"userType\":0,\"weight\":1,\"yearOfBirth\":0}";




//public JSONObject recursiveChange(JSONObject obj, String key, String value)
//{   	
//	// check keys list
//	Iterator keys = obj.keys();
//	while(keys.hasNext()) 
//	{
//  	String k = (String)keys.next();
//      if(k == key)
//      {
//      	obj.put(k, value);
//      	return obj;
//      }
//	}
//	
//	// recursive
//	keys = obj.keys();
//	while(keys.hasNext()) 
//	{
//		String k = (String)keys.next();
//		obj.put(k, recursiveChange(obj.getJSONObject(k), k, value));
//	}
//	
//	return obj;
//}