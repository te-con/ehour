package com.richemont.windchill;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.json.JsonArray;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author laurent.linck
 */
public class SoapHelper {

    /**
     *
     *  call Windchill Web Service (via getEPProjectInfo.xml)
     *  get getEPProjectInfo
     *
     */

    private static final Logger LOGGER = Logger.getLogger(SoapHelper.class.getName());

    private static final String SOAPUtils_XSI="xsi";
    private static final String SOAPUtils_XSI_NS="http://www.w3.org/2001/XMLSchema-instance";  //XMLSCHEMA_INSTANCE_URI
    private static final String SOAPUtils_XSD_NS="http://www.w3.org/2001/XMLSchema";
    private static final String SOAPUtils_XSD="xsd"; // XMLSCHEMA_PREFIX
    private static final String SOAP_ENC_PREFIX = "SOAP-ENC";



    public static SOAPMessage requestWebService(SOAPMessage aSOAPRequest, String endPoint) throws Exception {

        SOAPMessage soapMessage = null;
        SOAPConnection connection = null;
        try {
            connection = SOAPConnectionFactory.newInstance().createConnection();
            URL aURLEndPoint = new URL(endPoint );
            soapMessage =  connection.call(aSOAPRequest, aURLEndPoint);
            connection.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return soapMessage;
    }

    /**
     *
     * @param ieDelegateName
     * @param ehourUserName
     * @param ehourAction
     * @return
     * @throws Exception
     */
    public static SOAPMessage createSOAPRequest(String ieDelegateName, String ehourUserName, String ehourAction, String auth) throws Exception {

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
        hd.addHeader("Authorization", "Basic " + auth );

        SOAPHeader header = soapMessage.getSOAPHeader();
        if (header == null) header = soapEnvelope.addHeader();

        soapMessage.getMimeHeaders().setHeader("SOAPAction", "urn:ie-soap-rpc:ext.richemont.ws!" + ieDelegateName);

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

        return soapMessage;
    }

    /**
     *
     * @param ieDelegateName
     * @param ehourUserName
     * @param ehourAction
     * @param listToSend
     * @return
     * @throws javax.xml.soap.SOAPException
     * @throws java.io.IOException
     */
    public static SOAPMessage createSOAPRequestForUpdate(String ieDelegateName, String ehourUserName, String ehourAction, List<String> listToSend, String auth  ) throws SOAPException, IOException {

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
        hd.addHeader("Authorization", "Basic " + auth );

        SOAPHeader header = soapMessage.getSOAPHeader();
        if (header == null) header = soapEnvelope.addHeader();

        soapMessage.getMimeHeaders().setHeader("SOAPAction", "urn:ie-soap-rpc:ext.richemont.ws!" + ieDelegateName);

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

        soapElement = soapBodyElement.addChildElement(soapFactory.createName("eHourActivitiesTobeUpdated"));
        soapElement.addAttribute( soapFactory.createName( "xsi:type"), "mes:ArrayOfstring");
        //soapElement.addAttribute( soapFactory.createName( "xmlns:ns2"), "http://schemas.xmlsoap.org/soap/encoding/");
        //soapElement.addAttribute( soapFactory.createName( "xsi:type"), "ns2:ArrayOfstring");
        //soapElement.addAttribute( soapFactory.createName( "ns2:arrayType"), "ns1:Activity[]");

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
     * @param ieDelegateName
     * @param ehourUserName
     * @param ehourAction
     * @param listToSend
     * @return
     * @throws javax.xml.soap.SOAPException
     * @throws java.io.IOException
     */
    public static SOAPMessage createSOAPRequestForUpdateProjectPlan(String ieDelegateName, String ehourUserName, String ehourAction,  JsonArray listToSend, String auth  ) throws SOAPException {

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
        hd.addHeader("Authorization", "Basic " + auth );

        SOAPHeader header = soapMessage.getSOAPHeader();
        if (header == null) header = soapEnvelope.addHeader();

        soapMessage.getMimeHeaders().setHeader("SOAPAction", "urn:ie-soap-rpc:ext.richemont.ws!" + ieDelegateName);

        SOAPBody soapBody = soapEnvelope.getBody();
        Name msgName = soapEnvelope.createName(ieDelegateName, "mes", "");
        SOAPBodyElement soapBodyElement = soapBody.addBodyElement(msgName);

        SOAPElement soapElement;
        soapElement = addStringToSoapElement(soapBodyElement, "ehourUsername", ehourUserName );
        soapElement = addStringToSoapElement(soapBodyElement, "ehourAction", ehourAction);
        soapElement = addArrayToSoapElement(soapBodyElement, "eHourActivitiesTobeUpdated", listToSend );

        //soapElement = soapBodyElement.addChildElement(soapFactory.createName("eHourActivitiesTobeUpdated"));
        //soapElement.addAttribute( soapFactory.createName( "type", SOAPUtils_XSI, SOAPUtils_XSI_NS), SOAP_ENC_PREFIX + ":Array") ;
        //soapElement.addAttribute( soapFactory.createName( "arrayType", SOAP_ENC_PREFIX, SOAPConstants.URI_NS_SOAP_ENCODING), SOAPUtils_XSD + ":string[]") ;
        //soapElement = createSoapElementFromArrayOfHashTables(soapElement, listToSend);

        soapMessage.saveChanges();

        return soapMessage;

    }

    /**
     *
     * @param soapBodyElement
     * @param localName     eHourActivitiesTobeUpdated
     * @param listToSend
     * @return
     * @throws javax.xml.soap.SOAPException
     */
    public static SOAPElement addArrayToSoapElement(SOAPBodyElement soapBodyElement, String localName, JsonArray listToSend) throws SOAPException {
        SOAPFactory soapFactory = SOAPFactory.newInstance();
        SOAPElement soapElement = soapBodyElement.addChildElement(soapFactory.createName("eHourActivitiesTobeUpdated"));
        soapElement.addAttribute( soapFactory.createName( "xsi:type"), "mes:ArrayOfstring");  // array: just to be compatible with other actions

        soapElement = soapBodyElement.addChildElement(soapFactory.createName("activity"));
        soapElement.addAttribute(soapFactory.createName( "xsi:type"), "xsd:string");

        soapElement.addTextNode( JsonHelper.convertJsonArrayToString(listToSend) );

        return soapElement;
    }

    public static SOAPElement addStringToSoapElement(SOAPBodyElement soapBodyElement, String localName, String theValue) throws SOAPException {
        SOAPFactory soapFactory = SOAPFactory.newInstance();
        SOAPElement soapElement = soapBodyElement.addChildElement(soapFactory.createName(localName));
        soapElement.addAttribute( soapFactory.createName( "xsi:type"), "xsd:string");  // array: just to be compatible with other actions
        soapElement.addTextNode( theValue );
        return soapElement;
    }


    public static SOAPElement createSoapElementFromSimpleArray(SOAPElement soapElement, List<String> listToSend ) throws SOAPException {
        //QName activitiesQName = new QName("eHourActivitiesTobeUpdated");
        //SOAPElement activities = soapElement.addChildElement(activitiesQName);
        SOAPFactory soapFactory = SOAPFactory.newInstance();
        for (String anActivity : listToSend) {
            // an activity
            QName childName = new QName("activity");
            SOAPElement child = soapElement.addChildElement(childName);
            child.addAttribute(soapFactory.createName( "xsi:type"), "xsd:string");
            child.addTextNode(anActivity);
        }
        return soapElement;
    }

    public static SOAPElement createSoapElementFromArrayOfHashTables(SOAPElement soapElement, List<HashMap<String,Comparable>> listToSend ) throws SOAPException {
        //QName activitiesQName = new QName("eHourActivitiesTobeUpdated");
        //SOAPElement activities = soapElement.addChildElement(activitiesQName);
        SOAPFactory soapFactory = SOAPFactory.newInstance();
        for (HashMap<String,Comparable> hm : listToSend) {

            // an activity
            QName childName = new QName("activity");
            SOAPElement anActivity = soapElement.addChildElement(childName);
            anActivity.addAttribute(soapFactory.createName( "xsi:type"), "xsd:string");
            //anActivity.addAttribute(soapFactory.createName( "xsi:type"), "mes:ArrayOfString");
            //anActivity.addAttribute( soapFactory.createName( "type", SOAPUtils_XSI, SOAPUtils_XSI_NS), SOAP_ENC_PREFIX + ":Array") ;
            //anActivity.addAttribute( soapFactory.createName( "arrayType", SOAP_ENC_PREFIX, SOAPConstants.URI_NS_SOAP_ENCODING), SOAPUtils_XSD + ":string[]") ;
            for (Map.Entry<String, Comparable> entry  : hm.entrySet()){
                String theKey = entry.getKey();
                String theClass = "";
                Comparable theValue = entry.getValue();
                if (theKey != null) theClass = hm.get(theKey).getClass().toString();
                if (hm.get(theKey) instanceof  String) theValue= (String)hm.get(theKey);
                else if (hm.get(theKey) instanceof Date) theValue= DateUtils.convertDateToString( (Date) hm.get(theKey) , WindchillConst.WIND_DATE_FORMAT);
                else theValue = "?";

                // props for the activity
                childName = new QName(theKey);
                SOAPElement aKey = anActivity.addChildElement(childName);
                aKey.addAttribute(soapFactory.createName( "xsi:type"), "xsd:string");
                aKey.addTextNode(theValue.toString());


            }
        }
        return soapElement;
    }


    /**
     * get content of a SOAP Message
     * @param iterator
     * @param indent
     */
    public static void getContents(Iterator iterator, String indent) {

        while (iterator.hasNext()) {
            javax.xml.soap.Node node = (javax.xml.soap.Node) iterator.next();
            SOAPElement element = null;
            Text text = null;
            if (node instanceof SOAPElement) {
                element = (SOAPElement)node;
                QName name = element.getElementQName();
                System.out.println(indent + "Name is " + name.toString());
                Iterator attrs = element.getAllAttributesAsQNames();
                while (attrs.hasNext()){
                    QName attrName = (QName)attrs.next();
                    System.out.println(indent + " Attribute name is " + attrName.toString());
                    System.out.println(indent + " Attribute value is " + element.getAttributeValue(attrName));
                }
                Iterator iter2 = element.getChildElements();
                getContents(iter2, indent + " ");
            } else {
                text = (Text) node;
                String content = text.getValue();
                System.out.println(indent + "Content is: " + content);
            }
        }
    }


    /**
     *
     * @param iterator
     * @param attributeName
     * @return
     */
    public static String getAttributeValue(Iterator iterator, String attributeName){
        String attrValue = searchAttribute(iterator, attributeName);
        if ( attrValue.contains(WindchillConst.VALUES_PAIR_SEPARATOR)) {
            //return attrValue.split(WindchillConst.VALUES_PAIR_SEPARATOR)[1];
            return attrValue.substring( attrValue.indexOf(WindchillConst.VALUES_PAIR_SEPARATOR)+1, attrValue.length() );
        }else{
            return null;
        }
    }


    /**
     * Recursive method
     * to search an attribute value in a xml structure
     * @param iterator
     * @param attributeName
     * @return
     */
    public static String searchAttribute(Iterator iterator, String attributeName) {
        String result = null;
        while (iterator.hasNext() && result ==null ) {
            Node node = (Node) iterator.next();
            SOAPElement element = null;
            Text text = null;
            if (node instanceof SOAPElement) {
                element = (SOAPElement)node;
                Iterator iter2 = element.getChildElements();
                result = searchAttribute(iter2, attributeName);
            } else {
                text = (Text) node;
                String content = text.getValue();
                if (content.startsWith(attributeName )) {
                    result = content;
                }
            }
        }
        return result;
    }


    public static void getItemValue (Node n ) {
        log(">> Node " + n.getNodeName() );
        // Now traverse the rest of the tree in depth-first order.
        if (n.hasChildNodes()) {
            NodeList nl = n.getChildNodes();
            for (int i=0; i<nl.getLength(); i++){
                log("----");
                getItemValue (nl.item(i) );
            }
        }
    }


    public static List <HashMap<String, Comparable>> parseResponse(SOAPBody aBody) {

        log("aBody.getNodeName() = " + aBody.getNodeName());
        List<HashMap<String, Comparable>> result = new ArrayList<HashMap<String, Comparable>>();

        Node aListGroupesDiffNode = aBody.getChildNodes().item(0); // wc:TimeSheetMgtResponse
        log("aListGroupesDiffNode.getNodeName() = " + aListGroupesDiffNode.getNodeName());

        Node aGroupArrayNode = aListGroupesDiffNode.getChildNodes().item(0); // return
        log("aGroupArrayNode.getNodeName() = " + aGroupArrayNode.getNodeName());

        Node aItemList0 = aGroupArrayNode.getChildNodes().item(1); // Elements
        log("aGroupArrayNode.getNodeName() = " + aItemList0.getNodeName());

        // Ensemble des elements
        NodeList nodeList = aItemList0.getChildNodes();

        for (int i=1; i<nodeList.getLength()+1; i++) {
            log("\nNew ELEMENT # " + i);
            // Recursively traverse each of the children.
            HashMap<String, Comparable> h = new HashMap<String, Comparable>();
            getItemValue (nodeList.item(i-1), h );
            result.add(h);
        }

        return result;
    }

    public static void getItemValue (Node n, HashMap<String, Comparable> h) {
        if (n.getNodeName().equalsIgnoreCase("#text") ){

            if (n.getNodeValue().contains("=")){
                log("Node " + n.getNodeName() + " --> value=[" + n.getNodeValue() + "]");
                String[] nameValuePair = n.getNodeValue().split(WindchillConst.VALUES_PAIR_SEPARATOR);
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


    public static SOAPMessage getSoapMessageFromString(String xml) throws SOAPException, IOException {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))));
        return message;
    }


    public static void setEnvSSL (String keyStore, String trustStore, String storePass, String storeType){
        System.getProperties().setProperty("javax.net.ssl.trustStore", trustStore );
        System.getProperties().setProperty("javax.net.ssl.keyStore", keyStore );
        System.getProperties().setProperty("javax.net.ssl.keyStorePassword", storePass );
        System.getProperties().setProperty("javax.net.ssl.keyStoreType", storeType);

        LOGGER.debug("javax.net.ssl.trustStore=" + trustStore);
        LOGGER.debug("javax.net.ssl.keyStore=" + keyStore);
        LOGGER.debug("javax.net.ssl.keyStoreType=" + storeType);
    }

    public static String getEncodedAuth(String username, String password){
        String authentication= username + ":" + password;
        String hiddenPassword = StringUtils.rightPad(password.substring(0, 1), password.length(), "*");
        LOGGER.debug( String.format("Configured windchill endpoint with user %s, password %s", username, hiddenPassword ));
        return new String(Base64.encodeBase64(authentication.getBytes()));
    }

    private static void log(String aMessage) {
        LOGGER.debug(aMessage);
    }

}

