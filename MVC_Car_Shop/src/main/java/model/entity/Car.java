package model.entity;

import model.DTO.CarDTO;
import model.DTO.CarDetailDTO;
import model.DTO.CarManufacturerDTO;

import java.sql.Timestamp;

public class Car extends Vehicle {

    // Attributes specific to Car entity
    private String manufacturer;
    private String model;
    private FuelType fuelType;
    private int horsepower;

    public Car(int id, String manufacturer, String model, long price, Timestamp productionDate, FuelType fuelType, int horsepower) {
        // Call superclass constructor to initialize common attributes
        super(id, productionDate, price);
        // Assign the provided ID directly
//        this.setId(id);
        this.manufacturer = manufacturer;
        this.model = model;
        this.fuelType = fuelType;
        this.horsepower = horsepower;

    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public int getHorsepower() {
        return horsepower;
    }


    // Convert entity to DTO methods
    public CarDetailDTO toCarDetailDTO() {
        return new CarDetailDTO(this.getId(), manufacturer, model, this.getPrice(), this.getProductionDate(), fuelType, horsepower);
    }

    public CarManufacturerDTO toCarManufacturerDTO() {
        return new CarManufacturerDTO(this.getId(), model, this.getPrice());
    }

    public CarDTO toCarDTO() {
        return new CarDTO(this.getId(), manufacturer, model, this.getPrice());
    }
}
