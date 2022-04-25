package edu.nbu.f101445;

import edu.nbu.f101445.adjustments.CampaignAdjustment;
import edu.nbu.f101445.adjustments.ProfitForCategoryAdjustment;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        final var now = LocalDate.now();

        final var cokeCola = new Good("Coke-Cola 330ml", GoodCategory.GENERAL, now.plusMonths(4), 1.99);
        final var bread = new Good("Whole wheat bread", GoodCategory.FOOD, now.plusDays(2), 1.56);

        final var store = new Store();
        store.addToInventory(cokeCola, 10);
        store.addToInventory(bread, 8);
        store.addAdjustment(new CampaignAdjustment(2, 0.3));
        final var profits = new ProfitForCategoryAdjustment();
        profits.addProfitForCategory(GoodCategory.FOOD, 0.15);
        profits.addProfitForCategory(GoodCategory.GENERAL, 0.25);
        store.addAdjustment(profits);

        final var john = new Cashier("John", 760);
        final var anna = new Cashier("Anna", 800);
        final var cash1 = new CashDesk(john, store);
        final var cash2 = new CashDesk(anna, store);

        Thread t = new Thread(cash1);
        t.start();

        Thread t2 = new Thread(cash2);
        t2.start();

        cash1.addToCart(cokeCola, 3);
        cash1.addToCart(bread, 1);

        cash2.addToCart(cokeCola, 3);
        cash2.addToCart(bread, 1);

        cash2.checkout(8.86);
        cash1.checkout(9.00);
    }
}