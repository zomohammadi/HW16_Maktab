package util;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import java.time.ZonedDateTime;
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

    public static ZonedDateTime getDate(Function<ZonedDateTime, ZonedDateTime> dateProvider) {
        Supplier<ZonedDateTime> defaultDateSupplier = ZonedDateTime::now;
        return dateProvider.apply(null) == null ? defaultDateSupplier.get() : dateProvider.apply(null);
    }

    public static class UseDate {
        public static ZonedDateTime useDate() {
            /* ZonedDateTime currentDate = getDate(date -> null);*/

             /*ZonedDateTime currentDate = getDate(date -> ZonedDateTime.of(2018, 10, 23 , ZonedDateTime.now().getHour(), ZonedDateTime.now().getMinute(),
                                ZonedDateTime.now().getSecond(), 0, ZonedDateTime.now().getZone()));*/

            /*ZonedDateTime currentDate = getDate(date -> ZonedDateTime.of(2019, 2, 14, ZonedDateTime.now().getHour(), ZonedDateTime.now().getMinute(),
                    ZonedDateTime.now().getSecond(), 0, ZonedDateTime.now().getZone()));*/
            //out of range
           /* ZonedDateTime currentDate = getDate(date -> ZonedDateTime.of(2019, 2, 21 , ZonedDateTime.now().getHour(), ZonedDateTime.now().getMinute(),
                    ZonedDateTime.now().getSecond(), 0, ZonedDateTime.now().getZone()));*/
//for 2 years --- entryYear = > 2018
           /* ZonedDateTime currentDate = getDate(date -> ZonedDateTime.of(2021, 10, 23
                    , ZonedDateTime.now().getHour(), ZonedDateTime.now().getMinute(),
                    ZonedDateTime.now().getSecond(), 0, ZonedDateTime.now().getZone()));*/

            //for 4 years --- entryYear = > 2015
            ZonedDateTime currentDate = getDate(date -> ZonedDateTime.of(2024, 10, 23
                    , ZonedDateTime.now().getHour(), ZonedDateTime.now().getMinute(),
                    ZonedDateTime.now().getSecond(), 0, ZonedDateTime.now().getZone()));
            return currentDate;
        }
    }
}
