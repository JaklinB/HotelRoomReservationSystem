package controllers;

import models.Booking;
import models.User;
import utils.DateUtils;

import java.io.*;
import java.util.*;

public class UserController {
    private static final String USER_FILE_PATH = "src/data/users.csv";
    private static final String DELIMITER = ",";

    private final Map<String, User> users = new HashMap<>();
    private BookingController bookingController;

    public UserController() {
        loadUsers();
    }

    public void setBookingManager(BookingController bookingController) {
        this.bookingController = bookingController;
    }

    public boolean register(String username, String password) {
        if (users.containsKey(username)) {
            System.out.println("Username already exists!");
            return false;
        } else if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password cannot be empty!");
            return false;
        }
        users.put(username, new User(username, password));
        saveUsers();
        return true;
    }

    public boolean login(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password cannot be empty!");
            return false;
        }
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }

    public void viewProfile(String username) {
        User user = users.get(username);
        if (user != null) {
            System.out.println("Username: " + user.getUsername());
            List<Booking> userBookings = bookingController.getBookingsByUser(user);
            if (userBookings.isEmpty()) {
                System.out.println("No bookings found for this user.");
            } else {
                System.out.println("Bookings:");
                for (Booking booking : userBookings) {
                    System.out.println("Booking ID: " + booking.getBookingID() +
                            ", Room Number: " + booking.getRoomNumber() +
                            ", Check-in Date: " + DateUtils.formatDate(booking.getCheckInDate()) +
                            ", Check-out Date: " + DateUtils.formatDate(booking.getCheckOutDate()));
                }
            }
        } else {
            System.out.println("User not found!");
        }
    }

    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(DELIMITER);
                if (parts.length == 2) {
                    User user = new User(parts[0], parts[1]);
                    users.put(parts[0], user);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users from file!");
        }
    }

    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE_PATH))) {
            for (Map.Entry<String, User> entry : users.entrySet()) {
                User user = entry.getValue();
                writer.write(user.getUsername() + DELIMITER + user.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users to file!");
        }
    }

    public User getUserByUsername(String username) {
        return users.get(username);
    }
}
