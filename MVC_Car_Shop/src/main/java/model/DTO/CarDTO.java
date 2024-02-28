package model.DTO;

public class CarDTO {
    int id;
    String manufacturer;
    String model;
    long price;

    public CarDTO(int id, String manufacturer, String model, long price) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.model = model;
        this.price = price;
    }

    // Override toString method to provide a formatted string representation of the CarDTO object
    @Override
    public String toString() {
        return "Car ID: " + id +
                ", Manufacturer: " + manufacturer +
                ", Model: " + model +
                ", Price: " + price;
    }
}