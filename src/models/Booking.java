package models;

import java.util.Date;
import java.util.UUID;

public class Booking {
    private final String bookingID;
    private final String roomNumber;
    private final User user;
    private final Date checkInDate;
    private final Date checkOutDate;

    public Booking(String bookingID, String roomNumber, User user, Date checkInDate, Date checkOutDate) {
        this.bookingID = bookingID;
        this.roomNumber = roomNumber;
        this.user = user;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public String getBookingID() {
        return bookingID;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public User getUser() {
        return user;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }
}
