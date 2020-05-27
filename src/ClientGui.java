import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientGui extends Application {
    private String localHost = "localhost";
    private int portNum = 10000;
    private DataOutputStream out = null;
    private String nickName;
    boolean codeIsSet = false;
    private String codeToBreak = "";
    private boolean playerOneTaken = false;

    public TextArea readMessages;
    public TextField writeMessages;

    public static void main(String[] args) {
        launch(ClientGui.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //LOGIN SCENE
        BorderPane loginPane = new BorderPane();
        TextField loginName = new TextField();
        TextField hostAddress = new TextField(localHost);
        TextField port = new TextField(Integer.toString(portNum));
        port.setEditable(false);
        hostAddress.setEditable(false);
        Label loginLabel = new Label("(nick)Name: ");
        Label hostaddressLabel = new Label("Hostaddress ");
        Label portLabel = new Label("Port ");

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

        //CHAT SCENE
        writeMessages = new TextField();
        readMessages = new TextArea();
        Button send = new Button("Send");

        BorderPane chatPane = new BorderPane();
        chatPane.setCenter(readMessages);
        readMessages.setEditable(false);
        HBox writeAndSend = new HBox();
        writeAndSend.setSpacing(30);
        writeAndSend.getChildren().addAll(writeMessages, send);
        chatPane.setBottom(writeAndSend);
        Scene chatScreen = new Scene(chatPane);


        //BUTTON ACTIONS
        //LOGIN SCREEN BUTTON ACTION
        buttonLogin.setOnAction(e -> {
            if(!loginName.getText().isEmpty()){
                nickName = loginName.getText();
                try {
                    Socket socket = new Socket(localHost, portNum);
                    readMessages.appendText("You are now connected!\n");
                    out = new DataOutputStream(socket.getOutputStream());

                    ReadThread threadReader = new ReadThread(socket, this);
                    Thread t = new Thread(threadReader);
                    t.start();
                } catch (IOException ex){
                    ex.printStackTrace();
                }
                primaryStage.setScene(chatScreen);
            } else {
                System.out.println("Please fill in all the boxes!");
            }

        });

        //CHAT SCREEN BUTTON ACTIONS
        send.setOnAction(e -> {
            if(writeMessages.getText().isEmpty()){
                return;
            }
            //Start a game
            if(writeMessages.getText().equals("player one")){
                //TODO FIX THIS, RIGHT NOW PLAYER TWO CAN ALSO BE PLAYER ONE!! FIX ERRORCATCHING
                if(!playerOneTaken){
                    playerOneTaken = true;
                    try {
                        out.writeUTF(nickName + " is player one!");
                    } catch (IOException ex){
                        ex.printStackTrace();
                    }
                    playerOneStage();

                } else {
                    try {
                        out.writeUTF("Player one is taken! " + nickName + " is player 2!");
                    } catch (IOException ex){
                        ex.printStackTrace();
                    }
                    playerTwoStage();
                }
            }

            if(writeMessages.getText().equals("player two")){
                try {
                    out.writeUTF(nickName + " is player two!");
                } catch (IOException ex){
                    ex.printStackTrace();
                }
                playerTwoStage();
            }

            //Quit and disconnect from the sever
            if(writeMessages.getText().equals("quit")){
                try {
                    out.writeUTF("<" + nickName + "> has disconnected!");
                    writeMessages.setEditable(false);
//                    primaryStage.close();

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

    private void playerOneStage() {
        //PLAYER ONE STAGE SETUP
        PlayerOneStage one = new PlayerOneStage();
        Stage gameStage = new Stage();

        try{
            one.start(gameStage);
        } catch (Exception e){
            e.printStackTrace();
        }

        one.appendText("Welcome!");

        codeToBreak = one.getCodeToBreak();
        if(one.isHasBeenSet()){
            one.appendText("Code has been SET!!");
            codeIsSet = true;
            System.out.println(codeToBreak);
        }
    }


    private void playerTwoStage() {
        //PLAYER TWO STAGE SETUP
        PlayerTwoStage two = new PlayerTwoStage();
        Stage playerTwoStage = new Stage();

        try {
            two.start(playerTwoStage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (codeIsSet = true) {
            two.appendText("Code has been SET!");
        }
    }
}
