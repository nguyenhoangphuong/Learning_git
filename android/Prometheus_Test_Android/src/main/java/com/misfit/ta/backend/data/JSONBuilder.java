package com.misfit.ta.backend.data;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class JSONBuilder 
{
	private JSONObject json = new JSONObject();
	public Map<String, Object> map = new HashMap<String, Object>();
	
	public void addValue(String key, Object value)
	{
		json.put(key, value);
		map.put(key, value);
	}
	
	public String toJSONString()
	{
		return json.toString();
	}
	
}
