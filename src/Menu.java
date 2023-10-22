import models.User;
import controllers.*;
import menus.AdminMenu;
import menus.UserMenu;

import java.util.Scanner;

public class Menu {
    private final UserController userManagement;
    private final RoomController roomController;
    private final BookingController bookingController;
    private final AdminController adminController;
    private final UserMenu userMenu;
    private final AdminMenu adminMenu;
    private final PromoCodeController promoCodeController;
    private User currentUser;
    private final Scanner scanner;

    public Menu() {
        userManagement = new UserController();
        roomController = new RoomController();
        bookingController = new BookingController(userManagement, roomController);
        userManagement.setBookingManager(bookingController);
        adminController = new AdminController(roomController, bookingController);
        userMenu = new UserMenu(userManagement, roomController, bookingController);
        promoCodeController = new PromoCodeController();
        adminMenu = new AdminMenu(roomController, bookingController, adminController, promoCodeController);
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
                case 1 -> register();
                case 2 -> login();
                case 3 -> adminLogin();
                case 4 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
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
        String adminPass = "admin123";
        System.out.println("\nAdmin Login");
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();
        if (adminPass.equals(password)) {
            System.out.println("Admin login successful!");
            adminMenu.start();
        } else {
            System.out.println("Invalid admin password.");
        }
    }
}
