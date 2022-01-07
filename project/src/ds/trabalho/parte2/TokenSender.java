// package ds.trabalho.parte2;

// import java.io.PrintWriter;
// import java.net.InetAddress;
// import java.net.Socket;

// public class TokenSender implements Runnable {
// Client client;
// String initToken;
// String server;
// String port;

// public TokenSender(Client client, String server, String port) {
// this.client = client;
// this.server = server;
// this.port = port;
// }

// @Override
// public void run() {
// while (true) {
// try {
// Thread.sleep(1000);

// if (initToken.equals("token")) {
// this.client.peer.hasToken = true;
// System.out.println("I have the token");
// initToken = "";
// }

// if ((!this.client.isLocked) && this.client.peer.hasToken) {

// Socket socket = new Socket(InetAddress.getByName(server),
// Integer.parseInt(port));

// PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

// out.println(this.client.peer.token);
// out.flush();
// this.client.peer.hasToken = false;
// socket.close();
// } else if (this.client.peer.hasToken && this.client.isLocked) {
// System.out.println("Locked the token " + this.client.peer.token);
// }

// } catch (Exception e) {
// e.printStackTrace();
// }
// }
// }
// }