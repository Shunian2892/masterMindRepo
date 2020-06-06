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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PlayerOneStage extends Application {
    Button red = new Button("RED");
    Button blue = new Button("BLUE");
    Button green = new Button(  "GREEN");
    Button purple = new Button("PURPLE");
    Button yellow = new Button("YELLOW");
    Button orange = new Button("ORANGE");


    private BorderPane gamePane;
    private VBox allColors;

    private TextArea gameArea = new TextArea();
    private Label colors = new Label("");
    private Button setCode;
    private Button clearCode;

    private Label feedbackLabel;

    private String codeToBreak = "";
    private String colorsUnfinished = "";
    private  String colorsFinished = "";

    private String feedback = "";
    private String feedbackUnfinished = "";
    private  String feedbackFinished = "";

    private int amOfColors = 0;
    private int amOfFeedback = 0;

    private boolean hasBeenSet = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        gamePane = new BorderPane();
        HBox topColors = new HBox();
        HBox bottomColors = new HBox();
        HBox doneOrClear = new HBox();
        allColors = new VBox();
        setCode = new Button("Done");
        clearCode = new Button("clear");
        Label yourCode = new Label("Your 4 colored code: ");


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
        primaryStage.show();

        colorButtonActions();

    }


    public void setColorLabel(String text) {
        if(amOfColors <4){
            amOfColors++;
            colorsUnfinished = colorsFinished + text + " ";
            colors.setText(colorsUnfinished);
            colorsFinished = colorsUnfinished;
        }
    }
    public void setFeedbackLabel(String text) {
        if(amOfFeedback <4){
            amOfFeedback++;
            feedbackUnfinished = feedbackFinished + text + " ";
            feedbackLabel.setText(feedbackUnfinished);
            feedbackFinished = feedbackUnfinished;
        }
    }

    public VBox getFeedbackLayout(){
        Button white = new Button("white");
        white.setMaxSize(200, 100);
        Button black = new Button( "black");
        black.setStyle("-fx-background-color: #000000;");
        black.setTextFill(Color.WHITE);
        black.setMaxSize(200, 100);
        Button none = new Button("blank");
        none.setStyle("-fx-background-color: #d0d0d0;");
        none.setMaxSize(200, 100);

        Label yourFeedback = new Label("your feedback is: ");
        feedbackLabel = new Label("");
        Button setFeedback = new Button("done");
        Button clearFeedback = new Button("clear");

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

        setFeedback.setOnAction(action -> {
            this.feedback = feedbackLabel.getText();
            appendText("your feedback is: " + this.feedback);

        });
        clearFeedback.setOnAction(action->{
            clearFeedback();
        });
        return feedback;
    }

    //PLAYER ONE STAGE COLOR BUTTON ACTIONS
    private void colorButtonActions() {
        red.setOnAction(action ->{ setColorLabel("red"); });

        blue.setOnAction(action ->{ setColorLabel("blue"); });

        green.setOnAction(action ->{ setColorLabel("green"); });

        yellow.setOnAction(action ->{ setColorLabel("yellow"); });

        purple.setOnAction(action ->{ setColorLabel("purple"); });

        orange.setOnAction(action ->{ setColorLabel("orange"); });

        setCode.setOnAction(action -> {
            codeToBreak = colors.getText();
            setCodeToBreak(codeToBreak);
            appendText("CODE HAS BEEN SET!");
            appendText("your code is: " +  codeToBreak);
            hasBeenSet = true;
            disable(setCode);
            disable(clearCode);
            HBox colorsAndFeedback = new HBox();
            colorsAndFeedback.getChildren().addAll(allColors,getFeedbackLayout());
            gamePane.setBottom(colorsAndFeedback);
        });

        clearCode.setOnAction(action ->{
            clearColorCode();
        });

    }

    public void clearFeedback(){
        amOfFeedback = 0;
        feedbackLabel.setText("");
        feedbackFinished = "";
        feedbackUnfinished = "";
    }

    public void clearColorCode(){
        amOfColors= 0;
        colors.setText("");
        colorsFinished = "";
        colorsUnfinished = "";
    }


    public String getCodeToBreak() {
        return codeToBreak;
    }

    public void setCodeToBreak(String codeToBreak) {
        this.codeToBreak = codeToBreak;
    }

    public void appendText(String text){
        gameArea.appendText(text + "\n");
    }

    public boolean isHasBeenSet() {
        return hasBeenSet;
    }

    public void disable( Button button){
        button.setDisable(true);
    }


}
