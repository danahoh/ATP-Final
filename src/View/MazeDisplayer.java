package View;

import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;

public class MazeDisplayer extends Canvas {

    private Maze maze;
    private int playerRow;
    private int playerCol;
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();

    private void drawMaze(Maze maze)
    {
        this.maze = maze;
        draw();
    }

    private void draw() {
        if(maze != null)
        {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze.getMaze().length;
            int cols = maze.getMaze()[0].length;

            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;


        }

    }


}
