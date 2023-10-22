package utils.managers;

import enums.RoomStatus;
import models.Booking;
import models.PromoCode;
import models.Room;

import java.util.List;

public class AdminManager {
    private RoomManager roomManager;
    private BookingManager bookingManager;
    private PromoCodeManager promoCodeManager;

    public AdminManager(RoomManager roomManager, BookingManager bookingManager) {
        this.roomManager = roomManager;
        this.bookingManager = bookingManager;
        this.promoCodeManager = new PromoCodeManager();
    }

    public void viewAllBookings() {
        List<Booking> bookings = bookingManager.getAllBookings();
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
        List<Booking> bookings = bookingManager.getAllBookings();
        double totalIncome = 0;
        for (Booking booking : bookings) {
            Room room = roomManager.getRoomByRoomNumber(booking.getRoomNumber());
            if (room != null && room.getStatus().equals(RoomStatus.BOOKED)) {
                long daysBetween = (booking.getCheckOutDate().getTime() - booking.getCheckInDate().getTime()) / (1000 * 60 * 60 * 24);
                totalIncome += room.getPricePerNight() * daysBetween;
            }
        }
        System.out.println("Total Income: $" + totalIncome);
    }

    public void viewTotalCancellationFees() {
        List<Booking> bookings = bookingManager.getAllBookings();
        double totalCancellationFees = 0;
        for (Booking booking : bookings) {
            Room room = roomManager.getRoomByRoomNumber(booking.getRoomNumber());
            if (room != null) {
                totalCancellationFees += room.getCancellationFee();
            }
        }
        System.out.println("Total Cancellation Fees: $" + totalCancellationFees);
    }

    public void addRoom(Room room) {
        roomManager.addRoom(room);
        System.out.println("Room added successfully!");
    }

    public void updateRoom(Room room) {
        roomManager.updateRoom(room);
        System.out.println("Room updated successfully!");
    }

    public void deleteRoom(String roomNumber) {
        Room room = roomManager.getRoomByRoomNumber(roomNumber);
        if (room != null) {
            roomManager.deleteRoom(room);
            System.out.println("Room deleted successfully!");
        } else {
            System.out.println("Room not found!");
        }
    }

    public void addPromoCode(PromoCode code) {
        if (!promoCodeManager.isValidPromoCode(code.getCode())) {
            promoCodeManager.addPromoCode(code);
            System.out.println("Promo code added successfully!");
        } else {
            System.out.println("Promo code already exists!");
        }
    }

    public void removePromoCode(String code) {
        if (promoCodeManager.isValidPromoCode(code)) {
            promoCodeManager.removePromoCode(code);
            System.out.println("Promo code removed successfully!");
        } else {
            System.out.println("Promo code not found!");
        }
    }

    public void viewAllPromoCodes() {
        List<PromoCode> promoCodes = promoCodeManager.getAllPromoCodes();
        if (promoCodes.isEmpty()) {
            System.out.println("No promotional codes found.");
        } else {
            for (PromoCode code : promoCodes) {
                System.out.println(code.getCode());
            }
        }
    }
}
