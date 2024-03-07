package model.entity;

import java.sql.Timestamp;

public abstract class Vehicle {
    private int id;
    private Timestamp productionDate;
    private long price;

    public Vehicle(int id, Timestamp productionDate, long price) {
        this.id = id;
        this.productionDate = productionDate;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public Timestamp getProductionDate() {
        return productionDate;
    }

    public long getPrice() {
        return price;
    }
}
