import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Connection extends Thread{
    private Socket clientSocket;
    private boolean isRunning;
    private Statistik stats;

    public Connection(Socket client) {
	this.clientSocket = client;
	this.isRunning = true;
    }

    public void sendToClient(String message) throws IOException{
	DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
	outToClient.writeUTF(message);
	outToClient.close();
    }

    @Override
    public void run() {
	try {
	    //send welcome message
	    this.sendToClient("Welcome to the echo-server!" + '\n');
	    while(this.isRunning){
		DataInputStream inFromClient = new DataInputStream(clientSocket.getInputStream());
		String clientMessage = inFromClient.readUTF();
		//echo back the message
		this.sendToClient(clientMessage + '\n');
		inFromClient.close();
	    }
	} catch (IOException e) {
	    System.err.println("Could not send message to " + clientSocket.getInetAddress() + ':' + clientSocket.getPort() + ". Ended connection.");
	    this.isRunning = false;
	}
    }

    public Statistik getStats() {
	return this.stats;
    }
}
