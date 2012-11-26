package com.misfit.ta.backend.data;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class SyncData 
{
	// static methods
    public static String fullData = "{\"profile\":{\"lastUpdated\":1352889104,\"serverId\":\"50a37315caedd02086000005\",\"localId\":\"0\",\"deleted\":0,\"weight\":132.2774,\"height\":66.92917,\"unit\":1,\"gender\":0,\"dateOfBirth\":342982800,\"goal\":0.0,\"name\":null,\"playlistName\":null,\"playMusic\":0,\"shuffleMode\":0,\"smartDJMode\":0,\"seedingTimestamps\":null,\"latestVersion\":\"3\"},\"personalPlans\":[{\"lastUpdated\":1352889127,\"serverId\":\"50a3732bcaedd02086000006\",\"localId\":\"0\",\"deleted\":0,\"name\":\"thinh\",\"detail\":\"[  {    \"goal\" : 140,    \"dailyGoals\" : [      0,      0,      0,      0,      0,      0,      0    ],    \"activityType\" : 0  }]\",\"type\":\"PTPersonalPlan\"}],\"timeFrames\":[],\"goals\":[],\"activities\":[]},\"last_successfully_synced\":1352889175}";
    public static String profile = "{\"personalPlans\":[],\"timeFrames\":[],\"goals\":[],\"activities\":[], \"profile\":{\"lastUpdated\":1452889104,\"serverId\":\"50a37315caedd02086000005\",\"localId\":\"0\",\"deleted\":0,\"weight\":132.2774,\"height\":66.92917,\"unit\":1,\"gender\":0,\"dateOfBirth\":342982800,\"goal\":0.0,\"name\":null,\"playlistName\":null,\"playMusic\":0,\"shuffleMode\":0,\"smartDJMode\":0,\"seedingTimestamps\":null,\"latestVersion\":\"3\"}}";
    public static SyncData getDefault()
    {
    	return new SyncData(0, profile);
    }
    
    // fields
    public Long timestamp;
    public String objects;
    
    public JSONObject json;
    
    // constructor
    public SyncData(long timestamp, String objects)
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
    
    public long getLastUpdated()
    {
    	String str = getValue("lastUpdated");
    	return Long.parseLong(str);
    }
    
    public void setLastUpdated(long value)
    {
    	setValue("lastUpdated", value);
    }
    
    public String getString()
    {
    	return objects;
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