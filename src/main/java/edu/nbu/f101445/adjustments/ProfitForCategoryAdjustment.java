/* (C)2022 */
package edu.nbu.f101445.adjustments;

import edu.nbu.f101445.Good;
import edu.nbu.f101445.GoodCategory;
import java.util.HashMap;
import java.util.Map;

public class ProfitForCategoryAdjustment implements IAdjustable {

    private final Map<GoodCategory, Double> profitForCategory;

    public ProfitForCategoryAdjustment() {
        profitForCategory = new HashMap<>();
    }

    public void addProfitForCategory(GoodCategory category, double profit) {
        profitForCategory.put(category, profit);
    }

    @Override
    public String label() {
        return "Tax";
    }

    @Override
    public boolean capAdjust() {
        return true;
    }

    @Override
    public boolean capAdjustQuantity() {
        return true;
    }

    @Override
    public double adjust(Good good) {
        return good.getPrice() * profitForCategory.getOrDefault(good.getCategory(), 0.0);
    }

    @Override
    public double adjust(Good good, double quantity) {
        return adjust(good) * quantity;
    }
}
