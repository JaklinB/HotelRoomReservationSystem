package controllers;

import enums.RoomStatus;
import models.Booking;
import models.User;
import utils.DateUtils;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class BookingController {
    private static final String BOOKING_FILE_PATH = "src/data/bookings.csv";
    private final List<Booking> bookings;
    private final UserController userController;
    private final RoomController roomController;

    public BookingController(UserController userController, RoomController roomController) {
        this.userController = userController;
        this.roomController = roomController;
        bookings = new ArrayList<>();
        loadBookings();
    }

    private void loadBookings() {
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKING_FILE_PATH))) {
            String line;
            String bookingDelimiter = ",";
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(bookingDelimiter);
                if (parts.length == 5) {
                    String bookingID = parts[0];
                    String roomNumber = parts[1];
                    String username = parts[2];
                    Date checkInDate = DateUtils.parseDate(parts[3]);
                    Date checkOutDate = DateUtils.parseDate(parts[4]);
                    User user = userController.getUserByUsername(username);
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
                    writer.write(
                            booking.getBookingID() + "," +
                                    booking.getRoomNumber() + "," +
                                    booking.getUser().getUsername() + "," +
                                    DateUtils.formatDate(booking.getCheckInDate()) + "," +
                                    DateUtils.formatDate(booking.getCheckOutDate())
                    );
                    writer.newLine();
                    roomController.updateRoomStatus(booking.getRoomNumber(), RoomStatus.BOOKED);
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

    public Optional<Booking> getBookingByID(String bookingID) {
        for (Booking booking : bookings) {
            if (booking.getBookingID().equals(bookingID)) {
                return Optional.of(booking);
            }
        }
        return Optional.empty();
    }

    public void cancelBooking(Booking booking) {
        bookings.remove(booking);
        roomController.updateRoomStatus(booking.getRoomNumber(), RoomStatus.AVAILABLE);
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

    public List<Booking> searchBookingsByUsername(String username) {
        List<Booking> filteredBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getUser().getUsername().equalsIgnoreCase(username)) {
                filteredBookings.add(booking);
            }
        }
        return filteredBookings;
    }

    public List<Booking> searchBookingsByRoomNumber(String roomNumber) {
        List<Booking> filteredBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getRoomNumber().equals(roomNumber)) {
                filteredBookings.add(booking);
            }
        }
        return filteredBookings;
    }

    public List<Booking> searchBookingsByDateRange(Date startDate, Date endDate) {
        List<Booking> filteredBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            if ((booking.getCheckInDate().after(startDate) || booking.getCheckInDate().equals(startDate)) &&
                    (booking.getCheckOutDate().before(endDate) || booking.getCheckOutDate().equals(endDate))) {
                filteredBookings.add(booking);
            }
        }
        return filteredBookings;
    }
}
