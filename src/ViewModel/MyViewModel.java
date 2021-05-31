package ViewModel;

import Model.MyModel;
import algorithms.mazeGenerators.Maze;

public class MyViewModel {

    public Maze generateMaze(int rows, int cols)
    {
        MyModel model = new MyModel();
        return model.generateMaze(rows,cols);
    }
}
