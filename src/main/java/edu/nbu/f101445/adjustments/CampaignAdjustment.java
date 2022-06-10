/* (C)2022 */
package edu.nbu.f101445.adjustments;

import edu.nbu.f101445.Good;
import java.time.LocalDate;

public class CampaignAdjustment implements IAdjustable {
    final int daysBeforeExpiration;
    final double campaignPercentage;

    public CampaignAdjustment(int daysBeforeExpiration, double campaignPercentage) {
        this.daysBeforeExpiration = daysBeforeExpiration;
        this.campaignPercentage = campaignPercentage;
    }

    @Override
    public String label() {
        return "Discount";
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

        final var target = good.getExpirationDate().minusDays(daysBeforeExpiration);
        final var now = LocalDate.now();
        if (now.isAfter(target) || now.isEqual(target)) {
            return -(campaignPercentage * good.getPrice());
        }

        return 0;
    }

    @Override
    public double adjust(Good good, double quantity) {
        return adjust(good) * quantity;
    }
}
