package com.myproject.autopartsestoresystem.models.entity;

import com.myproject.autopartsestoresystem.brands.entity.Brand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class ModelTest {

    @Test
    void testAllFields_isEqual_thenCorrect() {

        Model model1 = Model.builder()
                .id(new ModelId(1L,"318"))
                .brand(Brand.builder().build())
                .build();

        Model model2 = Model.builder()
                .id(new ModelId(1L,"318"))
                .brand(Brand.builder().build())
                .build();

        assertEquals(model1, model2);
        assertAll("Model fields validation",
                () -> assertEquals(model1.getId(), model2.getId(), "Model ID mismatch"),
                () -> assertEquals(model1.getBrand(), model2.getBrand(), "Model Brand mismatch"));

    }
}