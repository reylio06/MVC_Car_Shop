package model.DTO;

public class CarManufacturerDTO {
    private int id;
    private String model;
    private long price;

    public CarManufacturerDTO(int id, String model, long price) {
        this.id = id;
        this.model = model;
        this.price = price;
    }

    // Override toString method to provide a formatted string representation of the CarManufacturerDTO object
    @Override
    public String toString() {
        return "Car ID: " + id +
                ", Model: " + model +
                ", Price (€): " + price;
    }
}