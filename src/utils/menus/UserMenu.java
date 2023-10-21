package utils.menus;

import models.Booking;
import models.Room;
import models.User;
import utils.BookingManager;
import utils.DateUtils;
import utils.RoomManager;
import utils.UserManager;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class UserMenu {
    private final UserManager userManagement;
    private final RoomManager roomManager;
    private final BookingManager bookingManager;
    private User currentUser;
    private final Scanner scanner;

    public UserMenu(UserManager userManagement, RoomManager roomManager, BookingManager bookingManager, User currentUser) {
        this.userManagement = userManagement;
        this.roomManager = roomManager;
        this.bookingManager = bookingManager;
        this.currentUser = currentUser;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
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

    private void bookRoom() {
        System.out.println("\nRoom Booking");
        System.out.print("Enter check-in date (yyyy-MM-dd): ");
        Date checkInDate;
        Date checkOutDate;
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

    private void viewProfile() {
        System.out.println("\nUser Profile");
        if (currentUser != null) {
            userManagement.viewProfile(currentUser.getUsername());
        } else {
            System.out.println("User not logged in.");
        }
    }
}
