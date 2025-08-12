package com.qasummarygen.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentNameDictionary;
import org.apache.pdfbox.pdmodel.PDEmbeddedFilesNameTreeNode;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.common.filespecification.PDEmbeddedFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

/**
 * Utility class to attach an Excel file inside a PDF as an embedded file.
 * Business users can download it from the PDF attachments panel (📎) in Acrobat Reader.
 */
public class AttachExcelToPDF {

    /**
     * Attaches an Excel file to the given PDF document.
     *
     * @param document       The PDDocument to which the Excel file will be attached
     * @param excelFilePath  Path to the Excel file
     * @throws IOException   If file not found or error occurs
     */
    public static void attach(PDDocument document, String excelFilePath) throws IOException {
        File file = new File(excelFilePath);
        if (!file.exists()) {
            System.err.println("❌ Excel file not found: " + excelFilePath);
            return;
        }

        // Create File Specification
        PDComplexFileSpecification fileSpec = new PDComplexFileSpecification();
        fileSpec.setFile(file.getName());

        // Create Embedded File
     // Create Embedded File
        try (FileInputStream fis = new FileInputStream(file)) {
            PDEmbeddedFile embeddedFile = new PDEmbeddedFile(document, fis);
            embeddedFile.setSubtype("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            embeddedFile.setSize((int) file.length());

            // ✅ Use Calendar instead of Date
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTimeInMillis(file.lastModified());
            embeddedFile.setModDate(calendar);

            // Link Embedded File to File Spec
            fileSpec.setEmbeddedFile(embeddedFile);
        }

        // Create the Embedded Files Name Tree
        PDEmbeddedFilesNameTreeNode efTree = new PDEmbeddedFilesNameTreeNode();
        efTree.setNames(Collections.singletonMap(file.getName(), fileSpec));

        // Add the Embedded Files to the document
        PDDocumentNameDictionary names = new PDDocumentNameDictionary(document.getDocumentCatalog());
        names.setEmbeddedFiles(efTree);
        document.getDocumentCatalog().setNames(names);

        System.out.println("📎 Excel file attached to PDF: " + file.getAbsolutePath());
    }
}
