import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PlayerTwoStage extends Application {
    private Button red;
    private Button blue;
    private Button green;
    private Button purple;
    private Button yellow;
    private Button orange;
    private Button setCode;
    private Button clearCode;

    TextArea gameArea = new TextArea();
    Label colors = new Label("");

    private String codeToBreak = "";
    String colorsUnfinished = "";
    String colorsFinished = "";

    int amOfColors = 0;

    private Socket socket;
    private DataOutputStream out;


    public PlayerTwoStage(Socket s){
        this.socket = s;
        try {
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        red = new Button("RED");
        blue = new Button("BLUE");
        green = new Button(  "GREEN");
        purple = new Button("PURPLE");
        yellow = new Button("YELLOW");
        orange = new Button("ORANGE");
        BorderPane gamePane = new BorderPane();
        HBox topColors = new HBox();
        HBox bottomColors = new HBox();
        VBox allColors = new VBox();
        HBox doneOrClear = new HBox();
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
        primaryStage.setTitle("MasterMind player two");
        primaryStage.show();

        colorButtonActions();
    }

    //PLAYER TWO STAGE BUTTON ACTIONS
    private void colorButtonActions() {
        red.setOnAction(action ->{ setColorLabel("red"); });

        blue.setOnAction(action ->{ setColorLabel("blue"); });

        green.setOnAction(action ->{ setColorLabel("green"); });

        yellow.setOnAction(action ->{ setColorLabel("yellow"); });

        purple.setOnAction(action ->{ setColorLabel("purple"); });

        orange.setOnAction(action ->{ setColorLabel("orange"); });

        setCode.setOnAction(action -> {
            codeToBreak = colors.getText();
            appendText(codeToBreak);
            try{
                out.writeUTF("Player two made a move");
                out.writeUTF("P2: " + codeToBreak);
            } catch (IOException e){
                e.printStackTrace();
            }
        });

        clearCode.setOnAction(action ->{
            clearColorCode();
        });

    }

    public void setColorLabel(String text) {
        if(amOfColors <4){
            amOfColors++;
            colorsUnfinished = colorsFinished + text + " ";
            colors.setText(colorsUnfinished);
            colorsFinished = colorsUnfinished;
        }
    }

    public void appendText(String text){
        gameArea.appendText(text + "\n");
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
}
