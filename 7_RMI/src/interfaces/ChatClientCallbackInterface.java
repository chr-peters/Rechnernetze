package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClientCallbackInterface extends Remote {

	/**
	 * Ein Benutzer hat eine Nachricht an den Server geschickt
	 */
	void receiveChat(String userID, String message) throws RemoteException;

	/**
	 * Ein neuer Benutzer hat sich beim Server angemeldet
	 */
	void receiveUserLogin(String userID, Object[] users) throws RemoteException;

	/**
	 * Ein Benutzer hat sich beim Server abgemeldet
	 */
	void receiveUserLogout(String userID, Object[] users) throws RemoteException;
}
