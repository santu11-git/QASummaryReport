package com.qasummarygen.utils;

import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class ExcelReader {

    public static QASummary readSummaryFromExcel(String excelPath) {
        QASummary qaSummary = new QASummary();
        SprintSummary sprintSummary = new SprintSummary();

        try (FileInputStream fis = new FileInputStream(new File(excelPath));
             Workbook workbook = WorkbookFactory.create(fis)) {

            // 1. Read Sprint Summary
            Sheet sprintSheet = workbook.getSheet("SprintSummary");
            if (sprintSheet != null && sprintSheet.getPhysicalNumberOfRows() > 1) {
                Row row = sprintSheet.getRow(1);
                if (row != null) {
                    qaSummary.setProjectName(getCellValue(row.getCell(0)));
                    sprintSummary.setSprintName(getCellValue(row.getCell(1)));
                    sprintSummary.setSprintStartDate(getCellValue(row.getCell(2)));
                    sprintSummary.setSprintEndDate(getCellValue(row.getCell(3)));
                    qaSummary.setGeneratedBy(getCellValue(row.getCell(4)));
                }
            }

            // 2. Read Test Cases
            Sheet testCaseSheet = workbook.getSheet("TestCases");
            List<TestCaseUtils> testCaseList = new ArrayList<>();
            int total = 0, manual = 0, automated = 0, deliveredStories = 0;

            if (testCaseSheet != null) {
                for (int i = 1; i <= testCaseSheet.getLastRowNum(); i++) {
                    Row row = testCaseSheet.getRow(i);
                    if (row == null) continue;

                    String testCaseId = getCellValue(row.getCell(0));
                    String type = getCellValue(row.getCell(1));
                    String delivered = getCellValue(row.getCell(2));
                    String module = getCellValue(row.getCell(3));
                    String summaryText = getCellValue(row.getCell(4));

                    TestCaseUtils testCase = new TestCaseUtils();
                    testCase.setTestCaseId(testCaseId);
                    testCase.setType(type);
                    testCase.setDelivered(delivered);
                    testCase.setModule(module);
                    testCase.setSummary(summaryText);
                    testCaseList.add(testCase);

                    total++;
                    if ("Manual".equalsIgnoreCase(type)) manual++;
                    else if ("Automated".equalsIgnoreCase(type)) automated++;

                    if ("Yes".equalsIgnoreCase(delivered)) deliveredStories++;
                }
            }

            sprintSummary.setTestCases(testCaseList);
            qaSummary.setTotalTestCases(total);
            qaSummary.setManualTestCases(manual);
            qaSummary.setAutomatedTestCases(automated);
            qaSummary.setTotalStoriesDelivered(deliveredStories);

            // 3. Read Bugs
            Sheet bugSheet = workbook.getSheet("Bugs");
            List<BugUtils> bugList = new ArrayList<>();
            int totalBugs = 0, critical = 0, major = 0, minor = 0;

            if (bugSheet != null) {
                for (int i = 1; i <= bugSheet.getLastRowNum(); i++) {
                    Row row = bugSheet.getRow(i);
                    if (row == null) continue;

                    String bugId = getCellValue(row.getCell(0));
                    String title = getCellValue(row.getCell(1));
                    String severity = getCellValue(row.getCell(2));
                    String status = getCellValue(row.getCell(3));

                    BugUtils bug = new BugUtils();
                    bug.setBugId(bugId);
                    bug.setTitle(title);
                    bug.setSeverity(severity);
                    bug.setStatus(status);
                    bugList.add(bug);

                    totalBugs++;
                    if ("Critical".equalsIgnoreCase(severity)) critical++;
                    else if ("Major".equalsIgnoreCase(severity)) major++;
                    else if ("Minor".equalsIgnoreCase(severity)) minor++;
                }
            }

            sprintSummary.setBugs(bugList);
            qaSummary.setTotalBugs(totalBugs);
            qaSummary.setCriticalBugs(critical);
            qaSummary.setMajorBugs(major);
            qaSummary.setMinorBugs(minor);

            // 4. Read User Stories
            Sheet storySheet = workbook.getSheet("UserStories");
            List<UserStoryUtils> storyList = new ArrayList<>();
            int totalPointsCompleted = 0;

            if (storySheet != null) {
                for (int i = 1; i <= storySheet.getLastRowNum(); i++) {
                    Row row = storySheet.getRow(i);
                    if (row == null) continue;

                    String storyId = getCellValue(row.getCell(0));
                    String title = getCellValue(row.getCell(1));
                    String status = getCellValue(row.getCell(2));
                    String points = getCellValue(row.getCell(3));

                    int pointVal = 0;
                    try {
                        pointVal = Integer.parseInt(points);
                        totalPointsCompleted += pointVal;
                    } catch (NumberFormatException ignored) {}

                    UserStoryUtils story = new UserStoryUtils();
                    story.setStoryId(storyId);
                    story.setTitle(title);
                    story.setStatus(status);
                    story.setStoryPoints(pointVal);
                    storyList.add(story);
                }
            }

            sprintSummary.setUserStories(storyList);
            sprintSummary.setStoryPointsCompleted(totalPointsCompleted);

            // 5. Set Sprint Summary into QA Summary
            qaSummary.setSprintSummary(sprintSummary);

            // 6. Calculate Metrics
            qaSummary.setTestCoverage(calculateCoverage(automated, total));
            qaSummary.setBugDensity(calculateBugDensity(totalBugs, deliveredStories));
            qaSummary.setSprintVelocity(deliveredStories);

            // 7. Read Suggestions
            Sheet suggestionSheet = workbook.getSheet("Suggestions");
            if (suggestionSheet != null) {
                List<String> suggestions = new ArrayList<>();
                for (int i = 1; i <= suggestionSheet.getLastRowNum(); i++) {
                    Row row = suggestionSheet.getRow(i);
                    if (row == null) continue;
                    suggestions.add(getCellValue(row.getCell(0)));
                }
                qaSummary.setSuggestions(suggestions);
            }

        } catch (Exception e) {
            System.err.println("❌ Error reading Excel file: " + e.getMessage());
            e.printStackTrace();
        }

        return qaSummary;
    }

    // Coverage = (Automated / Total) * 100
    private static double calculateCoverage(int automated, int total) {
        return total == 0 ? 0 : ((double) automated / total) * 100;
    }

    // Bug density = total bugs / delivered stories
    private static double calculateBugDensity(int totalBugs, int stories) {
        return stories == 0 ? 0 : (double) totalBugs / stories;
    }

    public static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString(); // Customize if needed
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
            default:
                return "";
        }
    }
    
    
}
