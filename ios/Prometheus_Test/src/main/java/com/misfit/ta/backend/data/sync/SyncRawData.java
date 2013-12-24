package com.misfit.ta.backend.data.sync;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.apache.commons.io.FileUtils;

import com.misfit.ta.common.MVPCommon;

public class SyncRawData {

	// fields
	protected String fileHandle;			// 2 bytes
	protected String fileType;				// 2 bytes
	protected int fileSize;					// 4 bytes
	protected long timestamp;				// 4 bytes
	protected int miliSecondsOfTimestamp;	// 2 bytes
	protected int timezoneOffsetInMinutes;	// 2 bytes
	protected String minuteByMinuteData;
	protected String crc;					// 4 bytes
	
	
	// constructor
	public SyncRawData() {
		
		fileHandle = "";
		fileType = "0011";	// staging
		fileSize = 16 + 4;	// header + crc
		timestamp = System.currentTimeMillis() / 1000;
		miliSecondsOfTimestamp = 0;
		timezoneOffsetInMinutes = 0;
		minuteByMinuteData = "";
		crc = null;
	}
	
	public SyncRawData(String rawData) {
		
		if(rawData.length() < 20)
			return;
		
		fileHandle = rawData.substring(0, 4);
		fileType = rawData.substring(4, 8);
		fileSize = MVPCommon.litteEndianStringToInteger(rawData.substring(8, 16));
		timestamp = MVPCommon.litteEndianStringToLong(rawData.substring(16, 24));
		miliSecondsOfTimestamp = MVPCommon.litteEndianStringToInteger(rawData.substring(24, 28));
		timezoneOffsetInMinutes = MVPCommon.litteEndianStringToInteger(rawData.substring(28, 32));
		minuteByMinuteData = rawData.substring(32, rawData.length() - 8);
		crc = rawData.substring(rawData.length() - 8);
	}
	
	public SyncRawData(File rawDataFile) {
		
		if (!rawDataFile.isFile())
			return;
		
		String rawData = "";
		try {
			rawData = FileUtils.readFileToString(rawDataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		fileHandle = rawData.substring(0, 4);
		fileType = rawData.substring(4, 8);
		fileSize = MVPCommon.litteEndianStringToInteger(rawData.substring(8, 16));
		timestamp = MVPCommon.litteEndianStringToLong(rawData.substring(16, 24));
		miliSecondsOfTimestamp = MVPCommon.litteEndianStringToInteger(rawData.substring(24, 28));
		timezoneOffsetInMinutes = MVPCommon.litteEndianStringToInteger(rawData.substring(28, 32));
		minuteByMinuteData = rawData.substring(32, rawData.length() - 8);
		crc = rawData.substring(rawData.length() - 8);
	}

	
	// methods
	public String toRawDataString() {

		// prepare rawData's header: fileHandle + fileType + length 
		// 		+ timestamp + timestampMiliseconds + timezoneOffsetInMinutes
		String rawData = fileHandle + fileType + MVPCommon.toLittleEndianString(fileSize) 
				+ MVPCommon.toLittleEndianString(timestamp) 
				+ MVPCommon.toLittleEndianString(miliSecondsOfTimestamp).substring(0, 4)
				+ MVPCommon.toLittleEndianString(timezoneOffsetInMinutes).substring(0, 4);
		
		List<Byte> headerBytes = MVPCommon.hexStringToByteArray(rawData);
		
		
		// minute by minute data
		List<Byte> bytes = MVPCommon.hexStringToByteArray(minuteByMinuteData);
		rawData += minuteByMinuteData;
		
		
		// add 4 bytes crc
		byte[] byteArr = new byte[headerBytes.size() + bytes.size()];
		int index = 0;
		
		for(Byte b : headerBytes)
			byteArr[index++] = b;
		
		for(Byte b : bytes)
			byteArr[index++] = b;	
		
		Checksum checksum = new CRC32();
		checksum.update(byteArr, 0, byteArr.length);
		long checksumValue = checksum.getValue();
		
		crc = MVPCommon.toLittleEndianString(checksumValue);
		rawData += crc;
		
		return rawData;
	}
	

	// getters setters
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

	public int getFileSize() {
		return fileSize;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getMiliSecondsOfTimestamp() {
		return miliSecondsOfTimestamp;
	}

	public void setMiliSecondsOfTimestamp(int miliSecondsOfTimestamp) {
		this.miliSecondsOfTimestamp = miliSecondsOfTimestamp;
	}

	public int getTimezoneOffsetInMinutes() {
		return timezoneOffsetInMinutes;
	}

	public void setTimezoneOffsetInMinutes(int timezoneOffsetInMinutes) {
		this.timezoneOffsetInMinutes = timezoneOffsetInMinutes;
	}

	public String getMinuteByMinuteData() {
		return minuteByMinuteData;
	}

	public void setMinuteByMinuteData(String minuteByMinuteData) {
		
		if(minuteByMinuteData.length() % 2 == 1)
			return;
		
		this.minuteByMinuteData = minuteByMinuteData;
		this.fileSize = 16 + 4 + minuteByMinuteData.length() / 2;
	}

	public String getCrc() {
		return crc;
	}

}
