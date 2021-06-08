package View;

import Model.IModel;
import Model.MovementDirection;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class MainView extends Application {

    public Scene startScene;
    public Scene newGameScene;
    public Scene playScene;

    @Override
    public void start(Stage primaryStage) throws Exception{
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MazeStage.fxml"));
//        //Pane root = FXMLLoader.load(getClass().getResource("NewFXML.fxml"));
//        Parent root = fxmlLoader.load();
//        primaryStage.setTitle("Hello World");
//        //primaryStage.setScene(new Scene(root,1000,700));
//        Scene scene = new Scene(root,1000,700);
//
//        IModel model = new MyModel();
//        MyViewModel myViewModel = new MyViewModel(model);
//        MyViewController view = fxmlLoader.getController();
//        view.setViewModel(myViewModel);
//
////       root.prefWidthProperty().bind(scene.widthProperty());
////       root.prefHeightProperty().bind(scene.heightProperty());
//       primaryStage.setScene(scene);
//       primaryStage.show();
        primaryStage.setTitle("Funjoya");
        FXMLLoader menuFXML = new FXMLLoader(getClass().getResource("StartStage.fxml"));
        FXMLLoader viewFXML = new FXMLLoader(getClass().getResource("MazeStage.fxml"));
        //----------------------//
        /* set the Scene's layout */
        Parent root = menuFXML.load();
        Parent play = viewFXML.load();
        //----------------------//
        MyViewController viewController = viewFXML.getController();
        StartStageController startController = menuFXML.getController();
        //----------------------//
        startScene = new Scene(root,1000,700);
        playScene = new Scene(play,1000,700);
        //----------------------//
        startController.setPrimaryStage(primaryStage);
        startController.setScene(playScene);
        //----------------------//
        IModel model = new MyModel();
        MyViewModel myViewModel = new MyViewModel(model);
        viewController.setViewModel(myViewModel);
        SetStageCloseEvent(primaryStage, viewController);
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
