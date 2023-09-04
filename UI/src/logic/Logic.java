package logic;

import component.entitypopulationinput.EntityPopulationInputController;
import component.envarinput.EnvVarInputController;
import component.main.MainController;
import component.result.entity.ResultByEntityController;
import component.result.histogram.ResultByHistogramController;
import facade.EngineFacade;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import simulation.SimulationManager;
import simulation.world.detail.ISimulationComponent;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.environmentvariables.EnvironmentVariable;
import simulation.world.detail.rule.Rule;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Logic {
    private MainController mainController;
    private EngineFacade engine;

    public Logic(MainController mainController) {
        this.engine = new EngineFacade();
        this.mainController = mainController;
    }

    public void readFile(String path) {
        this.engine.readFile(path);
        mainController.setMaxThreadsNum(SimulationManager.getInstance().getThreadQueue().getMaxThearsNum());
    }

    public void updateTreeView(TreeView<String> componentsTree) {
        componentsTree.setRoot(new TreeItem<>("Simulation Details"));
        TreeItem<String> root = componentsTree.getRoot();
        root.setExpanded(true);
        addEntitiesToTreeView(componentsTree);
        addEnvVarsToTreeView(componentsTree);
        addRulesToTreeView(componentsTree);
        addGridToTreeView(componentsTree);
        addTerminationConditionsToTreeView(componentsTree);
        for (int i = 0; i < 5; i++) {
            root.getChildren().get(i).setExpanded(true);
        }
    }

    private void addEntitiesToTreeView(TreeView<String> componentsTree) {
        TreeItem<String> root = componentsTree.getRoot();
        root.getChildren().add(new TreeItem<>("Entities"));
        for (Entity entity : engine.getEntities()) {
            root.getChildren().get(0).getChildren().add(new TreeItem<>(entity.getName()));
        }
    }

    private void addEnvVarsToTreeView(TreeView<String> componentsTree) {
        TreeItem<String> root = componentsTree.getRoot();
        root.getChildren().add(new TreeItem<>("Environment Variables"));
        for (EnvironmentVariable envVar : engine.getEnvironmentVariables()) {
            root.getChildren().get(1).getChildren().add(new TreeItem<>(envVar.getName()));
        }
    }

    private void addRulesToTreeView(TreeView<String> componentsTree) {
        TreeItem<String> root = componentsTree.getRoot();
        root.getChildren().add(new TreeItem<>("Rules"));
        for (Rule rule : engine.getRules()) {
            root.getChildren().get(2).getChildren().add(new TreeItem<>(rule.getName()));
        }
    }

    private void addGridToTreeView(TreeView<String> componentsTree) {
        TreeItem<String> root = componentsTree.getRoot();
        root.getChildren().add(new TreeItem<>("Grid"));
    }

    private void addTerminationConditionsToTreeView(TreeView<String> componentsTree) {
        TreeItem<String> root = componentsTree.getRoot();
        root.getChildren().add(new TreeItem<>("Termination Conditions"));
    }

    public String getComponentInfo(String componentName, String componentParent) {
        if (componentName.equals("Termination Conditions")) {
            return this.engine.getTerminationCond().getInfo();
        } else if (componentName.equals("Grid")) {
            return this.engine.getGrid().getInfo();
        } else if (componentParent.equals("Entities")) {
            for (Entity entity : this.engine.getEntities()) {
                if (entity.getName().equals(componentName)) {
                    return entity.getInfo();
                }
            }
        } else if (componentParent.equals("Environment Variables")) {
            for (EnvironmentVariable envVar : this.engine.getEnvironmentVariables()) {
                if (envVar.getName().equals(componentName)) {
                    return envVar.getInfo();
                }
            }
        } else if (componentParent.equals("Rules")) {
            List<Rule> rules = this.engine.getRules();
            for (Rule rule : rules) {
                if (rule.getName().equals(componentName)) {
                    return rule.getInfo();
                }
            }
        } else {
            List<ISimulationComponent> components = this.engine.getAllSimulationComponents();
            for (ISimulationComponent component : components) {
                if (component.getName().equals(componentName)) {
                    return component.getInfo();
                }
            }
        }
        return null;
    }

    public void startSimulation(Map<String, Integer> entitiesPopulation, Map<String, Object> envVarsVals) {
        engine.startSimulation(entitiesPopulation, envVarsVals);
    }

    public Map<String, Integer> retrieveEntitiesPopulationsInputs(FlowPane entitiesPopulationsInputs) {
        Map<String, Integer> entityPopulationsMap = new HashMap<>();

        for (Node node : entitiesPopulationsInputs.getChildren()) {
            if (node instanceof GridPane) {
                GridPane gridPane = (GridPane) node;
                Label entityNameLabel = null;
                TextField entityPopulationInput = null;

                for (Node gridChild : gridPane.getChildren()) {
                    if (gridChild instanceof Label && "entityNameLabel".equals(gridChild.getId())) {
                        entityNameLabel = (Label) gridChild;
                    } else if (gridChild instanceof TextField && "entityPopulationInput".equals(gridChild.getId())) {
                        entityPopulationInput = (TextField) gridChild;
                    }
                }

                if (entityNameLabel != null && entityPopulationInput != null) {
                    String entityName = entityNameLabel.getText().substring(0, entityNameLabel.getText().indexOf("'"));
                    Integer entityPopulation = Integer.parseInt(entityPopulationInput.getText());
                    entityPopulationsMap.put(entityName, entityPopulation);
                }
            }
        }
        return entityPopulationsMap;
    }


    public Map<String, Object> retriveEnvVarsInputs(FlowPane envVarsInputs) {
        Map<String, Object> envVarsMap = new HashMap<>();

        for (Node node : envVarsInputs.getChildren()) {
            if (node instanceof GridPane) {
                GridPane gridPane = (GridPane) node;
                Label envVarNameLabel = null;
                TextField envVarValueInput = null;

                for (Node gridChild : gridPane.getChildren()) {
                    if (gridChild instanceof Label && "envVarNameLabel".equals(gridChild.getId())) {
                        envVarNameLabel = (Label) gridChild;
                    } else if (gridChild instanceof TextField && "envVarValueInput".equals(gridChild.getId())) {
                        envVarValueInput = (TextField) gridChild;
                    }
                }

                if (envVarNameLabel != null && envVarValueInput != null) {
                    String envVarName = envVarNameLabel.getText();
                    Object envVarValue = envVarValueInput.getText();
                    envVarsMap.put(envVarName, envVarValue);
                }
            }
        }
        return envVarsMap;
    }

    public void updateEntitiesAndEnvVars(FlowPane entitiesPopulationsInputs, FlowPane envVarsInputs) throws
            IOException {
        addEntityPopulationComponents(entitiesPopulationsInputs);
        addEnvVarsComponents(envVarsInputs);
    }

    private void addEntityPopulationComponents(FlowPane entitiesPopulationsInputs) throws IOException {
        for (Entity entity : this.engine.getEntities()) {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource("/component/entitypopulationinput/entityPopulationInput.fxml");
            loader.setLocation(url);
            Node entityPopulationInputNode = loader.load();
            EntityPopulationInputController entityPopulationInputController = loader.getController();
            entityPopulationInputController.setEntityName(entity.getName());
            entitiesPopulationsInputs.getChildren().add(entityPopulationInputNode);
        }
    }

    private void addEnvVarsComponents(FlowPane envVarsInputs) throws IOException {
        for (EnvironmentVariable envVar : this.engine.getEnvironmentVariables()) {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource("/component/envarinput/envVarInput.fxml");
            loader.setLocation(url);
            Node envVarsInputNode = loader.load();
            EnvVarInputController envVarInputController = loader.getController();
            envVarInputController.setEnvVarName(envVar.getName());
            envVarInputController.setEnvVarType(envVar.getType().toString());
            //envVarInputController.setEnvVarValue(envVar.getValue().toString());
            envVarsInputs.getChildren().add(envVarsInputNode);
        }
    }

    public void setEntityResultComponent(ResultByEntityController resultByEntityController) {
        resultByEntityController.setEntitiesList();
    }

    public void setHistogramResultComponent(ResultByHistogramController resultByHistogramController) {
        resultByHistogramController.setEntitiesProperties();
    }

    public ObservableList<String> getPropertyHistogram(String simulationGuid, String entityName, String propertyName) {
        Map<Object, Integer> histogram = SimulationManager.getInstance().getSimulationResultByHistogram(
                simulationGuid, entityName, propertyName);
        ObservableList<String> res = new SimpleListProperty<>(FXCollections.observableArrayList());
        for (Map.Entry<Object, Integer> entry : histogram.entrySet()) {
            res.add(entry.getKey() + ": " + entry.getValue());
        }
        return res;
    }

    public void rerunSimulation(String guid) {
        engine.rerunSimulation(guid);
    }
}
