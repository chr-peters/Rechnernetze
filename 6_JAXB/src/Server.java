import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
 
import javax.xml.bind.JAXBException;
 
public class Server {
 
  public static void main(String[] args) throws IOException, JAXBException {
    System.out.println("server starts");
    ServerSocket serverSocket = new ServerSocket(5678);
    Socket socket = serverSocket.accept();
 
    XMLSerialisation serializer = new XMLSerialisation(socket.getInetAddress().getHostName());
 
    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
    while (true) {
      EchoMessage message = serializer.xmlStringToMessage(dataInputStream.readUTF());
      message.setContent(message.getContent().toUpperCase());
      dataOutputStream.writeUTF(serializer.messageToXMLString(message));
    }
  }
 
}
