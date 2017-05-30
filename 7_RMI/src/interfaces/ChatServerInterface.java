package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServerInterface extends Remote {

    /**
     * Ein neuer Benutzer betritt das System. Zusammen mit dem Benutzer erhalten
     * wir auch die Callback-Information
     * 
     * @param userID
     *            Benutzername
     * @param receiver Zum Ausloesen von Clientmethoden
     * @return Gibt zurueck, ob der Login erfolgreich war
     * @throws RemoteException
     */
    boolean login(String userID, ChatClientCallbackInterface receiver) throws RemoteException;

    /**
     * Benutzer verlaesst das System
     * 
     * @param userID
     *            Benutzername
     * @throws RemoteException
     */
    void logout(String userID) throws RemoteException;

    /**
     * Benutzer verschickt eine Nachricht
     * 
     * @param userID
     *            Benutzername
     * @param message
     *            Nachricht
     * @throws RemoteException
     */
    void chat(String userID, String message) throws RemoteException;

    void privateMessage(String senderID,String receiverID, String message) throws RemoteException;
}
