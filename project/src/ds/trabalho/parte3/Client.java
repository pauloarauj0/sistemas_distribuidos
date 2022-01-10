package ds.trabalho.parte3;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.logging.Logger;

public class Client implements Runnable {
    String host;
    Logger logger;
    Scanner scanner;
    Peer peer;

    public Client(String host, Logger logger, Peer peer) throws Exception {
        this.host = host;
        this.logger = logger;
        this.scanner = new Scanner(System.in);
        this.peer = peer;
    }

    void register(Peer p) throws Exception {

        this.peer.register(p);
        // System.out.println("Current table: " + Arrays.toString(this.peer.table));

        try {
            Socket socket = new Socket(InetAddress.getByName(p.host), p.port);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println("register-" + String.valueOf(this.peer.port));
            out.flush();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendMessage(String msg) {
        String ip;
        int port;
        this.peer.clock += 1;
        System.out.println("IP:" + this.peer.table.get(0).host);
        System.out.println("PORT:" + this.peer.table.get(0).port);

        for (int i = 0; i < this.peer.table.size(); i++) {
            ip = this.peer.table.get(i).host;
            port = this.peer.table.get(i).port;
            if (ip != null)
                try {
                    Socket socket = new Socket(InetAddress.getByName(ip), port);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println("recieve" + "-" + msg + "-" + this.peer.clock);
                    out.flush();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    void recieveMessage() {

    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);
            String ip;
            String port;
            System.out.println("Type 'help' to see a list of possible commands");
            while (true) {
                System.out.print("$ ");
                String msg = scanner.nextLine();
                String[] command = msg.split(" ", 2);
                switch (command[0]) {
                    case "register":
                        System.out.print("register IP# ");
                        ip = scanner.next();
                        System.out.print("register Port# ");
                        port = scanner.next();
                        Peer p = new Peer(ip, Integer.parseInt(port));
                        // System.out.println("Host: " + p.host);
                        // System.out.println("Port: " + p.port);

                        register(p);
                        break;
                    case "send":
                        msg = scanner.nextLine();
                        sendMessage(msg);
                        break;
                    case "help":
                        System.out.println("List of possible commands:");
                        System.out.println("\tregister - registers a machine's ip on the allowed table");
                        break;
                    default:
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        // sendMessage(timestamp + " " + msg);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }

    }
}