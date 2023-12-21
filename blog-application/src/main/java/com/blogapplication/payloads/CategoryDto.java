package com.blogapplication.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {

    private Integer categoryId;

    @NotBlank
    @Size(min = 3, message = "min size should be of 3 chars")
    private String categoryTitle;

    @NotBlank
    @Size(min = 5, message = "min size should be of 5 chars")
    private String categoryDesc;
}
