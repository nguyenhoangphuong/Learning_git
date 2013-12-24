package com.misfit.ta.backend.data.sync;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class SyncFileData {

	// fields
	protected Long fileTimestamp;
	protected Integer timestampDifference;
	protected String fileHandle;
	protected String fileType;
	protected Integer fileSize;
	protected String rawData;
	
	
	// methods
    public JSONObject toJson() 
    {
        JSONObject object = new JSONObject();
        try 
        {
            object.accumulate("fileTimestamp", fileTimestamp);
            object.accumulate("timestampDifference", timestampDifference);
            object.accumulate("fileHandle", fileHandle);
            object.accumulate("fileType", fileType);
            object.accumulate("fileSize", fileSize);
            object.accumulate("rawData", rawData);
            
            return object;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }
  
	
	// getters setters
	public Long getFileTimestamp() {
		return fileTimestamp;
	}
	
	public void setFileTimestamp(Long fileTimestamp) {
		this.fileTimestamp = fileTimestamp;
	}
	
	public Integer getTimestampDifference() {
		return timestampDifference;
	}
	
	public void setTimestampDifference(Integer timestampDifference) {
		this.timestampDifference = timestampDifference;
	}
	
	public String getFileHandle() {
		return fileHandle;
	}
	
	public void setFileHandle(String fileHandle) {
		this.fileHandle = fileHandle;
	}
	
	public String getFileType() {
		return fileType;
	}
	
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	public Integer getFileSize() {
		return fileSize;
	}
	
	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}
	
	public String getRawData() {
		return rawData;
	}
	
	public void setRawData(String rawData) {
		this.rawData = rawData;
	}
	
}
