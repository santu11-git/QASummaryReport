package com.qasummarygen.utils;

import java.util.List;

public class SprintSummary {
    private String sprintId;
    private String sprintName;
    private String sprintStartDate;
    private String sprintEndDate;
    private int storyPointsCompleted;

    private List<UserStoryUtils> userStories;
    private List<TestCaseUtils> testCases;
    private List<BugUtils> bugs;

    // ---- Constructors ----
    public SprintSummary() {}

    public SprintSummary(String sprintId, String sprintName, String sprintStartDate, String sprintEndDate,
                         int storyPointsCompleted, List<UserStoryUtils> userStories,
                         List<TestCaseUtils> testCases, List<BugUtils> bugs) {
        this.sprintId = sprintId;
        this.sprintName = sprintName;
        this.sprintStartDate = sprintStartDate;
        this.sprintEndDate = sprintEndDate;
        this.storyPointsCompleted = storyPointsCompleted;
        this.userStories = userStories;
        this.testCases = testCases;
        this.bugs = bugs;
    }

    // ---- Getters and Setters ----
    public String getSprintId() {
        return sprintId;
    }

    public void setSprintId(String sprintId) {
        this.sprintId = sprintId;
    }

    public String getSprintName() {
        return sprintName;
    }

    public void setSprintName(String sprintName) {
        this.sprintName = sprintName;
    }

    public String getSprintStartDate() {
        return sprintStartDate;
    }

    public void setSprintStartDate(String sprintStartDate) {
        this.sprintStartDate = sprintStartDate;
    }

    public String getSprintEndDate() {
        return sprintEndDate;
    }

    public void setSprintEndDate(String sprintEndDate) {
        this.sprintEndDate = sprintEndDate;
    }

    public int getStoryPointsCompleted() {
        return storyPointsCompleted;
    }

    public void setStoryPointsCompleted(int storyPointsCompleted) {
        this.storyPointsCompleted = storyPointsCompleted;
    }

    public List<UserStoryUtils> getUserStories() {
        return userStories;
    }

    public void setUserStories(List<UserStoryUtils> userStories) {
        this.userStories = userStories;
    }

    public List<TestCaseUtils> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCaseUtils> testCases) {
        this.testCases = testCases;
    }

    
    public List<BugUtils> getBugs() {
        return bugs;
    }

    public void setBugs(List<BugUtils> bugs) {
        this.bugs = bugs;
    }
}
