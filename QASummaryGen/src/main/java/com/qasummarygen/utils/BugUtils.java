package com.qasummarygen.utils;

public class BugUtils {
    private String bugId;
    private String title;
    private String storyId;
    private String severity;
    private String status;
    private String foundBy;
    private String fixedInSprint;

    // ✅ Correct no-arg constructor
    public BugUtils() {}

    // ✅ Correct parameterized constructor
    public BugUtils(String bugId, String title, String storyId, String severity,
                    String status, String foundBy, String fixedInSprint) {
        this.bugId = bugId;
        this.title = title;
        this.storyId = storyId;
        this.severity = severity;
        this.status = status;
        this.foundBy = foundBy;
        this.fixedInSprint = fixedInSprint;
    }

    // ✅ Getters and Setters
    public String getBugId() {
        return bugId;
    }

    public void setBugId(String bugId) {
        this.bugId = bugId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFoundBy() {
        return foundBy;
    }

    public void setFoundBy(String foundBy) {
        this.foundBy = foundBy;
    }

    public String getFixedInSprint() {
        return fixedInSprint;
    }

    public void setFixedInSprint(String fixedInSprint) {
        this.fixedInSprint = fixedInSprint;
    }

    // ✅ Optional: helpful for debugging
    @Override
    public String toString() {
        return "BugUtils{" +
                "bugId='" + bugId + '\'' +
                ", title='" + title + '\'' +
                ", storyId='" + storyId + '\'' +
                ", severity='" + severity + '\'' +
                ", status='" + status + '\'' +
                ", foundBy='" + foundBy + '\'' +
                ", fixedInSprint='" + fixedInSprint + '\'' +
                '}';
    }
}
