import java.net.Socket;

import java.io.*;

import java.util.Map;

//TODO clean up unused imports

public class Connection extends Thread{
    private Socket clientSocket;
    private boolean isRunning;
    private Statistik stats;

    private BufferedReader inFromClient;
    private BufferedWriter outToClient;

    public Connection(Socket client) throws IOException{
	this.clientSocket = client;
	this.isRunning = true;

	inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	outToClient = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

	stats = new Statistik();
    }

    public void sendToClient(String message) throws IOException{
	outToClient.write(message);
	outToClient.flush();
    }

    public void processMessage(String message) throws IOException{
	if(message.charAt(0) == '\\'){
	    if (message.equals("\\showstat")){
		this.sendToClient(this.stats.toString()+'\n');
	    } else if(message.equals("\\showallstat")){
		Map<String, Statistik> statmap = Server.getInstance().getAllStats();
		this.sendToClient(statmap.toString()+'\n');
	    }
	} else {
	    this.sendToClient(message + '\n');
	}
    }

    @Override
    public void run() {
	try {
	    //send welcome message
	    this.sendToClient("Welcome to the echo-server!" + '\n');
	    while(this.isRunning){
		String clientMessage = inFromClient.readLine();
		if(clientMessage != null) {
		    this.stats.addMessage(clientMessage);
		    this.processMessage(clientMessage);
		}
	    }
	} catch (IOException e) {
	    System.err.println("Could not send message to " + clientSocket.getInetAddress() + ':' + clientSocket.getPort() + ". Ended connection.");
	    e.printStackTrace();
	    this.isRunning = false;
	}
    }

    public Statistik getStats() {
	return this.stats;
    }

    public String toString(){
	return clientSocket.toString();
    }
}
