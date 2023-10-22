
package controllers;

import models.PromoCode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PromoCodeController {

    private static final String PROMO_CODES_CSV = "src/data/promo-codes.csv";
    private final List<PromoCode> promoCodes;

    public PromoCodeController() {
        promoCodes = new ArrayList<>();
        loadPromoCodes();
    }

    public void addPromoCode(PromoCode promoCode) {
        promoCodes.add(promoCode);
        savePromoCodes();
    }

    public void removePromoCode(String code) {
        Optional<PromoCode> promoCodeToRemove = getPromoCodeByName(code);
        if (promoCodeToRemove.isPresent()) {
            promoCodes.remove(promoCodeToRemove.get());
            savePromoCodes();
        }
    }

    public List<PromoCode> getAllPromoCodes() {
        return promoCodes;
    }

    public boolean isValidPromoCode(String code) {
        for (PromoCode promoCode : promoCodes) {
            if (promoCode.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    public double getDiscountPercentage(String code) {
        for (PromoCode promoCode : promoCodes) {
            if (promoCode.getCode().equals(code)) {
                return promoCode.getDiscountPercentage();
            }
        }
        return 0;
    }

    private void loadPromoCodes() {
        File file = new File(PROMO_CODES_CSV);
        String promoCodeDelimiter = ",";
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(promoCodeDelimiter);
                    if (parts.length == 2) {
                        String code = parts[0].trim();
                        double discount = Double.parseDouble(parts[1].trim());
                        promoCodes.add(new PromoCode(code, discount));
                    }
                }
            } catch (IOException e) {
                System.out.println("Error when try to load promo code from file!");
            }
        }
    }

    private void savePromoCodes() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PROMO_CODES_CSV))) {
            for (PromoCode promoCode : promoCodes) {
                writer.println(promoCode.getCode() + "," + promoCode.getDiscountPercentage());
            }
        } catch (IOException e) {
            System.out.println("Error when try to save promo code in file!");
        }
    }

    public Optional<PromoCode> getPromoCodeByName(String codeName) {
        for (PromoCode promoCode : promoCodes) {
            if (promoCode.getCode().equals(codeName)) {
                return Optional.of(promoCode);
            }
        }
        return Optional.empty();
    }

}
