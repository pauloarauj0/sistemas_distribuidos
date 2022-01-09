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
        for (int i = 0; i < this.peer.table.length; i++)
            if (s.equals(this.peer.table[i]))
                return true;
        return false;
    }

    void recieved(String msg) {
        String[] msgSplitted = msg.split("|");
        int clock;

        clock = Integer.parseInt(msgSplitted[1]);
        this.peer.clock = Math.max(this.peer.clock, clock) + 1;

        boolean isBleat = false;
        if (!msgSplitted[0].equals("bleat"))
            isBleat = true;

        // bleat to everyone
        if (!isBleat) {
            for (String ip : this.peer.table) {
                try {
                    // !!!!!!!!!!!!!!!!!!!!!!!!!!! SUBSTITUIR !!!!!!!!!!!!!!!!!!!!!!!!!!!
                    Socket socket = new Socket(InetAddress.getByName("localhost"), Integer.parseInt(ip));
                    // !!!!!!!!!!!!!!!!!!!!!!!!!!! SUBSTITUIR !!!!!!!!!!!!!!!!!!!!!!!!!!!

                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println("bleat|" + this.peer.clock);
                    out.flush();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println();

    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String msg = in.readLine();
            // System.out.println(msg + " requested");
            String[] command = msg.split("\\W+");

            switch (command[0]) {
                case "register":
                    this.peer.register(clientAddress);
                    break;
                case "send":
                    if (isRegisterd(this.clientAddress)) {
                        recieved(in.readLine());
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