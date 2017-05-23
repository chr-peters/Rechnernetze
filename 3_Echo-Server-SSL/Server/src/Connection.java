import java.net.Socket;

import java.io.*;

import java.util.Map;

import javax.xml.bind.JAXBException;

//TODO clean up unused imports

public class Connection extends Thread{
    private Socket clientSocket;
    private boolean running;
    private Statistik stats;

    private BufferedReader inFromClient;
    private BufferedWriter outToClient;

    XMLSerialisation serializer;

    public Connection(Socket client) throws IOException, JAXBException{
	this.clientSocket = client;
	this.running = true;

	inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	outToClient = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

	serializer = new XMLSerialisation(clientSocket.getInetAddress().getHostName());

	stats = new Statistik();
    }

    public void sendToClient(String message) throws IOException, JAXBException{
	EchoMessage curMessage = serializer.getNewMessage();
	curMessage.setContent(message);
	outToClient.write(serializer.messageToXMLString(curMessage)+'\n');
	outToClient.flush();
    }

    public void sendToClient(EchoMessage message) throws IOException, JAXBException{
	outToClient.write(serializer.messageToXMLString(message)+'\n');
	outToClient.flush();
    }

    public void close() throws IOException, JAXBException{
	this.running = false;
	this.inFromClient.close();
	this.outToClient.close();
	Server.getInstance().cleanup();
    }

    public boolean isRunning() {
	return this.running;
    }

    public void processMessage(EchoMessage message) throws IOException, JAXBException{
	// message.trim();
	// if (message.equals("\\showstat")){
	//     this.sendToClient(this.stats.toString());
	// } else if(message.equals("\\showallstat")){
	//     Map<String, Statistik> statmap = Server.getInstance().getAllStats();
	//     this.sendToClient(statmap.toString());
	// } else if(message.startsWith("/broadc")){
	//     Server.getInstance().broadcast(message.replaceFirst("/broadc", "").trim());
	// } else if(message.endsWith("\\exit")) {
	//     this.sendToClient(message);
	//     this.close();
	// } else {
	//     this.sendToClient(message);
	// }
	switch (message.getType()) {
	case DEFAULT:
	    this.sendToClient(message.getContent());
	    break;
	case BROADCAST:
	    Server.getInstance().broadcast(message.getContent().replaceFirst("/broadc", "").trim());
	    break;
	case EXIT:
	    EchoMessage tmp = serializer.getNewMessage();
	    tmp.setType(EchoMessageType.EXIT);
	    tmp.setContent(message.getContent());
	    this.sendToClient(tmp);
	    this.close();
	    break;
	case SHOWSTAT:
	    this.sendToClient(this.stats.toString());
	    break;
	case SHOWALLSTAT:
	    Map<String, Statistik> statmap = Server.getInstance().getAllStats();
	    this.sendToClient(statmap.toString());
	    break;
	default: break;
	}
    }

    @Override
    public void run() {
	try {
	    //send welcome message
	    this.sendToClient("Welcome to the echo-server!");
	    while(this.running){
		EchoMessage clientMessage = serializer.xmlStringToMessage(inFromClient.readLine());
		if(clientMessage != null) {
		    this.stats.addMessage(clientMessage.getContent());
		    this.processMessage(clientMessage);
		}
	    }
	} catch (IOException | JAXBException e) {
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
