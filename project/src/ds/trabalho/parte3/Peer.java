package ds.trabalho.parte3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Peer {
    String host;
    int port;
    Logger logger;
    LinkedList<Peer> table = new LinkedList<Peer>();
    HashMap<Integer, String> messageHistory = new HashMap<>();
    // Peer[] table = new Peer[3];
    int clock;

    public Peer(String hostname, int port) {
        this.host = hostname;
        this.port = port;
        logger = Logger.getLogger("logfile");
        try {
            FileHandler handler = new FileHandler("./" + hostname + "_peer.log", true);
            logger.addHandler(handler);
            SimpleFormatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void register(Peer p) {
        for (int i = 0; i < table.size(); i++) {
            if (table.get(i).host.equals(p.host))
                return;
        }
        // debug
        if (p.host.equals("localhost")) {
            p.host = "127.0.0.1";
        }

        table.add(p);
        System.out.println("Added " + p.host + ":" + p.port);
    }

    void getMessages() {
        List<Integer> sortedList = new ArrayList<>(messageHistory.keySet());
        Collections.sort(sortedList);
        for (Integer n : sortedList)
            System.out.println(n + ": " + messageHistory.get(n));
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter your ip and port");
        String host = sc.next();
        String port = sc.next();

        Peer peer = new Peer(host, Integer.parseInt(port));

        System.out.printf("new peer @ host=%s\n", host);
        new Thread(new Server(host, Integer.parseInt(port), peer.logger, peer)).start();
        new Thread(new Client(host, peer.logger, peer)).start();
    }
}