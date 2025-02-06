package com.electronistore.dto;

import com.electronistore.validate.ImageNameValidate;
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

    @NotBlank(message = "Name is required")
    @Size(min=3, max = 25, message = "Name size should be 3 letters to 25 letters.")
    private String name;

    @Email(message = "Email format is not correct.")
    @NotNull(message = "Email is required.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.][a-zA-Z]{2,}$", message = "Email is not correct")
    private String email;

  //  @NotBlank(message = "Password is required!")
  //  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()])[A-Za-z\\d!@#$%^&*()]{4,}$", message = "Password pattern not correct")
  @Size(min = 4, message = "Password must be at least 6 characters")
    private String password;

    @NotNull(message = "Gender is Required")
    private String gender;

    @NotNull(message = "About is required")
    @Size(min = 15, message = "About section will be more than 15 letters")
    private String about;

    //? Custom Bean Validation
    @ImageNameValidate
    private String imageName;

    private List<RoleDto> roles;
}
