package com.meenah.meenahsignature.util;

import org.springframework.context.annotation.Configuration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class Validator {

    public final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public final Pattern VALID_FILE_EXTENSION_REGEX =
            Pattern.compile("([^\\s]+(\\.(?i)(jpe?g|png|gif|bmp|jfif))$)", Pattern.CASE_INSENSITIVE);

    public  boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }


    public  boolean validateFile(String type) {
        Matcher matcher = VALID_FILE_EXTENSION_REGEX.matcher(type);
        return matcher.find();
    }

}
