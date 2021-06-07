//package View;
//
//import ViewModel.MyViewModel;
//import algorithms.mazeGenerators.AMazeGenerator;
//import algorithms.mazeGenerators.IMazeGenerator;
//import algorithms.mazeGenerators.Maze;
//import javafx.event.ActionEvent;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Alert;
//import javafx.scene.control.TextField;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.input.MouseEvent;
//import javafx.stage.FileChooser;
//
//import java.io.File;
//import java.net.URL;
//import java.util.ResourceBundle;
//
//public class MyViewController implements IView, Initializable {
//
//    private MyViewModel viewModel;
//    public TextField textField_rows;
//    public TextField textField_cols;
//    public MazeDisplayer mazeDisplayer;
//
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//
//    }
//
//    public void mouseClicked(MouseEvent mouseEvent) {
//        mazeDisplayer.requestFocus();
//    }
//
//    public void solveMaze(ActionEvent actionEvent) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setContentText("Solving maze...");
//        alert.show();
//    }
//
//    public void generateMaze(ActionEvent actionEvent) {
////        if(viewModel == null)
////        {
////            viewModel = new MyViewModel();
////        }
//        int rows = Integer.valueOf(textField_rows.getText());
//        int cols = Integer.valueOf(textField_cols.getText());
//        Maze maze = viewModel.generateMaze(rows,cols);
////        if (mazeDisplayer == null)
////        {
////            mazeDisplayer = new MazeDisplayer();
////        }
//        mazeDisplayer.drawMaze(maze);
//    }
//    public void keyPressed(KeyEvent keyEvent) {
//        int row = mazeDisplayer.getPlayerRow();
//        int col = mazeDisplayer.getPlayerCol();
//
//        switch (keyEvent.getCode()) {
//            case UP -> row -= 1;
//            case DOWN -> row += 1;
//            case RIGHT -> col += 1;
//            case LEFT -> col -= 1;
//        }
//        //setPlayerPosition(row, col);
//
//        keyEvent.consume();
//    }
//    public void openFile(ActionEvent actionEvent) {
//        FileChooser fc = new FileChooser();
//        fc.setTitle("Open maze");
//        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
//        fc.setInitialDirectory(new File("./resources"));
//        File chosen = fc.showOpenDialog(null);
//        //...
//    }
//}

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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
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
    public Pane mainPane;
    public ScrollPane scrollPane;
    double currentZoomFactor = 1;
    @FXML
    public GraphicsContext gc;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;
    public int rows = 0;
    public int cols = 0;

    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

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

        //scrollPane = new ScrollPane(new Group(mainPane));

        mainPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> {
            mazeDisplayer.setWidth(newValue.doubleValue());
        });

        mainPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> {
            mazeDisplayer.setHeight(newValue.doubleValue());
        });


    }
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
////        playerRow.textProperty().bind(updatePlayerRow);
////        playerCol.textProperty().bind(updatePlayerCol);
//    }

    public void generateMaze(ActionEvent actionEvent) {


        rows = Integer.valueOf(textField_rows.getText());
        cols = Integer.valueOf(textField_cols.getText());
        viewModel.generateMaze(rows,cols);
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
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showOpenDialog(null);
        //...
    }

    public void keyPressed(KeyEvent keyEvent) {
//        int row = mazeDisplayer.getPlayerRow();
//        int col = mazeDisplayer.getPlayerCol();
//        int[][] map = mazeDisplayer.maze.getMaze();
//        switch (keyEvent.getCode()) {
//            case UP:
//            case DIGIT8:
//                if (row != 0) {
//                    row -= 1;
//                }
//                break;
//
//            case DOWN:
//            case DIGIT2:
//                if (row + 1 < rows) {
//                    row += 1;
//                }
//                break;
//
//            case RIGHT:
//            case DIGIT6:
//                if (col + 1 < cols) {
//                    col += 1;
//                }
//                break;
//
//            case LEFT:
//            case DIGIT4:
//                if (col != 0) {
//                    col -= 1;
//                }
//                break;
//
//            case DIGIT9:
//                if (row - 1 >= 0 && col + 1 < cols && map[row - 1][col + 1] == 0 && (map[row - 1][col] == 0 || map[row][col + 1] == 0)) {
//                    row -= 1;
//                    col += 1;
//                }
//                break;
//            case DIGIT3:
//                if (row + 1 < rows && col + 1 < cols && map[row + 1][col + 1] == 0 && (map[row][col + 1] == 0 || map[row + 1][col] == 0)) {
//                    row += 1;
//                    col += 1;
//                }
//                break;
//            case DIGIT7:
//                if (row - 1 >= 0 && col - 1 >= 0 && map[row - 1][col - 1] == 0 && (map[row - 1][col] == 0 || map[row][col - 1] == 0)) {
//                    row -= 1;
//                    col -= 1;
//                }
//                break;
//            case DIGIT1:
//                if (row + 1 < rows && col - 1 >= 0 && map[row + 1][col - 1] == 0 && (map[row + 1][col] == 0 || map[row][col - 1] == 0)) {
//                    row += 1;
//                    col -= 1;
//                }
//                break;
//        }
//        setPlayerPosition(row, col);
        viewModel.movePlayer(keyEvent);
        keyEvent.consume();
    }

    public void setPlayerPosition(int row, int col){
//        rows = Integer.parseInt(textField_rows.getText());
//        cols = Integer.parseInt(textField_cols.getText());
//        if ( mazeDisplayer.maze.getMaze()[row][col] != 1)
//        {
//            mazeDisplayer.setPlayerPosition(row, col);
//            setUpdatePlayerRow(row);
//            setUpdatePlayerCol(col);
//        }
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

//            if (currentZoomFactor == 1) {
//                this.scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//                this.scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//            }
//            else {
//                this.scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
//                this.scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
//            }

            mainPane.setScaleX(currentZoomFactor);
            mainPane.setScaleY(currentZoomFactor);
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
        setPlayerPosition(viewModel.getMaze().getStartPosition().getRowIndex(), viewModel.getMaze().getStartPosition().getColumnIndex());
    }
}

