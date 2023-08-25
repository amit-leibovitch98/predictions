package logic;

import component.main.MainController;
import facade.EngineFacade;

public class Logic {
    private MainController mainController;
    private EngineFacade engine;
    public Logic(MainController mainController) {
        this.engine = new EngineFacade();
        this.mainController = mainController;
    }

    public EngineFacade getEngine() {
        return engine;
    }
}
