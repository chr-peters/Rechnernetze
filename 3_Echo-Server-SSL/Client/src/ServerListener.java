import java.net.Socket;

import java.io.*;

import javax.xml.bind.JAXBException;

//TODO clean up unused imports

public class ServerListener extends Thread {

    private Socket serverSocket;
    private Client client;

    private BufferedReader inFromServer;

    XMLSerialisation serializer;

    public ServerListener(Socket socket, Client client) throws IOException, JAXBException {
	this.serverSocket = socket;
	this.client = client;
	this.serializer = new XMLSerialisation(socket.getLocalAddress().getHostName());

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
	} catch (IOException | JAXBException e) {
	    System.err.println("Could not retrieve message from server.");
	    e.printStackTrace();
	}
    }

    public void processReceivedMessage(String message) throws IOException, JAXBException{
	EchoMessage curMessage = serializer.xmlStringToMessage(message);
	System.out.println("Message from Server: "+curMessage.getContent());
	if (curMessage.getType() == EchoMessageType.EXIT) {
	    this.client.disconnect();
	}
    }

}
