package com.yosypchuk.tracker.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TimeFormatValidator implements ConstraintValidator<TimeFormatValidation, String> {
    private final static String TIME_FORMAT_REGEX = "^([0-1]?\\d|2[0-3])(?::([0-5]?\\d))?(?::([0-5]?\\d))?$";

    @Override
    public void initialize(TimeFormatValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String time, ConstraintValidatorContext constraintValidatorContext) {
        return time != null && !time.isBlank() && time.matches(TIME_FORMAT_REGEX);
    }
}
