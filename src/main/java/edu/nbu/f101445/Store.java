package edu.nbu.f101445;

import edu.nbu.f101445.adjustments.IAdjustable;

import java.util.ArrayList;
import java.util.List;

class Store implements IProfitable, IManageable {

    private final Inventory inventory;
    private List<Cashier> cashierList;
    private List<CashDesk> cashDesks;

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

    @Override
    public double profit() {
        return 0;
    }

    @Override
    public double income() {
        return 0;
    }

    @Override
    public double outcome() {
        final var cashiersOutcome = cashierList.stream().map(Cashier::getSalary).reduce(0.0, Double::sum);
        final var inventoryOutcome = inventory.outcome();
        return cashiersOutcome + inventoryOutcome;
    }
}
