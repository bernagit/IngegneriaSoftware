import controller.Controller;
import view.CliView;
import view.View;

public class MainFruitore {
    public static void main(String[] args) {
        View view = new CliView();
        Controller controller = new Controller(view);
        controller.run();
    }
}
