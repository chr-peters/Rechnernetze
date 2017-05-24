package server;
    
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

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
	users.put(userID, receiver);
	for(Map.Entry<String, ChatClientCallbackInterface> curUser: users.entrySet()) {
	    curUser.getValue().receiveUserLogin(userID, users.keySet().toArray());
	}
	return true;
    }

    public void logout(String userID) throws RemoteException {
	users.remove(userID);
	for(Map.Entry<String, ChatClientCallbackInterface> curUser: users.entrySet()) {
	    curUser.getValue().receiveUserLogout(userID, users.keySet().toArray());
	}
    }

    public void chat(String userID, String message) throws RemoteException {
	for(Map.Entry<String, ChatClientCallbackInterface> curUser: users.entrySet()) {
	    curUser.getValue().receiveChat(userID, message);
	}
    }

    public static void main(String[] args) {
	try {
	    LocateRegistry.createRegistry(registryPort);
	    Naming.bind("rmi://localhost/chat_server", new ChatServerImpl());
	    System.out.println("ChatServer ready");
	} catch (Exception ex) {
	    ex.printStackTrace();
	    System.exit(0);
	}
    }
}
