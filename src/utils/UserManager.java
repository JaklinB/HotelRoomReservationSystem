package utils;

import models.User;

import java.io.*;
import java.util.*;

public class UserManager {

    private static final String USER_FILE_PATH = "src/data/users.csv";
    private Map<String, String> users = new HashMap<>();

    public UserManager() {
        loadUsers();
    }

    public boolean register(String username, String password) {
        if (!UserValidator.isValidUsername(username) || !UserValidator.isValidPassword(password)) {
            System.out.println("Invalid username or password!");
            return false;
        }
        if (users.containsKey(username)) {
            System.out.println("Username already exists!");
            return false;
        }
        users.put(username, password);
        saveUsers();
        return true;
    }

    public boolean login(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public void viewProfile(String username) {
        if (users.containsKey(username)) {
            System.out.println("Username: " + username);
        } else {
            System.out.println("User not found!");
        }
    }

    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users!");
        }
    }

    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE_PATH))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users!" + e.toString());
        }
    }

    public static User getUserByUsername(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username)) {
                    return new User(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users!");
        }
        return null;
    }
}
