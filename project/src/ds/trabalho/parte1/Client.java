package ds.trabalho.parte1;

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

    @Override
    public void run() {
        try {
            // logger.info("client: endpoint running ...\n");
            this.isLocked = false;
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter server ip and port:");
            System.out.print("$ ");
            String server = scanner.next();
            String port = scanner.next();
            System.out.println("Enter 'token' to start with the token");
            String initToken = scanner.next();
            System.out.println("initToken: " + initToken);
            new Thread(new TokenSender(this, initToken, server, port)).start();

            // TODO Auto-generated method stub

            while (true) {
                System.out.print("$ ");
                String command = scanner.next();

                switch (command) {
                    case "lock":
                        this.isLocked = true;
                        // System.out.println("I am locked");
                        break;
                    case "unlock":
                        this.isLocked = false;
                        // System.out.println("I am unlocked");
                        break;
                    default:
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}