package Utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    public static final Pattern VALID_USERNAME_REGEX =
            Pattern.compile(".{3,15}");
    public static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile(".{8,20}");
    public static final Pattern VALID_EMAIL_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateUserName(String userNameStr){
        Matcher matcher = VALID_USERNAME_REGEX.matcher(userNameStr);
        return matcher.find();
    }

    public static boolean validatePassword(String passwordStr){
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(passwordStr);
        return matcher.find();
    }

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static void WaitTask() {
        try {
            Thread.sleep(4000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

    }
}
