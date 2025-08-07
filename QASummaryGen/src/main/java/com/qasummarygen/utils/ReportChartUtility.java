package com.qasummarygen.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class ReportChartUtility {

    // 1. Generate Pie Chart from Raw TestCases List
    public static BufferedImage generateTestCaseStatusChart(List<TestCaseUtils> testCases) {
        Map<String, Integer> statusCount = new HashMap<>();
        for (TestCaseUtils tc : testCases) {
            String status = tc.getStatus() != null ? tc.getStatus().trim() : "Unknown";
            statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
        }
        return generateTestCaseStatusChartFromSummary(statusCount);
    }

    // 2. Generate Pie Chart from Raw Bug List
    public static BufferedImage generateBugSeverityChart(List<BugUtils> bugs) {
        Map<String, Integer> severityCount = new HashMap<>();
        for (BugUtils bug : bugs) {
            String severity = bug.getSeverity() != null ? bug.getSeverity().trim() : "Unknown";
            severityCount.put(severity, severityCount.getOrDefault(severity, 0) + 1);
        }
        return generateBugSeverityChartFromSummary(severityCount);
    }

    // 3. NEW: Generate TestCaseStatus Chart using SummaryStats sheet data
    public static BufferedImage generateTestCaseStatusChartFromSummary(Map<String, Integer> testCaseStats) {
        if (testCaseStats == null || testCaseStats.isEmpty()) return null;

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        int total = 0;

        for (Map.Entry<String, Integer> entry : testCaseStats.entrySet()) {
            if (entry.getValue() > 0) {
                dataset.setValue(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue());
                total += entry.getValue();
            }
        }

        dataset.setValue("Total Test Cases (" + total + ")", total);

        JFreeChart chart = ChartFactory.createPieChart(
                "Test Case Execution Status",
                dataset,
                true,
                true,
                false
        );

        formatPieChart(chart);
        return chart.createBufferedImage(500, 300);
    }

    // 4. NEW: Generate BugSeverity Chart using SummaryStats sheet data
    public static BufferedImage generateBugSeverityChartFromSummary(Map<String, Integer> bugStats) {
        if (bugStats == null || bugStats.isEmpty()) return null;

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        int total = 0;

        for (Map.Entry<String, Integer> entry : bugStats.entrySet()) {
            if (entry.getValue() > 0) {
                dataset.setValue(entry.getKey() + " Bugs (" + entry.getValue() + ")", entry.getValue());
                total += entry.getValue();
            }
        }

        dataset.setValue("Total Bugs (" + total + ")", total);

        JFreeChart chart = ChartFactory.createPieChart(
                "Bug Severity Distribution",
                dataset,
                true,
                true,
                false
        );

        formatPieChart(chart);
        return chart.createBufferedImage(500, 300);
    }

    // 5. Common pie chart formatting
    private static void formatPieChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
    }

    // 6. Static report description
    public static String getStaticReportDescription() {
        return "This PDF report presents a QA summary for the sprint, including the number of test cases "
             + "(manual and automated), execution results, bug distribution by severity, and key suggestions. "
             + "Charts have been provided to visualize execution trends and bug severity distribution.";
    }
    
    public static BufferedImage generateBugSeverityChart(Map<String, Integer> bugSeverityCounts) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Integer> entry : bugSeverityCounts.entrySet()) {
            dataset.addValue(entry.getValue(), "Bug Severity", entry.getKey());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
            "Bug Severity Chart", "Severity", "Count", dataset,
            PlotOrientation.VERTICAL, false, true, false
        );

        return barChart.createBufferedImage(500, 300);
    }
    
    public static BufferedImage generateTestCaseStatusChart(Map<String, Integer> testCaseCounts) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Integer> entry : testCaseCounts.entrySet()) {
            dataset.addValue(entry.getValue(), "Test Case Status", entry.getKey());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
            "Test Case Status Chart", "Status", "Count", dataset,
            PlotOrientation.VERTICAL, false, true, false
        );

        return barChart.createBufferedImage(500, 300);
    }


}
