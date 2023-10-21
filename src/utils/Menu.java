
package utils;

import models.User;
import utils.menus.AdminMenu;
import utils.menus.UserMenu;

import java.util.Scanner;

public class Menu {
    private final UserManager userManagement;
    private final RoomManager roomManager;
    private final BookingManager bookingManager;
    private final AdminManager adminManager;
    private final UserMenu userMenu;
    private final AdminMenu adminMenu;
    private User currentUser;
    private final Scanner scanner;

    public Menu() {
        userManagement = new UserManager();
        roomManager = new RoomManager();
        bookingManager = new BookingManager(userManagement);
        userManagement.setBookingManager(bookingManager);
        adminManager = new AdminManager(roomManager, bookingManager);
        userMenu = new UserMenu(userManagement, roomManager, bookingManager);
        adminMenu = new AdminMenu(roomManager, bookingManager, adminManager);
        currentUser = null;
        scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the Hotel Room Reservation System!");

        while (true) {
            System.out.println("\nPlease select an option:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Admin Login");
            System.out.println("4. Exit");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    register();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    adminLogin();
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void register() {
        System.out.println("\nUser Registration");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (userManagement.register(username, password)) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Registration failed.");
        }
    }

    private void login() {
        System.out.println("\nUser Login");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (userManagement.login(username, password)) {
            System.out.println("Login successful! Hey, " + username + "!");
            currentUser = new User(username, password);
            userMenu.start(currentUser);
        } else {
            System.out.println("Login failed. Invalid username or password.");
        }
    }


    private void adminLogin() {
        System.out.println("\nAdmin Login");
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();
        if ("admin123".equals(password)) {
            System.out.println("Admin login successful!");
            adminMenu.start();
        } else {
            System.out.println("Invalid admin password.");
        }
    }
}
