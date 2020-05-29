import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerGui extends Application {
    private int port = 10000;
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream in;

    public TextArea ta;

    private ArrayList<ClientConnection> connections = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception {
        ta = new TextArea();
        ta.setEditable(false);
        Scene scene = new Scene(new ScrollPane(ta),450,200);
        stage.setTitle("MasterMind");
        stage.setScene(scene);
        stage.show();

        ta.appendText("Starting mastermind server...\n");
        ta.appendText("BackLog of events and messages:\n");

        new Thread( () -> {
            try {
                this.serverSocket = new ServerSocket(port);
                boolean isRunning = true;

                while (isRunning) {

                    if (connections.size() < 3) {
                        ta.appendText("Waiting for client\n");

                        this.socket = serverSocket.accept();
                        ta.appendText("Client connected via address: " + socket.getInetAddress().getHostAddress() + "\n");

                        ClientConnection connection = new ClientConnection(socket, this);
                        connections.add(connection);
                        Thread t = new Thread(connection);
                        t.start();
                    }
                }

                this.serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendToAllClients(String text) {
        for (ClientConnection client: connections){
            client.sendMessage(text);
        }
    }

    public void removeClient(ClientConnection clientConnection){
        this.connections.remove(clientConnection);
    }

//
//    public void writeStringToSocket(Socket socket, String text) {
//
//        try {
//            socket.getOutputStream().write(text.getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    public ArrayList<MasterMind_ServerClient> getClients() {
//        return clients;
//    }


}