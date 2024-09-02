package it.aboutbits.springboot.toolbox.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResultDetail;

import java.util.List;

public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, String> {
    private String message;
    private String lengthMessage;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        this.message = constraintAnnotation.message();
        this.lengthMessage = constraintAnnotation.lengthMessage();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return true;
        }

        var validator = new PasswordValidator(List.of(
                // at least 8 characters
                new LengthRule(8, 50)
        ));

        var result = validator.validate(new PasswordData(password));

        if (!password.isBlank() && result.isValid()) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(getMessageTemplate(result.getDetails()))
                .addConstraintViolation();

        return false;
    }

    private String getMessageTemplate(List<RuleResultDetail> ruleResultDetails) {
        if (ruleResultDetails.isEmpty()) {
            return message;
        }

        return switch (ruleResultDetails.getFirst().getErrorCode()) {
            case "TOO_SHORT" -> lengthMessage;
            case "TOO_LONG" -> lengthMessage;
            default -> message;
        };
    }
}
