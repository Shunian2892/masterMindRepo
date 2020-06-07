import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Reads every message that comes from the socket on which the user is connected to the server, sends it through the sever to other people and scans each message for a specific command.
 * If there is a specific command given, write this command to the ReadThread class for the action tied to that command.
 */
public class ClientConnection implements Runnable{
    private Socket socket;
    private ServerGui server;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean isRunning = true;

    public ClientConnection(Socket socket, ServerGui server){
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            while (isRunning){
                String receivingMessage = in.readUTF();
                System.out.println(receivingMessage);
                server.sendToAllClients(receivingMessage);
                server.ta.appendText(receivingMessage + "\n");
                out.flush();

                //Check if the client wants to disconnect
                if(receivingMessage.endsWith("quit")){
                    server.removeClient(this);
                }

                if(receivingMessage.endsWith("rules")){
                    out.writeUTF("RULES");
                }

                //Check if the player wants to play a game
                if(receivingMessage.endsWith("player one")){
                    if(!server.isPlayerOne()){
                        server.setPlayerOne(true);
                        out.writeUTF("You are player one!");
                        out.flush();
                        continue;
                    } else {
                        out.writeUTF("Player one taken!");
                        out.flush();
                        continue;
                    }
                }

                if (receivingMessage.endsWith("player two")){
                    if(!server.isPlayerTwo()){
                        server.setPlayerTwo(true);
                        out.writeUTF("You are player two!");
                        out.flush();
                        continue;
                    } else {
                        out.writeUTF("Player two is taken!");
                        out.flush();
                        continue;
                    }
                }

                //Check if the player closed the game GUI
                if(receivingMessage.equals("player one closed their game")){
                    server.setPlayerOne(false);
                    continue;
                }

                if(receivingMessage.equals("player two closed their game")){
                    server.setPlayerTwo(false);
                    continue;
                }

                //Write a generic answer message to ReadThread such that it knows that the received message doesn't have a specific action linked to it
                out.writeUTF("Received");
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //Send the whole message to the server
    public void sendMessage(String text){
        try {
            out.writeUTF(text);
            out.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
