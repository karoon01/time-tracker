package com.yosypchuk.tracker.model.DTO;

import com.yosypchuk.tracker.model.entity.Category;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class ActivityDTO {
    private Long id;
    @NotBlank(message = "${activity.name.not-blank}")
    @Size(min = 4)
    @Size(max = 25)
    private String name;

    @Size(min = 5)
    @Size(max = 255)
    private String description;

    @NotNull(message = "${category.name.not-blank}")
    private Category category;
}
