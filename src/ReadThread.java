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
//    private boolean taken = false;

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

                gui.readMessages.appendText(receivedMessage + "\n");
//                checkPlayerOneTaken(receivedMessage);
            } catch (IOException e){
                System.out.println("Cannot read message from server!\n"
                        + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void checkPlayerOneTaken(String receivedMessage) {
        if (receivedMessage.equals("true")){
            try {
                out.writeBoolean(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}