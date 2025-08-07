package com.qasummarygen.utils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.nio.file.Paths;

public class MainRunner {

    public static void main(String[] args) {

        // ✅ 1. Provide path to your Excel input file
        String excelPath = "C:\\Users\\SREETOMA\\OneDrive\\Desktop\\vibeCoding\\QASummaryGen\\src\\main\\java\\Resource\\sprint_data.xlsx";

        // ✅ 2. Generate timestamp for output file naming
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        // ✅ 3. Define output folder and final PDF path
        String outputFolder = "output";
        String outputPdfPath = outputFolder + "/QASummaryReport_" + timestamp + ".pdf";

        // ✅ 4. Ensure 'output' folder exists
        File folder = new File(outputFolder);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (created) {
                System.out.println("📁 'output' directory created.");
            }
        }

        // ✅ 5. Read summary from Excel
        QASummary qaSummary = ExcelReader.readSummaryFromExcel(excelPath);

        // ✅ 6. Generate PDF report
        try {
            PDFReportUtility.generatePDFReport(qaSummary, outputPdfPath);
            System.out.println("✅ PDF report successfully generated at: " + outputPdfPath);
        } catch (Exception e) {
            System.err.println("❌ Failed to generate PDF report: " + e.getMessage());
            e.printStackTrace();
        }
        
		/*
		 * String enhancedWithLinksPdfPath = "output/QASummaryReport_Links_" + timestamp
		 * + ".pdf"; PDFEnhancer_QuickLinks.addQuickLinksToPDF(outputPdfPath,
		 * enhancedWithLinksPdfPath);
		 */    }
    
    

}
