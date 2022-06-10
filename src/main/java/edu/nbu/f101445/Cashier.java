/* (C)2022 */
package edu.nbu.f101445;

import java.util.UUID;

public class Cashier {
    private final UUID id;
    private final String name;
    private final double salary;

    public Cashier(String name, double salary) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.salary = salary;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }
}
