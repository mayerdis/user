package org.mick.user.Validation;

import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

public class CustomPasswordValidation implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            final PasswordValidator validator = new PasswordValidator(Arrays.asList(
                    new LengthRule(8, 16),
                    new CharacterRule(EnglishCharacterData.Digit, 3),
                    new CharacterRule(EnglishCharacterData.Alphabetical),
                    new CharacterRule(EnglishCharacterData.Special, 1),
                    new WhitespaceRule()));
            final RuleResult result = validator.validate(new PasswordData(value));
            return result.isValid();
        } catch (NullPointerException ne) {
            return false;
        }

    }
}
