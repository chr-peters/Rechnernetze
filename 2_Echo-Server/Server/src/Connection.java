import java.net.Socket;

import java.io.*;

import java.util.Map;

//TODO clean up unused imports

public class Connection extends Thread{
    private Socket clientSocket;
    private boolean running;
    private Statistik stats;

    private BufferedReader inFromClient;
    private BufferedWriter outToClient;

    public Connection(Socket client) throws IOException{
	this.clientSocket = client;
	this.running = true;

	inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	outToClient = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

	stats = new Statistik();
    }

    public void sendToClient(String message) throws IOException{
	outToClient.write("IP = "+clientSocket.getInetAddress()+", Port = "+clientSocket.getPort()+"\n"+message+'\n');
	outToClient.flush();
    }

    public void close() throws IOException{
	this.running = false;
	this.inFromClient.close();
	this.outToClient.close();
	Server.getInstance().cleanup();
    }

    public boolean isRunning() {
	return this.running;
    }

    public void processMessage(String message) throws IOException{
	message.trim();
	if (message.equals("\\showstat")){
	    this.sendToClient(this.stats.toString());
	} else if(message.equals("\\showallstat")){
	    Map<String, Statistik> statmap = Server.getInstance().getAllStats();
	    this.sendToClient(statmap.toString());
	} else if(message.endsWith("\\exit")) {
	    this.sendToClient(message);
	    this.close();
	} else if(message.startsWith("/broadc")){
	    Server.getInstance().broadcast(message.replaceFirst("/broadc", "").trim());
	} else {
	    this.sendToClient(message);
	}
    }

    @Override
    public void run() {
	try {
	    //send welcome message
	    this.sendToClient("Welcome to the echo-server!");
	    while(this.running){
		String clientMessage = inFromClient.readLine();
		if(clientMessage != null) {
		    this.stats.addMessage(clientMessage);
		    this.processMessage(clientMessage);
		}
	    }
	} catch (IOException e) {
	    System.err.println("Could not send message to " + clientSocket.getInetAddress() + ':' + clientSocket.getPort() + ". Ended connection.");
	    e.printStackTrace();
	    this.running = false;
	}
    }

    public Statistik getStats() {
	return this.stats;
    }

    public String toString(){
	return clientSocket.toString();
    }
}
