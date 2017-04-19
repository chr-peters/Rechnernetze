import java.net.Socket;

import java.io.DataInputStream;
import java.io.IOException;

public class ServerListener extends Thread {

    private Socket serverSocket;
    private Client client;

    public ServerListener(Socket socket, Client client) {
	this.serverSocket = socket;
	this.client = client;
    }

    @Override
    public void run () {
	while ( this.client.isRunning() ) {
	    try {
		DataInputStream inFromServer = new DataInputStream(serverSocket.getInputStream());
		String message = inFromServer.readUTF();
		this.processReceivedMessage(message);
		inFromServer.close();
	    } catch (IOException e) {
		System.err.println("Could not retrieve message from server.");
	    }
	}
    }

    public void processReceivedMessage(String message) {
	System.out.println(message);
    }

}
