package component.result.tab;

import component.result.ResultsUpdater;
import component.result.tab.consistency.ResultByConsistencyController;
import component.result.tab.entity.ResultByEntityController;
import component.result.tab.histogram.ResultByHistogramController;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import logic.Logic;
import simulation.Simulation;
import simulation.SimulationDC;
import simulation.SimulationExecutionManager;
import simulation.SimulationManager;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ResultTabController {

    @FXML
    private ToggleGroup showRusltBy;
    @FXML
    private RadioButton propertyConsistencyRB;
    @FXML
    private RadioButton proptyHistogramRB;
    @FXML
    private RadioButton entityPopulationRB;
    @FXML
    private Label simulationStatusLabel;
    @FXML
    private Pane resultsPane;
    @FXML
    private TableView<Pair<Integer, Integer>> entitiesTable;
    @FXML
    private TableColumn<Pair<IntegerProperty, IntegerProperty>, Integer> primeryCountCol;
    @FXML
    private TableColumn<Pair<IntegerProperty, IntegerProperty>, Integer> secenderyCountCol;
    @FXML
    private Button pauseB;
    @FXML
    private Button resumeB;
    @FXML
    private Button stopB;
    @FXML
    private Label simCurrTime;
    @FXML
    private Label simCurrTick;

    private Logic logic;
    private String selectedSimulationGUID;
    private int primeryEntityCount;
    private int seconderyEntityCount;
    private SimulationDC simulationDC;
    private ScheduledExecutorService puller;
    public ResultTabController() {

    }

    public void initialize() {
        this.entityPopulationRB.setDisable(true);
        this.proptyHistogramRB.setDisable(true);
        this.propertyConsistencyRB.setDisable(true);

        primeryCountCol.setCellValueFactory(new PropertyValueFactory<>("key")); // Use "key" for the first value
        secenderyCountCol.setCellValueFactory(new PropertyValueFactory<>("value")); // Use "value" for the second value
    }

    @FXML
    void showResultByConsistency() throws IOException {
        updateResultByConsistency();
    }

    private void updateResultByConsistency() throws IOException {
        resultsPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource("/component/result/tab/consistency/resultByConsistency.fxml");
        loader.setLocation(url);
        Node byConsistencyResult = loader.load();
        ResultByConsistencyController resultByConsistencyController = loader.getController();
        resultByConsistencyController.setSimulationGuid(selectedSimulationGUID);
        logic.setConsistencyResultComponent(resultByConsistencyController);
        resultByConsistencyController.setLogic(logic);
        resultsPane.getChildren().add(byConsistencyResult);
    }

    @FXML
    void showResultByEntity(ActionEvent event) throws IOException {
        updateResultByEntityComponent();
    }

    private void updateResultByEntityComponent() throws IOException {
        resultsPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource("/component/result/tab/entity/resultByEntity.fxml");
        loader.setLocation(url);
        Node byEntityResult = loader.load();
        ResultByEntityController resultByEntityController = loader.getController();
        resultByEntityController.setSimulationGuid(selectedSimulationGUID);
        logic.setEntityResultComponent(resultByEntityController);
        resultByEntityController.setLogic(logic);
        resultsPane.getChildren().add(byEntityResult);
        resultByEntityController.selectFirst();
    }

    @FXML
    void ShowResultByHistogram(ActionEvent event) throws IOException {
        updateResultByHistogramComponent();
    }

    private void updateResultByHistogramComponent() throws IOException {
        resultsPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource("/component/result/tab/histogram/resultByHistogram.fxml");
        loader.setLocation(url);
        Node byHistogramResult = loader.load();
        ResultByHistogramController resultByHistogramController = loader.getController();
        resultByHistogramController.setSimulationGuid(selectedSimulationGUID);
        logic.setHistogramResultComponent(resultByHistogramController);
        resultByHistogramController.setLogic(logic);
        resultsPane.getChildren().add(byHistogramResult);
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    @FXML
    void rerunSimulation(ActionEvent event) {
        SimulationDC simulationDC = logic.rerunSimulation(selectedSimulationGUID);
    }

    public void setSimulation(SimulationDC simulationDC) {
        this.simulationDC = simulationDC;
        this.selectedSimulationGUID = simulationDC.getSimulation().getGuid();

        this.simulationStatusLabel.setText(this.simulationDC.getMassege());
        primeryEntityCount = simulationDC.getPrimeryEntityCount();
        seconderyEntityCount = simulationDC.getSeconderyEntityCount();
        Pair<Integer, Integer> rowData = new Pair<>(primeryEntityCount, seconderyEntityCount);
        entitiesTable.getItems().add(rowData);

        this.simulationDC.getIsRunning().addListener((observable, oldValue, running) -> {
            if (running) {
                this.pauseB.setDisable(false);
                this.resumeB.setDisable(false);
                this.stopB.setDisable(false);
                setRadioButtonDisable(true);
            } else {
                if(this.simulationDC.getMassege().contains("Finished") || this.simulationDC.getMassege().contains("Stopped")) {
                    setRadioButtonDisable(false);
                    Platform.runLater(() -> {
                        try {
                            updateResultByEntityComponent();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
        primeryCountCol.setText(this.simulationDC.getSimulation().getWorld().getEntities().get(0).getName());
        secenderyCountCol.setText(this.simulationDC.getSimulation().getWorld().getEntities().get(1).getName());
        setSechedualPull();
    }

    private void setSechedualPull() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        // Schedule the task to run every 200ms with an initial delay of 0ms
        executor.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> update(simulationDC));
        }, 0, 200, TimeUnit.MILLISECONDS);
        this.puller = executor;
    }

    @FXML
    void pauseSimulation(ActionEvent event) {
        this.pauseB.setDisable(true);
        this.resumeB.setDisable(false);
        this.stopB.setDisable(false);
        SimulationManager.getInstance().getSimulationExecutionManager().pauseSimulation(selectedSimulationGUID);
    }

    @FXML
    void resumeSimulation(ActionEvent event) {
        this.pauseB.setDisable(false);
        this.resumeB.setDisable(true);
        this.stopB.setDisable(false);
        SimulationManager.getInstance().getSimulationExecutionManager().resumeSimulation(selectedSimulationGUID);

    }

    @FXML
    void stopSimulation(ActionEvent event) {
        SimulationManager.getInstance().getSimulationExecutionManager().stopSimulation(selectedSimulationGUID);
        this.pauseB.setDisable(true);
        this.resumeB.setDisable(true);
        this.stopB.setDisable(true);
        this.puller.shutdown();
        update(simulationDC);
    }

    private void setRadioButtonDisable(boolean disable) {
        this.entityPopulationRB.setDisable(disable);
        this.proptyHistogramRB.setDisable(disable);
        this.propertyConsistencyRB.setDisable(disable);
    }

    public void update(SimulationDC simulationDC) {
        this.simulationStatusLabel.setText(simulationDC.getMassege());
        Pair<Integer, Integer> rowData = new Pair<>(simulationDC.getPrimeryEntityCount(), simulationDC.getSeconderyEntityCount());
        simCurrTime.setText(simulationDC.getSimulation().getcurrTime().toString());
        simCurrTick.setText(simulationDC.getSimulation().getTick().getValue().toString());
        entitiesTable.getItems().clear();
        entitiesTable.getItems().add(rowData);
    }
}
