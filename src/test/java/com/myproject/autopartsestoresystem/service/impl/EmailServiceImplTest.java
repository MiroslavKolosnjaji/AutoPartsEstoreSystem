package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.notifications.service.impl.EmailServiceImpl;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    void testSendMessageWithPDFAttachment_whenValidInputProvided_thenCorrect() throws MessagingException, IOException {

        //given
        String to = "test@test.com";
        String subject = "test";
        String text = "Text Body";
        ByteArrayResource attachment = new ByteArrayResource("PDF content".getBytes());

        mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        InternetAddress toAddress = new InternetAddress(to);

        doNothing().when(mimeMessage).setRecipient(eq(Message.RecipientType.TO), eq(toAddress));
        doNothing().when(mimeMessage).setSubject(anyString());

        //when
        emailService.sendMessageWithPDFAttachment(to, subject, text, attachment);

        //then
        ArgumentCaptor<MimeMessage> messageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        MimeMessage sentMessage = messageCaptor.getValue();

        verify(mimeMessage).setRecipient(eq(Message.RecipientType.TO), eq(toAddress));
        verify(sentMessage).setSubject(eq(subject));

        verify(mimeMessage).setSubject(eq(subject));
        verify(mimeMessage).setRecipient(eq(Message.RecipientType.TO), eq(toAddress));

        verify(mimeMessage).setContent(any(Multipart.class));
        verifyNoMoreInteractions(mimeMessage);
    }
}