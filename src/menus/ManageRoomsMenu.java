package menus;

import enums.RoomStatus;
import enums.RoomType;
import enums.Amenities;
import models.Room;
import controllers.RoomController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ManageRoomsMenu {
    private final RoomController roomController;
    private final Scanner scanner;

    public ManageRoomsMenu(RoomController roomController) {
        this.roomController = roomController;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\nManage Rooms");
            System.out.println("1. Add Room");
            System.out.println("2. Remove Room");
            System.out.println("3. Modify Room Details");
            System.out.println("4. View Available Rooms");
            System.out.println("5. Return to Admin Menu");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> addRoom();
                case 2 -> removeRoom();
                case 3 -> modifyRoom();
                case 4 -> viewAvailableRooms();
                case 5 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void viewAvailableRooms() {
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

    private List<Amenities> parseAmenities(String input) {
        String[] amenitiesArray = input.split(";");
        List<Amenities> amenities = new ArrayList<>();
        for (String amenity : amenitiesArray) {
            try {
                amenities.add(Amenities.valueOf(amenity.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("This amenity is not available: " + amenity + ". Skipping.");
            }
        }
        return amenities;
    }

    private void addRoom() {
        System.out.print("Enter room number: ");
        String roomNumber = scanner.nextLine().trim();

        RoomType roomType = getValidRoomType("Enter room type (SINGLE, DOUBLE, SUITE, DELUXE): ");

        double pricePerNight = getValidDouble("Enter price per night: ");

        double cancellationFee = getValidDouble("Enter cancellation fee: ");

        RoomStatus roomStatus = getValidRoomStatus("Enter room status (AVAILABLE, BOOKED): ");

        System.out.print("Enter amenities (separated by semicolons) \n " +
                "Available amenities : \n " +
                "    TV,\n" +
                "    WIFI,\n" +
                "    MiniBar,\n" +
                "    Balcony,\n" +
                "    RoomService,\n" +
                "    GoodView: ");
        String amenitiesInput = scanner.nextLine().trim();
        List<Amenities> amenities = parseAmenities(amenitiesInput);

        int maximumOccupancy = getValidInt("Enter maximum occupancy: ");

        if (isValidRoomDetails(roomNumber, roomType, pricePerNight, cancellationFee, roomStatus, amenities, maximumOccupancy)) {
            Room room = new Room(roomNumber, roomType, pricePerNight, cancellationFee, roomStatus, amenities, maximumOccupancy);
            roomController.addRoom(room);
            System.out.println("Room added successfully!");
        } else {
            System.out.println("Room not saved due to missing or invalid details.");
        }
    }

    private boolean isValidRoomDetails(String roomNumber, RoomType roomType, double pricePerNight, double cancellationFee, RoomStatus roomStatus, List<Amenities> amenities, int maximumOccupancy) {
        if (roomNumber.isEmpty() || roomController.getRoomByRoomNumber(roomNumber).isPresent()) {
            System.out.println("Either the room number is empty or a room with this number already exists.");
            return false;
        }
        if (roomType == null || pricePerNight <= 0 || cancellationFee <= 0 || roomStatus == null || amenities == null || amenities.isEmpty() || maximumOccupancy <= 0) {
            return false;
        }
        return true;
    }

    private RoomType getValidRoomType(String prompt) {
        while (true) {
            System.out.print(prompt);
            String roomTypeInput = scanner.nextLine();
            try {
                return RoomType.valueOf(roomTypeInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid room type: " + roomTypeInput);
            }
        }
    }

    private RoomStatus getValidRoomStatus(String prompt) {
        while (true) {
            System.out.print(prompt);
            String roomStatusInput = scanner.nextLine();
            try {
                return RoomStatus.valueOf(roomStatusInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid room status: " + roomStatusInput);
            }
        }
    }

    private double getValidDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input format. Please enter a valid number.");
            }
        }
    }

    private int getValidInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input format. Please enter a valid integer.");
            }
        }
    }

    private void removeRoom() {
        System.out.print("Enter room number to remove: ");
        String roomNumber = scanner.nextLine();
        Optional<Room> roomToDelete = roomController.getRoomByRoomNumber(roomNumber);
        roomToDelete.ifPresent(roomController::deleteRoom);
    }

    private void modifyRoom() {
        System.out.print("Enter room number to modify: ");
        String roomNumber = scanner.nextLine();
        Optional<Room> room = roomController.getRoomByRoomNumber(roomNumber);

        if (room.isEmpty()) {
            System.out.println("Room not found!");
            return;
        }

        RoomType newRoomType = getValidRoomType("Enter new room type (SINGLE, DOUBLE, SUITE, DELUXE): ");
        double newPricePerNight = getValidDouble("Enter new price per night: ");
        double newCancellationFee = getValidDouble("Enter new cancellation fee: ");
        RoomStatus newRoomStatus = getValidRoomStatus("Enter new room status (AVAILABLE, BOOKED): ");

        room.get().setRoomType(newRoomType);
        room.get().setPricePerNight(newPricePerNight);
        room.get().setCancellationFee(newCancellationFee);
        room.get().setStatus(newRoomStatus);

        roomController.updateRoom(room.get());

    }
}
