package component.result.histogram;

import component.result.ResultComponent;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.entity.EntityProperty;

import java.util.List;

public class ResultByHistogramController extends ResultComponent {

    @FXML
    private TreeView<String> entityPropertyTree;

    @FXML
    private ListView<String> propertyHistogramList;

    public ResultByHistogramController() {
        super();
    }

    public void setEntitysProperties(List<Entity> entityList) {
        entityPropertyTree.setRoot(new TreeItem<>("Entities:"));
        TreeItem<String> root = entityPropertyTree.getRoot();
        root.setExpanded(true);
        int entityIndx = 0;
        for (Entity entity : entityList) {
            root.getChildren().add(new TreeItem<>(entity.getName()));
            for(EntityProperty property: entityList.get(entityIndx).getProperties()) {
                root.getChildren().get(entityIndx).getChildren()
                        .add(new TreeItem<>(property.getName()));
            }
            entityIndx++;
        }
    }


}
