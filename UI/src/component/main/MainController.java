package component.main;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.Logic;

import java.io.File;

public class MainController {
    @FXML
    private Label componentDetail;
    @FXML
    private GridPane componentDetailsLabel;
    @FXML
    private TreeView<String> componentsTree;
    @FXML
    private Label pathLabel;
    @FXML
    private ListView<String> queueList;
    @FXML
    private Button uploadFileB;


    private StringProperty path;
    private Logic logic;
    private Stage primaryStage;

    public MainController() {
        System.out.println("MainController created!");
    }

    private void initialize() {
        System.out.println("MainController initialized!");
        pathLabel.textProperty().bind(path);
        updateTreeView();
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
        try {
            logic.getEngine().readFile(file.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTreeView() {
        componentsTree.setRoot(new TreeItem<>("Entities"));
        componentsTree.setRoot(new TreeItem<>("Environment Variables"));
        componentsTree.setRoot(new TreeItem<>("Actions"));
        componentsTree.getExpandedItemCount();
    }


}
