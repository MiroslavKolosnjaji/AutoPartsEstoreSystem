package com.myproject.autopartsestoresystem.model;

import com.myproject.autopartsestoresystem.cities.entity.City;
import com.myproject.autopartsestoresystem.stores.entity.Store;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class StoreTest {

    @Test
    void testAllFields_isEqual_thenCorrect() {

        Store store1 = Store.builder()
                .name("Store XYZ")
                .email("xyz@example.com")
                .phoneNumber("123123123")
                .city(City.builder().build())
                .build();

        Store store2 = Store.builder()
                .name("Store XYZ")
                .email("xyz@example.com")
                .phoneNumber("123123123")
                .city(City.builder().build())
                .build();

        boolean equals = store1.equals(store2);
        boolean hashCode = store1.hashCode() == store2.hashCode();

        assertTrue(equals, "The same Store objects should be equal");
        assertTrue(hashCode, "Store hashCode should be equal");
    }
}