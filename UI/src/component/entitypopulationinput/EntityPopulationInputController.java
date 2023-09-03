package component.entitypopulationinput;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EntityPopulationInputController {

    @FXML
    private Label entityNameLabel;

    @FXML
    private TextField entityPopulationInput;

    public String getEntityName() {
        return entityNameLabel.getText();
    }
    public String getEntityPopulation() {
        return entityPopulationInput.getText();
    }
    public void setEntityName(String entityName) {
        entityNameLabel.setText(entityName + "'s population");
    }

}
