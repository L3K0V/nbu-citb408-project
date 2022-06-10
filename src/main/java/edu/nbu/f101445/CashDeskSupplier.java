/* (C)2022 */
package edu.nbu.f101445;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CashDeskSupplier implements Supplier<Receipt> {

    Logger log = Logger.getGlobal();

    private final Store store;
    private final Receipt receipt;

    public CashDeskSupplier(Store store, Receipt receipt) {
        this.store = store;
        this.receipt = receipt;
    }

    private Receipt doCheckout() {
        AtomicBoolean outOfStock = new AtomicBoolean(false);
        final var items = receipt.getItems();
        items.keySet().forEach(it -> {
            try {
                store.removeFromInventory(it, items.get(it));
            } catch (OutOfStockException e) {
                outOfStock.set(true);
                log.log(Level.INFO, String.format("Receipt(%d): %s out of stock.",receipt.id, it.getName()));
            }
        });

        if (!outOfStock.get()) {
            return receipt;
        } else {
            log.log(Level.SEVERE, String.format("Receipt(%d): Cannot checkout. Out of stock.n", receipt.id));
            return null;
        }
    }

    @Override
    public Receipt get() {

        if (receipt.profit() > receipt.getPaid()) {
            log.log(Level.SEVERE, String.format("Receipt(%d): %f but paid only %f%n", receipt.id, receipt.profit(), receipt.getPaid()));
            return null;
        }

        return doCheckout();
    }
}
