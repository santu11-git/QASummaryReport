package com.qasummarygen.utils;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.awt.Color;
import java.io.*;

public class PDFEnhancer_QuickLinks {

    public static void addQuickLinksToPDF(String originalPdfPath, String enhancedPdfPath) {
        try {
            PdfReader reader = new PdfReader(originalPdfPath);
            Document document = new Document();
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(enhancedPdfPath));
            document.open();

            // 1. Add new top page with quick links
            PdfContentByte cb = copy.getDirectContent();
            Document introDoc = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter tempWriter = PdfWriter.getInstance(introDoc, baos);
            introDoc.open();

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.BLACK);
            Font linkFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLUE);

            // Title
            Paragraph title = new Paragraph("Quick Links", headerFont);
            title.setSpacingAfter(10f);
            introDoc.add(title);

            // Link 1: Release Confluence Page
            Anchor confluenceLink = new Anchor("Release Confluence Page", linkFont);
            confluenceLink.setReference("https://your-confluence-url");
            introDoc.add(confluenceLink);
            introDoc.add(Chunk.NEWLINE);

            // Link 2: JIRA Board
            Anchor jiraBoardLink = new Anchor("JIRA Board", linkFont);
            jiraBoardLink.setReference("https://your-jira-board-url");
            introDoc.add(jiraBoardLink);
            introDoc.add(Chunk.NEWLINE);

            // Link 3: JIRA XRay Report
            Anchor xrayLink = new Anchor("JIRA XRay Report", linkFont);
            xrayLink.setReference("https://your-jira-xray-url");
            introDoc.add(xrayLink);
            introDoc.add(Chunk.NEWLINE);

            introDoc.close();

            // Convert introDoc to PDF page and add to final doc
            PdfReader introReader = new PdfReader(baos.toByteArray());
            PdfImportedPage introPage = copy.getImportedPage(introReader, 1);
            copy.addPage(introPage);

            // 2. Add original content pages
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                copy.addPage(copy.getImportedPage(reader, i));
            }

            document.close();
            reader.close();
            introReader.close();

            System.out.println("✅ PDF with quick links generated: " + enhancedPdfPath);

        } catch (Exception e) {
            System.err.println("❌ Error adding quick links to PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
}
