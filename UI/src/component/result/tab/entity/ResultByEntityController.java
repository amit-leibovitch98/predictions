package component.result.tab.entity;

import component.result.ResultComponent;
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

    public ResultByEntityController() {
        super();
    }

    public void initialize() {
        entitiesList.getSelectionModel().selectFirst();
    }

    public void setEntitiesList() {
        for (Entity entity : SimulationManager.getInstance().getSimulationByGuid(simulationGuid.getValue()).getWorld().getEntities()) {
            entitiesList.getItems().add(entity.getName());
        }
    }

    @FXML
    void showEntityDetails(MouseEvent mouseEvent) {
        updateEntityDetails();
    }

    public void updateEntityDetails() {
        entityDetailsLabel.setText(
                SimulationManager.getInstance().getSimulationResultByEntities(
                        simulationGuid.getValue(),
                        entitiesList.getSelectionModel().getSelectedItem()
                )
        );    }
}
