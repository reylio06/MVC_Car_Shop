package model.DTO;

import model.entity.FuelType;

import java.sql.Timestamp;

public class CarDetailDTO {
    int id;
    String manufacturer;
    String model;
    long price;
    Timestamp productionDate;
    FuelType fuelType;
    int horsepower;

    public CarDetailDTO(int id, String manufacturer, String model, long price, Timestamp productionDate,
                        FuelType fuelType, int horsepower) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.model = model;
        this.price = price;
        this.productionDate = productionDate;
        this.fuelType = fuelType;
        this.horsepower = horsepower;
    }
    // Override toString() method to return a string representation of the car details
    @Override
    public String toString() {
        return "Car ID: " + id +
                ", Manufacturer: " + manufacturer +
                ", Model: " + model +
                ", Price (€): " + price +
                ", Production Date: " + productionDate +
                ", Fuel Type: " + fuelType +
                ", Horsepower: " + horsepower;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public long getPrice() {
        return price;
    }

    public Timestamp getProductionDate() {
        return productionDate;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public int getHorsepower() {
        return horsepower;
    }
}