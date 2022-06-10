/* (C)2022 */
package edu.nbu.f101445;

import java.time.LocalDate;
import java.util.UUID;

public class Good {
    final UUID id;
    final String name;
    final GoodCategory category;
    final LocalDate expirationDate;
    final double price;

    public Good(String name, GoodCategory category, LocalDate expirationDate, double price) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.category = category;
        this.expirationDate = expirationDate;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public GoodCategory getCategory() {
        return category;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }
}
