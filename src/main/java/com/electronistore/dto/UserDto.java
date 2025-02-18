package com.electronistore.dto;

import com.electronistore.entity.Providers;
import com.electronistore.validate.ImageNameValidate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String userId;

    // THis is for Swagger
    @Schema(description = "Name is required and should not be less than 3 letters", defaultValue = "Amit", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Name is required")
    @Size(min=3, max = 25, message = "Name size should be 3 letters to 25 letters.")
    private String name;

    // THis will use for Swagger
    @Schema(description = "This will work like username so this field will not be empty", defaultValue = "abc@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @Email(message = "Email format is not correct.")
    @NotNull(message = "Email is required.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.][a-zA-Z]{2,}$", message = "Email is not correct")
    private String email;

    @Schema(description = "This field is for password and should not be empty, Password should be more than 4 letters and contains a-z, A-Z, 0-9, and symbol", defaultValue = "abcd@123", requiredMode = Schema.RequiredMode.REQUIRED)
  //  @NotBlank(message = "Password is required!")
  //  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()])[A-Za-z\\d!@#$%^&*()]{4,}$", message = "Password pattern not correct")
  @Size(min = 4, message = "Password must be at least 4 characters")
    private String password;

    @Schema(description = "Gender should be Male or Female don't use M or F", defaultValue = "Male")
    @NotNull(message = "Gender is Required")
    private String gender;

    @NotNull(message = "About is required")
    @Size(min = 15, message = "About section will be more than 15 letters")
    private String about;

    //? Custom Bean Validation
    @ImageNameValidate
    private String imageName;

    private List<RoleDto> roles;

    @Schema(description = "It will take SELF by default and LIST will be [GOOGLE,FACEBOOK,GITHUB,SELF,X]", defaultValue = "SELF")
//    @Enumerated
    private Providers providers = Providers.SELF;
}
