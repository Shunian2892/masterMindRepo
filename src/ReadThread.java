import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Reads every message send form the ClientConnection class and performs a specific action based on the given command.
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

                //Check every incoming message and perform specific actions accordingly
                //Opens PlayerOneStage for the client who typed player one
                if (receivedMessage.equals("You are player one!")) {
                    out.writeUTF(gui.getNickName() + " is player one!");
                    gui.playerOneStage();
                } //Opens PlayerTwoStage for the client who typed player two
                else if (receivedMessage.equals("You are player two!")) {
                    out.writeUTF(gui.getNickName() + " is player two!");
                    gui.playerTwoStage();
                } else if (receivedMessage.equals("Player one is taken!")) {
                    out.writeUTF("Player one is already taken!");
                } else if (receivedMessage.equals("Player two is taken!")) {
                    out.writeUTF("Player two is already taken!");
                } //Opens the rules when typed in the chat
                else if(receivedMessage.equals("RULES")){
                    gui.rulesStage();
                }//Sends the messages from player two to player one
                else if (receivedMessage.startsWith("P2:")){
                    gui.stageOneAppendText(receivedMessage);
                } //Sends the messages from player one to player two
                else if(receivedMessage.startsWith("P1: ")) {
                    gui.stageTwoAppendText(receivedMessage);
                } //Interprets the text from PlayerOneStage to show that codemaker has won
                else if(receivedMessage.equals("CODEMAKER")){
                    writeToAllStages("THE WINNER IS THE CODEMAKER");
                } //Interprets the text from PlayerOneStage to show that codebreaker has won
                else if(receivedMessage.equals("CODEBREAKER")){
                    writeToAllStages("THE WINNER IS THE CODEBREAKER");
                } //Gives a warning to the players if player two is out of attempts
                 else if(receivedMessage.equals("LAST TRY")){
                     writeToAllStages("Last try for player two!");
                } else if(receivedMessage.equals("FULL")){
                     gui.readMessages.appendText("There are already two players...");
                     gui.disable();
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

    private void writeToAllStages(String message){
        gui.stageTwoAppendText(message);
        gui.stageOneAppendText(message);
    }
}
