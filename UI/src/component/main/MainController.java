package component.main;

import component.errormodal.ErrorModalController;
import component.result.tab.ResultTabController;
import component.result.tab.entity.ResultByEntityController;
import component.result.tab.histogram.ResultByHistogramController;
import javafx.beans.property.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Logic;
import simulation.Simulation;
import simulation.SimulationDC;
import simulation.SimulationManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MainController {
    @FXML
    private Label QueueManagerLabel;
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
    private FlowPane envVarsInputs;
    @FXML
    private Tab newExecTab;
    @FXML
    private Label pathLabel;
    @FXML
    private Button pauseB;
    @FXML
    private ListView<String> queueList;
    @FXML
    private Button rerunB;

    @FXML
    private Tab resultsTab;
    @FXML
    private Button resumeB;
    @FXML
    private ToggleGroup showRusltBy;
    @FXML
    private HBox simulationContreollers;
    @FXML
    private Label simulationDetailsLabel;
    @FXML
    private TabPane simulationsTabsPane;
    @FXML
    private Button startB;
    @FXML
    private Button stopB;
    @FXML
    private Label threadsNumLabel;
    @FXML
    private Button uploadFileB;

    private StringProperty selectedSimulationGUID;
    private StringProperty path;
    private StringProperty componentDetail;
    private BooleanProperty isFileUploaded;
    private Logic logic;
    private Stage primaryStage;
    private BooleanProperty isSimulationRunning;
    private IntegerProperty maxThreadsNum;

    public MainController() {
        this.path = new SimpleStringProperty();
        this.isFileUploaded = new SimpleBooleanProperty();
        this.componentDetail = new SimpleStringProperty();
        this.componentDetailLabel = new Label();
        this.queueList = new ListView<>();
        this.selectedSimulationGUID = new SimpleStringProperty();
        this.isSimulationRunning = new SimpleBooleanProperty();
        this.maxThreadsNum = new SimpleIntegerProperty();
    }

    public void initialize() {
        this.pathLabel.textProperty().bind(path);
        this.componentDetailLabel.textProperty().bind(componentDetail);
        this.newExecTab.disableProperty().bind(isFileUploaded.not());
        this.resultsTab.setDisable(true);
        this.simulationContreollers.disableProperty().bind(isSimulationRunning);
        this.threadsNumLabel.textProperty().bind(this.maxThreadsNum.asString());
        this.simulationsTabsPane.getTabs().clear();
        //TODO: implement start/pause/resume button and bind their disable property to worldDef 's isInteractive
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setSimulationStatus(boolean status) {
        this.isSimulationRunning.set(status);
    }

    public void setMaxThreadsNum(int maxThreadsNum) {
        this.maxThreadsNum.set(maxThreadsNum);
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
            logic.readFile(file.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        logic.updateTreeView(componentsTree);
        updateNewExecutionTab();
    }

    private void updateNewExecutionTab() {
        try {
            logic.updateEntitiesAndEnvVars(entitiesPopulationsInputs, envVarsInputs);
        } catch (IOException e) {
            e.printStackTrace();
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
                componentDetail.set("Please select a component");

        }
    }

    @FXML
    void clearInputs(ActionEvent event) {
        GridPane grid;
        for (Node entitiesPopulationsInput : entitiesPopulationsInputs.getChildren()) {
            grid = (GridPane) entitiesPopulationsInput;
            for (Node node : grid.getChildren()) {
                if (node instanceof TextField) {
                    ((TextField) node).setText("");
                }
            }
        }
        for (Node entitiesPopulationsInput : envVarsInputs.getChildren()) {
            grid = (GridPane) entitiesPopulationsInput;
            for (Node node : grid.getChildren()) {
                if (node instanceof TextField) {
                    ((TextField) node).setText("");
                }
            }
        }
    }

    @FXML
    void startSimulation(ActionEvent event) {
        isSimulationRunning.set(true);
        resultsTab.setDisable(false);
        try {
            SimulationDC currSimulationDC = logic.startSimulation(
                    logic.retrieveEntitiesPopulationsInputs(entitiesPopulationsInputs),
                    logic.retriveEnvVarsInputs(envVarsInputs)
            );
            updateSimulationResultsTab(currSimulationDC);
        } catch (IllegalArgumentException e) {
            raiseErrorModal(e.getMessage());
        }
    }

    private void raiseErrorModal(String massege) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource("/component/errormodal/errorModal.fxml");
            loader.setLocation(url);
            Parent root = loader.load();
            ErrorModalController errorModalController = loader.getController();

            Stage errorStage = new Stage();
            errorStage.setTitle("Error!");
            errorStage.setAlwaysOnTop(true);
            errorModalController.setErrorMessage(massege);
            Scene scene = new Scene(root);
            errorStage.initModality(Modality.APPLICATION_MODAL);
            errorStage.setResizable(false);
            errorStage.setScene(scene);
            errorStage.showAndWait();
            errorStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }


    private void updateSimulationResultsTab(SimulationDC currSimulationDC) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource("/component/result/tab/resultTab.fxml");
            loader.setLocation(url);
            Node root = loader.load();
            ResultTabController resultTabController = loader.getController();
            resultTabController.getSimulationGuid().bind(selectedSimulationGUID);
            resultTabController.setLogic(logic);
            resultTabController.set(currSimulationDC.getSimulation());
            Tab tab = new Tab(currSimulationDC.getSimulation().getGuid());
            tab.setContent(root);
            simulationsTabsPane.getTabs().add(tab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
