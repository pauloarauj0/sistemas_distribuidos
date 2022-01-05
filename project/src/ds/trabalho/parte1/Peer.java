package ds.trabalho.parte1;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Peer {
    String host;
    Logger logger;
    boolean hasToken;

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

        Peer peer = new Peer(args[0]);
        System.out.printf("new peer @ host=%s\n", args[0]);
        new Thread(new Server(args[0], Integer.parseInt(args[1]), peer.logger, peer)).start();
        new Thread(new Client(args[0], peer.logger, peer)).start();
    }
}