package com.qasummarygen.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ExcelSummaryGenerator {

    public static boolean generateSummarySheet(String excelFilePath) {
        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet testCaseSheet = workbook.getSheet("TestCases");
            Sheet bugSheet = workbook.getSheet("Bugs");

            // Counts for Test Cases
            int totalTestCases = 0;
            int manualTestCases = 0;
            int automatedTestCases = 0;

            if (testCaseSheet != null) {
                for (int i = 1; i <= testCaseSheet.getLastRowNum(); i++) {
                    Row row = testCaseSheet.getRow(i);
                    if (row != null) {
                        totalTestCases++;
                        Cell execTypeCell = row.getCell(3); // Assuming Execution Type is in column D (index 3)
                        if (execTypeCell != null) {
                            String type = execTypeCell.getStringCellValue().trim().toLowerCase();
                            if (type.equals("manual")) manualTestCases++;
                            else if (type.equals("automated")) automatedTestCases++;
                        }
                    }
                }
            }

            // Counts for Bugs
            int totalBugs = 0;
            int criticalBugs = 0;
            int majorBugs = 0;
            int minorBugs = 0;

            if (bugSheet != null) {
                for (int i = 1; i <= bugSheet.getLastRowNum(); i++) {
                    Row row = bugSheet.getRow(i);
                    if (row != null) {
                        totalBugs++;
                        Cell severityCell = row.getCell(3); // Assuming Severity is in column D (index 3)
                        if (severityCell != null) {
                            String severity = severityCell.getStringCellValue().trim().toLowerCase();
                            if (severity.equals("critical")) criticalBugs++;
                            else if (severity.equals("major")) majorBugs++;
                            else if (severity.equals("minor")) minorBugs++;
                        }
                    }
                }
            }

            // Remove old SummaryStats sheet if it exists
            int index = workbook.getSheetIndex("SummaryStats");
            if (index != -1) {
                workbook.removeSheetAt(index);
            }

            // Create new SummaryStats sheet
            Sheet summarySheet = workbook.createSheet("SummaryStats");
            Map<String, Integer> summaryData = new HashMap<>();
            summaryData.put("Total Test Cases", totalTestCases);
            summaryData.put("Manual Test Cases", manualTestCases);
            summaryData.put("Automated Test Cases", automatedTestCases);
            summaryData.put("Total Bugs", totalBugs);
            summaryData.put("Critical Bugs", criticalBugs);
            summaryData.put("Major Bugs", majorBugs);
            summaryData.put("Minor Bugs", minorBugs);

            int rowIndex = 0;
            for (Map.Entry<String, Integer> entry : summaryData.entrySet()) {
                Row row = summarySheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(entry.getKey());
                row.createCell(1).setCellValue(entry.getValue());
            }

            // Write back to the file
            try (FileOutputStream fos = new FileOutputStream(excelFilePath)) {
                workbook.write(fos);
            }

            System.out.println("✅ SummaryStats sheet generated and saved successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Failed to generate SummaryStats.");
        }
		return false;
    }
}
