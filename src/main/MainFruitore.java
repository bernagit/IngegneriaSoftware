package main;

import controller.ControllerFruitore;
import view.CliView;
import view.View;

public class MainFruitore {
    public static void main(String[] args) {
        View view = new CliView();
        ControllerFruitore controller = new ControllerFruitore(view);
        controller.run();
    }
}
