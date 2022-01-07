package ds.trabalho.parte1;

import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Peer {
    String host;
    Logger logger;
    boolean hasToken;
    int token;

    public Peer(String hostname) {
        host = hostname;
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

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your ip and port:");
        String host = sc.next();
        String port = sc.next();
        Peer peer = new Peer(host);
        System.out.printf("new peer @ host=%s\n", host);
        new Thread(new Server(host, Integer.parseInt(port), peer.logger, peer)).start();
        new Thread(new Client(host, peer.logger, peer)).start();
    }
}