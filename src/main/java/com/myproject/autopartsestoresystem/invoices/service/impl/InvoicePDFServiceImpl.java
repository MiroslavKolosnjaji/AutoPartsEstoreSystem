package com.myproject.autopartsestoresystem.invoices.service.impl;

import com.myproject.autopartsestoresystem.invoices.dto.InvoiceDTO;
import com.myproject.autopartsestoresystem.invoices.exception.InvoiceNotFoundException;
import com.myproject.autopartsestoresystem.invoices.exception.InvoicePDFGenerationFailedException;
import com.myproject.autopartsestoresystem.service.EmailService;
import com.myproject.autopartsestoresystem.invoices.service.InvoicePDFService;
import com.myproject.autopartsestoresystem.invoices.service.InvoiceService;
import com.myproject.autopartsestoresystem.invoices.service.PDFGenerator;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

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
