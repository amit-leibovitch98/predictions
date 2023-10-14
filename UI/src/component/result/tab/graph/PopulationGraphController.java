package component.result.tab.graph;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.util.Pair;

import java.util.Map;

public class PopulationGraphController {

    @FXML
    private LineChart<Number, Number> graph;

    @FXML
    private NumberAxis populationAxis;

    @FXML
    private NumberAxis ticksAxis;

    private XYChart.Series<Number, Number> primaryPopulation; // Corrected variable name
    private XYChart.Series<Number, Number> secondaryPopulation; // Corrected variable name

    public PopulationGraphController() {
        primaryPopulation = new XYChart.Series<>();
        primaryPopulation.setName("Primary Entity Population");
        secondaryPopulation = new XYChart.Series<>();
        secondaryPopulation.setName("Secondary Entity Population");

        ticksAxis = new NumberAxis();
        ticksAxis.setLabel("Ticks");
        populationAxis = new NumberAxis();
        populationAxis.setLabel("Population");

        graph = new LineChart<>(ticksAxis, populationAxis);
    }

    public void setGraphInputs(Map<Integer, Pair<Integer, Integer>> graphInputs) { // Corrected method name
        for (Map.Entry<Integer, Pair<Integer, Integer>> entry : graphInputs.entrySet()) {
            primaryPopulation.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue().getKey()));
            secondaryPopulation.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue().getValue()));
        }
        graph.getData().add(primaryPopulation);
        graph.getData().add(secondaryPopulation);
    }
}
