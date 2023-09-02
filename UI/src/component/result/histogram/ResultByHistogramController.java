package component.result.histogram;

import component.result.ResultComponent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import simulation.SimulationManager;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityProperty;
import javafx.scene.input.MouseEvent;


import java.util.List;

public class ResultByHistogramController extends ResultComponent {

    @FXML
    private TreeView<String> entityPropertyTree;

    @FXML
    private ListView<String> propertyHistogramList;

    public ResultByHistogramController() {
        super();
    }

    public void setEntitiesProperties() {
        List<Entity> entityList = SimulationManager.getInstance().getSimulationByGuid(simulationGuid.getValue()).getWorld().getEntities();
        entityPropertyTree.setRoot(new TreeItem<>("Entities:"));
        TreeItem<String> root = entityPropertyTree.getRoot();
        root.setExpanded(true);
        int entityIndx = 0;
        for (Entity entity : entityList) {
            root.getChildren().add(new TreeItem<>(entity.getName()));
            root.getChildren().get(entityIndx).setExpanded(true);
            for (EntityProperty property : entityList.get(entityIndx).getProperties()) {
                root.getChildren().get(entityIndx).getChildren()
                        .add(new TreeItem<>(property.getName()));
            }
            entityIndx++;
        }
    }

    @FXML
    void showPropertyHistogram(MouseEvent event) {
        propertyHistogramList.getItems().clear();
        if (entityPropertyTree.getSelectionModel().getSelectedItem().getParent() == null ||
                entityPropertyTree.getSelectionModel().getSelectedItem().getParent() == entityPropertyTree.getRoot()) {
            propertyHistogramList.getItems().add("Please select a property");
        } else {
            String entityName = entityPropertyTree.getSelectionModel().getSelectedItem().getParent().getValue();
            String propertyName = entityPropertyTree.getSelectionModel().getSelectedItem().getValue();
            propertyHistogramList.setItems(logic.getPropertyHistogram(simulationGuid.getValue(), entityName, propertyName));
        }
    }
}
