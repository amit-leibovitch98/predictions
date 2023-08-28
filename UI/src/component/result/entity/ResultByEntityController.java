package component.result.entity;

import component.main.MainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import simulation.SimulationManager;
import simulation.world.detail.entity.Entity;
import java.util.List;

public class ResultByEntityController {

    @FXML
    private ListView<String> entitiesList;
    @FXML
    private Label entityDetailsLabel;
    private StringProperty simulationGuid;

    public ResultByEntityController() {
        this.simulationGuid = new SimpleStringProperty();
    }

    public void setEntitiesList(List<Entity> entities) {
        for (Entity entity : entities) {
            entitiesList.getItems().add(entity.getName());
        }
    }

    public StringProperty getSimulationGuid() {
        return simulationGuid;
    }

    @FXML
    void showEntityDetails(MouseEvent mouseEvent) {
        entityDetailsLabel.setText(
                SimulationManager.getInstance().getSimulationResultByEntities(
                        simulationGuid.getValue(),
                        entitiesList.getSelectionModel().getSelectedItem()
                )
        );
    }

}
