package Model;

import Client.Client;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import Server.ServerStrategyGenerateMaze;
import Client.IClientStrategy;
import algorithms.search.Solution;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel{

    private Maze maze;
    private int playerRow;
    private int playerCol;
    private Solution solution;
    private Server mazeGeneratorServer;
    private Server solveMazeServer;

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    @Override
    public void stopServers() {
        mazeGeneratorServer.stop();
        solveMazeServer.stop();
    }

    private boolean gameOver = false;

    public MyModel(){
        mazeGeneratorServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveMazeServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
    }


    public void generateMaze(int rows,int cols)
    {
        mazeGeneratorServer.start();
        CommunicateWithServer_MazeGenerating(rows,cols);
        //mazeGeneratorServer.stop();
        setChanged();
        notifyObservers("maze generated");
        // start position:
        movePlayer(maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex());

    }

    @Override
    public Maze getMaze() {
        return this.maze;
    }

    private void movePlayer(int row, int col){
        if (row == maze.getGoalPosition().getRowIndex() && col == maze.getGoalPosition().getColumnIndex())
        {
            gameOver=true;
        }
        this.playerRow = row;
        this.playerCol = col;
        setChanged();
        notifyObservers("player moved");
    }

    public boolean gameOver(){
        return gameOver;
    }

    @Override
    public void updatePlayerLocation(MovementDirection direction) {
        switch (direction) {
            case UP:
            case DIGIT8:
            {
                if (playerRow > 0 && maze.getMaze()[playerRow-1][playerCol]!=1)
                    movePlayer(playerRow - 1, playerCol);
            }
            break;
            case DOWN:
            case DIGIT2:
            {
                if (playerRow < maze.getMaze().length - 1 && maze.getMaze()[playerRow+1][playerCol]!=1)
                    movePlayer(playerRow + 1, playerCol);
            }
            break;
            case LEFT:
            case DIGIT4:
            {
                if (playerCol > 0 && maze.getMaze()[playerRow][playerCol-1]!=1)
                    movePlayer(playerRow, playerCol - 1);
            }
            break;
            case RIGHT:
            case DIGIT6:
            {
                if (playerCol < maze.getMaze()[0].length - 1 && maze.getMaze()[playerRow][playerCol+1]!=1)
                    movePlayer(playerRow, playerCol + 1);
            }
            break;

            case DIGIT9:
                if (playerRow - 1 >= 0 && playerCol + 1 < maze.getMaze()[0].length && maze.getMaze()[playerRow - 1][playerCol + 1] == 0 && (maze.getMaze()[playerRow - 1][playerCol] == 0 || maze.getMaze()[playerRow][playerCol + 1] == 0)) {
                    movePlayer(playerRow-1,playerCol+1);
                }
                break;
            case DIGIT3:
                if (playerRow + 1 < maze.getMaze().length && playerCol + 1 < maze.getMaze()[0].length && maze.getMaze()[playerRow + 1][playerCol + 1] == 0 && (maze.getMaze()[playerRow][playerCol + 1] == 0 || maze.getMaze()[playerRow + 1][playerCol] == 0)) {
                    movePlayer(playerRow+1,playerCol+1);

                }
                break;
            case DIGIT7:
                if (playerRow - 1 >= 0 && playerCol - 1 >= 0 && maze.getMaze()[playerRow - 1][playerCol - 1] == 0 && (maze.getMaze()[playerRow - 1][playerCol] == 0 || maze.getMaze()[playerRow][playerCol - 1] == 0)) {
                    movePlayer(playerRow-1,playerCol-1);

                }
                break;
            case DIGIT1:
                if (playerRow + 1 < maze.getMaze().length && playerCol - 1 >= 0 && maze.getMaze()[playerRow + 1][playerCol - 1] == 0 && (maze.getMaze()[playerRow + 1][playerCol] == 0 || maze.getMaze()[playerRow][playerCol - 1] == 0)) {
                    movePlayer(playerRow+1 , playerCol-1);

                }
                break;
        }

    }

    @Override
    public int getPlayerRow() {
        return playerRow;
    }

    @Override
    public int getPlayerCol() {
        return playerCol;
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void solveMaze() {
        //solve the maze
        mazeGeneratorServer.start();
        setChanged();
        notifyObservers("maze solved");

        CommunicateWithServer_SolveSearchProblem(this.maze);

    }

    @Override
    public Solution getSolution() {
        return this.solution;
    }

    private void CommunicateWithServer_MazeGenerating(int rows,int cols) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rows, cols};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[])fromServer.readObject();
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[100000];
                        is.read(decompressedMaze);
                        maze = new Maze(decompressedMaze);
                    }
                    catch (Exception var10)
                    {
                        var10.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1)
        {
            var1.printStackTrace();
        }
    }

    private void CommunicateWithServer_SolveSearchProblem(Maze maze) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze);
                        toServer.flush();
                        solution = (Solution)fromServer.readObject();

                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
    }
}
