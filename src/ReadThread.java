
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
    private PlayerOneStage stageOne;
    private PlayerTwoStage stageTwo;

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

                System.out.println("reached");

                //opens playeronestage for the client who typed player one
                if (receivedMessage.equals("You are player one!")) {
                    out.writeUTF(gui.getNickName() + " is player one!");
                    gui.playerOneStage();
                } //opens playertwostage for the client who typed player two
                else if (receivedMessage.equals("You are player two!")) {
                    out.writeUTF(gui.getNickName() + " is player two!");
                    gui.playerTwoStage();
                } else if (receivedMessage.equals("Player one is taken!")) {
                } else if (receivedMessage.equals("Player two is taken!")) {

                } //opens rules when typed in the chat
                else if(receivedMessage.endsWith("rules")){
                        gui.rulesStage();
                }//sends the messages from player two to player one
                else if (receivedMessage.startsWith("P2:")){
                    gui.stageOneAppendText(receivedMessage);
                } //sends the messages from player one to player two
                else if(receivedMessage.startsWith("P1: ")) {
                    gui.stageTwoAppendText(receivedMessage);
                } //interpets the text from playeronestage to show that codemaker has won
                else if(receivedMessage.endsWith("CODEMAKER")){
                    gui.stageTwoAppendText("THE WINNER IS THE CODEMAKER");
                    gui.stageOneAppendText("The WINNER IS THE CODEMAKER");
                } //interpets the text from playeronestage to show that codebreaker has won
                else if(receivedMessage.equals("CODEBREAKER")){
                    gui.stageTwoAppendText("THE WINNER IS THE CODEBREAKER");
                    gui.stageOneAppendText("The WINNER IS THE CODEBREAKER");
                } else if(!receivedMessage.equals("Received")) {
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
