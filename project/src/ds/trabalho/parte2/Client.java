package ds.trabalho.parte2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Logger;

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

    void register(String ip, String port) throws Exception {
        this.peer.register(ip);
        System.out.println("Current table: " + Arrays.toString(this.peer.table));

        Socket socket = new Socket(InetAddress.getByName(ip), Integer.parseInt(port));

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        out.println("register");
        out.flush();
        socket.close();
    }

    void pull(String ip, String port) throws Exception {

        Socket socket = new Socket(InetAddress.getByName(ip), Integer.parseInt(port));

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println("pull");
        out.flush();

        this.peer.recieveValues(in.readLine());
        socket.close();

    }

    void push(String ip, String port) throws Exception {

        Socket socket = new Socket(InetAddress.getByName(ip), Integer.parseInt(port));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("push");
        out.flush();

        this.peer.sendValues(out);
        socket.close();
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
                String command = scanner.next();

                switch (command) {
                    case "register":
                        System.out.print("register IP# ");
                        ip = scanner.next();
                        System.out.print("register Port# ");
                        port = scanner.next();
                        register(ip, port);
                        break;
                    case "push":
                        System.out.print("push IP# ");
                        ip = scanner.next();
                        System.out.print("push Port# ");
                        port = scanner.next();
                        push(ip, port);
                        break;
                    case "pull":
                        System.out.print("pull IP# ");
                        ip = scanner.next();
                        System.out.print("pull Port# ");
                        port = scanner.next();
                        pull(ip, port);
                        break;
                    case "pushpull":
                        System.out.print("push-pull IP# ");
                        ip = scanner.next();
                        System.out.print("push-pull Port# ");
                        port = scanner.next();
                        pull(ip, port);
                        push(ip, port);
                        break;
                    case "add":
                        this.peer.dictionary.add(scanner.next());
                        break;
                    case "get":
                        System.out.println("Current Dictionay: " + this.peer.dictionary);
                        break;
                    case "help":
                        System.out.println("List of possible commands:");
                        System.out.println("\t1-'push' : Pushes the dictionary");
                        System.out.println("\t2-'pull' : Pulls the dictionary");
                        System.out.println("\t3-'pushpull': Pulls the dictionary");
                        System.out.println("\t4-'get': Displays the current dictionary");

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