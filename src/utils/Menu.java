package utils;

import enums.Amenities;
import enums.RoomStatus;
import enums.RoomType;
import models.Booking;
import models.Room;
import models.User;

import java.text.ParseException;
import java.util.*;

public class Menu {
    private final UserManager userManagement;
    private final RoomManager roomManager;
    private final BookingManager bookingManager;
    private final AdminManager adminManager;
    private User currentUser;
    private final Scanner scanner;

    public Menu() {
        userManagement = new UserManager();
        roomManager = new RoomManager();
        bookingManager = new BookingManager();
        adminManager = new AdminManager(roomManager, bookingManager);
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
            System.out.println("Login successful!");
            currentUser = new User(username, password);
            userMenu();
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
            adminMenu();
        } else {
            System.out.println("Invalid admin password.");
        }
    }

    private void userMenu() {
        while (true) {
            System.out.println("\nUser Menu");
            System.out.println("1. View Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View Profile");
            System.out.println("5. Logout");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    viewRooms();
                    break;
                case 2:
                    bookRoom();
                    break;
                case 3:
                    cancelBooking();
                    break;
                case 4:
                    viewProfile();
                    break;
                case 5:
                    currentUser = null;
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void adminMenu() {
        while (true) {
            System.out.println("\nAdmin Menu");
            System.out.println("1. View All Bookings");
            System.out.println("2. View Total Income");
            System.out.println("3. View Total Cancellation Fees");
            System.out.println("4. Manage Rooms");
            System.out.println("5. Manage Promotional Codes");
            System.out.println("6. Logout");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    adminManager.viewAllBookings();
                    break;
                case 2:
                    adminManager.viewTotalIncome();
                    break;
                case 3:
                    adminManager.viewTotalCancellationFees();
                    break;
                case 4:
                    manageRooms();
                    break;
                case 5:
                    managePromoCodes();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void manageRooms() {
        System.out.println("\nManage Rooms");
        System.out.println("1. Add Room");
        System.out.println("2. Remove Room");
        System.out.println("3. Modify Room Details");
        System.out.println("4. Return to Admin Menu");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                addRoom();
                break;
            case 2:
                removeRoom();
                break;
            case 3:
                modifyRoom();
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void viewRooms() {
        List<Room> availableRooms = roomManager.getAvailableRooms();
        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms.");
        } else {
            System.out.println("Available Rooms:");
            for (Room room : availableRooms) {
                System.out.println("Room Number: " + room.getRoomNumber() +
                        ", Room Type: " + room.getRoomType() +
                        ", Price per Night: $" + room.getPricePerNight());
            }
        }
    }

    private List<Amenities> parseAmenities(String input) {
        String[] amenitiesArray = input.split(";");
        List<Amenities> amenities = new ArrayList<>();
        for (String amenity : amenitiesArray) {
            try {
                amenities.add(Amenities.valueOf(amenity.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("This amenity is not available: " + amenity + ". Skipping.");
            }
        }
        return amenities;
    }

    private void addRoom() {
        System.out.print("Enter room number: ");
        String roomNumber = scanner.nextLine();

        if (roomManager.getRoomByRoomNumber(roomNumber) != null) {
            System.out.println("Room with this number already exists. Please enter a unique room number.");
            return;
        }

        RoomType roomType = getValidRoomType("Enter room type (SINGLE, DOUBLE, SUITE, DELUXE): ");
        if (roomType == null) {
            return;
        }

        double pricePerNight = getValidDouble("Enter price per night: ");
        double cancellationFee = getValidDouble("Enter cancellation fee: ");

        RoomStatus roomStatus = getValidRoomStatus("Enter room status (AVAILABLE, BOOKED): ");
        if (roomStatus == null) {
            return;
        }

        System.out.print("Enter amenities (separated by semicolons) \n " +
                "Available amenities : \n " +
                "    TV,\n" +
                "    WIFI,\n" +
                "    MiniBar,\n" +
                "    Balcony,\n" +
                "    RoomService,\n" +
                "    GoodView: ");
        String amenitiesInput = scanner.nextLine();
        List<Amenities> amenities = parseAmenities(amenitiesInput);

        int maximumOccupancy = getValidInt("Enter maximum occupancy: ");

        Room room = new Room(roomNumber, roomType, pricePerNight, cancellationFee, roomStatus, amenities, maximumOccupancy);
        roomManager.addRoom(room);
    }

    private RoomType getValidRoomType(String prompt) {
        while (true) {
            System.out.print(prompt);
            String roomTypeInput = scanner.nextLine();
            try {
                return RoomType.valueOf(roomTypeInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid room type: " + roomTypeInput);
            }
        }
    }

    private RoomStatus getValidRoomStatus(String prompt) {
        while (true) {
            System.out.print(prompt);
            String roomStatusInput = scanner.nextLine();
            try {
                return RoomStatus.valueOf(roomStatusInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid room status: " + roomStatusInput);
            }
        }
    }

    private double getValidDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input format. Please enter a valid number.");
            }
        }
    }

    private int getValidInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input format. Please enter a valid integer.");
            }
        }
    }

    private void removeRoom() {
        System.out.print("Enter room number to remove: ");
        String roomNumber = scanner.nextLine();
        Room roomToDelete = roomManager.getRoomByRoomNumber(roomNumber);
        roomManager.deleteRoom(roomToDelete);
    }

    private void modifyRoom() {
        System.out.print("Enter room number to modify: ");
        String roomNumber = scanner.nextLine();
        Room room = roomManager.getRoomByRoomNumber(roomNumber);

        if (room == null) {
            System.out.println("Room not found!");
            return;
        }

        RoomType newRoomType = getValidRoomType("Enter new room type (SINGLE, DOUBLE, SUITE, DELUXE): ");
        double newPricePerNight = getValidDouble("Enter new price per night: ");
        double newCancellationFee = getValidDouble("Enter new cancellation fee: ");
        RoomStatus newRoomStatus = getValidRoomStatus("Enter new room status (AVAILABLE, OCCUPIED): ");

        room.setRoomType(newRoomType);
        room.setPricePerNight(newPricePerNight);
        room.setCancellationFee(newCancellationFee);
        room.setStatus(newRoomStatus);

        roomManager.updateRoom(room);
    }

    private void managePromoCodes() {
        System.out.println("\nManage Promotional Codes");
        System.out.println("1. Add Promotional Code");
        System.out.println("2. Remove Promotional Code");
        System.out.println("3. View All Promotional Codes");
        System.out.println("4. Return to Admin Menu");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                addPromoCode();
                break;
            case 2:
                removePromoCode();
                break;
            case 3:
                adminManager.viewAllPromoCodes();
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void addPromoCode() {
        System.out.print("Enter new promotional code: ");
        String code = scanner.nextLine();
        adminManager.addPromoCode(code);
    }

    private void removePromoCode() {
        System.out.print("Enter promotional code to remove: ");
        String code = scanner.nextLine();
        adminManager.removePromoCode(code);
    }

    private void viewProfile() {
        System.out.println("\nUser Profile");
        if (currentUser != null) {
            userManagement.viewProfile(currentUser.getUsername());
        } else {
            System.out.println("User not logged in.");
        }
    }

    private void bookRoom() {
        if (currentUser == null) {
            System.out.println("User not logged in. Please login first.");
            return;
        }

        System.out.println("\nRoom Booking");
        System.out.print("Enter check-in date (yyyy-MM-dd): ");

        Date checkInDate = null;
        Date checkOutDate = null;

        try {
            checkInDate = DateUtils.parseDate(scanner.nextLine());

            System.out.print("Enter check-out date (yyyy-MM-dd): ");
            checkOutDate = DateUtils.parseDate(scanner.nextLine());
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd format for dates.");
            return;
        }

        List<Room> availableRooms = roomManager.getAvailableRooms();
        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms.");
        } else {
            System.out.println("Available Rooms:");
            for (int i = 0; i < availableRooms.size(); i++) {
                Room room = availableRooms.get(i);
                System.out.println(i + 1 + ". Room Number: " + room.getRoomNumber() +
                        ", Room Type: " + room.getRoomType() +
                        ", Price per Night: $" + room.getPricePerNight());
            }
            System.out.print("Select a room (1-" + availableRooms.size() + "): ");
            int roomIndex = Integer.parseInt(scanner.nextLine()) - 1;

            if (roomIndex >= 0 && roomIndex < availableRooms.size()) {
                Room selectedRoom = availableRooms.get(roomIndex);
                String bookingID = UUID.randomUUID().toString();
                Booking booking = new Booking(bookingID, selectedRoom.getRoomNumber(), currentUser, checkInDate, checkOutDate);
                bookingManager.addBooking(booking);
                System.out.println("Booking successful!");
            } else {
                System.out.println("Invalid room selection.");
            }
        }
    }

    private void cancelBooking() {
        if (currentUser == null) {
            System.out.println("User not logged in. Please login first.");
            return;
        }

        System.out.println("\nBooking Cancellation");
        System.out.print("Enter booking ID: ");
        String bookingID = scanner.nextLine();

        Booking booking = bookingManager.getBookingByID(bookingID);
        if (booking != null && booking.getUser().getUsername().equals(currentUser.getUsername())) {
            Room room = roomManager.getRoomByRoomNumber(booking.getRoomNumber());

            if (room != null) {
                double cancellationFee = room.getCancellationFee();
                bookingManager.cancelBooking(booking);
                System.out.println("Booking canceled. Cancellation Fee: $" + cancellationFee);
            } else {
                System.out.println("Room not found for the booking.");
            }
        } else {
            System.out.println("Invalid booking ID or not authorized.");
        }
    }

}
