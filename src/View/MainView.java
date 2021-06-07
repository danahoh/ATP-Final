package View;

import Model.IModel;
import Model.MovementDirection;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainView extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewFXML.fxml"));
        //Pane root = FXMLLoader.load(getClass().getResource("NewFXML.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root,1000,700));
        //Scene scene = new Scene(root,1000,700);
        primaryStage.show();

        IModel model = new MyModel();
        MyViewModel myViewModel = new MyViewModel(model);
        MyViewController view = fxmlLoader.getController();
        view.setViewModel(myViewModel);

//        root.prefWidthProperty().bind(scene.widthProperty());
//        root.prefHeightProperty().bind(scene.heightProperty());
//
//
//
//        primaryStage.setScene(scene);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
