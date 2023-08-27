package component.envarinput;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class EnvVarInputController {

    @FXML
    private Label envVarNameLabel;

    @FXML
    private TextField envVarValueInput;

    @FXML
    private Label envVarTypeLabel;

    public String getEnvVarName() {
        return envVarNameLabel.getText();
    }
    public String getEnvVarValue() {
        return envVarValueInput.getText();
    }

    public void setEnvVarValue(String envVarValue) {
        envVarValueInput.setText(envVarValue);
    }
    public void setEnvVarName(String envVarName) {
        envVarNameLabel.setText(envVarName);
    }

    public void setEnvVarType(String envVarType) {
        envVarTypeLabel.setText(" (" + envVarType.toUpperCase() + ")");
    }
}
