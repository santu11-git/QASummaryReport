package com.qasummarygen.utils;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.HorizontalAlignment;
import be.quodlibet.boxable.Row;

public class PDFReportUtility {

    public static void generatePDFReport(QASummary summary, String outputFilePath) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        float margin = 50;
        PDRectangle pageSize = page.getMediaBox();
        float yStart = pageSize.getHeight() - margin;
        float tableWidth = pageSize.getWidth() - (2 * margin);
        float yPosition = yStart;

        // Add Quick Links
        yPosition = addQuickLinks(document, page, margin, yPosition);

        // Add Description
        yPosition = addDescription(document, page, margin, yPosition - 20, ReportChartUtility.getStaticReportDescription());

        // Table Start
        BaseTable table = new BaseTable(yPosition - 20, yStart, margin, tableWidth, margin, document, page, true, true);
        addHeaderRow(table, "QA Summary Report");
        addProjectDetails(table, summary);
        addTestCaseMetrics(table, summary);
        addBugMetrics(table, summary);
        addQualityMetrics(table, summary);
        addSuggestions(table, summary.getSuggestions());
        addFooter(table, summary.getGeneratedBy());
        table.draw();

        saveDocument(document, outputFilePath);
    }

    // ----------------- CORE SECTIONS --------------------

    private static float addQuickLinks(PDDocument document, PDPage page, float margin, float yPosition) throws IOException {
        PDPageContentStream cs = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

        PDType1Font fontBold = PDType1Font.HELVETICA_BOLD;
        PDType1Font fontRegular = PDType1Font.HELVETICA;
        int fontSize = 12;
        float leading = 1.5f * fontSize;

        cs.beginText();
        cs.setFont(fontBold, fontSize);
        cs.newLineAtOffset(margin, yPosition);
        cs.showText("Quick Links:");
        cs.endText();

        yPosition -= leading;

        String[] links = {
            "Release Confluence Page - https://your-confluence-url",
            "JIRA Board - https://your-jira-board-url",
            "JIRA XRay Report - https://your-jira-xray-url"
        };
           

        for (String link : links) {
            cs.beginText();
            cs.setFont(fontRegular, fontSize);
            cs.newLineAtOffset(margin, yPosition);
            cs.showText(link);
            cs.endText();
            yPosition -= leading;
        }

        cs.close();
        return yPosition;
    }
    
    

    public static float addDescription(PDDocument document, PDPage page, float margin, float yPosition, String descriptionText) throws IOException {
        PDPageContentStream cs = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

        PDType1Font boldFont = PDType1Font.HELVETICA_BOLD;
        PDType1Font regularFont = PDType1Font.HELVETICA;
        int headingFontSize = 12;
        int fontSize = 11;
        float leading = 1.5f * fontSize;
        float width = PDRectangle.A4.getWidth() - 2 * margin;
        float textY = yPosition;

        cs.beginText();
        cs.setFont(boldFont, headingFontSize);
        cs.newLineAtOffset(margin, textY);
        cs.showText("Report Description:");
        cs.endText();

        textY -= leading;

        List<String> lines = getWrappedLines(descriptionText, regularFont, fontSize, width);
        for (String line : lines) {
            cs.beginText();
            cs.setFont(regularFont, fontSize);
            cs.newLineAtOffset(margin, textY);
            cs.showText(line);
            cs.endText();
            textY -= leading;
        }

        cs.close();
        return textY;
    }

    private static List<String> getWrappedLines(String text, PDType1Font font, int fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (String word : text.split(" ")) {
            String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
            float textWidth = font.getStringWidth(testLine) / 1000 * fontSize;

            if (textWidth > maxWidth) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            } else {
                currentLine = new StringBuilder(testLine);
            }
        }

        if (currentLine.length() > 0) lines.add(currentLine.toString());
        return lines;
    }

    private static void saveDocument(PDDocument document, String outputFilePath) throws IOException {
        File file = new File(outputFilePath);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            document.save(fos);
        } finally {
            document.close();
        }
        System.out.println("✅ PDF report generated successfully: " + file.getAbsolutePath());
    }

    // ---------------- TABLE HELPERS ----------------

    private static void addHeaderRow(BaseTable table, String title) {
        Row<PDPage> headerRow = table.createRow(20);
        Cell<PDPage> headerCell = headerRow.createCell(100, title);
        headerCell.setFont(PDType1Font.HELVETICA_BOLD);
        headerCell.setFontSize(16);
        headerCell.setFillColor(new Color(173, 216, 230));
        headerCell.setAlign(HorizontalAlignment.CENTER);
        table.addHeaderRow(headerRow);
    }

    private static void addRow(BaseTable table, String key, String value) {
        Row<PDPage> row = table.createRow(16);
        Cell<PDPage> cell1 = row.createCell(30, key);
        Cell<PDPage> cell2 = row.createCell(70, value);
        cell1.setFont(PDType1Font.HELVETICA_BOLD);
        cell2.setFont(PDType1Font.HELVETICA);
        cell1.setFontSize(11);
        cell2.setFontSize(11);
    }

    private static void addProjectDetails(BaseTable table, QASummary summary) {
        addRow(table, "Project Name", summary.getProjectName());
        addRow(table, "Sprint Name", summary.getSprintName());
        addRow(table, "Sprint Duration", summary.getSprintStartDate() + " to " + summary.getSprintEndDate());
    }

    private static void addTestCaseMetrics(BaseTable table, QASummary summary) {
        addRow(table, "Total Test Cases", String.valueOf(summary.getTotalTestCases()));
        addRow(table, "Automated Test Cases", String.valueOf(summary.getAutomatedTestCases()));
        addRow(table, "Manual Test Cases", String.valueOf(summary.getManualTestCases()));
        addRow(table, "Stories Delivered", String.valueOf(summary.getTotalStoriesDelivered()));
    }

    private static void addBugMetrics(BaseTable table, QASummary summary) {
        addRow(table, "Total Bugs", String.valueOf(summary.getTotalBugs()));
        addRow(table, "Critical Bugs", String.valueOf(summary.getCriticalBugs()));
        addRow(table, "Major Bugs", String.valueOf(summary.getMajorBugs()));
        addRow(table, "Minor Bugs", String.valueOf(summary.getMinorBugs()));
    }

    private static void addQualityMetrics(BaseTable table, QASummary summary) {
        addRow(table, "Test Coverage (%)", String.format("%.2f", summary.getTestCoverage()));
        addRow(table, "Bug Density", String.format("%.2f", summary.getBugDensity()));
        addRow(table, "Sprint Velocity", String.valueOf(summary.getSprintVelocity()));
    }

    private static void addSuggestions(BaseTable table, List<String> suggestions) {
        if (suggestions != null && !suggestions.isEmpty()) {
            for (int i = 0; i < suggestions.size(); i++) {
                addRow(table, "Suggestion " + (i + 1), suggestions.get(i));
            }
        }
    }

    private static void addFooter(BaseTable table, String generatedBy) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        addRow(table, "Generated On", now);
        addRow(table, "Generated By", generatedBy);
    }
    
    private static void drawDividerLine(PDPageContentStream cs, float startX, float y, float width) throws IOException {
        cs.setStrokingColor(Color.GRAY);  // Light gray divider
        cs.setLineWidth(0.5f);
        cs.moveTo(startX, y);
        cs.lineTo(startX + width, y);
        cs.stroke();
    }

}
