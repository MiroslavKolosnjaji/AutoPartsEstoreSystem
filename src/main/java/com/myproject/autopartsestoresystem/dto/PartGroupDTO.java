package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.Part;
import com.myproject.autopartsestoresystem.model.PartGroupType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartGroupDTO {

    @NotNull(groups = Update.class)
    private Long id;

    @NotNull(message = "Part group not selected")
    private PartGroupType name;

    @NotNull(message = "Parts list cannot be null")
    private List<Part> parts;
}
