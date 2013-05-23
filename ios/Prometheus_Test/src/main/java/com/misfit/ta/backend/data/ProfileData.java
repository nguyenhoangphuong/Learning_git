package com.misfit.ta.backend.data;

public class ProfileData {
    // fields
    public String serverId = null;
    public Long updatedAt = null;
    public String localId = null;
    public Double weight = null;
    public Double height = null;
    public Integer unit = null;
    public Integer gender = null;
    public Long dateOfBirth = null;
    public String name = null;
    public String latestVersion = null;
    public Integer goalLevel = null;
    public String trackingDeviceId = null;
    public RelativeLevelData[] userRelativeLevelsNSData = null;
    
    public boolean isNull() {
    	if (serverId != null ||	
    			updatedAt != null ||
    			localId != null ||
    			weight != null ||
    			height != null ||
    			unit != null ||
    			gender != null ||
    			dateOfBirth != null ||
    			name != null ||
    			latestVersion != null ||
    			goalLevel != null ||
    			trackingDeviceId != null ||
    			userRelativeLevelsNSData != null)
    		return false;
    	else
    		return true;
	}
}
