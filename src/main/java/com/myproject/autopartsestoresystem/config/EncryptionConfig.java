package com.myproject.autopartsestoresystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

/**
 * @author Miroslav Kološnjaji
 */
@Configuration
public class EncryptionConfig {

    private static final String ENCRYPTION_KEY = "encryptionKey";
    private static final String ENCRYPTION_SALT = "encryptionSalt";

    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.text(ENCRYPTION_KEY, ENCRYPTION_SALT);
    }
}
