package ds.trabalho.parte3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Arrays;
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

    void register(String ip, String port) throws Exception {
        this.peer.register(ip);
        System.out.println("Current table: " + Arrays.toString(this.peer.table));

        try {
            Socket socket = new Socket(InetAddress.getByName(ip), Integer.parseInt(port));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println("register");
            out.flush();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendMessage(String msg) {
        String ip;
        String port = "5051";
        for (int i = 0; i < this.peer.table.length; i++) {
            ip = this.peer.table[i];
            if (ip != null)
                try {
                    Socket socket = new Socket(InetAddress.getByName(ip), Integer.parseInt(port));

                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                    out.println("send");
                    out.println(msg);
                    out.flush();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
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
                        register(ip, port);
                        break;

                    case "help":
                        System.out.println("List of possible commands:");
                        System.out.println("\tregister - registers a machine's ip on the allowed table");
                        break;
                    default:
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        sendMessage(timestamp + " " + msg);
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