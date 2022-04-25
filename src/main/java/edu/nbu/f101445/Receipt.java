package edu.nbu.f101445;

import edu.nbu.f101445.adjustments.IAdjustable;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Receipt implements IProfitable {

    private static long count;
    private final long id;
    private final Cashier cashier;
    private final LocalDateTime dateTime;
    private final Map<Good, Double> items;
    private final List<IAdjustable> adjustments;


    public Receipt(Cashier cashier, List<IAdjustable> adjustments) {
        count++;
        this.id = count;
        this.cashier = cashier;
        this.dateTime = LocalDateTime.now();
        this.items = new HashMap<>();
        this.adjustments = adjustments;
    }

    public void addItem(Good item, double quantity) {
        if (items.containsKey(item)) {
            items.merge(item, quantity, Double::sum);
        } else {
            items.put(item, quantity);
        }
    }

    public Map<Good, Double> getItems() {
        return items;
    }

    @Override
    public double profit() {
        return items.keySet().stream().map(it -> it.getPrice() * items.get(it) + adjustments.stream().map(ad -> ad.adjust(it, items.get(it))).reduce(0.0, Double::sum)).reduce(0.0, Double::sum);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("ID: %d\n", id));
        sb.append(String.format("Operator: %s\n", cashier.getName()));
        sb.append(String.format("Date: %s\n", dateTime.toString()));
        sb.append("------------------------------------------------\n\n");
        items.forEach((good, aDouble) -> {
            final var price = new AtomicReference<>(good.getPrice());
            sb.append(String.format("%-32s x %.3f\t%.2f\n", good.getName(), aDouble, good.getPrice()));
            adjustments.forEach(iAdjustable -> {
                price.set(price.get() + price.get() * iAdjustable.adjust(good));
                sb.append(String.format("\t\t%.2f\t%s\n", iAdjustable.adjust(good), iAdjustable.label()));
            });
            sb.append(String.format("\t\t%-28s \t\t%-10.2f\n", "Subtotal:", price.get()));
            sb.append(String.format("\t\t%-28s \t\t%-10.2f\n", "Total:", price.get() * aDouble));
        });

        sb.append("------------------------------------------------\n\n");
        sb.append(String.format("TOTAL: \t%.2f\n\n", profit()));

        return sb.toString();
    }
}
