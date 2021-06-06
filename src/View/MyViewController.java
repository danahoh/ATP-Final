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
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MyViewController implements Initializable {
   //public MazeGenerator generator;
    private MyViewModel viewModel;
    public TextField textField_rows;
    public TextField textField_cols;
    public Pane mainPane;
    @FXML
    public GraphicsContext gc;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;
    public int rows = 0;
    public int cols = 0;

    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

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

        if(viewModel == null)
        {
            viewModel = new MyViewModel();
        }
        if (mazeDisplayer == null)
        {
            mazeDisplayer = new MazeDisplayer();
        }
        Maze maze = viewModel.generateMaze(rows,cols);
        mazeDisplayer.drawMaze(maze);
        setPlayerPosition(maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex());
    }

    public void solveMaze(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Solving maze...");
        alert.show();
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
        int row = mazeDisplayer.getPlayerRow();
        int col = mazeDisplayer.getPlayerCol();
        int[][] map = mazeDisplayer.maze.getMaze();
        switch (keyEvent.getCode()) {
            case UP:
            case DIGIT8:
                if (row != 0) {
                    row -= 1;
                }
                break;

            case DOWN:
            case DIGIT2:
                if (row + 1 < rows) {
                    row += 1;
                }
                break;

            case RIGHT:
            case DIGIT6:
                if (col + 1 < cols) {
                    col += 1;
                }
                break;

            case LEFT:
            case DIGIT4:
                if (col != 0) {
                    col -= 1;
                }
                break;

            case DIGIT9:
                if (row - 1 >= 0 && col + 1 < cols && map[row - 1][col + 1] == 0 && (map[row - 1][col] == 0 || map[row][col + 1] == 0)) {
                    row -= 1;
                    col += 1;
                }
                break;
            case DIGIT3:
                if (row + 1 < rows && col + 1 < cols && map[row + 1][col + 1] == 0 && (map[row][col + 1] == 0 || map[row + 1][col] == 0)) {
                    row += 1;
                    col += 1;
                }
                break;
            case DIGIT7:
                if (row - 1 >= 0 && col - 1 >= 0 && map[row - 1][col - 1] == 0 && (map[row - 1][col] == 0 || map[row][col - 1] == 0)) {
                    row -= 1;
                    col -= 1;
                }
                break;
            case DIGIT1:
                if (row + 1 < rows && col - 1 >= 0 && map[row + 1][col - 1] == 0 && (map[row + 1][col] == 0 || map[row][col - 1] == 0)) {
                    row += 1;
                    col -= 1;
                }
                break;
        }
        setPlayerPosition(row, col);
        keyEvent.consume();
    }

    public void setPlayerPosition(int row, int col){
        rows = Integer.parseInt(textField_rows.getText());
        cols = Integer.parseInt(textField_cols.getText());
        if ( mazeDisplayer.maze.getMaze()[row][col] != 1)
        {
            mazeDisplayer.setPlayerPosition(row, col);
            setUpdatePlayerRow(row);
            setUpdatePlayerCol(col);
        }

    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }
}

