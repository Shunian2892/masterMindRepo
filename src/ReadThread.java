
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

                if (receivedMessage.equals("You are player one!")) {
                    out.writeUTF(gui.getNickName() + " is player one!");
                    gui.playerOneStage();
                } else if (receivedMessage.equals("You are player two!")) {
                    out.writeUTF(gui.getNickName() + " is player two!");
                    gui.playerTwoStage();
                } else if (receivedMessage.equals("Player one is taken!")) {
                } else if (receivedMessage.equals("Player two is taken!")) {

                } else if (receivedMessage.startsWith("P2:")){
                    gui.stageOneAppendText(receivedMessage);
                } else if(receivedMessage.startsWith("P1: ")) {
                    gui.stageTwoAppendText(receivedMessage);
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
