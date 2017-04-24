import java.net.Socket;
import java.net.UnknownHostException;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import java.io.*;

//TODO clean up imports

public class Client {

    private ServerListener serverListener;
    private Socket serverSocket;
    private AtomicBoolean running;

    private BufferedWriter outToServer;

    public Client(String address, int port) throws IOException{
	this.serverSocket = new Socket(address, port);
	this.serverListener = new ServerListener(this.serverSocket, this);

	outToServer = new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream()));
	
	this.running = new AtomicBoolean(true);
	this.serverListener.start();
    }

    public void sendToServer(String message) throws IOException{
	outToServer.write(message);
	outToServer.flush();
    }

    public void disconnect() throws IOException {
	this.running.set(false);
	this.serverSocket.close();
    }

    public boolean isRunning() {
	return this.running.get();
    }

    public static void main (String args[]) {
	if(args.length < 2) {
	    System.err.println("You have to specify an address and a port!");
	    return;
	}
	Client client;
	try {
	    client = new Client(args[0], Integer.parseInt(args[1]));
	} catch (IOException  e) {
	    System.err.println("Could not connect to server.");
	    return;
	}
	try (Scanner sc = new Scanner(System.in)) {
	    while( client.isRunning() ) {
		System.out.println("Please enter a message you want to send to the server.");
		String message = sc.nextLine();
		client.sendToServer(message + '\n');
	    }
	} catch (Exception e) {
	    System.err.println("Could not send message to server. Shutting down client.");
	}
    }

}
