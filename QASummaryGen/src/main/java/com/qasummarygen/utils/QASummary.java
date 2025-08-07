package com.qasummarygen.utils;

import java.util.List;

/**
 * Represents a complete QA Summary data model for a given sprint.
 * This model is used for generating Excel and PDF reports.
 */
public class QASummary {

    // === Project & Sprint Information ===
    private String projectName;
    private String sprintName;
    private String sprintStartDate;
    private String sprintEndDate;

    // === Test Case Metrics ===
    private int totalTestCases;
    private int automatedTestCases;
    private int manualTestCases;
    private int totalStoriesDelivered;

    // === Bug Metrics ===
    private int totalBugs;
    private int criticalBugs;
    private int majorBugs;
    private int minorBugs;

    // === Quality Metrics ===
    private double testCoverage;
    private double bugDensity;
    private int sprintVelocity;

    // === Additional Details ===
    private List<String> suggestions;
    private String reportGeneratedOn;
    private String generatedBy;

    // === Core Utility Lists ===
    private List<TestCaseUtils> testCases;
    private List<BugUtils> bugs;
    private List<UserStoryUtils> userStories;  // ✅ NEW: Added User Stories list

    // === Sprint Summary Details ===
    private SprintSummary sprintSummary;       // ✅ NEW: Full Sprint Summary reference

    // === Excel File Path (for traceability) ===
    private String excelFilePath;

    // === Getters & Setters ===

    // --- Project & Sprint Info ---
    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    // --- Test Case Metrics ---
    public int getTotalTestCases() {
        return totalTestCases;
    }
    public void setTotalTestCases(int totalTestCases) {
        this.totalTestCases = totalTestCases;
    }

    public int getAutomatedTestCases() {
        return automatedTestCases;
    }
    public void setAutomatedTestCases(int automatedTestCases) {
        this.automatedTestCases = automatedTestCases;
    }

    public int getManualTestCases() {
        return manualTestCases;
    }
    public void setManualTestCases(int manualTestCases) {
        this.manualTestCases = manualTestCases;
    }

    public int getTotalStoriesDelivered() {
        return totalStoriesDelivered;
    }
    public void setTotalStoriesDelivered(int totalStoriesDelivered) {
        this.totalStoriesDelivered = totalStoriesDelivered;
    }

    // --- Bug Metrics ---
    public int getTotalBugs() {
        return totalBugs;
    }
    public void setTotalBugs(int totalBugs) {
        this.totalBugs = totalBugs;
    }

    public int getCriticalBugs() {
        return criticalBugs;
    }
    public void setCriticalBugs(int criticalBugs) {
        this.criticalBugs = criticalBugs;
    }

    public int getMajorBugs() {
        return majorBugs;
    }
    public void setMajorBugs(int majorBugs) {
        this.majorBugs = majorBugs;
    }

    public int getMinorBugs() {
        return minorBugs;
    }
    public void setMinorBugs(int minorBugs) {
        this.minorBugs = minorBugs;
    }

    // --- Quality Metrics ---
    public double getTestCoverage() {
        return testCoverage;
    }
    public void setTestCoverage(double testCoverage) {
        this.testCoverage = testCoverage;
    }

    public double getBugDensity() {
        return bugDensity;
    }
    public void setBugDensity(double bugDensity) {
        this.bugDensity = bugDensity;
    }

    public int getSprintVelocity() {
        return sprintVelocity;
    }
    public void setSprintVelocity(int sprintVelocity) {
        this.sprintVelocity = sprintVelocity;
    }

    // --- Utility Lists ---
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

    public List<UserStoryUtils> getUserStories() {
        return userStories;
    }
    public void setUserStories(List<UserStoryUtils> userStories) {
        this.userStories = userStories;
    }

    // --- Sprint Summary Object ---
    public SprintSummary getSprintSummary() {
        return sprintSummary;
    }
    public void setSprintSummary(SprintSummary sprintSummary) {
        this.sprintSummary = sprintSummary;
    }

    // --- Additional Details ---
    public List<String> getSuggestions() {
        return suggestions;
    }
    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public String getReportGeneratedOn() {
        return reportGeneratedOn;
    }
    public void setReportGeneratedOn(String reportGeneratedOn) {
        this.reportGeneratedOn = reportGeneratedOn;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }
    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    // --- Excel Path for Metadata ---
    public String getExcelFilePath() {
        return excelFilePath;
    }
    public void setExcelFilePath(String excelFilePath) {
        this.excelFilePath = excelFilePath;
    }
}
