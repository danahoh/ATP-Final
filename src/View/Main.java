package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class Main extends Application {

    public Scene startScene;
    public Scene gameScene;

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("Funjoya Maze");
        primaryStage.getIcons().add(new Image("/Images/icon.png"));
        FXMLLoader startFXML = new FXMLLoader(getClass().getResource("StartStage.fxml"));
        FXMLLoader viewFXML = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = startFXML.load();
        Parent game = viewFXML.load();
        MyViewController viewController = viewFXML.getController();
        StartStageController startController = startFXML.getController();
        startScene = new Scene(root,1000,700);
        gameScene = new Scene(game,1000,700);
        startController.setPrimaryStage(primaryStage);
        startController.setScene(gameScene);
        IModel model = new MyModel();
        MyViewModel myViewModel = new MyViewModel(model);
        viewController.setViewModel(myViewModel);
        SetStageCloseEvent(primaryStage, viewController);
        viewController.setPrimaryStage(primaryStage);
        primaryStage.setScene(startScene);
        primaryStage.show();
    }
    private void SetStageCloseEvent(Stage primaryStage, MyViewController viewController) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Are you sure?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    viewController.stopServers();
                    System.exit(0);
                } else { windowEvent.consume(); }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
