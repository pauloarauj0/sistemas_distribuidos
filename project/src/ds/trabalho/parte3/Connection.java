package ds.trabalho.parte3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
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
        for (int i = 0; i < this.peer.table.size(); i++)
            if (s.equals(this.peer.table.get(i).host))
                return true;
        return false;
    }

    void recieved(String msg, int clock) {
        // String[] msgSplitted = msg.split("-");
        // int clock;

        // clock = Integer.parseInt(msgSplitted[1]);
        this.peer.clock = Math.max(this.peer.clock, clock) + 1;
        boolean isBleat = false;
        if (msg.equals("bleat"))
            isBleat = true;
        else {
            String host;
            int port;
            for (int i = 0; i < this.peer.table.size(); i++) {
                try {
                    host = this.peer.table.get(i).host;
                    port = this.peer.table.get(i).port;
                    System.out.println("Message from: " + this.peer.table.get(i).port + " " + msg);

                    // !!!!!!!!!!!!!!!!!!!!!!!!!!! SUBSTITUIR !!!!!!!!!!!!!!!!!!!!!!!!!!!
                    Socket socket = new Socket(InetAddress.getByName(host), port);
                    // !!!!!!!!!!!!!!!!!!!!!!!!!!! SUBSTITUIR !!!!!!!!!!!!!!!!!!!!!!!!!!!

                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    // out.println("" + "-" + this.peer.clock);
                    // out.flush();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // }
            // // bleat to everyone
            // if (!isBleat) {
            // String host;
            // int port;
            // for (int i = 0; i < this.peer.table.size(); i++) {
            // try {
            // host = this.peer.table.get(i).host;
            // port = this.peer.table.get(i).port;

            // // !!!!!!!!!!!!!!!!!!!!!!!!!!! SUBSTITUIR !!!!!!!!!!!!!!!!!!!!!!!!!!!
            // Socket socket = new Socket(InetAddress.getByName(host), port);
            // // !!!!!!!!!!!!!!!!!!!!!!!!!!! SUBSTITUIR !!!!!!!!!!!!!!!!!!!!!!!!!!!

            // PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // out.println("bleat-" + this.peer.clock);
            // out.flush();
            // socket.close();
            // } catch (Exception e) {
            // e.printStackTrace();
            // }
            // }
        }

        System.out.println();

    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            int clock;
            String msg = in.readLine();
            // System.out.println(msg + " requested");
            // System.out.println("na merda: " + msg);
            if (msg == null) {

            }
            String[] command = msg.split("-");
            for (String s : command)
                System.out.println("Mensgame:" + s);
            switch (command[0]) {
                case "register":
                    Peer p = new Peer(this.clientAddress, Integer.parseInt(command[1]));
                    this.peer.register(p);
                    break;
                case "recieve":
                    if (isRegisterd(this.clientAddress)) {
                        msg = command[1];
                        clock = Integer.parseInt(command[2]);
                        recieved(msg, clock);
                    }
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