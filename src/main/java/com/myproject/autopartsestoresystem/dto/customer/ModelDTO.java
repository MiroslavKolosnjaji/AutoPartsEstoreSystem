package com.myproject.autopartsestoresystem.dto.customer;

import com.myproject.autopartsestoresystem.model.Brand;
import com.myproject.autopartsestoresystem.model.ModelId;
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
public class ModelDTO {

    private ModelId id;
    private Brand brand;

}
