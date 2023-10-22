package utils.menus;

import enums.Amenities;
import models.PromoCode;
import utils.managers.PromoCodeManager;
import utils.managers.AdminManager;

import java.util.List;
import java.util.Scanner;

public class ManagePromoCodesMenu {
    private final PromoCodeManager promoCodeManager;
    private final Scanner scanner;
    private final AdminManager adminManager;

    public ManagePromoCodesMenu(PromoCodeManager promoCodeManager, AdminManager adminManager) {
        this.promoCodeManager = promoCodeManager;
        this.adminManager = adminManager;
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
                case 1:
                    addPromoCode();
                    break;
                case 2:
                    removePromoCode();
                    break;
                case 3:
                    adminManager.viewAllPromoCodes();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void addPromoCode() {
        String codeName = getValidPromoCodeName();
        if (codeName == null) return;

        double codePercentage = getValidDiscountPercentage();
        if (codePercentage == -1) return;

        promoCodeManager.addPromoCode(new PromoCode(codeName, codePercentage));
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
        return promoCodeManager.getPromoCodeByName(codeName) != null;
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
        promoCodeManager.removePromoCode(code);
    }
}
