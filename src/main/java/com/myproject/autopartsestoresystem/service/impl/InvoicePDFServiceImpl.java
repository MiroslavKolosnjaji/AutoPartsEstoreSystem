package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.InvoiceDTO;
import com.myproject.autopartsestoresystem.dto.InvoiceItemDTO;
import com.myproject.autopartsestoresystem.exception.service.InvoiceNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.InvoicePDFGenerationFailedException;
import com.myproject.autopartsestoresystem.model.InvoiceItem;
import com.myproject.autopartsestoresystem.service.EmailService;
import com.myproject.autopartsestoresystem.service.InvoicePDFService;
import com.myproject.autopartsestoresystem.service.InvoiceService;
import com.myproject.autopartsestoresystem.service.PDFGenerator;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class InvoicePDFServiceImpl implements InvoicePDFService {

    private final InvoiceService invoiceService;
    private final PDFGenerator pdfGenerator;
    private final EmailService emailService;


    @Override
    public void generateAndSendInvoicePDF(Long invoiceId, String recipientEmail) throws InvoicePDFGenerationFailedException {
        try{
            InvoiceDTO invoiceDTO = invoiceService.getInvoice(invoiceId);

            byte[] pdfBytes = pdfGenerator.generateInvoicePDF(invoiceDTO);

            ByteArrayResource pdfAttachmentResource = new ByteArrayResource(pdfBytes);

            emailService.sendMessageWithPDFAttachment(recipientEmail,"Invoice", "Please find your invoice attached.", pdfAttachmentResource);

        } catch (InvoiceNotFoundException | MessagingException e) {
            throw new InvoicePDFGenerationFailedException(e.getMessage());
        }
    }
}
