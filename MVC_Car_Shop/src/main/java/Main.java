import exceptions.BusinessException;
import controller.CarController;
import view.CarShopView;

public class Main {
    public static void main(String[] args) {
        try {
            CarController carController = new CarController();
            carController.loadCarsFromCSV();
            CarShopView carShopView = new CarShopView(carController);

            carShopView.handleUserInput();
        } catch (BusinessException e) {
            System.out.println(e.getMessage());
        }
    }
}