package server;
    
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import interfaces.*;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServerInterface {

	private static final long serialVersionUID = 1L;
	private static final int registryPort = 1099;

	// Hier speichern wir die Callbacks!
	private Map<String, ChatClientCallbackInterface> users;

	private ChatServerImpl() throws RemoteException {
		super();
		HashMap<String, ChatClientCallbackInterface> callbackHashMap = new HashMap<>();
		users = Collections.synchronizedMap(callbackHashMap);
	}

	public boolean login(String userID, ChatClientCallbackInterface receiver) throws RemoteException {
		return false;
		// TODO Alle informieren
	}

	public void logout(String userID) throws RemoteException {
		// TODO Alle informieren
	}

	public void chat(String userID, String message) throws RemoteException {
		// TODO: Alle die Chatnachricht empfangen lassen
	}

	public static void main(String[] args) {
		try {
			// TODO: Registry, Binding
			System.out.println("ChatServer ready");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}
}
