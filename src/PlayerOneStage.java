import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * GUI for the game mastermind specifically for the player who makes the color code.
 */
public class PlayerOneStage extends Application {
    private Button red,
            blue,
            green,
            purple,
            yellow,
            orange,
            white,
            black,
            none,
            setCode,
            clearCode,
            setFeedback,
            clearFeedback;

    private BorderPane gamePane;
    private VBox allColors;

    protected TextArea gameArea;
    private Label colors,
            feedbackLabel,
            yourFeedback;

    private String codeToBreak,
            colorsUnfinished,
            colorsFinished;

    private String feedback,
            feedbackUnfinished,
            feedbackFinished;

    private int amOfColors,
            amOfFeedback,
            amOfTurns;

    private Socket socket;
    private DataOutputStream out;

    public PlayerOneStage(Socket s){
        this.socket = s;
        try {
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e){
            e.printStackTrace();
        }

        initializeStartVariables();
        initializeFeedbackVariables();
    }

    //Initialize all start variables
    private void initializeStartVariables() {
        red = new Button("RED");
        blue = new Button("BLUE");
        green = new Button(  "GREEN");
        purple = new Button("PURPLE");
        yellow = new Button("YELLOW");
        orange = new Button("ORANGE");
        gamePane = new BorderPane();

        gameArea = new TextArea();
        colors = new Label("");

        allColors = new VBox();
        setCode = new Button("Done");
        clearCode = new Button("clear");

        codeToBreak = "";
        colorsUnfinished = "";
        colorsFinished = "";

        feedback = "";
        feedbackUnfinished = "";
        feedbackFinished = "";

        amOfColors = 0;
        amOfFeedback = 0;
        amOfTurns = 0;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox topColors = new HBox();
        HBox bottomColors = new HBox();
        HBox doneOrClear = new HBox();
        Label yourCode = new Label("Your 4 colored code: ");

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
        primaryStage.setTitle("MasterMind player one");
        primaryStage.setResizable(false);
        primaryStage.show();

        colorButtonActions();

        //when the stage is closed it writes a message to the server
        primaryStage.setOnCloseRequest(action->{
            try {
                out.writeUTF("player one closed their game");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    //Player one stage color buttons actions
    private void colorButtonActions() {
        red.setOnAction(action ->{ setColorLabel("red"); });
        blue.setOnAction(action ->{ setColorLabel("blue"); });
        green.setOnAction(action ->{ setColorLabel("green"); });
        yellow.setOnAction(action ->{ setColorLabel("yellow"); });
        purple.setOnAction(action ->{ setColorLabel("purple"); });
        orange.setOnAction(action ->{ setColorLabel("orange"); });

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

        setCode.setOnAction(action -> {
            codeToBreak = colors.getText();
            setCodeToBreak(codeToBreak);
            if(amOfColors < 4){
                appendText("The code must be 4 colors long");
            } else {
                appendText("CODE HAS BEEN SET!");
                appendText("Your code is: " +  codeToBreak);
                try{
                    out.writeUTF("Player one has made a move");
                    out.writeUTF("P1: CODE HAS BEEN SET!");
                } catch (IOException e){
                    e.printStackTrace();
                }

                disable(setCode);
                disable(clearCode);
                //create new buttons for feedback
                HBox colorsAndFeedback = new HBox();

                colorsAndFeedback.getChildren().addAll(allColors,getFeedbackLayout());
                gamePane.setBottom(colorsAndFeedback);
            }
        });

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

    //Initialize all feedback variables
    private void initializeFeedbackVariables() {
        white = new Button("white");
        black = new Button( "black");
        none = new Button("blank");
        yourFeedback = new Label("Your feedback is: ");
        setFeedback = new Button("Send");
        clearFeedback = new Button("Clear");
    }

    //Layout of the feedback box
    private VBox getFeedbackLayout(){
        white.setMaxSize(200, 100);
        black.setStyle("-fx-background-color: #000000;");
        black.setTextFill(Color.WHITE);
        black.setMaxSize(200, 100);
        none.setStyle("-fx-background-color: #d0d0d0;");
        none.setMaxSize(200, 100);

        feedbackLabel = new Label("");

        HBox setOrClear = new HBox();
        setOrClear.getChildren().addAll(setFeedback,clearFeedback);

        HBox feedbackButtons = new HBox();
        feedbackButtons.getChildren().addAll(white,black,none);
        feedbackButtons.setSpacing(10);

        VBox feedback = new VBox();
        feedback.getChildren().addAll(yourFeedback,feedbackLabel,feedbackButtons, setOrClear);
        white.setOnAction(action -> {
            setFeedbackLabel("white");
        });

        black.setOnAction(action -> {
            setFeedbackLabel("black");
        });

        none.setOnAction(action -> {
            setFeedbackLabel("none");
        });

        //Set the feedback button action and increase turn amount by 1
        setFeedback.setOnAction(action -> {
            this.feedback = feedbackLabel.getText();
            if(amOfFeedback < 4){
                appendText("The feedback must be 4 colors long");
            } else {
                appendText("Your feedback is: " + this.feedback);
                //Write to ReadThread that the player has made a move
                try{
                    amOfTurns++;
                    out.writeUTF("Player one has made a move");
                    out.writeUTF("P1: " + this.feedback);
                    //If everything is right it writes a message that the ReadThread interprets
                    if(this.feedback.equals("black black black black ")){
                        out.writeUTF("CODEBREAKER");
                        disable(setFeedback);
                        disable(clearFeedback);
                    }//12 is the maximum of turns then it writes a messages that the ReadThread interprets
                    else if(amOfTurns == 12){
                        out.writeUTF("CODEMAKER");
                        disable(setFeedback);
                        disable(clearFeedback);
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            //Feedback cleared after sending it
            clearFeedback();
        });

        //Manually clear feedback
        clearFeedback.setOnAction(action->{
            clearFeedback();
        });
        return feedback;
    }

    //Set max amount of feedback buttons clicked to 4
    private void setFeedbackLabel(String text) {
        if(amOfFeedback <4){
            amOfFeedback++;
            feedbackUnfinished = feedbackFinished + text + " ";
            feedbackLabel.setText(feedbackUnfinished);
            feedbackFinished = feedbackUnfinished;
        }
    }

    //Clears feedback label
    private void clearFeedback(){
        amOfFeedback = 0;
        feedbackLabel.setText("");
        feedbackFinished = "";
        feedbackUnfinished = "";
    }

    //Clears color code label
    private void clearColorCode(){
        amOfColors= 0;
        colors.setText("");
        colorsFinished = "";
        colorsUnfinished = "";
    }

    //Set the code to break
    private void setCodeToBreak(String codeToBreak) {
        this.codeToBreak = codeToBreak;
    }

    //Appends text to the game area
    void appendText(String text){
        gameArea.appendText(text + "\n");
    }

    //Disables the usage off a button
    private void disable(Button button){
        button.setDisable(true);
    }

}
