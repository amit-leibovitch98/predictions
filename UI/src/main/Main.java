package main;

import component.main.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import logic.Logic;

import javax.swing.border.Border;
import java.net.URL;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage primeryStage)  throws Exception {
        FXMLLoader loader = new FXMLLoader();
        primeryStage.setTitle("Predictions");
        //Parent load = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/component/main/mainUI.fxml")));
        URL url = getClass().getResource("/component/main/mainUI.fxml");
        loader.setLocation(url);
        BorderPane root = loader.load();

        MainController mainController = loader.getController();
        Logic logic = new Logic(mainController);
        mainController.setPrimaryStage(primeryStage);
        mainController.setLogic(logic);
        Scene scene = new Scene(root, 1200, 750);
        primeryStage.setScene(scene);
        primeryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}