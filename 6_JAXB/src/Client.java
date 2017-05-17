import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
 
import javax.xml.bind.JAXBException;
 
public class Client {
 
  public static void main(String[] args) throws UnknownHostException, IOException, JAXBException {
    System.out.println("client starts");
    Socket socket = new Socket("localhost", 5678);
 
    XMLSerialisation serializer = new XMLSerialisation(socket.getLocalAddress().getHostName());
 
    Scanner scanner = new Scanner(System.in);
    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
    while (scanner.hasNextLine()) {
      EchoMessage message = serializer.getNewMessage();
      message.setContent(scanner.nextLine());
      dataOutputStream.writeUTF(serializer.messageToXMLString(message));
      EchoMessage answer = serializer.xmlStringToMessage(dataInputStream.readUTF());
 
      System.out.println(answer.getSender() + ": " + answer.getContent());
    }
    scanner.close();
    socket.close();
  }
 
}
