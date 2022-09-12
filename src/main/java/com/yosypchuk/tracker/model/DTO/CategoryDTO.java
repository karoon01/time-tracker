package com.yosypchuk.tracker.model.DTO;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class CategoryDTO {
    private Long id;

    @NotBlank(message = "${category.name.not-blank}")
    @Size(min = 4)
    @Size(max = 25)
    private String name;
}
