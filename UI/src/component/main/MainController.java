package component.main;

import component.errormodal.ErrorModalController;
import component.result.tab.ResultTabController;
import javafx.beans.property.*;
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
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Logic;
import simulation.SimulationDC;

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
    private ListView<String> queueList;
    @FXML
    private Button rerunB;

    @FXML
    private Tab resultsTab;
    @FXML
    private ToggleGroup showRusltBy;
    @FXML
    private Label simulationDetailsLabel;
    @FXML
    private TabPane simulationsTabsPane;
    @FXML
    private Button startB;
    @FXML
    private Label threadsNumLabel;
    @FXML
    private Button uploadFileB;
    @FXML
    private Pane paneComponentDetail;

    private StringProperty selectedSimulationGUID;
    private StringProperty path;
    private StringProperty componentDetail;
    private BooleanProperty isFileUploaded;
    private Logic logic;
    private Stage primaryStage;
    private IntegerProperty maxThreadsNum;

    public MainController() {
        this.path = new SimpleStringProperty();
        this.isFileUploaded = new SimpleBooleanProperty();
        this.componentDetail = new SimpleStringProperty();
        this.componentDetailLabel = new Label();
        this.queueList = new ListView<>();
        this.selectedSimulationGUID = new SimpleStringProperty();
        this.maxThreadsNum = new SimpleIntegerProperty();
    }

    public void initialize() {
        this.pathLabel.textProperty().bind(path);
        this.componentDetailLabel.textProperty().bind(componentDetail);
        this.newExecTab.disableProperty().bind(isFileUploaded.not());
        this.resultsTab.setDisable(true);
        this.threadsNumLabel.textProperty().bind(this.maxThreadsNum.asString());
        this.simulationsTabsPane.getTabs().clear();
        //TODO: implement start/pause/resume button and bind their disable property to worldDef 's isInteractive
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
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
            raiseErrorModal(e.getMessage());
        }
        logic.updateTreeView(componentsTree);
        updateNewExecutionTab();
    }

    private void reset(){
        resultsTab.getTabPane().getTabs().clear();
        resultsTab.setDisable(true);
        logic.reset();
    }

    private void updateNewExecutionTab() {
        try {
            logic.updateEntitiesAndEnvVars(entitiesPopulationsInputs, envVarsInputs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Pane getPaneComponentDetail() {
        return paneComponentDetail;
    }

    @FXML
    void componentTreeItemRequested(MouseEvent event) {
        TreeItem<String> item = componentsTree.getSelectionModel().getSelectedItem();
        if (item != null) {
            if (item.getParent().getValue().equals("Rules")) {
                paneComponentDetail.getChildren().clear();
                logic.getComponentInfo(item.getValue(), item.getParent().getValue());
            } else {
                paneComponentDetail.getChildren().clear();
                paneComponentDetail.getChildren().add(componentDetailLabel);
                componentDetailLabel.setAlignment(javafx.geometry.Pos.CENTER);
                String str = logic.getComponentInfo(item.getValue(), item.getParent().getValue());
                if (str != null) {
                    componentDetail.set(str);
                } else {
                    componentDetail.set("Please select a component");
                }
            }
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
        resultsTab.setDisable(false);
        try {
            SimulationDC currSimulationDC = logic.startSimulation(
                    this.queueList,
                    logic.retrieveEntitiesPopulationsInputs(entitiesPopulationsInputs),
                    logic.retriveEnvVarsInputs(envVarsInputs)
            );
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


    public void updateSimulationResultsTab(SimulationDC currSimulationDC) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource("/component/result/tab/resultTab.fxml");
            loader.setLocation(url);
            Node root = loader.load();
            ResultTabController resultTabController = loader.getController();
            resultTabController.setLogic(logic);
            resultTabController.setSimulation(currSimulationDC);
            Tab tab = new Tab(currSimulationDC.getSimulation().getGuid());
            tab.setContent(root);
            simulationsTabsPane.getTabs().add(tab);
            resultsTab.getTabPane().getSelectionModel().select(resultsTab);
            simulationsTabsPane.getSelectionModel().select(tab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
