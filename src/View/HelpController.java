package View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable {

    @FXML
    private TextFlow textFlow;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Text text1 = new Text("Dana and Navit have been lost in the way and all their friends are already in the Funjoya! The purpose of the game is to take Dana and Navit to their friends who are in the Funjoya.\n" +
                "In order to move the character you can use the arrows on the keyboard.\n" +
                "You can also use the numPad:\n" +
                "2 – down, 4 – left, 6 – right, 8 – up, 1 - down left, 3 - down right, 7 - up left, 9 - up right\n" +
                "In the game you can create your maze with different algorithms. You can change the size of the maze (enter number of rows and columns).\n" +
                "In addition, you can get the maze solution, in case you have difficulty.\n" +
                "If background music bothers you, you can always mute it by pressing the mute button.\n");
        text1.setFill(Color.BLACK);
        text1.setFont(Font.font("Helvetica", FontPosture.ITALIC, 20));
        textFlow.getChildren().add(text1);

    }
}
