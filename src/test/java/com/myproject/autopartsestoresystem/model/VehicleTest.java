package com.myproject.autopartsestoresystem.model;

import com.myproject.autopartsestoresystem.vehicles.entity.Vehicle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class VehicleTest {

    @Test
    void testAllFields_isEqual_thenCorrect() {

        //given
        Vehicle vehicle1 =  Vehicle.builder()
                .id(1L)
                .parts(new ArrayList<>())
                .model(null)
                .engineType("2.0i Injection")
                .series("Series 3")
                .build();

        Vehicle vehicle2 =  Vehicle.builder()
                .id(1L)
                .parts(new ArrayList<>())
                .model(null)
                .engineType("2.0i Injection")
                .series("Series 3")
                .build();

        boolean equals = vehicle1.equals(vehicle2);
        boolean hashCode = vehicle1.hashCode() == vehicle2.hashCode();

        //when && then
        assertAll("Vehicle fields validation",
                () -> assertEquals(vehicle1.getId(), vehicle2.getId(), "Vehicle ID mismatch"),
                () -> assertEquals(vehicle1.getParts(), vehicle2.getParts(), "Vehicle parts mismatch"),
                () -> assertEquals(vehicle1.getModel(), vehicle2.getModel(), "Vehicle model mismatch"),
                () -> assertEquals(vehicle1.getEngineType(), vehicle2.getEngineType(), "Vehicle engine type mismatch"),
                () -> assertEquals(vehicle1.getSeries(), vehicle2.getSeries(), "Vehicle series mismatch"));

        assertTrue(equals, "Vehicles should be equals");
        assertTrue(hashCode, "Vehicles hash code should be equal");
    }
}