package util;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.function.Supplier;

public class Utility {
    public static String generateSecurePassword() {


        CharacterRule LCR = new CharacterRule(EnglishCharacterData.LowerCase);
        LCR.setNumberOfCharacters(2);

        CharacterRule UCR = new CharacterRule(EnglishCharacterData.UpperCase);
        UCR.setNumberOfCharacters(2);

        CharacterRule DR = new CharacterRule(EnglishCharacterData.Digit);
        DR.setNumberOfCharacters(3);

        CharacterData specialChars = new CharacterData() {
            @Override
            public String getErrorCode() {
                return "ERROR_SPECIAL_CHAR";
            }

            @Override
            public String getCharacters() {
                return "@#$%&";
            }
        };

        CharacterRule SR = new CharacterRule(specialChars);
        SR.setNumberOfCharacters(1);

        PasswordGenerator passGen = new PasswordGenerator();

        return passGen.generatePassword(8, SR, LCR, UCR, DR);
    }
    public static LocalDate getDate(Function<LocalDate, LocalDate> dateProvider) {
        Supplier<LocalDate> defaultDateSupplier = LocalDate::now;
        return dateProvider.apply(null) == null ? defaultDateSupplier.get() : dateProvider.apply(null);
    }
}
