package io.todaksun.study.demoboard.util.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    private final Pattern regexp;
    public PasswordValidator() {
        this.regexp = Pattern.compile("^(?=.*[0-9])(?=.*[a-z|A-Z])(?=\\S+$)(?=.*[!@#$%^&*]).{8,20}$");
    }
    @Override
    public void initialize(Password constraintAnnotation) {
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return regexp.matcher(value).matches();
    }
}
