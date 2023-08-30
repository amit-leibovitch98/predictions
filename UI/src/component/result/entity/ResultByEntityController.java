package component.result.entity;

import component.main.MainController;
import component.result.ResultComponent;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import simulation.SimulationManager;
import simulation.world.detail.entity.Entity;
import java.util.List;

public class ResultByEntityController extends ResultComponent {

    @FXML
    private ListView<String> entitiesList;
    @FXML
    private Label entityDetailsLabel;
    private StringProperty simulationGuid;

    public ResultByEntityController() {
        super();
    }

    public void setEntitiesList(List<Entity> entities) {
        for (Entity entity : entities) {
            entitiesList.getItems().add(entity.getName());
        }
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
