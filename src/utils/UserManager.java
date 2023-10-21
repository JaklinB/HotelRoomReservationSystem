package utils;

import models.User;

import java.io.*;
import java.util.*;

public class UserManager {

    private static final String USER_FILE_PATH = "src/data/users.csv";
    private Map<String, User> users = new HashMap<>();
    private User currentUser = null;

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
        User newUser = new User(username, password);
        users.put(username, newUser);
        saveUsers();
        return true;
    }

    public boolean login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public void viewProfile(String username) {
        User user = users.get(username);
        if (user != null) {
            System.out.println("Username: " + user.getUsername());
        } else {
            System.out.println("User not found!");
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    User user = new User(parts[0], parts[1]);
                    users.put(parts[0], user);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users!");
        }
    }

    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE_PATH))) {
            for (Map.Entry<String, User> entry : users.entrySet()) {
                User user = entry.getValue();
                writer.write(user.getUsername() + "," + user.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users!" + e.toString());
        }
    }

    public User getUserByUsername(String username) {
        return users.get(username);
    }
}
