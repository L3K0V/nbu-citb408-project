package edu.nbu.f101445;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Inventory implements IManageable {
    private final Map<Good, Double> stock;
    private double outcome;
    private double income;

    public Inventory() {
        this.stock = new ConcurrentHashMap<>();
    }

    public void addToStock(Good good, double quantity) {
        outcome += good.getPrice() * quantity;
        if (stock.containsKey(good)) {
            stock.merge(good, quantity, (Double::sum));
        } else {
            stock.put(good, quantity);
        }
    }

    public Good checkStock(Good good, double quantity) throws OutOfStockException {
        if (!stock.containsKey(good) || stock.get(good) < quantity) {
            throw new OutOfStockException();
        }

        if (good.expirationDate.isBefore(LocalDate.now())) {
            throw new OutOfStockException();
        }

        return good;
    }

    public synchronized void removeFromStock(Good good, double quantity) throws OutOfStockException {
        if (!stock.containsKey(good) || stock.get(good) < quantity) {
            throw new OutOfStockException();
        }

        if (good.expirationDate.isBefore(LocalDate.now())) {
            throw new OutOfStockException();
        }

        income += good.getPrice() * quantity;
        stock.merge(good, -quantity, Double::sum);
    }

    @Override
    public double income() {
        return income;
    }

    @Override
    public double outcome() {
        return outcome;
    }
}
