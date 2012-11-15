package com.misfit.ta.backend.data;

public class PersonalPlan {
    public boolean deleted = false;
    public String serverId = "";
    public long lastUpdated = 0;
    public boolean needsSync = false;
    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    public String getServerId() {
        return serverId;
    }
    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
    public long getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public boolean isNeedsSync() {
        return needsSync;
    }
    public void setNeedsSync(boolean needsSync) {
        this.needsSync = needsSync;
    }
 
    
    
}
