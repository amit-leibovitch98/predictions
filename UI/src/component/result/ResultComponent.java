package component.result;

import facade.EngineFacade;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class ResultComponent {
    private StringProperty simulationGuid;

    public StringProperty getSimulationGuid() {
        return simulationGuid;
    }

    public ResultComponent() {
        this.simulationGuid = new SimpleStringProperty();
    }

}
