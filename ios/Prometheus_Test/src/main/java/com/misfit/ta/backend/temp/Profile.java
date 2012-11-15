package com.misfit.ta.backend.temp;

public class Profile {
    
    public String  authToken = "wUDf1GC1eRqLKJGTDE9w";
    public int dateOfBirth;
    public String email;
    public int gender;
    public float goal;
    public float height;
    public int lastSyncTime;
    public int lastUpdated;
    public String latestVersion;
    public String name;
    public boolean needsSynch = false;
    public String playlistName;
    public boolean playMusic;
    public long seedingTimestamps;
    public boolean shuffleMode = false;
    public boolean smartDJMode = false;
    public int unit = 0;
    public int userType;
    public float weight = 1;
    public int yearOfBirth = 0;
    

    public Profile(String email, String authToken) {
        this.email = email;
        this.authToken = authToken;
    }


    public String getAuthToken() {
        return authToken;
    }


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


    public int getDateOfBirth() {
        return dateOfBirth;
    }


    public void setDateOfBirth(int dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public int getGender() {
        return gender;
    }


    public void setGender(int gender) {
        this.gender = gender;
    }


    public float getGoal() {
        return goal;
    }


    public void setGoal(float goal) {
        this.goal = goal;
    }


    public float getHeight() {
        return height;
    }


    public void setHeight(float height) {
        this.height = height;
    }


    public int getLastSyncTime() {
        return lastSyncTime;
    }


    public void setLastSyncTime(int lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }


    public int getLastUpdated() {
        return lastUpdated;
    }


    public void setLastUpdated(int lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


    public String getLatestVersion() {
        return latestVersion;
    }


    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public boolean isNeedsSynch() {
        return needsSynch;
    }


    public void setNeedsSynch(boolean needsSynch) {
        this.needsSynch = needsSynch;
    }


    public String getPlaylistName() {
        return playlistName;
    }


    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }


    public boolean isPlayMusic() {
        return playMusic;
    }


    public void setPlayMusic(boolean playMusic) {
        this.playMusic = playMusic;
    }


    public long getSeedingTimestamps() {
        return seedingTimestamps;
    }


    public void setSeedingTimestamps(long seedingTimestamps) {
        this.seedingTimestamps = seedingTimestamps;
    }


    public boolean isShuffleMode() {
        return shuffleMode;
    }


    public void setShuffleMode(boolean shuffleMode) {
        this.shuffleMode = shuffleMode;
    }


    public boolean isSmartDJMode() {
        return smartDJMode;
    }


    public void setSmartDJMode(boolean smartDJMode) {
        this.smartDJMode = smartDJMode;
    }


    public int getUnit() {
        return unit;
    }


    public void setUnit(int unit) {
        this.unit = unit;
    }


    public int getUserType() {
        return userType;
    }


    public void setUserType(int userType) {
        this.userType = userType;
    }


    public float getWeight() {
        return weight;
    }


    public void setWeight(float weight) {
        this.weight = weight;
    }


    public int getYearOfBirth() {
        return yearOfBirth;
    }


    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }
    
    
}
