package ds.trabalho.parte1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class Connection implements Runnable {
    String clientAddress;
    Socket clientSocket;
    Logger logger;
    Peer peer;

    public Connection(String clientAddress, Socket clientSocket, Logger logger, Peer peer) {
        this.clientAddress = clientAddress;
        this.clientSocket = clientSocket;
        this.logger = logger;
        this.peer = peer;
    }

    @Override
    public void run() {
        /*
         * prepare socket I/O channels
         */
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            int token = Integer.parseInt(in.readLine());
            // System.out.println("Antes de receber o token: HasToken: " + peer.hasToken);
            peer.hasToken = true;

            System.out.println("Recebi o token: " + token + "\nHasToken: " + peer.hasToken);

            if (peer.hasToken) {
                peer.token = token + 1;
            }

            /*
             * close connection
             */
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}