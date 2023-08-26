package logic;

import component.main.MainController;
import facade.EngineFacade;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import simulation.world.detail.ISimulationComponent;
import simulation.world.detail.entity.Entity;
import simulation.world.detail.environmentvariables.EnvironmentVariable;
import simulation.world.detail.rule.Rule;

import java.util.List;

public class Logic {
    private MainController mainController;
    private EngineFacade engine;

    public Logic(MainController mainController) {
        this.engine = new EngineFacade();
        this.mainController = mainController;
    }

    public EngineFacade getEngine() {
        return engine;
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
}
