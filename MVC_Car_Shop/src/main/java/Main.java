import exceptions.BusinessException;
import controller.CarController;
import view.CarShopView;

public class Main {
    public static void main(String[] args) throws BusinessException {
        CarController carController = new CarController();
        CarShopView carShopView = new CarShopView(carController);

        carShopView.handleUserInput();
    }
}