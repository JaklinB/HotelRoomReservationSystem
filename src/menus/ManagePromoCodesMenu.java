package menus;

import models.PromoCode;
import controllers.PromoCodeController;
import controllers.AdminController;

import java.util.Scanner;

public class ManagePromoCodesMenu {
    private final PromoCodeController promoCodeController;
    private final Scanner scanner;
    private final AdminController adminController;

    public ManagePromoCodesMenu(PromoCodeController promoCodeController, AdminController adminController) {
        this.promoCodeController = promoCodeController;
        this.adminController = adminController;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\nManage Promotional Codes");
            System.out.println("1. Add Promotional Code");
            System.out.println("2. Remove Promotional Code");
            System.out.println("3. View All Promotional Codes");
            System.out.println("4. Return to Admin Menu");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> addPromoCode();
                case 2 -> removePromoCode();
                case 3 -> adminController.viewAllPromoCodes();
                case 4 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void addPromoCode() {
        String codeName = getValidPromoCodeName();
        if (codeName == null) return;

        double codePercentage = getValidDiscountPercentage();
        if (codePercentage == -1) return;

        promoCodeController.addPromoCode(new PromoCode(codeName, codePercentage));
        System.out.println("Promo code added successfully!");
    }

    private String getValidPromoCodeName() {
        System.out.print("Enter new promotional code name: ");
        String codeName = scanner.nextLine().trim();

        if (codeName.isEmpty()) {
            System.out.println("Promo code name cannot be empty.");
            return null;
        }

        if (promoCodeExists(codeName)) {
            System.out.println("A promo code with this name already exists. Please choose a unique name.");
            return null;
        }

        return codeName;
    }

    private boolean promoCodeExists(String codeName) {
        return promoCodeController.getPromoCodeByName(codeName).isPresent();
    }

    private double getValidDiscountPercentage() {
        System.out.print("Enter discount percentage for the promo code (0-100): ");
        try {
            double codePercentage = Double.parseDouble(scanner.nextLine().trim());
            if (codePercentage < 0 || codePercentage > 100) {
                System.out.println("Invalid percentage. Please enter a value between 0 and 100.");
                return -1;
            }
            return codePercentage;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid percentage.");
            return -1;
        }
    }

    private void removePromoCode() {
        System.out.print("Enter promotional code to remove: ");
        String code = scanner.nextLine();
        promoCodeController.removePromoCode(code);
    }
}
