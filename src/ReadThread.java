
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Gets the socket and client gui from the ClientGui class when the person has logged in.
 * Create a new DataInputStream for reading the messages the user put in the textfield and put them in a String variable.
 * Append this message to the TextArea in the ClientGui such that the client can see the send message
 */
public class ReadThread implements Runnable {
    private Socket socket;
    private ClientGui gui;
    private DataInputStream in;
    private DataOutputStream out;

    public ReadThread(Socket socket, ClientGui gui){
        this.socket = socket;
        this.gui = gui;
    }

    @Override
    public void run() {

        while (true){
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                String receivedMessage = in.readUTF();
                System.out.println(receivedMessage);

                //TODO new problem, mastermind stages dont open + chat stops working
                System.out.println("reached");

                if (receivedMessage.equals("You are player one!")) {
                    out.writeUTF(gui.getNickName() + " is player one!");
                    gui.playerOneStage();
                } else if (receivedMessage.equals("You are player two!")) {
                    out.writeUTF(gui.getNickName() + " is player two!");
                    gui.playerTwoStage();
                } else if (receivedMessage.equals("Player one is taken!")) {
                    out.writeUTF("Player one is taken! " + gui.getNickName() + " is player 2!");
                    gui.playerTwoStage();
                } else if (receivedMessage.equals("Player two is taken!")) {
                    out.writeUTF(gui.getNickName() + " is player one!");
                    gui.playerOneStage();
                } else if(!receivedMessage.equals("Received")){
                    gui.readMessages.appendText(receivedMessage + "\n");
                }
            } catch (IOException e){
                System.out.println("Cannot read message from server!\n"
                        + e.getMessage());
                e.printStackTrace();
            }
        }
    }


}
