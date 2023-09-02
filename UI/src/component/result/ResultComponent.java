package component.result;

import facade.EngineFacade;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logic.Logic;

public abstract class ResultComponent {
    protected StringProperty simulationGuid;
    protected Logic logic;

    public StringProperty getSimulationGuid() {
        return simulationGuid;
    }

    public ResultComponent() {
        this.simulationGuid = new SimpleStringProperty();
    }
    public void setLogic(Logic logic) {
        this.logic = logic;
    }

}
