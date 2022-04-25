package edu.nbu.f101445;

import java.util.ArrayList;
import java.util.List;

public class CashDesk implements Runnable, IProfitable, IManageable {
    private final Cashier cashier;
    private final Store store;
    private final List<Receipt> receipts;
    private Receipt openReceipt;

    private boolean isOpen;
    private boolean checkout;
    private double checkoutPrice;

    public CashDesk(Cashier cashier, Store store) {
        this.cashier = cashier;
        this.store = store;
        this.receipts = new ArrayList<>();
        isOpen = true;
    }

    public void addToCart(Good good, double quantity) {
        if (openReceipt == null) {
            System.out.println("OPEN NEW RECEIPT");
            openReceipt = new Receipt(cashier, store.getAdjustmentsList());
        }

        System.out.printf("ADD ITEM %s x %.3f%n", good.getName(), quantity);
        openReceipt.addItem(good, quantity);
    }

    public void totalCart() {
        System.out.println(openReceipt.toString());
    }

    public void checkout(double paid) {
        checkoutPrice = paid;
        checkout = true;
        System.out.printf("CHECKOUT %.2f%n", paid);
    }

    public void close() {
        isOpen = false;
    }

    private void doCheckout() {
        final var items = openReceipt.getItems();
        items.keySet().forEach(it -> {
            try {
                store.removeFromInventory(it, items.get(it));
            } catch (OutOfStockException e) {
                System.out.println("OUT OF STOCK, SORRY...");
                openReceipt = null;
                checkout = false;
                checkoutPrice = 0;
            }
        });
    }

    private synchronized void closeReceipt() {
        System.out.println("CLOSE RECEIPT");
        openReceipt = null;
        checkout = false;
        checkoutPrice = 0;
    }

    @Override
    public void run() {
        while (isOpen) {
            if (checkout && checkoutPrice > 0) {
                totalCart();

                if (openReceipt.profit() > checkoutPrice) {
                    closeReceipt();
                    continue;
                }

                doCheckout();
                closeReceipt();
            }
        }
    }

    @Override
    public double profit() {
        return income() - outcome();
    }

    @Override
    public double income() {
        return receipts.stream().map(Receipt::profit).reduce(0.0, Double::sum);
    }

    @Override
    public double outcome() {
        return cashier.getSalary();
    }
}
