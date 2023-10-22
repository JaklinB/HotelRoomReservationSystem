package menus;

import models.Booking;
import utils.DateUtils;
import controllers.BookingController;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class SearchMenu {
    private final BookingController bookingController;
    private final Scanner scanner;

    public SearchMenu(BookingController bookingController) {
        this.bookingController = bookingController;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\nSearch Bookings");
            System.out.println("1. Search by Username");
            System.out.println("2. Search by Room Number");
            System.out.println("3. Search by Date Range");
            System.out.println("4. Return to Admin Menu");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> searchByUserName();
                case 2 -> searchByRoomNumber();
                case 3 -> searchByDateRange();
                case 4 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void searchByUserName() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        List<Booking> bookings = bookingController.searchBookingsByUsername(username);
        displaySearchResults(bookings);
    }

    private void searchByRoomNumber() {
        System.out.print("Enter room number: ");
        String roomNumber = scanner.nextLine();
        List<Booking> bookings = bookingController.searchBookingsByRoomNumber(roomNumber);
        displaySearchResults(bookings);
    }

    private Date inputDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return DateUtils.parseDate(scanner.nextLine());
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd format.");
            }
        }
    }

    private void searchByDateRange() {
        Date startDate = inputDate("Enter start date (yyyy-MM-dd): ");
        Date endDate = inputDate("Enter end date (yyyy-MM-dd): ");
        List<Booking> bookings = bookingController.searchBookingsByDateRange(startDate, endDate);
        displaySearchResults(bookings);
    }

    private void displaySearchResults(List<Booking> bookings) {
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Booking booking : bookings) {
            System.out.println("\nBooking ID: " + booking.getBookingID());
            System.out.println("Room Number: " + booking.getRoomNumber());
            System.out.println("Username: " + booking.getUser().getUsername());
            System.out.println("Check-In Date: " + booking.getCheckInDate());
            System.out.println("Check-Out Date: " + booking.getCheckOutDate());
        }
    }
}
