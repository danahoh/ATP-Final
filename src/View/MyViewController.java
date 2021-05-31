package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class MyViewController implements IView, Initializable {

    public MyViewModel viewModel;
    public TextField textField_rows;
    public TextField textField_cols;
    public MazeDisplayer mazeDisplayer;


    public void generateMaze(ActionEvent actionEvent)
    {
        if(viewModel == null)
        {
            viewModel = new MyViewModel();
        }
        int rows = Integer.valueOf(textField_rows.getText());
        int cols = Integer.valueOf(textField_cols.getText());
        Maze maze = viewModel.generateMaze(rows,cols);
        if (mazeDisplayer == null)
        {
            mazeDisplayer = new MazeDisplayer();
        }
        mazeDisplayer.drawMaze(maze);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
