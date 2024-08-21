package com.myproject.autopartsestoresystem;

import com.myproject.autopartsestoresystem.dto.InvoiceDTO;
import com.myproject.autopartsestoresystem.dto.InvoiceItemDTO;
import com.myproject.autopartsestoresystem.model.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootApplication
public class AutoPartsEstoreSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoPartsEstoreSystemApplication.class, args);
    }

}
