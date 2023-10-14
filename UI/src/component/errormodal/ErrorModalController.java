package component.errormodal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public class ErrorModalController {

    @FXML
    private Label errorMassegeLabel;

    @FXML
    private Button okB;

    public ErrorModalController() {
        this.errorMassegeLabel = new Label();
        this.okB = new Button();
    }

    public void setErrorMessage(String message) {
    	errorMassegeLabel.setText(message);
    }

    @FXML
    void closeModal(ActionEvent event) {
        okB.getScene().getWindow().hide();
    }

}
