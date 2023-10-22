package utils.managers;

import enums.RoomStatus;
import models.Booking;
import models.User;
import utils.DateUtils;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingManager {
    private static final String BOOKING_FILE_PATH = "src/data/bookings.csv";
    private List<Booking> bookings;
    private UserManager userManager;
    private RoomManager roomManager;

    public BookingManager(UserManager userManager, RoomManager roomManager) {
        this.userManager = userManager;
        this.roomManager = roomManager;
        bookings = new ArrayList<>();
        loadBookings();
    }

    private void loadBookings() {
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKING_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String bookingID = parts[0];
                    String roomNumber = parts[1];
                    String username = parts[2];
                    Date checkInDate = DateUtils.parseDate(parts[3]);
                    Date checkOutDate = DateUtils.parseDate(parts[4]);
                    User user = userManager.getUserByUsername(username);
                    if (user != null) {
                        Booking booking = new Booking(bookingID, roomNumber, user, checkInDate, checkOutDate);
                        bookings.add(booking);
                    } else {
                        System.out.println("User not found for booking with ID: " + bookingID);
                    }
                }
            }
        } catch (IOException | ParseException e) {
            System.out.println("Error loading booking details!");
        }
    }

    public void saveBookings() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKING_FILE_PATH))) {
            for (Booking booking : bookings) {
                User user = booking.getUser();
                if (user != null) {
                    roomManager.updateRoomStatus(booking.getRoomNumber(), RoomStatus.BOOKED);
                    writer.write(
                            booking.getBookingID() + "," +
                                    booking.getRoomNumber() + "," +
                                    booking.getUser().getUsername() + "," +
                                    DateUtils.formatDate(booking.getCheckInDate()) + "," +
                                    DateUtils.formatDate(booking.getCheckOutDate())
                    );
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving booking details!");
        }
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
        saveBookings();
    }

    public Booking getBookingByID(String bookingID) {
        for (Booking booking : bookings) {
            if (booking.getBookingID().equals(bookingID)) {
                return booking;
            }
        }
        return null;
    }

    public void cancelBooking(Booking booking) {
        roomManager.updateRoomStatus(booking.getRoomNumber(), RoomStatus.AVAILABLE);
        bookings.remove(booking);
        saveBookings();
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    public List<Booking> getBookingsByUser(User user) {
        List<Booking> userBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getUser().equals(user)) {
                userBookings.add(booking);
            }
        }
        return userBookings;
    }
}
