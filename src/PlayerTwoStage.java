import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * GUI for the game mastermind specifically for the player who guesses the color code.
 */
public class PlayerTwoStage extends Application {
    private Button red,
            blue,
            green,
            purple,
            yellow,
            orange,
            setCode,
            clearCode;

    private BorderPane gamePane;

    TextArea gameArea;
    private Label colors;

    private String codeToBreak,
            colorsUnfinished,
            colorsFinished;

    private int amOfColors,
            amOfTurns;

    private Socket socket;
    private DataOutputStream out;


    public PlayerTwoStage(Socket s){
        this.socket = s;
        try {
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e){
            e.printStackTrace();
        }

        initializeVariables();
    }

    private void initializeVariables() {
        red = new Button("RED");
        blue = new Button("BLUE");
        green = new Button(  "GREEN");
        purple = new Button("PURPLE");
        yellow = new Button("YELLOW");
        orange = new Button("ORANGE");

        red.setStyle("-fx-background-color: #f54242; ");
        red.setMaxSize(200, 100);

        blue.setStyle("-fx-background-color: #357de8; ");
        blue.setMaxSize(200, 100);

        green.setStyle("-fx-background-color: #45ed4d; ");
        green.setMaxSize(200, 100);

        purple.setStyle("-fx-background-color: #d534eb; ");
        purple.setMaxSize(200, 100);

        orange.setStyle("-fx-background-color: #eba234; ");
        orange.setMaxSize(200, 100);

        yellow.setStyle("-fx-background-color: #f2e338; ");
        yellow.setMaxSize(200, 100);

        gameArea = new TextArea();
        colors = new Label("");

        codeToBreak = "";
        colorsUnfinished = "";
        colorsFinished = "";

        gamePane = new BorderPane();
        gameArea = new TextArea();
        colors = new Label("");

        setCode = new Button("Done");
        clearCode = new Button("clear");

        amOfTurns = 0;
        amOfColors = 0;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox topColors = new HBox();
        HBox bottomColors = new HBox();
        VBox allColors = new VBox();
        HBox doneOrClear = new HBox();

        Label yourCode = new Label("Your guess is: ");

        doneOrClear.getChildren().addAll(setCode,clearCode);

        topColors.getChildren().addAll(red, blue, green);
        topColors.setSpacing(20);

        bottomColors.getChildren().addAll(purple, yellow, orange);
        bottomColors.setSpacing(20);

        allColors.getChildren().addAll(yourCode, colors, topColors, bottomColors, doneOrClear);

        gamePane.setCenter(gameArea);
        gameArea.setEditable(false);
        gamePane.setBottom(allColors);

        Scene gameScene = new Scene(gamePane, 500, 700);
        primaryStage.setScene(gameScene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("MasterMind player two");
        primaryStage.show();

        buttonActions();

        //When the stage is closed it writes a message to the server
        primaryStage.setOnCloseRequest(action->{
            try {
                out.writeUTF("player two closed their game");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    //Player two stage button actions
    private void buttonActions() {
        red.setOnAction(action ->{ setColorLabel("red"); });
        blue.setOnAction(action ->{ setColorLabel("blue"); });
        green.setOnAction(action ->{ setColorLabel("green"); });
        yellow.setOnAction(action ->{ setColorLabel("yellow"); });
        purple.setOnAction(action ->{ setColorLabel("purple"); });
        orange.setOnAction(action ->{ setColorLabel("orange"); });

        setCode.setOnAction(action -> {
            codeToBreak = colors.getText();
            if(amOfColors < 4){
                appendText("The code must be 4 colors long");
            } else {
                appendText(codeToBreak);
                try{
                    amOfTurns++;
                    out.writeUTF("Player two has made a move");
                    out.writeUTF("P2: " + codeToBreak);

                    if(amOfTurns == 11){
                        out.writeUTF("LAST TRY");
                        out.writeUTF("Player two has made a move");
                        out.writeUTF("P2: " + codeToBreak);
                    }

                    if (amOfTurns == 12){
                        disable(setCode);
                        disable(clearCode);
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            //Colors cleared after sending it
            clearColorCode();
        });

        //Manually cleat code
        clearCode.setOnAction(action ->{
            clearColorCode();
        });
    }

    //Set max amount of colors clicked to 4
    private void setColorLabel(String text) {
        if(amOfColors <4){
            amOfColors++;
            colorsUnfinished = colorsFinished + text + " ";
            colors.setText(colorsUnfinished);
            colorsFinished = colorsUnfinished;
        }
    }

    //Appends text to the game area
    void appendText(String text){
        gameArea.appendText(text + "\n");
    }

    //Clears color code label
    private void clearColorCode(){
        amOfColors= 0;
        colors.setText("");
        colorsFinished = "";
        colorsUnfinished = "";
    }

    //Disables the usage off a button
    private void disable(Button button){
        button.setDisable(true);
    }
}
