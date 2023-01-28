package main;

import controller.Controller;
import controller.ControllerConfiguratore;
import view.CliView;
import view.View;

public class MainConfiguratore {
    public static void main(String[] args) {
        View view = new CliView();
        Controller controller = new ControllerConfiguratore(view);
        controller.run();
    }
}
