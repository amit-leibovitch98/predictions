package component.result.tab;

import component.result.tab.entity.ResultByEntityController;
import component.result.tab.histogram.ResultByHistogramController;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import logic.Logic;
import simulation.Simulation;

import java.io.IOException;
import java.net.URL;

public class ResultTabController {
    private final int PRIMARY_ENTITY_COL = 0;
    private final int SECONDARY_ENTITY_COL = 1;

    @FXML
    private RadioButton propertyConsistencyRB;
    @FXML
    private RadioButton proptyHistogramRB;
    @FXML
    private RadioButton entityPopulationRB;
    @FXML
    private Pane resultsPane;
    @FXML
    private TableView<StringProperty> entitiesTable;

    private Logic logic;
    private StringProperty selectedSimulationGUID;
    private StringProperty primeryEntityCount;
    private StringProperty seconderyEntityCount;

    public ResultTabController() {
        this.selectedSimulationGUID = new SimpleStringProperty();
        this.primeryEntityCount = new SimpleStringProperty();
        this.seconderyEntityCount = new SimpleStringProperty();
    }

    @FXML
    public void simulationSelected(Event event) throws IOException {
        if (logic == null) {
            return;
        }
        try {
            if (entityPopulationRB.isSelected()) {
                updateResultByEntityComponent();
            } else if (proptyHistogramRB.isSelected()) {
                updateResultByHistogramComponent();
            } else if (propertyConsistencyRB.isSelected()) {
                //implement
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void showResultByConsistency() throws IOException {
        updateResultByConsistency();
    }

    private void updateResultByConsistency() throws IOException {
        //implement
        System.out.println("consistency");
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
        resultByEntityController.getSimulationGuid().bind(selectedSimulationGUID);
        logic.setEntityResultComponent(resultByEntityController);
        resultByEntityController.setLogic(logic);
        resultsPane.getChildren().add(byEntityResult);
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
        resultByHistogramController.getSimulationGuid().bind(selectedSimulationGUID);
        logic.setHistogramResultComponent(resultByHistogramController);
        resultByHistogramController.setLogic(logic);
        resultsPane.getChildren().add(byHistogramResult);

    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    @FXML
    void rerunSimulation(ActionEvent event) {
        logic.rerunSimulation(selectedSimulationGUID.getValue());
    }

    public StringProperty getSimulationGuid() {
        return selectedSimulationGUID;
    }


    public void set(Simulation simulation) {
        String primeryEntityName = simulation.getWorld().getPrimeryEntityInstances().get(0).getName();
        String seconderyEntityName = simulation.getWorld().getSeconderyEntityInstances().get(0).getName();
        ObservableList<StringProperty> data = FXCollections.observableArrayList();
        entitiesTable.getColumns().get(PRIMARY_ENTITY_COL).setText(primeryEntityName);
        entitiesTable.getColumns().get(SECONDARY_ENTITY_COL).setText(seconderyEntityName);
        primeryEntityCount.bind(simulation.getPrimeryEntityCount().asString());
        seconderyEntityCount.bind(simulation.getSeconderyEntityCount().asString());
        data.add(primeryEntityCount);
        data.add(seconderyEntityCount);
        entitiesTable.getItems().addAll(data);
    }

    public void simulationResultsComponents() {
        try {
            //updateResultByConsistency();
            updateResultByEntityComponent();
            updateResultByHistogramComponent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void pauseSimulation(ActionEvent event) {
        System.out.println("pause");
        //implement
    }

    @FXML
    void resumeSimulation(ActionEvent event) {
        System.out.println("resume");
        //implement
    }

    @FXML
    void stopSimulation(ActionEvent event) {
        System.out.println("stop");
        //implement
    }
}
