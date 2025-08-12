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

/**
 * Utility class for generating Test Case & Bug Charts for PDF QA Summary Reports.
 */
public class ReportChartUtility {

    private static final int CHART_WIDTH = 500;
    private static final int CHART_HEIGHT = 300;

    /** ----------------------------
     *  PIE CHART GENERATION METHODS
     *  ----------------------------
     */

    // From TestCase list
    public static BufferedImage generateTestCaseStatusPie(List<TestCaseUtils> testCases) {
        Map<String, Integer> statusCount = new HashMap<>();
        for (TestCaseUtils tc : testCases) {
            String status = tc.getStatus() != null ? tc.getStatus().trim() : "Unknown";
            statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
        }
        return generatePieChart(statusCount, "Test Case Execution Status");
    }

    // From Bug list
    public static BufferedImage generateBugSeverityPie(List<BugUtils> bugs) {
        Map<String, Integer> severityCount = new HashMap<>();
        for (BugUtils bug : bugs) {
            String severity = bug.getSeverity() != null ? bug.getSeverity().trim() : "Unknown";
            severityCount.put(severity, severityCount.getOrDefault(severity, 0) + 1);
        }
        return generatePieChart(severityCount, "Bug Severity Distribution");
    }
    

    // Generic Pie Chart generator
    private static BufferedImage generatePieChart(Map<String, Integer> data, String title) {
        if (data == null || data.isEmpty()) {
            return null; // No chart if no data
        }

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            if (entry.getValue() > 0) {
                dataset.setValue(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue());
            }
        }

        JFreeChart chart = ChartFactory.createPieChart(
                title,
                dataset,
                true,
                true,
                false
        );

        formatPieChart(chart);
        return chart.createBufferedImage(CHART_WIDTH, CHART_HEIGHT);
    }

    /** ----------------------------
     *  BAR CHART GENERATION METHODS
     *  ----------------------------
     */

    public static BufferedImage generateTestCaseStatusBar(Map<String, Integer> testCaseStats1) {
        return generateBarChart(testCaseStats1, "Test Case Status Chart", "Status", "Count");
    }

    public static BufferedImage generateBugSeverityBar(Map<String, Integer> bugSeverityCounts) {
        return generateBarChart(bugSeverityCounts, "Bug Severity Chart", "Severity", "Count");
    }

    private static BufferedImage generateBarChart(Map<String, Integer> data, String title, String xAxis, String yAxis) {
        if (data == null || data.isEmpty()) {
            return null;
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            dataset.addValue(entry.getValue(), title, entry.getKey());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                title, xAxis, yAxis, dataset,
                PlotOrientation.VERTICAL, false, true, false
        );

        barChart.setBackgroundPaint(Color.WHITE);
        return barChart.createBufferedImage(CHART_WIDTH, CHART_HEIGHT);
    }

    /** ----------------------------
     *  COMMON FORMATTING
     *  ----------------------------
     */
    private static void formatPieChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
    }

    /** ----------------------------
     *  STATIC REPORT DESCRIPTION
     *  ----------------------------
     */
    public static String getStaticReportDescription() {
        return "This PDF report presents a QA summary for the sprint, including the number of test cases "
             + "(manual and automated), execution results, bug distribution by severity, and key suggestions. "
             + "Charts provide a visual overview of testing progress and defect trends.";
    }
}
