package com.electronistore.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface  ImageNameValidate {

    // For Message
    String message() default "Invalid image name!!";
    // Group of constraint
    Class<?>[] groups() default {};
    // Additional information about annotation
    Class<? extends Payload>[] payload() default {};
}
