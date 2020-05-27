import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PlayerOneStage extends Application {
    Button red = new Button("RED");
    Button blue = new Button("BLUE");
    Button green = new Button(  "GREEN");
    Button purple = new Button("PURPLE");
    Button yellow = new Button("YELLOW");
    Button orange = new Button("ORANGE");

    TextArea gameArea = new TextArea();
    Label colors = new Label("");

    private String codeToBreak = "";
    String colorsUnfinished = "";
    String colorsFinished = "";

    int amOfColors = 0;

    private boolean hasBeenSet = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane gamePane = new BorderPane();
        HBox topColors = new HBox();
        HBox bottomColors = new HBox();
        VBox allColors = new VBox();
        Button setCode = new Button("Done");
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

        topColors.getChildren().addAll(red, blue, green);
        topColors.setSpacing(20);
        bottomColors.getChildren().addAll(purple, yellow, orange);
        bottomColors.setSpacing(20);
        allColors.getChildren().addAll(yourCode, colors, topColors, bottomColors, setCode);

        gamePane.setCenter(gameArea);
        gameArea.setEditable(false);
        gamePane.setBottom(allColors);
        Scene gameScene = new Scene(gamePane, 500, 700);
        primaryStage.setScene(gameScene);
        primaryStage.setTitle("MasterMind player one");
        primaryStage.show();


        //PLAYER ONE STAGE BUTTON ACTIONS
        red.setOnAction(action ->{
           setColorLabel("red");
        });

        blue.setOnAction(action ->{
          setColorLabel("blue");
        });

        green.setOnAction(action ->{
           setColorLabel("green");
        });

        yellow.setOnAction(action ->{
           setColorLabel("yellow");
        });

        purple.setOnAction(action ->{
            setColorLabel("purple");
        });

        orange.setOnAction(action ->{
            setColorLabel("orange");
        });

        setCode.setOnAction(action -> {
            codeToBreak = colors.getText();
            setCodeToBreak(codeToBreak);
            System.out.println(codeToBreak);
            appendText("CODE HAS BEEN SET!");
            hasBeenSet = true;

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
}
