package ds.trabalho.parte3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    void sendBleat(int clock) {
        // System.out.println("Current Peer clock:" + this.peer.clock);
        // System.out.println("Clock Recieved:" + clock);
        this.peer.clock = Math.max(this.peer.clock, clock) + 1;

        String host;
        int port;
        for (int i = 0; i < this.peer.table.size(); i++) {
            try {
                host = this.peer.table.get(i).host;
                port = this.peer.table.get(i).port;
                Socket socket = new Socket(InetAddress.getByName(host), port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println("bleat" + '-' + this.peer.clock);
                out.flush();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void recieve(int clock, String msg) {
        if (!msg.equals("bleat")) {
            sendBleat(clock);
        }

        this.peer.messageHistory.put(clock, msg);

        while (!this.peer.messageHistory.isEmpty()) {
            List<Integer> sortedList = new ArrayList<>(this.peer.messageHistory.keySet());
            Collections.sort(sortedList);

            for (Integer n : sortedList) {
                System.out.println(n + ": " + this.peer.messageHistory.get(n));
                this.peer.messageHistory.remove(n);
            }

        }
        // System.out.println(clock + " - " + msg);
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String msg = in.readLine();
            String[] command = msg.split("-");
            // System.out.println(Arrays.toString(command));
            // for (String s : command)
            // System.out.println("Mensagem: " + s);
            switch (command[0]) {
                case "register":
                    Peer p = new Peer(command[1], Integer.parseInt(command[2]));
                    this.peer.register(p);
                    break;
                case "recieve":
                    if (isRegisterd(this.clientAddress)) {
                        int clock = Integer.parseInt(command[1]);
                        msg = command[2];
                        recieve(clock, msg);

                    }
                    break;
                case "bleat":
                    this.peer.clock = Math.max(this.peer.clock, Integer.parseInt(command[1])) + 1;
                    break;
                default:
                    break;
            }

            // System.out.print("$ ");
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}