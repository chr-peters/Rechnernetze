import java.net.Socket;

import java.io.*;

//TODO clean up unused imports

public class ServerListener extends Thread {

    private Socket serverSocket;
    private Client client;

    private BufferedReader inFromServer;

    public ServerListener(Socket socket, Client client) throws IOException {
	this.serverSocket = socket;
	this.client = client;

	inFromServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
    }

    @Override
    public void run () {
	try {
	    while ( this.client.isRunning() ) {
		String message = inFromServer.readLine();
		if (message != null) {
		    this.processReceivedMessage(message);
		}
	    }
	} catch (IOException e) {
	    System.err.println("Could not retrieve message from server.");
	    e.printStackTrace();
	}
    }

    public void processReceivedMessage(String message) throws IOException{
	System.out.println("Message from Server: "+message);
	if (message.endsWith("\\exit")) {
	    this.client.disconnect();
	}
    }

}
