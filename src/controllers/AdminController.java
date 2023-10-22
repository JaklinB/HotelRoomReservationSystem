package controllers;

import enums.RoomStatus;
import models.Booking;
import models.PromoCode;
import models.Room;
import utils.DateUtils;

import java.util.List;
import java.util.Optional;

public class AdminController {
    private final RoomController roomController;
    private final BookingController bookingController;
    private final PromoCodeController promoCodeController;

    public AdminController(RoomController roomController, BookingController bookingController) {
        this.roomController = roomController;
        this.bookingController = bookingController;
        this.promoCodeController = new PromoCodeController();
    }

    public void viewAllBookings() {
        List<Booking> bookings = bookingController.getAllBookings();
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
        } else {
            System.out.println("All Bookings:");
            for (Booking booking : bookings) {
                System.out.println("Booking ID: " + booking.getBookingID());
                System.out.println("Room Number: " + booking.getRoomNumber());
                System.out.println("User: " + booking.getUser().getUsername());
                System.out.println("Check-In Date: " + booking.getCheckInDate());
                System.out.println("Check-Out Date: " + booking.getCheckOutDate());
                System.out.println();
            }
        }
    }

    public void viewTotalIncome() {
        List<Booking> bookings = bookingController.getAllBookings();
        double totalIncome = 0;
        for (Booking booking : bookings) {
            Optional<Room> room = roomController.getRoomByRoomNumber(booking.getRoomNumber());
            if (room.isPresent() && room.get().getStatus().equals(RoomStatus.BOOKED)) {
                long daysBetween = DateUtils.daysBetween(booking.getCheckOutDate(), booking.getCheckInDate());
                totalIncome += room.get().getPricePerNight() * daysBetween;
            }
        }
        System.out.println("Total Income: $" + totalIncome);
    }

    public void viewTotalCancellationFees() {
        List<Booking> bookings = bookingController.getAllBookings();
        double totalCancellationFees = 0;
        for (Booking booking : bookings) {
            Optional<Room> room = roomController.getRoomByRoomNumber(booking.getRoomNumber());
            if (room.isPresent()) {
                totalCancellationFees += room.get().getCancellationFee();
            }
        }
        System.out.println("Total Cancellation Fees: $" + totalCancellationFees);
    }

    public void viewAllPromoCodes() {
        List<PromoCode> promoCodes = promoCodeController.getAllPromoCodes();
        if (promoCodes.isEmpty()) {
            System.out.println("No promotional codes found.");
        } else {
            for (PromoCode code : promoCodes) {
                System.out.println(code.getCode());
            }
        }
    }
}
