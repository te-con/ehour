package com.richemont.windchill;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.soap.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


/**
 * @author laurent.linck
 *
 *  call Windchill Web Service (via TimeSheetMgt.xml)
 *  get TimeSheets (Windchill WorkItems for a given user)
 *
 */
@Component
public class CallWindchillWS {
	
	private static Logger LOGGER = Logger.getLogger("ext.service.WindchillServiceImpl");

	private static final String SOAP_VALUES_PAIR_SEPARATOR = "="; // used in the ProjectConnectionHelper.xml car la reponse soap renvoie VALUE et NAME comme 2 noeuds quelconque
	
	private static final String SOAPUtils_XSI="xsi";
	private static final String SOAPUtils_XSI_NS="http://www.w3.org/2001/XMLSchema-instance";
	private static final String SOAPUtils_XSD_NS="http://www.w3.org/2001/XMLSchema";
	private static final String SOAPUtils_XSD="xsd";

    private String encodedAuth;

    @Autowired
    public CallWindchillWS (@Value("${richemont.windchill.user}") String username,
                            @Value("${richemont.windchill.pwd}") String password,
                            @Value("${javax.net.ssl.keyStore}") String keyStore,
                            @Value("${javax.net.ssl.keyStorePassword}") String storePass,
                            @Value("${javax.net.ssl.keyStoreType}") String storeType) {

        SoapHelper.setEnvSSL(keyStore, keyStore, storePass, storeType);
        this.encodedAuth = SoapHelper.getEncodedAuth(username, password);
    }

    /**
     *  Update Activities from Ehour
     *
     * @param localName
     * @param ehourUserName
     * @param ehourAction
     * @param listToSend
     * @return
     * @throws javax.xml.soap.SOAPException
     */
    public SOAPMessage createSOAPRequestForUpdate(String localName, String ehourUserName, String ehourAction, List<String> listToSend  ) throws SOAPException, IOException {

		SOAPFactory soapFactory = SOAPFactory.newInstance();
		SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();
		SOAPEnvelope soapEnvelope = soapPart.getEnvelope();

		soapEnvelope.getHeader().detachNode();

		soapEnvelope.addNamespaceDeclaration(SOAPUtils_XSI, SOAPUtils_XSI_NS);
		soapEnvelope.addNamespaceDeclaration(SOAPUtils_XSD, SOAPUtils_XSD_NS);
		//soapEnvelope.addNamespaceDeclaration(SOAPUtils_TARGET_NAMESPACE_ID, SOAPUtils.getUrn());
		//soapEnvelope.addNamespaceDeclaration(SOAPUtils.SOAP_ENC, SOAPUtils.SOAP_ENC_NS);

		soapEnvelope.addNamespaceDeclaration("mes", "http://www.ptc.com/infoengine/soap/rpc/message/");
		soapEnvelope.addNamespaceDeclaration("urn", "http://www.ptc.com/infoengine/soap/rpc/message/");


		MimeHeaders hd = soapMessage.getMimeHeaders();
		hd.addHeader("Authorization", "Basic " + encodedAuth);

		SOAPHeader header = soapMessage.getSOAPHeader();
		if (header == null) header = soapEnvelope.addHeader();

		soapMessage.getMimeHeaders().setHeader("SOAPAction", "urn:ie-soap-rpc:ext.richemont.ws!TimeSheetMgt");

		SOAPBody soapBody = soapEnvelope.getBody();
		Name msgName = soapEnvelope.createName(localName, "mes", "");
		SOAPBodyElement soapBodyElement = soapBody.addBodyElement(msgName);

		SOAPElement soapElement = soapBodyElement.addChildElement(soapFactory.createName("ehourUsername"));
		soapElement.addAttribute(soapFactory.createName( "xsi:type"), "xsd:string");
		soapElement.addTextNode(ehourUserName);

		soapElement = soapBodyElement.addChildElement(soapFactory.createName("ehourAction"));
		soapElement.addAttribute(soapFactory.createName( "xsi:type"), "xsd:string");
		soapElement.addTextNode(ehourAction);

		soapElement = soapBodyElement.addChildElement(soapFactory.createName("eHourActivitiesTobeUpdated"));
		soapElement.addAttribute( soapFactory.createName( "xsi:type"), "mes:ArrayOfstring");
		//soapElement.addAttribute( soapFactory.createName( "soapenc:arrayType"), "xsd:string[]");

		for (String item: listToSend){
			soapElement = soapBodyElement.addChildElement(soapFactory.createName("activity"));
			soapElement.addAttribute(soapFactory.createName( "xsi:type"), "xsd:string");
			soapElement.addTextNode(item);
		}


		soapMessage.saveChanges();

        System.out.println("\n");
        soapMessage.writeTo(System.out);
        System.out.println("\n");

        return soapMessage;

	}


	/**
	 * 
	 * @param ieDelegateName (nom du service. Ex: TimeSheetMgt)
	 * @param ehourUserName
	 * @return
	 * @throws Exception
	 */
	public SOAPMessage createSOAPRequest(String ieDelegateName, String ehourUserName, String ehourAction) throws Exception {
		SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();
		SOAPEnvelope soapEnvelope = soapPart.getEnvelope();

		soapEnvelope.getHeader().detachNode();
		
		soapEnvelope.addNamespaceDeclaration(SOAPUtils_XSI, SOAPUtils_XSI_NS);
		soapEnvelope.addNamespaceDeclaration(SOAPUtils_XSD, SOAPUtils_XSD_NS);
		//soapEnvelope.addNamespaceDeclaration(SOAPUtils_TARGET_NAMESPACE_ID, SOAPUtils.getUrn());
		//soapEnvelope.addNamespaceDeclaration(SOAPUtils.SOAP_ENC, SOAPUtils.SOAP_ENC_NS);

		soapEnvelope.addNamespaceDeclaration("mes", "http://www.ptc.com/infoengine/soap/rpc/message/");
		soapEnvelope.addNamespaceDeclaration("urn", "http://www.ptc.com/infoengine/soap/rpc/message/");


		MimeHeaders hd = soapMessage.getMimeHeaders();
		hd.addHeader("Authorization", "Basic " + encodedAuth);

		SOAPHeader header = soapMessage.getSOAPHeader();
		if (header == null) header = soapEnvelope.addHeader();

		soapMessage.getMimeHeaders().setHeader("SOAPAction", "urn:ie-soap-rpc:ext.richemont.ws!TimeSheetMgt");

		SOAPBody soapBody = soapEnvelope.getBody();
		Name msgName = soapEnvelope.createName(ieDelegateName, "mes", "");
		SOAPBodyElement soapBodyElement = soapBody.addBodyElement(msgName);

		SOAPFactory soapFactory = SOAPFactory.newInstance();

		SOAPElement soapElement = soapBodyElement.addChildElement(soapFactory.createName("ehourUsername"));
		soapElement.addAttribute(soapFactory.createName( "xsi:type"), "xsd:string");
		soapElement.addTextNode(ehourUserName);

		soapElement = soapBodyElement.addChildElement(soapFactory.createName("ehourAction"));
		soapElement.addAttribute(soapFactory.createName( "xsi:type"), "xsd:string");
		soapElement.addTextNode(ehourAction);
		
		soapMessage.saveChanges();
		soapMessage.writeTo(System.out); 

		return soapMessage;
	}


	
	public void getItemValue (Node n, HashMap<String, Comparable> h) {
		if (n.getNodeName().equalsIgnoreCase("#text") ){

			if (n.getNodeValue().contains("=")){
				log("Node " + n.getNodeName() + " --> value=[" + n.getNodeValue() + "]");
				String[] nameValuePair = n.getNodeValue().split(SOAP_VALUES_PAIR_SEPARATOR);
				String name = nameValuePair[0];
				String value = nameValuePair[1];
				if (value.equalsIgnoreCase("null")) value = "";
				h.put(name, value );
			}
		}

		// Now traverse the rest of the tree in depth-first order.
		if (n.hasChildNodes()) {
			NodeList nl = n.getChildNodes();
			for (int i=0; i<nl.getLength(); i++)
				getItemValue (nl.item(i), h);
		}
	}


	private void log(String aMessage) {
		LOGGER.debug(aMessage);
	}
	
}
