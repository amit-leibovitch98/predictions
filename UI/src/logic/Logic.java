package logic;

import component.entitypopulationinput.EntityPopulationInputController;
import component.envarinput.EnvVarInputController;
import component.main.MainController;
import facade.EngineFacade;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import simulation.Simulation;
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

public class Logic {
    private MainController mainController;
    private EngineFacade engine;

    public Logic(MainController mainController) {
        this.engine = new EngineFacade();
        this.mainController = mainController;
    }

    public void updateTreeView(TreeView<String> componentsTree) {
        engine.showSimulationDetails();
        componentsTree.setRoot(new TreeItem<>("Simulation Details"));
        TreeItem<String> root = componentsTree.getRoot();
        root.setExpanded(true);
        addEntitiesToTreeView(componentsTree);
        addEnvVarsToTreeView(componentsTree);
        addRulesToTreeView(componentsTree);
        addGridToTreeView(componentsTree);
        addTerminationConditionsToTreeView(componentsTree);
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
        //TODO: add grid to system whatever that means
    }

    private void addTerminationConditionsToTreeView(TreeView<String> componentsTree) {
        TreeItem<String> root = componentsTree.getRoot();
        root.getChildren().add(new TreeItem<>("Termination Conditions"));
        if (engine.getTerminationCond().getByTicks() != null)
            root.getChildren().get(4).getChildren().add(new TreeItem<>("Ticks"));
        if (engine.getTerminationCond().getByTime() != null)
            root.getChildren().get(4).getChildren().add(new TreeItem<>("Time"));
        //TODO: add user interaction if relevant
    }

    public String getComponentInfo(String componentName, String componentParent) {
        if (componentParent.equals("Termination Conditions")) {
            String str = this.engine.getTerminationCond().getInfo();
            if (componentName.equals("Ticks")) {
                return str.substring(0, str.indexOf("\n"));
            } else if (componentName.equals("Time")) {
                return str.substring(str.indexOf("\n") + 1);
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
            entityPopulationInputController.setEntityPopulation(entity.getPopulation());
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
            envVarInputController.setEnvVarValue(envVar.getValue().toString());
            envVarsInputs.getChildren().add(envVarsInputNode);
        }
    }

    public void readFile(String path) {
        this.engine.readFile(path);
    }
}
