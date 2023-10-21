package models;

import enums.Amenities;
import enums.RoomStatus;
import enums.RoomType;

import java.util.List;

public class Room {
    private String roomNumber;
    private RoomType roomType;
    private double pricePerNight;
    private double cancellationFee;
    private RoomStatus status;
    private List<Amenities> amenities;
    private int maximumOccupancy;

    public Room(String roomNumber, RoomType roomType, double pricePerNight, double cancellationFee, RoomStatus roomStatus, List<Amenities> amenities, int maximumOccupancy) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.cancellationFee = cancellationFee;
        this.status = roomStatus;
        this.amenities = amenities;
        this.maximumOccupancy = maximumOccupancy;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public double getCancellationFee() {
        return cancellationFee;
    }

    public void setCancellationFee(double cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public List<Amenities> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Amenities> amenities) {
        this.amenities = amenities;
    }

    public int getMaximumOccupancy() {
        return maximumOccupancy;
    }

    public void setMaximumOccupancy(int maximumOccupancy) {
        this.maximumOccupancy = maximumOccupancy;
    }
}
