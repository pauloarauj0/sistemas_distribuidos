package ds.trabalho.parte2;

import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

class Populator implements Runnable {
    Peer peer;

    Populator(Peer peer) {
        this.peer = peer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(2000);
                // Adds a new value to the peer dictionary
                peer.dictionary.add(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class Peer {
    String host;
    Logger logger;
    String[] table = new String[5];
    LinkedList<String> dictionary = new LinkedList<String>();

    public Peer(String hostname) {
        host = hostname;
        logger = Logger.getLogger("logfile");
        try {
            FileHandler handler = new FileHandler("./" + hostname + "_peer.log", true);
            logger.addHandler(handler);
            SimpleFormatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);

            // new Thread(new Populator<T>(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void register(String s) {
        for (int i = 0; i < table.length; i++) {
            if (s.equals(table[i]))
                return;
            if (table[i] == null) {
                table[i] = s;
                return;
            }
        }
    }

    void addValue(String s) {
        if (!dictionary.contains(s))
            dictionary.add(s);
    }

    public static void main(String[] args) throws Exception {
        Peer peer = new Peer(args[0]);

        System.out.printf("new peer @ host=%s\n", args[0]);
        new Thread(new Server(args[0], Integer.parseInt(args[1]), peer.logger, peer)).start();
        new Thread(new Client(args[0], peer.logger, peer)).start();
    }
}