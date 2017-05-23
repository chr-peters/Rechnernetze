import java.util.Scanner;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.SSLServerSocketFactory;

import javax.xml.bind.JAXBException;

public class Server {
    private List<Connection> connections;
    private boolean isRunning;
    private ServerSocket serverSocket;
    
    private static Server instance;
    private static final int STANDARD_PORT = 12345;

    private Server(int port) throws IOException, JAXBException{
	//Setup keystore
	System.setProperty("javax.net.ssl.keyStore", "data/rn-ssl.jks");
	//System.setProperty("javax.net.ssl.keyStorePassword", "geheim");
	System.setProperty("javax.net.ssl.keyStorePassword", "LvU1t}d.db(RLK/F");
	
	connections = new LinkedList<Connection>();
	serverSocket = SSLServerSocketFactory.getDefault().createServerSocket(port);
	isRunning = true;
	
	//listen for clients
	new Thread() {
	    @Override
	    public void run() {
		while (Server.this.isRunning) {
		    try {
			Socket clientSocket = Server.this.serverSocket.accept();
			Connection conn = new Connection(clientSocket);
			conn.start();
			connections.add(conn);
		    } catch (IOException | JAXBException e) {
			e.printStackTrace();
		    }
		}
	    }
	}.start();
    }

    public void broadcast(String message) {
	for(Connection c: this.connections) {
	    try {
		c.sendToClient(message);
	    } catch (IOException | JAXBException e) {
		e.printStackTrace();
	    }
	}
    }

    public void stop() {

    }

    //clean up all terminated connections
    public void cleanup() {
	for(Connection c: this.connections) {
	    if(!c.isRunning()) {
		this.connections.remove(c);
	    }
	}
    }

    public Map<String, Statistik> getAllStats() {
	Map<String, Statistik> res = new HashMap();
	for (Connection c: this.connections) {
	    res.put(c.toString(), c.getStats());
	}
	return res;
    }

    public static Server getInstance(int port) throws IOException, JAXBException {
	if (instance == null) {
	    instance = new Server(port);
	    return instance;
	} else {
	    return instance;
	}
    }

    public static Server getInstance() throws IOException, JAXBException {
	if (instance == null) {
	    instance = new Server(STANDARD_PORT);
	    return instance;
	} else {
	    return instance;
	}
    }

    public String getInetAddress(){
	return serverSocket.getInetAddress().getHostAddress();
    }

    public static void main(String [] args) {
	try {
	    int port = STANDARD_PORT;
	    if (args.length > 0) {
		port = Integer.parseInt(args[0]);
	    }
	    Server server = Server.getInstance(port);
	    System.out.println("Started Server running on "+server.getInetAddress()+":"+port+".");
	    Scanner sc = new Scanner(System.in);
	    sc.nextLine();
	}
	catch (IOException | JAXBException e) {
	    System.err.println("Could not start the server!");
	}
    }
}
