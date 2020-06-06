import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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

                if(receivingMessage.equals("quit")){
                    server.removeClient(this);
                }

                if(receivingMessage.endsWith("player one")){
                    if(!server.isPlayerOne()){
                        server.setPlayerOne(true);
                        out.writeUTF("You are player one!");
                        continue;
                    } else {
                        out.writeUTF("Player one taken!");
                        continue;
                    }
                }

                if (receivingMessage.endsWith("player two")){
                    if(!server.isPlayerTwo()){
                        server.setPlayerTwo(true);
                        out.writeUTF("You are player two!");
                        continue;
                    } else {
                        out.writeUTF("Player two is taken!");
                        continue;
                    }
                }

                out.writeUTF("Received");
                //System.out.println("Here!");
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public void sendMessage(String text){
        try {
            out.writeUTF(text);
            out.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
