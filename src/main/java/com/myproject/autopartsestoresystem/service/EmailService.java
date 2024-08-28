package com.myproject.autopartsestoresystem.service;

import jakarta.mail.MessagingException;
import org.springframework.core.io.ByteArrayResource;

/**
 * @author Miroslav Kološnjaji
 */
public interface EmailService {

    void sendMessageWithPDFAttachment(String to, String subject, String text, ByteArrayResource attachment) throws MessagingException;
}
