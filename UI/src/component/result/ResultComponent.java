package component.result;

import facade.EngineFacade;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logic.Logic;

public abstract class ResultComponent {
    protected String simulationGuid;
    protected Logic logic;

    public String getSimulationGuid() {
        return simulationGuid;
    }

    public void setSimulationGuid(String simulationGuid) {
        this.simulationGuid = simulationGuid;
    }

    public ResultComponent() {

    }
    public void setLogic(Logic logic) {
        this.logic = logic;
    }

}
