package utils;
public class UserValidator {

    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty() || username.length() < 5) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9]*$");
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.trim().isEmpty() || password.length() < 4) {
            return false;
        }
        return true;
    }
}

