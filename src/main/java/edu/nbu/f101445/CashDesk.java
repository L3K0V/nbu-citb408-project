/* (C)2022 */
package edu.nbu.f101445;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CashDesk implements IProfitable, IManageable {
    private final Cashier cashier;
    private final Store store;
    private final List<Receipt> receipts;
    private Receipt openReceipt;

    public CashDesk(Cashier cashier, Store store) {
        this.cashier = cashier;
        this.store = store;
        this.receipts = new ArrayList<>();
    }

    public CompletableFuture<Receipt> checkout(double price)
            throws ExecutionException, InterruptedException {
        openReceipt.setPaid(price);
        return CompletableFuture.supplyAsync(new CashDeskSupplier(store, openReceipt))
                .thenApply(this::trackSale)
                .thenApply(this::persistReceipt);
    }

    private Receipt trackSale(Receipt receipt) {
        if (receipt != null) {
            receipts.add(receipt);
            openReceipt = null;
        }
        return receipt;
    }

    private Receipt persistReceipt(Receipt receipt) {
        if (receipt != null) {
            try {
                Files.writeString(
                        Path.of("receipts", receipt.id + ".txt"),
                        receipt.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return receipt;
    }

    public void addToCart(Good good, double quantity) {
        if (openReceipt == null) {
            openReceipt = new Receipt(cashier, store.getAdjustmentsList());
        }
        openReceipt.addItem(good, quantity);
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
