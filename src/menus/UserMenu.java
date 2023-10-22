package menus;

import models.Booking;
import models.Room;
import models.User;
import controllers.BookingController;
import utils.DateUtils;
import controllers.PromoCodeController;
import controllers.RoomController;
import controllers.UserController;

import java.text.ParseException;
import java.util.*;

public class UserMenu {
    private final UserController userManagement;
    private final RoomController roomController;
    private final BookingController bookingController;
    private final Scanner scanner;
    private final PromoCodeController promoCodeController;
    private User currentUser;

    public UserMenu(UserController userManagement, RoomController roomController, BookingController bookingController) {
        this.userManagement = userManagement;
        this.roomController = roomController;
        this.bookingController = bookingController;
        this.scanner = new Scanner(System.in);
        this.promoCodeController = new PromoCodeController();
    }

    public void start(User currentUser) {
        this.currentUser = currentUser;
        while (true) {
            System.out.println("\nUser Menu");
            System.out.println("1. View Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View Profile");
            System.out.println("5. Logout");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1 -> viewRooms();
                case 2 -> bookRoom();
                case 3 -> cancelBooking();
                case 4 -> viewProfile();
                case 5 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void viewRooms() {
        List<Room> availableRooms = roomController.getAvailableRooms();
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

        Date checkInDate = promptForDate("Enter check-in date (yyyy-MM-dd): ");
        if (checkInDate == null || !DateUtils.isDateInFuture(checkInDate)) {
            System.out.println("Invalid check-in date. Date should be in the future.");
            return;
        }

        Date checkOutDate = promptForDate("Enter check-out date (yyyy-MM-dd): ");
        if (checkOutDate == null || !DateUtils.isValidDateRange(checkInDate, checkOutDate)) {
            System.out.println("Invalid date range. Check-in date should be before check-out date.");
            return;
        }

        List<Room> availableRooms = roomController.getAvailableRooms();
        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms.");
            return;
        }

        displayAvailableRooms(availableRooms);

        int roomIndex = promptForRoomSelection(availableRooms.size());
        if (roomIndex == -1) {
            System.out.println("Invalid room selection.");
            return;
        }

        Room selectedRoom = availableRooms.get(roomIndex);
        createBooking(selectedRoom, checkInDate, checkOutDate);
    }

    private Date promptForDate(String message) {
        System.out.print(message);
        try {
            return DateUtils.parseDate(scanner.nextLine());
        } catch (ParseException e) {
            return null;
        }
    }

    private void displayAvailableRooms(List<Room> rooms) {
        System.out.println("Available Rooms:");
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            System.out.println((i + 1) + ". Room Number: " + room.getRoomNumber() +
                    ", Room Type: " + room.getRoomType() +
                    ", Price per Night: $" + room.getPricePerNight());
        }
    }

    private int promptForRoomSelection(int roomCount) {
        System.out.print("Select a room (1-" + roomCount + "): ");
        int selection = Integer.parseInt(scanner.nextLine()) - 1;
        if (selection >= 0 && selection < roomCount) {
            return selection;
        }
        return -1;
    }

    private void createBooking(Room selectedRoom, Date checkInDate, Date checkOutDate) {
        String bookingID = UUID.randomUUID().toString();
        long numberOfNights = DateUtils.daysBetween(checkInDate, checkOutDate);
        double originalPrice = selectedRoom.getPricePerNight() * numberOfNights;
        double finalPrice = originalPrice;

        System.out.print("Do you have a promo code? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        if ("yes".equalsIgnoreCase(response)) {
            System.out.print("Enter your promo code: ");
            String promoCode = scanner.nextLine().trim();
            if (promoCodeController.isValidPromoCode(promoCode)) {
                double discount = promoCodeController.getDiscountPercentage(promoCode);
                finalPrice -= (originalPrice * discount / 100);
                System.out.println("Applied Promo Code! Original Price: $" + originalPrice + ", Discounted Price: $" + finalPrice);
            } else {
                System.out.println("Invalid promo code. Using original price.");
                System.out.println("Total Price: $" + finalPrice);
            }
        } else if ("no".equalsIgnoreCase(response)) {
            System.out.println("Total Price: $" + finalPrice);
        } else {
            System.out.println("Invalid answer. Total Price without promo code: $" + finalPrice);
        }

        if (currentUser != null) {
            Booking booking = new Booking(bookingID, selectedRoom.getRoomNumber(), currentUser, checkInDate, checkOutDate);
            bookingController.addBooking(booking);
            System.out.println("Booking successful!");
        } else {
            System.out.println("User not logged in. Please login first.");
        }
    }

    private void cancelBooking() {
        System.out.println("\nBooking Cancellation");
        System.out.print("Enter booking ID: ");
        String bookingID = scanner.nextLine();
        Optional<Booking> booking = bookingController.getBookingByID(bookingID);
        if (booking.isPresent() && currentUser != null && booking.get().getUser().getUsername().equals(currentUser.getUsername())) {
            Optional<Room> room = roomController.getRoomByRoomNumber(booking.get().getRoomNumber());
            if (room.isPresent()) {
                double cancellationFee = room.get().getCancellationFee();
                bookingController.cancelBooking(booking.get());
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
