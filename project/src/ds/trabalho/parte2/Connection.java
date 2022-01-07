package ds.trabalho.parte2;

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

    boolean isRegisterd(String s) {
        for (int i = 0; i < this.peer.table.length; i++)
            if (s.equals(this.peer.table[i]))
                return true;
        return false;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String msg = in.readLine();
            System.out.println(msg + " requested");
            String[] command = msg.split("\\W+");

            switch (command[0]) {
                case "register":
                    this.peer.register(clientAddress);
                    break;
                case "pull":
                    if (isRegisterd(clientAddress))
                        this.peer.sendValues(out);
                    else
                        System.out.println("Connetcion refused - (" + clientAddress + ") not registerd");
                    break;
                case "push":
                    if (isRegisterd(clientAddress))
                        this.peer.recieveValues(in.readLine());
                    else
                        System.out.println("Connetcion refused - (" + clientAddress + ") not registerd");
                    break;
                default:
                    break;
            }

            System.out.print("$ ");
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}