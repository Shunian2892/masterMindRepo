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

    private int token;

    public ClientConnection(Socket socket, ServerGui server){
        this.socket = socket;
        this.server = server;
        this.token = token;
    }

    @Override
    public void run() {

        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            while (isRunning){
                String receivingMessage = in.readUTF();
                server.sendToAllClients(receivingMessage);
                server.ta.appendText(receivingMessage + "\n");

                if(receivingMessage.equals("quit")){
                    server.removeClient(this);
                }
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
