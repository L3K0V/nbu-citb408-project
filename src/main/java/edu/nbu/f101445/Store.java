/* (C)2022 */
package edu.nbu.f101445;

import edu.nbu.f101445.adjustments.IAdjustable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Store implements IProfitable, IManageable {

    private final Inventory inventory;
    private final List<Cashier> cashierList;
    private final List<CashDesk> cashDesks;

    private final List<IAdjustable> adjustmentsList;

    protected Store() {
        this.inventory = new Inventory();
        this.cashierList = new ArrayList<>();
        this.cashDesks = new ArrayList<>();
        this.adjustmentsList = new ArrayList<>();
    }

    public void addToInventory(Good good, double quantity) {
        inventory.addToStock(good, quantity);
    }

    public void removeFromInventory(Good good, double quantity) throws OutOfStockException {
        inventory.removeFromStock(good, quantity);
    }

    public void addAdjustment(IAdjustable adjustable) {
        adjustmentsList.add(adjustable);
    }

    public List<IAdjustable> getAdjustmentsList() {
        return this.adjustmentsList;
    }

    public void addCashier(Cashier... cashiers) {
        Collections.addAll(cashierList, cashiers);
    }

    public void addDesks(CashDesk... desks) {
        Collections.addAll(cashDesks, desks);
    }

    @Override
    public double profit() {
        return cashDesks.stream().map(CashDesk::profit).reduce(0.0, Double::sum);
    }

    @Override
    public double income() {
        return cashDesks.stream().map(CashDesk::income).reduce(0.0, Double::sum);
    }

    @Override
    public double outcome() {
        final var cashiersOutcome =
                cashierList.stream().map(Cashier::getSalary).reduce(0.0, Double::sum);
        final var inventoryOutcome = inventory.outcome();
        return cashiersOutcome + inventoryOutcome;
    }
}
