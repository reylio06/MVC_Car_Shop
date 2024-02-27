package model.entity;

import java.sql.Timestamp;

public abstract class Vehicle {
    public int id;
    public Timestamp productionDate;
    public long price;

    public Vehicle(int id, Timestamp productionDate, long price) {
        this.id = id;
        this.productionDate = productionDate;
        this.price = price;
    }
}
