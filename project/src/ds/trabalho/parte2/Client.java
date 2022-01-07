package ds.trabalho.parte2;

import java.util.Scanner;
import java.util.logging.Logger;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

public class Client implements Runnable {
    String host;
    Logger logger;
    Scanner scanner;
    boolean isLocked;
    Peer peer;

    public Client(String host, Logger logger, Peer peer) throws Exception {
        this.host = host;
        this.logger = logger;
        this.scanner = new Scanner(System.in);
        this.peer = peer;
    }

    void register(String ip) throws Exception {
        this.peer.register(ip);
        System.out.println("Current table: " + Arrays.toString(this.peer.table));

        System.out.print("registerPort# ");
        String port = scanner.next();
        // String port = ip; // <-------------
        // ip = "localhost"; // <-------------

        Socket socket = new Socket(InetAddress.getByName(ip), Integer.parseInt(port));

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        out.println("register");
        out.flush();
        socket.close();
    }

    void pull(String ip) throws Exception {
        System.out.print("registerPort# ");
        String port = scanner.next();
        // String port = ip; // <-------------
        // ip = "localhost"; // <-------------

        Socket socket = new Socket(InetAddress.getByName(ip), Integer.parseInt(port));

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println("pull");
        out.flush();

        String msgRecieved;
        if ((msgRecieved = in.readLine()) != null) {
            for (String a : msgRecieved.split(" "))
                this.peer.addValue(a);
        }
        socket.close();

    }

    void push(String ip) throws Exception {
        // System.out.print("registerPort# ");
        // String port = scanner.next();
        String port = ip; // <-------------
        ip = "localhost"; // <-------------

        Socket socket = new Socket(InetAddress.getByName(ip), Integer.parseInt(port));

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        out.println("push ");
        out.flush();
        socket.close();
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);
            String ip;
            while (true) {
                System.out.print("$ ");
                String command = scanner.next();

                switch (command) {
                    case "register":
                        ip = scanner.next();
                        register(ip);
                        break;
                    case "push":
                        ip = scanner.next();
                        push(ip);
                        break;
                    case "pull":
                        ip = scanner.next();
                        pull(ip);
                        break;
                    case "pushpull":
                        ip = scanner.next();
                        break;
                    case "add":
                        this.peer.dictionary.add(scanner.next());
                        break;
                    case "get":
                        System.out.println("Current Dictionay: " + this.peer.dictionary);
                        break;
                    default:
                        System.out.println("Unknown Command!");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}