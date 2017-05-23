import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocketFactory;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import java.io.*;

import javax.xml.bind.JAXBException;

//TODO clean up imports

public class Client {

    private ServerListener serverListener;
    private Socket serverSocket;
    private AtomicBoolean running;

    XMLSerialisation serializer;

    private BufferedWriter outToServer;

    public Client(String address, int port) throws IOException, JAXBException{

	//setup truststore
	System.setProperty("javax.net.ssl.trustStore", "data/truststore.jks");
	System.setProperty("javax.net.ssl.trustStorePassword", "LvU1t}d.db(RLK/F");

	this.serverSocket = SSLSocketFactory.getDefault().createSocket(address, port);
	this.serverListener = new ServerListener(this.serverSocket, this);

	outToServer = new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream()));

	serializer = new XMLSerialisation(serverSocket.getLocalAddress().getHostName());
	
	this.running = new AtomicBoolean(true);
	this.serverListener.start();
    }

    public void sendToServer(String message) throws IOException, JAXBException{
	EchoMessage curMessage = createMessage(message);
	outToServer.write(serializer.messageToXMLString(curMessage)+'\n');
	outToServer.flush();
    }

    public void disconnect() throws IOException {
	this.running.set(false);
	this.serverSocket.close();
    }

    public boolean isRunning() {
	return this.running.get();
    }

    public EchoMessage createMessage(String message) throws JAXBException{
	EchoMessageType messageType;
	message.trim();
	if (message.equals("\\showstat")){
	    messageType = EchoMessageType.SHOWSTAT;
	} else if(message.equals("\\showallstat")){
	    messageType = EchoMessageType.SHOWALLSTAT;
	} else if(message.startsWith("/broadc")){
	    messageType = EchoMessageType.BROADCAST;
	} else if(message.endsWith("\\exit")) {
	    messageType = EchoMessageType.EXIT;
	} else {
	    messageType = EchoMessageType.DEFAULT;
	}
	EchoMessage curMessage = serializer.getNewMessage();
	curMessage.setType(messageType);
	curMessage.setContent(message);
	return curMessage;
    }

    public static void main (String args[]) {
	if(args.length < 2) {
	    System.err.println("You have to specify an address and a port!");
	    return;
	}
	Client client;
	try {
	    client = new Client(args[0], Integer.parseInt(args[1]));
	} catch (IOException | JAXBException  e) {
	    System.err.println("Could not connect to server.");
	    e.printStackTrace();
	    return;
	}
	try (Scanner sc = new Scanner(System.in)) {
	    while( client.isRunning() ) {
		System.out.println("Please enter a message you want to send to the server.");
		String message = sc.nextLine();
		if (client.isRunning()) {
		    client.sendToServer(message);
		}
	    }
	} catch (Exception e) {
	    System.err.println("Could not send message to server. Shutting down client.");
	}
    }

}
