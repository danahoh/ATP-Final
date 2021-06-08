package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController implements Initializable, Observer {
    //public MazeGenerator generator;
    public MyViewModel viewModel;
    public TextField textField_rows;
    public TextField textField_cols;
    public BorderPane borderPane;
    public AnchorPane mainPane;
    public ScrollPane scrollPane;
    double currentZoomFactor = 1;
    @FXML
    public GraphicsContext gc;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;
    public int rows = 0;
    public int cols = 0;
    boolean MusicOn = true;


    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    String background = new File("./Resources/music/Background.mp3").toURI().toString();
    MediaPlayer backgroundPlayer = new MediaPlayer(new Media(background));

    String win = new File("./Resources/music/FeelSoClose.mp3").toURI().toString();
    MediaPlayer winningPlayer = new MediaPlayer(new Media(win));

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addObserver(this);
    }

    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }

    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }

    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }

    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        scrollPane.setContent(mainPane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        mazeDisplayer.widthProperty().bind(this.scrollPane.widthProperty());
        mazeDisplayer.heightProperty().bind(this.scrollPane.heightProperty());
//        mainPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> {
//            mazeDisplayer.setWidth(newValue.doubleValue());
//        });
//
//        mainPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> {
//            mazeDisplayer.setHeight(newValue.doubleValue());
//        });
        //mainPane.widthProperty().bind(this.scrollPane.widthProperty());
        //mainPane.heightProperty().bind(this.scrollPane.heightProperty());
//        mainPane.widthProperty().addListener((obs, oldVal, newValue) -> mazeDisplayer.setWidth(newValue.doubleValue()));
//        mainPane.heightProperty().addListener((obs, oldVal, newValue) -> mazeDisplayer.setHeight(newValue.doubleValue()));
        backgroundPlayer.setOnEndOfMedia(() -> backgroundPlayer.seek(Duration.ZERO));
        backgroundPlayer.play();

        FileInputStream input = null;
        try {
            input = new FileInputStream("./Resources/Images/pool.png");
        } catch (FileNotFoundException e) {
            System.out.println("There is no back image file");
        }
        Image image = new Image(input);
        BackgroundImage backgroundimage = new BackgroundImage(image,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundimage);
        mainPane.setBackground(background);

    }
    private void playBackgroundMusic() {

        viewModel.setGameOver(false);
        winningPlayer.stop();
        backgroundPlayer.play();
    }

    private void playWinningMusic() {
        winningPlayer.play();
        backgroundPlayer.stop();
    }

//    public void toggleMusic() {
//        MusicOn = !MusicOn;
//        if (MusicOn) {
//            backgroundPlayer.setMute(false);
//            winningPlayer.setMute(false);
//        } else {
//            backgroundPlayer.setMute(true);
//            winningPlayer.setMute(true);
//        }
//    }
    public void generateMaze(ActionEvent actionEvent) {


        rows = Integer.valueOf(textField_rows.getText());
        cols = Integer.valueOf(textField_cols.getText());
        viewModel.generateMaze(rows,cols);
        playBackgroundMusic();

//        if(viewModel == null)
//        {
//            viewModel = new MyViewModel();
//        }
//        if (mazeDisplayer == null)
//        {
//            mazeDisplayer = new MazeDisplayer();
//        }
//        Maze maze = viewModel.generateMaze(rows,cols);
//        mazeDisplayer.drawMaze(maze);
//        setPlayerPosition(maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex());
    }

    public void solveMaze(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Solving maze...");
        alert.show();
        viewModel.solveMaze();
    }

    public void openFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (.maze)", ".maze"));
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showOpenDialog(null);
        //...
    }

    public void keyPressed(KeyEvent keyEvent) {
        viewModel.movePlayer(keyEvent);
        keyEvent.consume();
    }

    public void setPlayerPosition(int row, int col){
        mazeDisplayer.setPlayerPosition(row,col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);

    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    public void zoomIn(ScrollEvent scrollEvent) {
        if (scrollEvent.isControlDown()) {
            double deltaY = scrollEvent.getDeltaY();
            if (deltaY > 0)
                currentZoomFactor += 0.1;
            else if (deltaY < 0)
                currentZoomFactor -= 0.1;
            currentZoomFactor = Math.max(currentZoomFactor, 1);
            currentZoomFactor = Math.min(currentZoomFactor, 5);

            mainPane.setScaleX(currentZoomFactor);
            mainPane.setScaleY(currentZoomFactor);
            if (currentZoomFactor == 1) {
                this.scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                this.scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            }
            else {
                this.scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                this.scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            }
            Group contentGroup = new Group();
            Group zoomGroup = new Group();
            contentGroup.getChildren().add(zoomGroup);
            zoomGroup.getChildren().add(mainPane);
            scrollPane.setContent(contentGroup);
            Scale scaleTransform = new Scale(currentZoomFactor, currentZoomFactor, 0, 0);
            zoomGroup.getTransforms().add(scaleTransform);
        }
        scrollEvent.consume();
    }


    @Override
    public void update(Observable o, Object arg) {
        String change = (String) arg;
        switch (change){
            case "maze generated" -> mazeGenerated();
            case "player moved" -> playerMoved();
            case "maze solved" -> mazeSolved();
            default -> System.out.println("Not implemented change: " + change);
        }
        if (viewModel.gameOver()) {
            playWinningMusic();
        }
    }

    private void playerMoved() {
        setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
    }

    private void mazeSolved()
    {
        mazeDisplayer.setSolution(viewModel.getSolution());
    }

    private void mazeGenerated()
    {
        mazeDisplayer.drawMaze(viewModel.getMaze());
        //setPlayerPosition(viewModel.getMaze().getStartPosition().getRowIndex(), viewModel.getMaze().getStartPosition().getColumnIndex());
    }

    public void stopServers() {

        viewModel.stopServers();
    }
}