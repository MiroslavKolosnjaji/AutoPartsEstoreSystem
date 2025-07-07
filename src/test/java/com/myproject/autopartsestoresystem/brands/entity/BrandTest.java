package com.myproject.autopartsestoresystem.brands.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class BrandTest {

    @Test
    void testAllFields_isEqual_thenCorrect() {
        //given
        Brand brand1 = Brand.builder()
                .id(1)
                .name("BMW")
                .models(null)
                .build();


        Brand brand2 = Brand.builder()
                .id(1)
                .name("BMW")
                .models(null)
                .build();

        boolean equals = brand1.equals(brand2);
        boolean hashCode = brand1.hashCode() == brand2.hashCode();

        //when && then
        assertAll("Brand fields validation",
                () -> assertEquals(brand1.getId(), brand2.getId(), "Brand ID mismatch"),
                () -> assertEquals(brand1.getName(), brand2.getName(), "Brand name mismatch"),
                () -> assertEquals(brand1.getModels(), brand2.getModels(), "Brand models mismatch"));

        assertTrue(equals, "Brand is not equal");
        assertTrue(hashCode, "Brand hash code is not equal");
    }
}