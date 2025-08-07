package com.qasummarygen.utils;

public class TestCaseUtils {
    private String testId;
    private String testCaseId;
    private String title;
    private String storyId;
    private String type;       // Manual / Automated
    private String status;     // Passed / Failed / Blocked
    private String priority;
    private String executedBy;
    private String delivered;
    private String module;
    private String summary;

    // Default constructor
    public TestCaseUtils() {}

    // Parametrized constructor (optional, if used anywhere)
    public TestCaseUtils(String testId, String title, String storyId, String type,
                         String status, String priority, String executedBy,
                         String delivered, String module, String summary) {
        this.testId = testId;
        this.title = title;
        this.storyId = storyId;
        this.type = type;
        this.status = status;
        this.priority = priority;
        this.executedBy = executedBy;
        this.delivered = delivered;
        this.module = module;
        this.summary = summary;
    }

    // Getters and Setters
    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getExecutedBy() {
        return executedBy;
    }

    public void setExecutedBy(String executedBy) {
        this.executedBy = executedBy;
    }

    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
