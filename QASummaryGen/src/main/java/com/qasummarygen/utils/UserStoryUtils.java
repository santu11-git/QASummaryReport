package com.qasummarygen.utils;


public class UserStoryUtils {
    private String storyId;
    private String title;
    private String sprintId;
    private int storyPoints;
    private String owner;
    private String status;

    // Constructors, Getters, Setters
    public void UserStory() {}

    public void UserStory(String storyId, String title, String sprintId, int storyPoints, String owner, String status) {
        this.storyId = storyId;
        this.title = title;
        this.sprintId = sprintId;
        this.storyPoints = storyPoints;
        this.owner = owner;
        this.status = status;
    }

    // Getters and setters here
    public String getStoryId() { return storyId; }
    public void setStoryId(String storyId) { this.storyId = storyId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSprintId() { return sprintId; }
    public void setSprintId(String sprintId) { this.sprintId = sprintId; }

    public int getStoryPoints() { return storyPoints; }
    public void setStoryPoints(int storyPoints) { this.storyPoints = storyPoints; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

	
}

