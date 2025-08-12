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
import org.jfree.data.general.DefaultPieDataset;

/**
 * Utility class for generating Test Case & Bug Charts for PDF QA Summary Reports.
 */
public class ReportChartUtility {

    private static final int CHART_WIDTH = 500;
    private static final int CHART_HEIGHT = 300;

    /** =========================
     *  PUBLIC METHODS (For PDF)
     *  =========================
     */

    public static BufferedImage generateTestCaseChart(QASummary summary) {
        if (summary == null || summary.getTestCases() == null || summary.getTestCases().isEmpty()) {
            return null;
        }
        Map<String, Integer> statusCount = new HashMap<>();
        for (TestCaseUtils tc : summary.getTestCases()) {
            String status = (tc.getStatus() != null) ? tc.getStatus().trim() : "Unknown";
            statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
        }
        return generatePieChart(statusCount, "Test Case Execution Status");
    }

    public static BufferedImage generateBugChart(QASummary summary) {
        if (summary == null || summary.getBugs() == null || summary.getBugs().isEmpty()) {
            return null;
        }
        Map<String, Integer> severityCount = new HashMap<>();
        for (BugUtils bug : summary.getBugs()) {
            String severity = (bug.getSeverity() != null) ? bug.getSeverity().trim() : "Unknown";
            severityCount.put(severity, severityCount.getOrDefault(severity, 0) + 1);
        }
        return generatePieChart(severityCount, "Bug Severity Distribution");
    }

    /** =========================
     *  PRIVATE HELPERS
     *  =========================
     */

    private static BufferedImage generatePieChart(Map<String, Integer> data, String title) {
        if (data == null || data.isEmpty()) {
            return null;
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

    private static void formatPieChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
    }

    /** =========================
     *  STATIC DESCRIPTION
     *  =========================
     */
    public static String getStaticReportDescription() {
        return "This PDF report presents a QA summary for the sprint, including the number of test cases "
             + "(manual and automated), execution results, bug distribution by severity, and key suggestions. "
             + "This Report provide a visual overview of testing progress and defect trends, along with the attached summary details.";
    }
    
    /** =========================
     *  STATIC NOTE FOR EXCEL DOWNLOAD
     *  =========================
     */
    public static String getExcelDownloadNote() {
        return "Note: This report contains an attached Excel file with detailed QA metrics. To access it, open the PDF in Adobe Acrobat and check the Attachments panel.\n"
             + "To download it:\n"
             + "1. Go to View-Show/Hide-Navigation Panes-Attachments.\n"
             + "2. You will see a list of attached files (your .xlsx will be there)\n"
             + "3. Right-click the Excel file and choose 'Save As' to download.";
    }
}
