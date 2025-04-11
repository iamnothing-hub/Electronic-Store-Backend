package com.electronistore.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageNameValidator implements ConstraintValidator<ImageNameValidate,String> {

    // Logger implementation
    private Logger logger = LoggerFactory.getLogger(ImageNameValidator.class);

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        logger.info("Message from isValid : {} " + s);
        //Logic
        if(s==null || s.isEmpty()){
            return false;
        }
        else return  true;
//        return !s.isBlank();


    }
}
