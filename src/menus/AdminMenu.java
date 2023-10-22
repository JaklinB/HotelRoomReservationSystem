package menus;

import controllers.AdminController;
import controllers.BookingController;
import controllers.RoomController;
import controllers.PromoCodeController;

import java.util.Scanner;

public class AdminMenu {
    private final RoomController roomController;
    private final BookingController bookingController;
    private final AdminController adminController;
    private final Scanner scanner;
    private final PromoCodeController promoCodeController;

    public AdminMenu(RoomController roomController, BookingController bookingController, AdminController adminController, PromoCodeController promoCodeController) {
        this.roomController = roomController;
        this.bookingController = bookingController;
        this.adminController = adminController;
        this.scanner = new Scanner(System.in);
        this.promoCodeController = promoCodeController;
    }

    public void start() {
        while (true) {
            System.out.println("\nAdmin Menu");
            System.out.println("1. View All Bookings");
            System.out.println("2. View Total Income");
            System.out.println("3. View Total Cancellation Fees");
            System.out.println("4. Manage Rooms");
            System.out.println("5. Manage Promotional Codes");
            System.out.println("6. Search");
            System.out.println("7. Logout");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1 -> adminController.viewAllBookings();
                case 2 -> adminController.viewTotalIncome();
                case 3 -> adminController.viewTotalCancellationFees();
                case 4 -> new ManageRoomsMenu(roomController).start();
                case 5 -> new ManagePromoCodesMenu(promoCodeController, adminController).start();
                case 6 -> new SearchMenu(bookingController).start();
                case 7 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
