package ds.trabalho.parte2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.Scanner;
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
                Thread.sleep(5000);

                // Adds a new value to the peer dictionary
                StringBuilder result = new StringBuilder();
                URL url = new URL("http://random-word-api.herokuapp.com/word?number=2");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {
                    for (String line; (line = reader.readLine()) != null;) {
                        result.append(line);
                    }
                }
                result = result.deleteCharAt(0);
                result = result.deleteCharAt(result.length() - 1);

                String[] msg = result.toString().split(",");

                for (String m : msg)
                    peer.addValue(m);
            }
        } catch (Exception e) {
            System.out.println("errou");
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
        new Thread(new Populator(this)).start();

        try {
            FileHandler handler = new FileHandler("./" + hostname + "_peer.log", true);
            logger.addHandler(handler);
            SimpleFormatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);

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

    void sendValues(PrintWriter out) {
        for (int i = 0; i < this.dictionary.size(); i++) {
            out.print(this.dictionary.get(i) + " ");
        }
        out.flush();
    }

    void recieveValues(String newValues) {
        if (newValues != null)
            for (String a : newValues.split(" ")) {
                addValue(a);

            }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter your ip and port");
        String host = sc.next();
        String port = sc.next();

        Peer peer = new Peer(host);

        System.out.printf("new peer @ host=%s\n", host);
        new Thread(new Server(host, Integer.parseInt(port), peer.logger, peer)).start();
        new Thread(new Client(host, peer.logger, peer)).start();
    }
}