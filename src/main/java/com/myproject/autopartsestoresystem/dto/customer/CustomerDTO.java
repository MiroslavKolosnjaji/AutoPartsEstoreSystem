package com.myproject.autopartsestoresystem.dto.customer;

import com.myproject.autopartsestoresystem.model.City;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String phone;
    private City city;
}
