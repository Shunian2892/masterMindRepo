import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Explanation gui for the game mastermind.
 */
public class Rules extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane pane = new BorderPane();
        TextArea area = new TextArea();
        area.setMaxSize(1000,50);
        area.setEditable(false);
        TextArea rules = new TextArea();
        rules.setMaxSize(1000,200);
        rules.setEditable(false);

        area.setFont(Font.font("Serif", 24));
        area.setText("Rules To MasterMind!");
        rules.setText("CodeMaker (player one):" +
                "\nChoose four (4) colors to form a code." +
                "\nThe CODEBREAKER has to try and guess the code." +
                "\nAfter each attempt of the CODEBREAKER, the CODEMAKER gives feedback by presenting:" +
                "\nOne (1) black peg for each correct color in the correct place" +
                "\nOne (1) white peg for each correct color, but in the wrong place" +
                "\nZero (0) pegs for each color that doesn't exist in the code" +
                "\n" +
                "\n" +
                "\nCodeBreaker (player two):" +
                "\nTries to decode the code of the CODEMAKER." +
                "\nSelects up to four (4) colors in a specific order per turn." +
                "\nAfter each attempt, the CODEBREAKER receives feedback from the CODEMAKER using black, white, or no pegs." +
                "\n" +
                "\nIf, after 12 attempts (rounds) of guessing and feedback the CODEBREAKER did not guess the correct code, the CODEMAKER wins the game." +
                "\nIf the CODEBREAKER guesses the code within 12 attempts (rounds), then the CODEBREAKER wins the game");

        pane.setTop(area);
        pane.setCenter(rules);

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Rules of mastermind");
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
        });
    }
}
