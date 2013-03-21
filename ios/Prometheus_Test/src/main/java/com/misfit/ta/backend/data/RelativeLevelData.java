package com.misfit.ta.backend.data;

// relavtive level data
public class RelativeLevelData
{
	public String level = null;
	public String absoluteLevel = null;
	public String point = null;

	public RelativeLevelData()
	{
		
	}
	
	public RelativeLevelData(Integer level, Integer absoluteLevel, Integer point)
	{
		this.level = level.toString();
		this.absoluteLevel = absoluteLevel.toString();
		this.point = point.toString();
	}
	
	public String toString()
	{
		return "{level:" + level + ",absoluteLevel:" + absoluteLevel + ",point:" + point + "}";
	}

}
