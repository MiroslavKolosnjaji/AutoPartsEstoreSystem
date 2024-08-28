package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.dto.InvoiceDTO;
import com.myproject.autopartsestoresystem.exception.service.InvoicePDFGenerationFailedException;
import com.myproject.autopartsestoresystem.model.InvoiceItem;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Miroslav Kološnjaji
 */
@Component
public class PDFGenerator {


    private static final int LOGO_X_POSITION = 50;
    private static final int LOGO_Y_POSITION = 700;
    private static final int LOGO_WIDTH = 500;
    private static final int LOGO_HEIGHT = 120;

    private static final int TITLE_X_POSITION = 50;
    private static final int TITLE_Y_POSITION = 650;
    private static final int TITLE_FONT_SIZE = 12;

    private static final int HEADER_X_POSITION = 50;
    private static final int HEADER_Y_POSITION = 605;
    private static final int HEADER_FONT_SIZE = 12;

    private static final int TABLE_X_POSITION = 50;
    private static final int TABLE_Y_POSITION = 600;
    private static final int TABLE_WIDTH = 500;
    private static final int TABLE_HEADER_HEIGHT = 20;
    private static final float TABLE_LEADING = 14.5f;
    private static final int ROW_START_Y_POSITION = 580;


    public byte[] generateInvoicePDF(InvoiceDTO invoiceDTO) throws InvoicePDFGenerationFailedException {

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();


        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

            document.addPage(page);

            createLogo(document, contentStream);
            createInvoiceTitle(contentStream);
            createHeader(invoiceDTO, contentStream);
            createTable(contentStream);
            putInvoiceItemsInfo(contentStream, invoiceDTO);

            contentStream.endText();


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            throw new InvoicePDFGenerationFailedException("Error while generating Invoice PDF", e);
        }
    }


    public void generatePDFToFile(InvoiceDTO invoiceDTO) throws InvoicePDFGenerationFailedException, IOException {
        byte[] pdfBytes = generateInvoicePDF(invoiceDTO);
        Files.write(Paths.get("invoice.pdf"), pdfBytes);
    }

    private void createLogo(PDDocument document, PDPageContentStream contentStream) throws IOException {
        PDImageXObject logo = PDImageXObject.createFromFile("images/AP_Logo.png", document);
        contentStream.drawImage(logo, LOGO_X_POSITION, LOGO_Y_POSITION, LOGO_WIDTH, LOGO_HEIGHT);
    }

    private void createInvoiceTitle(PDPageContentStream contentStream) throws IOException {
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), TITLE_FONT_SIZE);
        contentStream.newLineAtOffset(TITLE_X_POSITION, TITLE_Y_POSITION);
        contentStream.showText("INVOICE");
        contentStream.newLine();
    }

    private void createHeader(InvoiceDTO invoiceDTO, PDPageContentStream contentStream) throws IOException {
        contentStream.showText("Invoice Number: " + invoiceDTO.getId().toString());
        contentStream.newLine();
        contentStream.showText("Date Issued: ");
        contentStream.newLine();
        contentStream.showText("Purchase Order ID: " + invoiceDTO.getPurchaseOrder().getPurchaseOrderNumber().toString());
    }

    private void createTable(PDPageContentStream contentStream) throws IOException {
        contentStream.setNonStrokingColor(Color.LIGHT_GRAY);
        contentStream.addRect(TABLE_X_POSITION, TABLE_Y_POSITION, TABLE_WIDTH, TABLE_HEADER_HEIGHT);
        contentStream.fill();

        contentStream.setNonStrokingColor(Color.RED);
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), HEADER_FONT_SIZE);
        contentStream.beginText();
        contentStream.newLineAtOffset(HEADER_X_POSITION, HEADER_Y_POSITION);
        contentStream.showText(String.format("%-20s %-10s %-10s %-10s %-10s %-10s",
                "Product Name", "Quantity", "Unit Price", "Discount", "Tax", "Total Price"));


        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), HEADER_FONT_SIZE);
        contentStream.beginText();
        contentStream.setLeading(TABLE_LEADING);
        contentStream.newLineAtOffset(TABLE_X_POSITION, ROW_START_Y_POSITION);
    }

    private void putInvoiceItemsInfo(PDPageContentStream contentStream, InvoiceDTO invoiceDTO) throws IOException {

        for (InvoiceItem invoiceItem : invoiceDTO.getInvoiceItems()) {

            String productName = invoiceItem.getProductName();
            String quantity = invoiceItem.getQuantity().toString();
            String unitPrice = invoiceItem.getUnitPrice().toString();
            String totalPrice = invoiceItem.getTotalPrice().toString();
            String discount = invoiceItem.getDiscountPercentage().toString();
            String tax = invoiceItem.getTaxPercentage().toString();

            contentStream.showText(String.format("%-20s %-10s %-10s %-10s %-10s %-10s",
                    productName, quantity, unitPrice, discount, tax, totalPrice));
            contentStream.newLine();
        }
    }

}
