package component.rule;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import simulation.world.detail.rule.Rule;
import simulation.world.detail.rule.action.Action;
import simulation.world.detail.rule.action.Proximity;

public class RuleController {
    @FXML
    private ListView<String> actionsList;
    @FXML
    private Label actionsNumLabel;
    @FXML
    private Label activationProbLabel;
    @FXML
    private Label activationProbTitleLabel;
    @FXML
    private Label activationTicksLabel;
    @FXML
    private Label activationTicksTitleLabel;
    @FXML
    private Label ruleNameLabel;

    public void RuleController() {
        System.out.println("Rulecontroller");
    }

    public void setRule(Rule rule) {
        this.ruleNameLabel.setText(rule.getName());
        this.activationTicksLabel.setText(rule.getActivation().getTicks().toString());
        this.activationProbLabel.setText(rule.getActivation().getProbability().toString());
        this.actionsNumLabel.setText(String.valueOf(rule.getActions().size()));
        this.actionsList.getItems().clear();
        for (Action action : rule.getActions()) {
            this.actionsList.getItems().add(action.getType());
        }
    }


}
