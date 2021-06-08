package View;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class StartStageController {
    public Scene scene;
    public Button btn_start;
    public Button btn_exit;
    private MazeDisplayer mazeDisplayer;
    public Stage primaryStage;
    @FXML
    private Pane startPane;


    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public void setScene(Scene scene) {
        this.scene = scene;

        FileInputStream input = null;
        try {
            input = new FileInputStream("Resources/Images/LOGO.png");
        } catch (FileNotFoundException e) {
            System.out.println("There is no start image file");
        }
        Image image = new Image(input);
        BackgroundImage backgroundimage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundimage);
        startPane.setBackground(background);

    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        //MazeDisplayer.audioChooser(0);
    }
    public void setMazeDisplayer(MazeDisplayer mazeDisplayer) {
        this.mazeDisplayer = mazeDisplayer;
    }

    public void startClicked() throws Exception {
        primaryStage.setScene(scene);
    }



}
