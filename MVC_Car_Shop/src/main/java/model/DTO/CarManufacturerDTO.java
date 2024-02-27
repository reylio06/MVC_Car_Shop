package model.DTO;

public class CarManufacturerDTO {
    int id;
    String model;
    long price;

    public CarManufacturerDTO(int id, String model, long price) {
        this.id = id;
        this.model = model;
        this.price = price;
    }

    public String toString() {
        return "Car ID: " + id +
                ", Model: " + model +
                ", Price: " + price;
    }
}