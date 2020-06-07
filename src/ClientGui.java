import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientGui extends Application {
    private String localHost = "localhost";
    private int portNum = 10000;
    private DataOutputStream out = null;

    private String nickName;
    private Stage playerOneStage;
    private Stage playerTwoStage;
    private Stage rulesStage;
    public TextArea readMessages;
    public TextField writeMessages;
    private Socket socket;
    private ReadThread threadReader;

    private PlayerOneStage one;
    private PlayerTwoStage two;
    private Rules rules;

    public static void main(String[] args) {
        launch(ClientGui.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Login scene
        BorderPane loginPane = new BorderPane();
        TextField loginName = new TextField();
        TextField hostAddress = new TextField(localHost);
        TextField port = new TextField(Integer.toString(portNum));
        port.setEditable(false);
        hostAddress.setEditable(false);
        Label loginLabel = new Label("(nick)Name: ");
        Label hostaddressLabel = new Label("Hostaddress: ");
        Label portLabel = new Label("Port: ");

        VBox labels = new VBox();
        labels.getChildren().addAll(loginLabel, hostaddressLabel, portLabel );
        labels.setSpacing(35);

        VBox fields = new VBox();
        fields.getChildren().addAll(loginName, hostAddress, port);
        fields.setSpacing(20);

        Button buttonLogin = new Button("Login");
        loginPane.setBottom(buttonLogin);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(labels, fields);
        hBox.setSpacing(30);
        loginPane.setCenter(hBox);

        Scene loginScene = new Scene(loginPane, 400, 200);
        primaryStage.setScene(loginScene);
        primaryStage.show();

        //Chat scene
        writeMessages = new TextField();
        writeMessages.setPrefWidth(500);
        readMessages = new TextArea();
        Button send = new Button("Send");

        BorderPane chatPane = new BorderPane();
        chatPane.setCenter(readMessages);
        readMessages.setEditable(false);
        HBox writeAndSend = new HBox();
        writeAndSend.setSpacing(30);
        writeAndSend.getChildren().addAll(writeMessages, send);
        chatPane.setBottom(writeAndSend);
        Scene chatScreen = new Scene(chatPane, 700, 500);

        playerTwoStage = new Stage();
        playerOneStage = new Stage();
        rulesStage = new Stage();

        //Button actions
        //Login screen button actions
        buttonLogin.setOnAction(e -> {
            if(!loginName.getText().isEmpty()){
                nickName = loginName.getText();
                try {
                    primaryStage.setScene(chatScreen);
                    primaryStage.setResizable(false);

                    socket = new Socket(localHost, portNum);
                    readMessages.appendText("You are now connected as "+ nickName + " !\n");
                    out = new DataOutputStream(socket.getOutputStream());

                    readMessages.appendText("You can now chat!!\n" +
                                            "Type 'player one' to start the game as the Codemaker\n" +
                                            "Type 'player two' to start the game as the Codebreaker\n" +
                                            "Type 'rules' to show the rules of the game\n");

                    threadReader = new ReadThread(socket, this);

                    Thread t = new Thread(threadReader);
                    t.start();

                    rules = new Rules();
                    two = new PlayerTwoStage(this.socket);
                    one = new PlayerOneStage(this.socket);
                } catch (IOException ex){
                    ex.printStackTrace();
                }

            } else {
                System.out.println("Please fill in all the boxes!");
            }
        });

        //Chat screen button action
        send.setOnAction(e -> {
            if(writeMessages.getText().isEmpty()){
                return;
            }

            //Quit and disconnect from the sever
            if(writeMessages.getText().equals("quit")){
                try {
                    out.writeUTF("<" + nickName + "> has disconnected!");
                    writeMessages.setEditable(false);
                } catch (IOException ex){
                    ex.printStackTrace();
                }
            }
            try{
                out.writeUTF("<" + nickName + "> " + writeMessages.getText());
                out.flush();
                writeMessages.clear();
            } catch (IOException ex){
                ex.printStackTrace();
            }
        });
    }

    //Player one stage setup
    protected void playerOneStage() {
        Platform.runLater( () -> {
            try{
                one = new PlayerOneStage(this.socket);
                one.start(playerOneStage);
            } catch (Exception e){
                e.printStackTrace();
            }
            one.appendText("Welcome");
        });
    }

    //Append text to the game area of player one
    protected void stageOneAppendText(String text){
        one.gameArea.appendText(text + "\n");
    }

    //Player two stage setup
    protected void playerTwoStage() {
        Platform.runLater(() -> {
            try {
                two = new PlayerTwoStage(this.socket);
                two.start(playerTwoStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            two.appendText("Welcome");
        });
    }

    //Append text to the game area of player two
    protected void stageTwoAppendText(String text){
        two.gameArea.appendText(text + "\n");
    }

    public String getNickName() {
        return nickName;
    }

    //Rules stage setup
    protected void rulesStage(){
        Platform.runLater(()->{
            try {
                rules.start(rulesStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
