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

/**
 * PDFReportUtility
 *
 * - Keeps your existing table/description logic intact
 * - Adds page border, horizontal dividers and places quick links AFTER the table
 * - Uses Helvetica-safe output (strips unsupported characters like emoji)
 * - Properly opens/closes PDPageContentStream to avoid null-output NPEs
 */
public class PDFReportUtility {

    public static void generatePDFReport(QASummary summary, String outputFilePath) throws IOException {
        PDDocument document = new PDDocument();
        try {
            // first page
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            float margin = 50;
            PDRectangle pageSize = page.getMediaBox();
            float yStart = pageSize.getHeight() - margin;
            float tableWidth = pageSize.getWidth() - (2 * margin);
            float yPosition = yStart;

            // 1) Description on page 1
            addDescription(document, page, margin, yPosition, ReportChartUtility.getStaticReportDescription());
            // reduce Y for table start (estimate space consumed by description)
            yPosition -= 80;

            // 2) Create the table (Boxable handles its own content stream internally)
            BaseTable table = new BaseTable(yPosition, yStart, margin, tableWidth, margin, document, page, true, true);
            addHeaderRow(table, "QA Summary Report");
            addProjectDetails(table, summary);
            addTestCaseMetrics(table, summary);
            addBugMetrics(table, summary);
            addQualityMetrics(table, summary);
            addSuggestions(table, summary.getSuggestions());
            addFooter(table, safe(summary.getGeneratedBy()));
            // draw table
            table.draw();
            
            

            // 3) Draw a divider at the bottom of page 1 to separate content visually
            // We cannot reliably compute exact table-end y, so place a divider near the bottom page
            float dividerY = 120; // safe location above footer/margins (adjust if you need different)
         //   drawHorizontalDivider(document, page, margin, dividerY, tableWidth);

            // 4) Add a second page for Quick Links (always after the table)
            PDPage quickPage = new PDPage(PDRectangle.A4);
            document.addPage(quickPage);

            // draw page border for both pages (append border on page1 and page2)
            drawPageBorder(document, page, 20f);       // draw border on page 1 (20 margin inside)
            drawPageBorder(document, quickPage, 20f);  // draw border on quick links page

            // 5) Add a small header on quickPage and the Quick Links content
            float quickMargin = 50;
            float quickY = quickPage.getMediaBox().getHeight() - quickMargin;
            addQuickLinks(document, quickPage, quickMargin, quickY);

            // draw divider under quick links
            float quickDividerY = quickY - 80f; // enough space for quick links; adjust if you add more links
            drawHorizontalDivider(document, quickPage, quickMargin, quickDividerY, tableWidth);

            
         // Attach the Excel file to PDF:
			/*
			 * System.out.println("Excel file path in summary: " +
			 * summary.getExcelFilePath()); if (summary.getExcelFilePath() != null &&
			 * !summary.getExcelFilePath().isEmpty()) { AttachExcelToPDF.attach(document,
			 * summary.getExcelFilePath()); System.out.println("Document attached"); }
			 */
            
            AttachExcelToPDF.attach(document, "C:\\Users\\SREETOMA\\git\\QASummaryReport\\QASummaryGen\\src\\main\\java\\Resource\\sprint_data.xlsx");
           
            // 6) Save document
            saveDocument(document, outputFilePath);

        } finally {
            try {
                document.close();
            } catch (Exception ignored) {}
        }
    }

    // ----------------------- Helpers & sections -----------------------

    private static void addHeaderRow(BaseTable table, String title) {
        Row<PDPage> headerRow = table.createRow(20);
        Cell<PDPage> headerCell = headerRow.createCell(100, safe(title));
        headerCell.setFont(PDType1Font.HELVETICA_BOLD);
        headerCell.setFontSize(16);
        headerCell.setFillColor(new Color(173, 216, 230));
        headerCell.setAlign(HorizontalAlignment.CENTER);
        table.addHeaderRow(headerRow);
    }

    private static void addProjectDetails(BaseTable table, QASummary summary) {
        addRow(table, "Project Name", safe(summary.getProjectName()));
        // Sprint Name and Duration may be in the SprintSummary; try both locations
        String sprintName = safe(summary.getSprintName());
        if ((sprintName == null || sprintName.isBlank()) && summary.getSprintSummary() != null) {
            sprintName = safe(summary.getSprintSummary().getSprintName());
        }
        String sprintStart = safe(summary.getSprintStartDate());
        String sprintEnd = safe(summary.getSprintEndDate());
        if ((sprintStart == null || sprintStart.isBlank() || sprintEnd == null || sprintEnd.isBlank())
                && summary.getSprintSummary() != null) {
            if (sprintStart == null || sprintStart.isBlank())
                sprintStart = safe(summary.getSprintSummary().getSprintStartDate());
            if (sprintEnd == null || sprintEnd.isBlank())
                sprintEnd = safe(summary.getSprintSummary().getSprintEndDate());
        }

        addRow(table, "Sprint Name", sprintName == null ? "" : sprintName);
        addRow(table, "Sprint Start Date", sprintStart == null ? "" : sprintStart);
        addRow(table, "Sprint End Date", sprintEnd == null ? "" : sprintEnd);
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
                addRow(table, "Suggestion " + (i + 1), safe(suggestions.get(i)));
            }
        }
    }

    private static void addFooter(BaseTable table, String generatedBy) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        addRow(table, "Generated On", now);
        addRow(table, "Generated By", safe(generatedBy));
    }

    private static void addRow(BaseTable table, String key, String value) {
        Row<PDPage> row = table.createRow(16);
        Cell<PDPage> cell1 = row.createCell(30, safe(key));
        Cell<PDPage> cell2 = row.createCell(70, safe(value));
        cell1.setFont(PDType1Font.HELVETICA_BOLD);
        cell2.setFont(PDType1Font.HELVETICA);
        cell1.setFontSize(11);
        cell2.setFontSize(11);
    }

    // ---------------- Description ----------------
    public static void addDescription(PDDocument document, PDPage page, float margin, float yPosition, String descriptionText) throws IOException {
        // Use append mode and close afterwards to avoid interfering with BaseTable
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
            String heading = "Report Description:";
            PDType1Font boldFont = PDType1Font.HELVETICA_BOLD;
            PDType1Font regularFont = PDType1Font.HELVETICA;
            int headingFontSize = 12;
            int fontSize = 11;
            float leading = 1.5f * fontSize;
            float width = PDRectangle.A4.getWidth() - 2 * margin;
            float textY = yPosition;

            // Heading
            contentStream.beginText();
            contentStream.setFont(boldFont, headingFontSize);
            contentStream.newLineAtOffset(margin, textY);
            contentStream.showText(stripUnsupportedCharacters(heading));
            contentStream.endText();

            // lines
            List<String> lines = getWrappedLines(descriptionText, regularFont, fontSize, width);
            textY -= leading;

            for (String line : lines) {
                contentStream.beginText();
                contentStream.setFont(regularFont, fontSize);
                contentStream.newLineAtOffset(margin, textY);
                contentStream.showText(stripUnsupportedCharacters(line));
                contentStream.endText();
                textY -= leading;
            }

            // divider right after description
            
            float quickDividerY = textY - 10;
            drawHorizontalDivider(document, page, margin, quickDividerY, page.getMediaBox().getWidth() - 2 * margin);
        }
    }

    // ---------------- Quick links (new page) ----------------
    private static void addQuickLinks(PDDocument document, PDPage page, float margin, float yPosition) throws IOException {
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
            PDType1Font boldFont = PDType1Font.HELVETICA_BOLD;
            PDType1Font regularFont = PDType1Font.HELVETICA;
            int fontSize = 12;
            float leading = 1.5f * fontSize;
            float y = yPosition;

            contentStream.beginText();
            contentStream.setFont(boldFont, 14);
            contentStream.newLineAtOffset(margin, y);
            contentStream.showText(stripUnsupportedCharacters("Quick Links:"));
            contentStream.endText();

            y -= leading;

            String[][] links = {
                    {"Release Confluence Page", "https://your-confluence-url"},
                    {"JIRA Board", "https://your-jira-board-url"},
                    {"JIRA XRay Report", "https://your-jira-xray-url"}
            };

            for (String[] link : links) {
                contentStream.beginText();
                contentStream.setFont(regularFont, fontSize);
                contentStream.newLineAtOffset(margin, y);
                contentStream.showText(stripUnsupportedCharacters(link[0] + " - " + link[1]));
                contentStream.endText();
                y -= leading;
            }
         // Draw divider after Quick Links
            float quickDividerY = yPosition - (leading * (links.length + 1));
            drawHorizontalDivider(document, page, margin, quickDividerY, page.getMediaBox().getWidth() - 2 * margin);
            
            float noteStartY = quickDividerY - 20; // gap after divider
            
            
            
            try (PDPageContentStream contentStream1 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
                List<String> noteLines = getWrappedLines(ReportChartUtility.getExcelDownloadNote(), regularFont, 8, page.getMediaBox().getWidth() - 2 * margin);
                for (String line : noteLines) {
                    contentStream1.beginText();
                    contentStream1.setFont(regularFont, 8);
                    contentStream1.newLineAtOffset(margin, noteStartY);
                    contentStream1.showText(stripUnsupportedCharacters(line));
                    contentStream1.endText();
                    noteStartY -= 14; // line spacing
                }
                
                // Draw divider after the note
                drawHorizontalDivider(document, page, margin, noteStartY - 5, page.getMediaBox().getWidth() - 2 * margin);
            }
        }
    }
    
    

    // ---------------- Page border & divider utilities ----------------

    private static void drawPageBorder(PDDocument document, PDPage page, float innerMargin) throws IOException {
        // Append border after content: open/close contentStream safely
        try (PDPageContentStream cs = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
            float w = page.getMediaBox().getWidth();
            float h = page.getMediaBox().getHeight();
            float x = innerMargin;
            float y = innerMargin;
            float width = w - 2 * innerMargin;
            float height = h - 2 * innerMargin;

            cs.setStrokingColor(Color.DARK_GRAY);
            cs.setLineWidth(1.0f);
            cs.addRect(x, y, width, height);
            cs.stroke();
        }
    }

    private static void drawHorizontalDivider(PDDocument document, PDPage page, float startX, float y, float width) throws IOException {
        try (PDPageContentStream cs = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
            drawLineInternal(cs, startX, y, width);
        }
    }

    private static void drawLineInternal(PDPageContentStream cs, float startX, float y, float width) throws IOException {
        cs.setStrokingColor(Color.GRAY);
        cs.setLineWidth(0.7f);
        cs.moveTo(startX, y);
        cs.lineTo(startX + width, y);
        cs.stroke();
    }

    // ---------------- small helpers ----------------

    private static String safe(String s) {
        if (s == null) return "";
        return s;
    }

    private static String stripUnsupportedCharacters(String input) {
        if (input == null) return "";
        // keep common ASCII characters only to avoid PDFBox font issues (no emojis)
        return input.replaceAll("[^\\x00-\\x7F]", "");
    }

    private static List<String> getWrappedLines(String text, PDType1Font font, int fontSize, float maxWidth) throws IOException {
        if (text == null) return new ArrayList<>();
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        for (String word : text.split("\\s+")) {
            String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
            float textWidth = font.getStringWidth(stripUnsupportedCharacters(testLine)) / 1000 * fontSize;
            if (textWidth > maxWidth) {
                if (currentLine.length() > 0) lines.add(currentLine.toString());
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
            // don't close document here; caller will close in finally
        }
        System.out.println("✅ PDF report generated successfully: " + file.getAbsolutePath());
    }
    
    
}
