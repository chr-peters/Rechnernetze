import java.net.Socket;

public class Connection extends Thread{
    private Socket clientSocket;
    private boolean isRunning;
    private Statistik stats;

    public Connection(Socket client) {
	this.clientSocket = client;
    }

    public void sendToClient(String message) {

    }

    @Override
    public void run() {

    }

    public Statistik getStats() {
	return this.stats;
    }
}
