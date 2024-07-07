package com.myproject.autopartsestoresystem.model;

import com.myproject.autopartsestoresystem.dto.PartDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class PartTest {

    @Test
    void testAllFields_isEqual_thenCorrect() {

        //given
        Part part1 = Part.builder()
                .id(1L)
                .partNumber("BR56789")
                .partName("Rear Brake Rotor")
                .description("High-quality rear brake rotor designed for optimal heat dissapation and minimal wear. Ensures smooth and effective braking perfomance")
                .prices(new ArrayList<>())
                .partGroup(PartGroup.builder().id(1L).name(PartGroupType.BRAKING_SYSTEM).parts(new ArrayList<>()).build())
                .vehicles(new ArrayList<>())
                .build();

        Part part2 = Part.builder()
                .id(1L)
                .partNumber("BR56789")
                .partName("Rear Brake Rotor")
                .description("High-quality rear brake rotor designed for optimal heat dissapation and minimal wear. Ensures smooth and effective braking perfomance")
                .prices(new ArrayList<>())
                .partGroup(PartGroup.builder().id(1L).name(PartGroupType.BRAKING_SYSTEM).parts(new ArrayList<>()).build())
                .vehicles(new ArrayList<>())
                .build();

        boolean equals = part1.equals(part2);
        boolean hashCode = part1.hashCode() == part2.hashCode();

        //when & then
        assertAll("Part fields validation",
                () -> assertEquals(part1.getId(), part2.getId(), "Part ID mismatch"),
                () -> assertEquals(part1.getPartNumber(), part2.getPartNumber(), "Part number mismatch"),
                () -> assertEquals(part1.getPartName(), part2.getPartName(), "Part name mismatch"),
                () -> assertEquals(part1.getDescription(), part2.getDescription(), "Part description mismatch"),
                () -> assertEquals(part1.getPrices(), part2.getPrices(), "Part prices mismatch"),
                () -> assertEquals(part1.getPartGroup(), part2.getPartGroup(), "Part group mismatch"),
                () -> assertEquals(part1.getVehicles(), part2.getVehicles(), "Part vehicles mismatch"));

        assertTrue(equals, "Part1 is not equal to Part2");
        assertTrue(hashCode, "Part1 hash code is not equal to Part2 hash code");

    }
}