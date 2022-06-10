package edu.nbu.f101445;

import edu.nbu.f101445.adjustments.CampaignAdjustment;
import edu.nbu.f101445.adjustments.ProfitForCategoryAdjustment;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) {
        final var now = LocalDate.now();

        final var cokeCola = new Good("Coke-Cola 330ml", GoodCategory.GENERAL, now.plusMonths(4), 1.99);
        final var bread = new Good("Whole wheat bread", GoodCategory.FOOD, now.plusDays(2), 1.56);
        final var freshMeat = new Good("Fresh Red Meat", GoodCategory.FOOD, now.plusDays(1), 9.50);

        final var store = new Store();
        store.addToInventory(cokeCola, 10);
        store.addToInventory(bread, 3);
        store.addToInventory(freshMeat, 8);
        store.addAdjustment(new CampaignAdjustment(2, 0.3));
        store.addAdjustment(new CampaignAdjustment(1, 0.5));
        final var profits = new ProfitForCategoryAdjustment();
        profits.addProfitForCategory(GoodCategory.FOOD, 0.15);
        profits.addProfitForCategory(GoodCategory.GENERAL, 0.5);
        store.addAdjustment(profits);

        final var john = new Cashier("John", 760);
        final var anna = new Cashier("Anna", 800);
        final var cash1 = new CashDesk(john, store);
        final var cash2 = new CashDesk(anna, store);

        store.addCashier(john, anna);
        store.addDesks(cash1, cash2);

        try {

            cash1.addToCart(cokeCola, 3);
            cash1.addToCart(bread, 1);

            cash2.addToCart(cokeCola, 5);
            cash2.addToCart(bread, 1);
            cash2.addToCart(freshMeat, 1);

            CompletableFuture.allOf(
                    cash1.checkout(100),
                    cash2.checkout(30)).get();

            cash1.addToCart(cokeCola, 1);
            cash2.addToCart(cokeCola, 1);
            cash2.addToCart(freshMeat, 8);

            CompletableFuture.allOf(
                    cash1.checkout(3),
                    cash2.checkout(19)).get();

            System.out.println(store.outcome());
            System.out.println(store.income());
            System.out.println(store.profit());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}