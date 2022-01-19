package ds.trabalho.parte3;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Server implements Runnable {
    String host;
    int port;
    ServerSocket server;
    Logger logger;
    Peer peer;

    public Server(String host, int port, Logger logger, Peer peer) throws Exception {
        this.host = host;
        this.port = port;
        this.logger = logger;
        this.server = new ServerSocket(port, 1, InetAddress.getByName(host));
        this.peer = peer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                try {
                    Socket client = server.accept();
                    String clientAddress = client.getInetAddress().getHostAddress();
                    // Prints out the Address of the newly created connection
                    // System.out.println("Connection from " + clientAddress + " allowed");

                    // Creates new connections and adds them to a hashmap
                    new Thread(new Connection(clientAddress, client, logger, peer)).start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}