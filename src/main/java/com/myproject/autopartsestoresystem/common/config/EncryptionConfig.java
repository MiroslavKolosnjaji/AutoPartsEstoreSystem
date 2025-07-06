package com.myproject.autopartsestoresystem.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

/**
 * @author Miroslav Kološnjaji
 */
@Configuration
public class EncryptionConfig {

    private static final String ENCRYPTION_KEY = "00112233445566778899AABBCCDDEEFF";;
    private static final String ENCRYPTION_SALT = "1122334455667788";

    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.text(ENCRYPTION_KEY, ENCRYPTION_SALT);
    }
}
