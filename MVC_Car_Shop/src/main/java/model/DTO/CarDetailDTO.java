package model.DTO;

import model.entity.FuelType;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

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

    // Override toString method to provide a formatted string representation of the CarDetailDTO object
    @Override
    public String toString() {
        return "Car ID: " + id +
                ", Manufacturer: " + manufacturer +
                ", Model: " + model +
                ", Price (€): " + price +
                ", Production Date: " + getFormattedProductionDate() +
                ", Fuel Type: " + fuelType +
                ", Horsepower: " + horsepower;
    }

    // Method to return the production date in a formatted string
    public String getFormattedProductionDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(this.productionDate);
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