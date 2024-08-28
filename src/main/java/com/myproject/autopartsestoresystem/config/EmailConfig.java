package com.myproject.autopartsestoresystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * @author Miroslav Kološnjaji
 */
@Configuration
public class EmailConfig {

    private String host;
    private int port;
    private String username;
    private String password;
    private String smtpAuth;
    private String starttlsEnable;
    private String defaultEncoding;

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", starttlsEnable);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.debug", "true");

        mailSender.setDefaultEncoding(defaultEncoding);

        return mailSender;
    }
}
