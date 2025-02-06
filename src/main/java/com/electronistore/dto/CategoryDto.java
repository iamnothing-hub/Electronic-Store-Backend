package com.electronistore.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private String categoryId;

    @NotBlank
    @Length(min = 4, message = "Category title should be minimum 4 characters!!")
    private String title;

    @NotBlank(message = "Description is required.")
    private String description;

    private String coverImage;

}
