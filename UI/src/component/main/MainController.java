package component.main;

import component.result.entity.ResultByEntityController;
import component.result.histogram.ResultByHistogramController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.Logic;
import simulation.SimulationManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class MainController {
    @FXML
    private Label componentDetailLabel;
    @FXML
    private TreeView<String> componentsTree;
    @FXML
    private FlowPane entitiesPopulationsInputs;
    @FXML
    private Tab newExecTab;
    @FXML
    private Label pathLabel;
    @FXML
    private Label progressPrecantegeLabel;
    @FXML
    private ListView<String> simulationGuidsList;
    @FXML
    private ListView<String> queueList;
    @FXML
    private Tab resultsTab;
    @FXML
    private ProgressBar simulationProccessBar;
    @FXML
    private FlowPane envVarsInputs;
    @FXML
    private Pane resultsPane;
    @FXML
    private StringProperty selectedSimulationGUID;
    private StringProperty path;
    private StringProperty componentDetail;
    private BooleanProperty isFileUploaded;
    private Logic logic;
    private Stage primaryStage;
    private StringProperty progressPrecantege;

    public MainController() {
        this.path = new SimpleStringProperty();
        this.isFileUploaded = new SimpleBooleanProperty();
        this.componentDetail = new SimpleStringProperty();
        this.componentDetailLabel = new Label();
        this.progressPrecantege = new SimpleStringProperty();
        this.queueList = new ListView<>();
        this.selectedSimulationGUID = new SimpleStringProperty();
    }

    public void initialize() {
        this.pathLabel.textProperty().bind(path);
        this.componentDetailLabel.textProperty().bind(componentDetail);
        this.newExecTab.disableProperty().bind(isFileUploaded.not());
        this.resultsTab.disableProperty().bind(SimulationManager.getInstance().getSimulations().emptyProperty());
        SimulationManager.getInstance().getSimulations().emptyProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != oldValue) {
                updateSimulationResults();
            }
        });
        //        this.selectedSimulationGUID.bind(simulationGuidsList.getSelectionModel().selectedItemProperty());
        simulationGuidsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!Objects.equals(newValue, oldValue)) {
                selectedSimulationGUID.set(newValue);
                try {
                    updateResultByEntityComponent();
                    updateResultByHistogramComponent();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        });

    }


    public void bindTaskToProgressBar(Task<Boolean> aTask) {
        simulationProccessBar.progressProperty().bind(aTask.progressProperty());
        progressPrecantegeLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        aTask.progressProperty(),
                                        100)),
                        " %"));
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
                componentDetail.set("No details available");
        }
    }

    @FXML
    void clearInputs(ActionEvent event) {
        //TODO: implement
    }

    @FXML
    void startSimulation(ActionEvent event) {
        logic.startSimulation(
                logic.retrieveEntitiesPopulationsInputs(entitiesPopulationsInputs),
                logic.retriveEnvVarsInputs(envVarsInputs)
        );
    }

    private void updateSimulationResults() {
        simulationGuidsList.setItems(SimulationManager.getInstance().getSimulationGuids());
    }

    @FXML
    void showResultByEntity(ActionEvent event) throws IOException {
        updateResultByEntityComponent();
    }

    private void updateResultByEntityComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource("/component/result/entity/resultByEntity.fxml");
        loader.setLocation(url);
        Node byEntityResult = loader.load();
        ResultByEntityController resultByEntityController = loader.getController();
        resultByEntityController.getSimulationGuid().bind(selectedSimulationGUID);
        logic.setEntityResultComponent(byEntityResult, resultByEntityController);
        resultsPane.getChildren().add(byEntityResult);
    }

    @FXML
    void ShowResultByHistogram(ActionEvent event) throws IOException {
        updateResultByHistogramComponent();
    }

    private void updateResultByHistogramComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource("/component/result/histogram/resultByHistogram.fxml");
        loader.setLocation(url);
        Node byHistogramResult = loader.load();
        ResultByHistogramController resultByHistogramController = loader.getController();
        resultByHistogramController.getSimulationGuid().bind(selectedSimulationGUID);
        logic.setHistogramResultComponent(byHistogramResult, resultByHistogramController);
        resultsPane.getChildren().add(byHistogramResult);

    }


}
