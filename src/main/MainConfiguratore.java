package main;

import controller.ControllerConfiguratore;
import view.CliView;
import view.View;

public class MainConfiguratore {
    public static void main(String[] args) {
        View view = new CliView();
        ControllerConfiguratore controller = new ControllerConfiguratore(view);
        controller.run();
    }
}
