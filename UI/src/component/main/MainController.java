package component.main;

import component.entitypopulationinput.EntityPopulationInputController;
import component.envarinput.EnvVarInputController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.Logic;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.environmentvariables.EnvironmentVariable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class MainController {
    @FXML
    private Label QueueManagerLabel;

    @FXML
    private Button StartB;

    @FXML
    private Button clearB;

    @FXML
    private Label componentDetailLabel;

    @FXML
    private TreeView<String> componentsTree;

    @FXML
    private Tab detailsTab;

    @FXML
    private FlowPane entitiesPopulationsInputs;

    @FXML
    private Tab newExecTab;

    @FXML
    private Label pathLabel;

    @FXML
    private Label progressPrecantegeLabel;

    @FXML
    private ListView<String> queueList;

    @FXML
    private Tab resultsTab;

    @FXML
    private ProgressBar simulationProccessBar;

    @FXML
    private Button uploadFileB;

    @FXML
    private FlowPane envVarsInputs;


    private StringProperty path;
    private StringProperty componentDetail;
    private BooleanProperty isFileUploaded;
    private Logic logic;
    private Stage primaryStage;
    private StringProperty progressPrecantege;

    public MainController() {
        System.out.println("MainController created!");
        this.path = new SimpleStringProperty();
        this.isFileUploaded = new SimpleBooleanProperty();
        this.componentDetail = new SimpleStringProperty();
        this.componentDetailLabel = new Label();
        this.progressPrecantege = new SimpleStringProperty();

    }

    public void initialize() {
        pathLabel.textProperty().bind(path);
        componentDetailLabel.textProperty().bind(componentDetail);
        newExecTab.setDisable(true);
    }

    public void bindTaskToProgressBar(Task<Boolean> aTask, Runnable onFinish) {
        simulationProccessBar.progressProperty().bind(aTask.progressProperty());
        progressPrecantegeLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        aTask.progressProperty(),
                                        100)),
                        " %"));
        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished(Optional.ofNullable(onFinish));
        });
    }

    private void onTaskFinished(Optional<Runnable> onFinish) {
        //TODO: implement
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void uploadFileB(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file == null) {
            return;
        }
        String path = file.getAbsolutePath();
        this.path.set(path);
        this.isFileUploaded.set(true);
        try {
            logic.getEngine().readFile(file.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        logic.updateTreeView(componentsTree);
        newExecTab.setDisable(false);
        updateNewExecutionTab();
    }

    private void updateNewExecutionTab() {
        try {
            addEntityPopulationComponents();
            addEnvVarsComponents();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addEntityPopulationComponents() throws IOException {
        for (Entity entity : logic.getEngine().getEntities()) {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource("/component/entitypopulationinput/entityPopulationInput.fxml");
            loader.setLocation(url);
            Node entityPopulationInputNode = loader.load();
            EntityPopulationInputController entityPopulationInputController = loader.getController();
            entityPopulationInputController.setEntityName(entity.getName());
            entityPopulationInputController.setEntityPopulation(entity.getPopulation());
            entitiesPopulationsInputs.getChildren().add(entityPopulationInputNode);
        }
    }

    private void addEnvVarsComponents() throws IOException {
        for (EnvironmentVariable envVar : logic.getEngine().getEnvironmentVariables()) {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource("/component/envarinput/envVarInput.fxml");
            loader.setLocation(url);
            Node envVarsInputNode = loader.load();
            EnvVarInputController envVarInputController = loader.getController();
            envVarInputController.setEnvVarName(envVar.getName());
            envVarInputController.setEnvVarType(envVar.getType().toString());
            envVarInputController.setEnvVarValue(envVar.getValue().toString());
            envVarsInputs.getChildren().add(envVarsInputNode);
        }
    }

    @FXML
    void componentTreeItemRequested(MouseEvent event) {
        TreeItem<String> item = componentsTree.getSelectionModel().getSelectedItem();
        if (item != null) {
            String str = logic.getComponentInfo(item.getValue(), item.getParent().getValue());
            if (str != null)
                componentDetail.set(str);
            else
                componentDetail.set("No details available");
        }
    }

    @FXML
    void clearInputs(ActionEvent event) {
        //TODO: implement
    }

    @FXML
    void startSimulation(ActionEvent event) {
        logic.getEngine().startSimulation();
    }

}
