package utils;

import enums.Amenities;
import models.Room;
import enums.RoomStatus;
import enums.RoomType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RoomManager {
    private static final String ROOM_FILE_PATH = "src/data/rooms.csv";
    private List<Room> rooms;

    public RoomManager() {
        rooms = new ArrayList<>();
        loadRooms();
    }

    private void loadRooms() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ROOM_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    String roomNumber = parts[0];
                    RoomType roomType = RoomType.valueOf(parts[1]);
                    double pricePerNight = Double.parseDouble(parts[2]);
                    double cancellationFee = Double.parseDouble(parts[3]);
                    RoomStatus status = RoomStatus.valueOf(parts[4]);
                    String[] amenitiesArray = parts[5].split(";");
                    List<Amenities> amenities = new ArrayList<>();
                    for (String amenity : amenitiesArray) {
                        amenities.add(Amenities.valueOf(amenity));
                    }
                    int maximumOccupancy = Integer.parseInt(parts[6]);
                    Room room = new Room(roomNumber, roomType, pricePerNight, cancellationFee, status, amenities, maximumOccupancy);
                    rooms.add(room);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading room details!");
        }
    }


    private void saveRooms() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ROOM_FILE_PATH))) {
            for (Room room : rooms) {
                writer.write(
                        room.getRoomNumber() + "," +
                                room.getRoomType() + "," +
                                room.getPricePerNight() + "," +
                                room.getCancellationFee() + "," +
                                room.getStatus()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving room details!");
        }
    }

    public void addRoom(Room room) {
        rooms.add(room);
        saveRooms();
    }

    public void updateRoom(Room room) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomNumber().equals(room.getRoomNumber())) {
                rooms.set(i, room);
                saveRooms();
                return;
            }
        }
        System.out.println("Room not found for updating.");
    }

    public boolean isRoomAvailable(String roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber().equals(roomNumber)) {
                return room.getStatus() == RoomStatus.AVAILABLE;
            }
        }
        return false;
    }

    public List<Room> getAvailableRooms() {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getStatus() == RoomStatus.AVAILABLE) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public Room getRoomByRoomNumber(String roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber().equals(roomNumber)) {
                return room;
            }
        }
        return null;
    }

    public void deleteRoom(Room room) {
        rooms.remove(room);
        saveRooms();
    }
}