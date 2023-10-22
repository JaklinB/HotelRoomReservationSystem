package utils.menus;

import utils.managers.AdminManager;
import utils.managers.BookingManager;
import utils.managers.RoomManager;
import utils.managers.PromoCodeManager;

import java.util.Scanner;

public class AdminMenu {
    private final RoomManager roomManager;
    private final BookingManager bookingManager;
    private final AdminManager adminManager;
    private final Scanner scanner;
    private final PromoCodeManager promoCodeManager;

    public AdminMenu(RoomManager roomManager, BookingManager bookingManager, AdminManager adminManager, PromoCodeManager promoCodeManager) {
        this.roomManager = roomManager;
        this.bookingManager = bookingManager;
        this.adminManager = adminManager;
        this.scanner = new Scanner(System.in);
        this.promoCodeManager = promoCodeManager;
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
                case 1:
                    adminManager.viewAllBookings();
                    break;
                case 2:
                    adminManager.viewTotalIncome();
                    break;
                case 3:
                    adminManager.viewTotalCancellationFees();
                    break;
                case 4:
                    new ManageRoomsMenu(roomManager).start();
                    break;
                case 5:
                    new ManagePromoCodesMenu(promoCodeManager, adminManager).start();
                    break;
                case 6:
                    new SearchMenu(bookingManager).start();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
