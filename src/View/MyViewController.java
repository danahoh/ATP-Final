package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

public class MyViewController implements Initializable, Observer {
    //public MazeGenerator generator;
    public MyViewModel viewModel;
    public TextField textField_rows;
    public TextField textField_cols;
    public BorderPane borderPane;
    public AnchorPane mainPane;
    public ScrollPane scrollPane;
    public ToggleButton toggleMusic;
    double currentZoomFactor = 1;
    public Button solutionButton;
    @FXML
    public GraphicsContext gc;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;
    public int rows = 0;
    public int cols = 0;
    boolean MusicOn = true;
    boolean showSolution = false;
    boolean moveWithMouse = false;




    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    String background = new File("./src/Resources/music/Background.mp3").toURI().toString();
    MediaPlayer backgroundPlayer = new MediaPlayer(new Media(background));

    String win = new File("./src/Resources/music/FeelSoClose.mp3").toURI().toString();
    MediaPlayer winningPlayer = new MediaPlayer(new Media(win));

    String video = new File("./src/Resources/music/funjoya.mp4").toURI().toString();
    MediaPlayer videoPlayer = new MediaPlayer(new Media(video));
    MediaView mediaView = new MediaView(videoPlayer);

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

//
//        FileInputStream input = null;
//        try {
//            input = new FileInputStream("./src/Resources/Images/pool.png");
//        } catch (FileNotFoundException e) {
//            System.out.println("There is no back image file");
//        }
//        Image image = new Image(input);
//        BackgroundImage backgroundimage = new BackgroundImage(image,
//                BackgroundRepeat.REPEAT,
//                BackgroundRepeat.REPEAT,
//                BackgroundPosition.CENTER,
//                BackgroundSize.DEFAULT);
//        Background background = new Background(backgroundimage);
//        mainPane.setBackground(background);

    }
    private void playBackgroundMusic() {

        viewModel.setGameOver(false);
        winningPlayer.stop();
        backgroundPlayer.play();
    }

    private void playWinningMusic() {
        backgroundPlayer.stop();
        winningPlayer.play();

    }

    public void toggleMusic() {
        MusicOn = !MusicOn;
        if (MusicOn) {
            backgroundPlayer.setMute(false);
            winningPlayer.setMute(false);
        } else {
            backgroundPlayer.setMute(true);
            winningPlayer.setMute(true);
        }
    }
    public void generateMaze(ActionEvent actionEvent) {

        solutionButton.setText("Show Solution");
        showSolution = false;
        try {
            rows = Integer.valueOf(textField_rows.getText());
            cols = Integer.valueOf(textField_cols.getText());
            if (rows <= 1 || cols <= 1 || rows > 1000 || cols > 1000) {
                Alert alertGenerate = new Alert(Alert.AlertType.WARNING);
                alertGenerate.setContentText("Invalid Input");
                alertGenerate.show();
            } else {
                viewModel.generateMaze(rows, cols);
            }
        }
        catch (Exception e)
        {
            Alert alertGenerate = new Alert(Alert.AlertType.WARNING);
            alertGenerate.setContentText("Invalid Input");
            alertGenerate.show();
        }
        backgroundPlayer.setOnEndOfMedia(() -> backgroundPlayer.seek(Duration.ZERO));
        backgroundPlayer.play();
        playBackgroundMusic();

    }

    public void solveMaze(ActionEvent actionEvent) {

        if(!showSolution)
        {
            try {
                viewModel.solveMaze();
                solutionButton.setText("Hide Solution");
                showSolution = true;
                viewModel.setShowSolution(true);

            }
            catch (Exception e)
            {
                Alert alertSolve = new Alert(Alert.AlertType.WARNING);
                alertSolve.setContentText("You need to generate a maze first");
                alertSolve.show();
            }
        }
        else {
            this.viewModel.removeSolution();
            solutionButton.setText("Show Solution");
            showSolution = false;
            viewModel.setShowSolution(false);

        }
    }

    public void keyPressed(KeyEvent keyEvent) {
        viewModel.movePlayer(keyEvent.getCode());
        keyEvent.consume();
    }

    public void setPlayerPosition(int row, int col){
        mazeDisplayer.setPlayerPosition(row,col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);

    }

    public void mouseClicked(MouseEvent mouseEvent)
    {
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
            case "maze loaded" -> mazeLoaded();
            case "hide solution" -> hideSolution();
            default -> System.out.println("Not implemented change: " + change);
        }
        if (viewModel.gameOver()) {
            try {
                playWinningMusic();
                openVideo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openVideo() throws IOException {

       // videoPlayer.setAutoPlay(true);
        videoPlayer.play();
        videoPlayer.setMute(true);
        videoPlayer.setOnEndOfMedia(() -> videoPlayer.seek(Duration.ZERO));
        Group root = new Group();
        root.getChildren().add(mediaView);
        Scene scene = new Scene(root, 1000, 700);
        Stage stage = new Stage();
        stage.setTitle("You Won!!!");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
        SetEndStageCloseEvent(stage);

    }
    private void SetEndStageCloseEvent(Stage stage) {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("You made it! Do you want to start again?");

                ButtonType newGame = new ButtonType("New Game");
                ButtonType closeGame = new ButtonType("Close Game");
                alert.getButtonTypes().setAll(newGame, closeGame);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == newGame)
                {
                    videoPlayer.pause();
                    viewModel.setGameOver(false);
                    generateMaze(new ActionEvent());
                    stage.close();

                }
                else if (result.get() == closeGame) {
                    exitGame();
                }

            }
        });
    }
    public void mouseDragged(MouseEvent mouseEvent) {
        if(viewModel.getMaze() != null) {
            int maximumSize = Math.max(viewModel.getMaze().getMaze()[0].length, viewModel.getMaze().getMaze().length);
            double mousePosX = helperMouseDragged(maximumSize,mazeDisplayer.getHeight(),
                    viewModel.getMaze().getMaze().length ,mouseEvent.getX(),mazeDisplayer.getWidth() / maximumSize);
            double mousePosY=helperMouseDragged(maximumSize,mazeDisplayer.getWidth(),
                    viewModel.getMaze().getMaze()[0].length,mouseEvent.getY(),mazeDisplayer.getHeight() / maximumSize);
            if ( mousePosX == viewModel.getPlayerCol() && mousePosY < viewModel.getPlayerRow() )
                viewModel.movePlayer(KeyCode.DIGIT8);
            else if (mousePosY == viewModel.getPlayerRow() && mousePosX > viewModel.getPlayerCol() )
                viewModel.movePlayer(KeyCode.DIGIT6);
            else if ( mousePosY == viewModel.getPlayerRow() && mousePosX < viewModel.getPlayerCol() )
                viewModel.movePlayer(KeyCode.DIGIT4);
            else if (mousePosX == viewModel.getPlayerCol() && mousePosY > viewModel.getPlayerRow()  )
                viewModel.movePlayer(KeyCode.DIGIT2);

        }
    }
    private  double helperMouseDragged(int maxsize, double canvasSize, int mazeSize,double mouseEvent,double temp){
        double cellSize=canvasSize/maxsize;
        double start = (canvasSize / 2 - (cellSize * mazeSize / 2)) / cellSize;
        double mouse = (int) ((mouseEvent) / (temp) - start);
        return mouse;
    }


    private void hideSolution()
    {
        viewModel.setShowSolution(false);
        mazeDisplayer.setShowSolution(false);
    }

    private void mazeLoaded() {

        mazeDisplayer.drawMaze(viewModel.getMaze());
    }

    private void playerMoved() {

        setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
        mazeDisplayer.setSolution(viewModel.getSolution());

    }

    private void mazeSolved()
    {
        mazeDisplayer.setSolution(viewModel.getSolution());
    }

    private void mazeGenerated()
    {
        mazeDisplayer.drawMaze(viewModel.getMaze());
    }
    public void exitGame()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            stopServers();
            System.exit(0);
        }
    }

    public void stopServers() {

        viewModel.stopServers();
    }

    public void saveFile(ActionEvent actionEvent) {
        if (viewModel.saveFile())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Maze Saved");
            alert.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Maze saving failed");
            alert.show();
        }
    }

    public void loadFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./savedMazes"));
        File chosen = fc.showOpenDialog(null);
        if (viewModel.loadFile(chosen.getName()))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Maze loaded");
            alert.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Maze loading failed");
            alert.show();
        }
    }

    public void newFile(ActionEvent actionEvent) {
        generateMaze(actionEvent);
    }

    public void openProperties() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Properties.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent, 300, 200);
        Stage stage = new Stage();
        stage.setTitle("Manage properties");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }
    public void openAbout() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("About.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent, 600, 400);
        Stage stage = new Stage();
        stage.setTitle("About Us");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
    }

    public void openHelp() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Help.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent, 600, 400);
        Stage stage = new Stage();
        stage.setTitle("Help");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
    }
}