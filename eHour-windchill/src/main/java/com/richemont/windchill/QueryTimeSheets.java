package com.richemont.windchill;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.soap.SOAPMessage;
import java.util.*;


/**
 * call Windchill Web Service (via TimeSheetMgt.xml)
 * get TimeSheets (Windchill WorkItems for a given user)
 *
 * @author laurent.linck
 * @version 16-sept-13
 */
@Component
public class QueryTimeSheets {

    private static final Logger LOGGER = Logger.getLogger("ext.service.WindchillServiceImpl");

    @Autowired
    private CallWindchillWS callWindchillWS;

    @Value("${richemont.windchill.soap.endpoint}")
    private String endpoint;

    List<HashMap<String, Comparable>> getTimeSheets(String ehourUserName, String ehourAction, String endPoint) throws Exception {

        log("QueryTimeSheets: createSOAPRequest");
        SOAPMessage aRequest = callWindchillWS.createSOAPRequest("TimeSheetMgt", ehourUserName, ehourAction);

        log("QueryTimeSheets: requestWebService");
        SOAPMessage aResponse = SoapHelper.requestWebService(aRequest, endPoint );

        if (aResponse != null) return SoapHelper.parseResponse(aResponse.getSOAPBody());
        else return null;
    }


    protected SOAPMessage updateTimeSheets(String ehourUserName, List<String> listToSend, String ehourAction, String endPoint) throws Exception {
        log("QueryTimeSheets: createSOAPRequestForUpdate");
        // vire les doublons
        listToSend = removeDouble(listToSend);

        SOAPMessage aRequest = callWindchillWS.createSOAPRequestForUpdate("TimeSheetMgt", ehourUserName, ehourAction, listToSend);
        aRequest.writeTo(System.out);
        SOAPMessage aResponse = SoapHelper.requestWebService(aRequest, endPoint);
        return aResponse;
    }


    private static void log(String aMessage) {
        LOGGER.debug(aMessage);
    }


    /**
     * on vire les doublons
     *
     * @param aList
     * @return
     */
    private List<String> removeDouble(List<String> aList) {

        //Create a HashSet which allows no duplicates
        HashSet<String> hashSet = new HashSet<String>(aList);

        //Assign the HashSet to a new ArrayList
        aList = new ArrayList<String>(hashSet);

        //Ensure correct order, since HashSet doesn't
        Collections.sort(aList);

        return aList;
    }

}
