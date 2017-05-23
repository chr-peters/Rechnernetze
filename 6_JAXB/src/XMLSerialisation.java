import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import java.io.StringReader;
import java.io.StringWriter;
 
public class XMLSerialisation {
  private ObjectFactory of;
  private JAXBContext jc;
  private Marshaller marshaller;
  private Unmarshaller unmarshaller;
  private String sender;
 
  /**
  * Erstellt ein Serialiser Objekt was ein EchoMessageObject in einen
  * XML-String serialisiert/deserialisiert
  * <p>
  * In der Methode werdern die Klassenparameter intialisiert. Ausserdem wird
  * der Fomatierte Output zur besseren Lesbarkeit eingeschaltet.
  *
  * @param sender
  *            an absolute URL giving the base location of the image
  * @see JAXBContext, marshaller, unmarshaller
  */
  public XMLSerialisation(String sender) throws JAXBException {
      this.sender = sender;

      // configurate the environment
      this.of = new ObjectFactory();
      this.jc = JAXBContext.newInstance(EchoMessage.class);
      this.marshaller = jc.createMarshaller();
      this.unmarshaller = jc.createUnmarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
  }
 
  /**
  * Serialisiert das Object in die XML Repraesentation.
  *
  * @param message
  *            Object das serialisiert werden soll
  * @return die XML-Repraesentation des Objects als String
  * @see StringWriter, Marshaller.marshall()
  */
  String messageToXMLString(EchoMessage message) throws JAXBException {
      StringWriter stringWriter = new StringWriter();
      this.marshaller.marshal(message, stringWriter);
      return stringWriter.toString();
  }
 
  EchoMessage getNewMessage() {
    EchoMessage em = of.createEchoMessage();
    em.setSender(sender);
    em.setType(EchoMessageType.DEFAULT);
    return em;
  }
 
  /**
  * Deserialisiert von eine XML-String ein Object aus dem JAXBContext
  *
  * @param xml
  *            XML-repraesentation eines ECHO-Message Objectes
  * @return EchoMessage-Object
  * @throws JAXBException
  * @see StringReader, Unmarshaller.unmarshall()
  */
  EchoMessage xmlStringToMessage(String xml) throws JAXBException {
      StringReader stringReader = new StringReader(xml);
      return (EchoMessage) this.unmarshaller.unmarshal(stringReader);
  }
 
}
