package edu.nbu.f101445.adjustments;

import edu.nbu.f101445.Good;

public interface IAdjustable {

    String label();

    boolean capAdjust();

    boolean capAdjustQuantity();

    double adjust(Good good);

    double adjust(Good good, double quantity);
}
