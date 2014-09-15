package com.richemont.windchill;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 *  call Windchill Web Service (via TimeSheetMgt.xml)
 *  get TimeSheets (Windchill WorkItems for a given user)
 *
 * @author laurent.linck
 * @version 02-oct-13
 *
 */
public class UpdateProjectPlan_TestUnits {

    private static Logger LOGGER = Logger.getLogger("ext.service.WindchillServiceImpl");
    private static boolean DEBUG = true;

    private static String VALUES_PAIR_SEPARATOR = WindchillConst.VALUES_PAIR_SEPARATOR;

    private static final String SOAPUtils_XSI="xsi";
    private static final String SOAPUtils_XSI_NS="http://www.w3.org/2001/XMLSchema-instance";
    private static final String SOAPUtils_XSD_NS="http://www.w3.org/2001/XMLSchema";
    private static final String SOAPUtils_XSD="xsd";


    /**
     * For test purpose only
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //int num = Integer.parseInt( args[0]);
        int num = 4;

        switch (num) {
            case 0 :
                // simple test for float calculation
                test0();
                break;
            case 1 :
                // From a dummy SOAP response, get the ProxyWindActivity
                test1();
                break;
            case 2 :
                // test HTTP updateProjectPlan from JSON input
                test2();
                break;
            case 3 :
                // test HTTP SOAP request
                test3();
                break;
            case 4 :
                // test HTTP SOAP request
                test4();
                break;
        }
    }


    public static void test3() throws Exception {
        String ehourUserName = "laurent.linck";
        String ehourAction = "updateTimeSheets";
        String endPoint = "http://grutlink.ch.rccad.net/Windchill/servlet/SimpleTaskDispatcher";
        String authentication= "wcadmin:xxxxxxx.";
        String encodedAuth = new String(Base64.encodeBase64(authentication.getBytes()));
        String ieDelegateName = WindchillConst.IE_TASK_DELEGATE_NAME_TIMESHEET_MGT; // TimeSheetMgt
        String toUpdate ="com.ptc.projectmanagement.assignment.ResourceAssignment:6632949176~19.0";

        ArrayList<String> toUpdateList = new ArrayList<String>();
        toUpdateList.add(toUpdate);

        SOAPMessage aSOAPRequest = CallWindchillWS_tests.createSOAPRequestForUpdate(ieDelegateName, ehourUserName, ehourAction, toUpdateList, encodedAuth);
        // createSOAPRequestForUpdate(String localName, String ehourUserName, String ehourAction, List<String> listToSend, String encodedAuth
        aSOAPRequest.writeTo(System.out);

        SOAPMessage aResponse = CallWindchillWS_tests.requestWebService(aSOAPRequest, endPoint);

        System.out.println(aResponse);

    }


    public static void test4() throws Exception {

        //String javaHome = System.getProperty("java.home");

        //String javaHomePath = "D:\\ptc\\windchill_10.0\\Java\\jre\\lib\\security";
        String javaHomePath = "C:\\temp";
        //String keystore = javaHomePath + "\\lib\\security\\jssecacerts";
        String keystore = javaHomePath + "\\SSL\\jssecacerts";
        String storepass= "changeit";
        String storetype= "JKS";

        String[][] props = {
                { "javax.net.ssl.trustStore", keystore, },
                { "javax.net.ssl.keyStore", keystore, },
                { "javax.net.ssl.keyStorePassword", storepass, },
                { "javax.net.ssl.keyStoreType", storetype, },
        };
        for (int i = 0; i < props.length; i++) {
            System.getProperties().setProperty(props[i][0], props[i][1]);
            System.out.println(props[i][0] + "=" +  props[i][1]);
        }

        String ehourUserName = "laurent.linck";
        String ehourAction = "updateTimeSheets";
        String endPoint = "https://grutlink.ch.rccad.net/Windchill/servlet/SimpleTaskDispatcher";
        String authentication= "wcadmin_lli:Room0Tan3819.";
        String encodedAuth = new String(Base64.encodeBase64(authentication.getBytes()));
        String ieDelegateName = WindchillConst.IE_TASK_DELEGATE_NAME_TIMESHEET_MGT; // TimeSheetMgt
        String toUpdate ="com.ptc.projectmanagement.assignment.ResourceAssignment:6632949176~19.0";

        ArrayList<String> toUpdateList = new ArrayList<String>();
        toUpdateList.add(toUpdate);

        SOAPMessage aSOAPRequest = CallWindchillWS_tests.createSOAPRequestForUpdate(ieDelegateName, ehourUserName, ehourAction, toUpdateList, encodedAuth);
        // createSOAPRequestForUpdate(String localName, String ehourUserName, String ehourAction, List<String> listToSend, String encodedAuth
        aSOAPRequest.writeTo(System.out);

        SOAPMessage aResponse = CallWindchillWS_tests.requestWebService(aSOAPRequest, endPoint);

        System.out.println(aResponse);

    }


    public static void test0(){
        float ARTIFICIAL_WORK_ADDITION = 0.1f;
        Float work = new Float("7.1");
        Float tmp = work % 1 - ARTIFICIAL_WORK_ADDITION;
        System.out.println( "" + tmp);
        if ( work % 1 - ARTIFICIAL_WORK_ADDITION >= 0 ){
            System.out.println("OK");
        } else {
            System.out.println("Add " + ARTIFICIAL_WORK_ADDITION) ;
        }
    }

    /**
     * From a dummy SOAP response, get the ProxyWindActivity
     *
     * @throws javax.xml.soap.SOAPException
     */
    public static void test1() throws SOAPException {
        SOAPMessage aResponse = createDummyJiraSoapResponse();
        //SoapHelper.parseResponse( aResponse.getSOAPBody() );

        SOAPElement soapElement;
        final Iterator<?> iter = aResponse.getSOAPBody().getChildElements();
        String jsonArrayStr = SoapHelper.getAttributeValue(iter, "jsonArrayActivities="); // attention au "=" dans la valeur de l'attribut !!
        ArrayList<ProxyWindActivity> updatedActivitiesList = JsonHelper.convertJsonArrayStringToArrayList(jsonArrayStr) ;

        String issueKey ;
        String activityId ;
        for (ProxyWindActivity aProxyWindActivity :updatedActivitiesList ){
            issueKey = aProxyWindActivity.getActivityName();
            activityId = aProxyWindActivity.getActivityUrl().toString();
            System.out.println("Need to update " + issueKey + " for activityId=" + activityId);
        }

    }


    /**
     * Create a dummy SOAP request
     * Paste a Soap request here
     * @return
     * @throws javax.xml.soap.SOAPException
     * @throws java.io.IOException
     */
    public static SOAPMessage createDummySoapJiraRequest() throws SOAPException, IOException {
        String xml = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:mes=\"http://www.ptc.com/infoengine/soap/rpc/message/\" xmlns:urn=\"http://www.ptc.com/infoengine/soap/rpc/message/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "<SOAP-ENV:Header/><SOAP-ENV:Body>\n" +
                "<mes:TimeSheetMgt>\n" +
                "<ehourUsername xsi:type=\"xsd:string\">laurent.linck</ehourUsername>\n" +
                "<ehourAction xsi:type=\"xsd:string\">updateProjectPlan</ehourAction><eHourActivitiesTobeUpdated xsi:type=\"mes:ArrayOfstring\"/>\n" +
                "<activity xsi:type=\"xsd:string\">[{\"ActivityName\":\"EVO-2384\",\"ActivityDescription\":\"01 Sub Task test lli\",\"ActivityId\":\"Not set\",\"OrgName\":\"richemont\",\"OrgId\":\"\",\"ProjectId\":\"wt.projmgmt.admin.Project2:6590790224\",\"ProjectName\":\"Exploitation Windchill BU14\",\"ProjectDescription\":\"\",\"ProjectManager\":\"\",\"startDate\":\"11-09-2013 10:40:32\",\"endDate\":\"31-03-2014 03:18:36\",\"Parents\":[{\"ActivityName\":\"EVO-1156\",\"ActivityDescription\":\"Test lli STO and Tasks\",\"ActivityId\":\"Not set\",\"OrgName\":\"richemont\",\"OrgId\":\"\",\"ProjectId\":\"wt.projmgmt.admin.Project2:6590790224\",\"ProjectName\":\"Exploitation Windchill BU14\",\"ProjectDescription\":\"\",\"ProjectManager\":\"\",\"startDate\":\"11-09-2013 10:40:32\",\"endDate\":\"31-03-2014 03:18:36\",\"Parents\":[],\"work\":16.0,\"performedWork\":0.0,\"status\":0}],\"work\":16.0,\"performedWork\":1.0,\"status\":0},{\"ActivityName\":\"EVO-2391\",\"ActivityDescription\":\"Test lli TEC01 with STO and Epic\",\"ActivityId\":\"Not set\",\"OrgName\":\"richemont\",\"OrgId\":\"\",\"ProjectId\":\"wt.projmgmt.admin.Project2:6590790224\",\"ProjectName\":\"Exploitation Windchill BU14\",\"ProjectDescription\":\"\",\"ProjectManager\":\"\",\"startDate\":\"11-09-2013 01:53:05\",\"endDate\":\"31-03-2014 03:18:22\",\"Parents\":[{\"ActivityName\":\"EVO-2388\",\"ActivityDescription\":\"Test lli Epic\",\"ActivityId\":\"Not set\",\"OrgName\":\"richemont\",\"OrgId\":\"\",\"ProjectId\":\"wt.projmgmt.admin.Project2:6590790224\",\"ProjectName\":\"Exploitation Windchill BU14\",\"ProjectDescription\":\"\",\"ProjectManager\":\"\",\"startDate\":\"11-09-2013 01:53:05\",\"endDate\":\"31-03-2014 03:18:22\",\"Parents\":[],\"work\":8.0,\"performedWork\":0.0,\"status\":0},{\"ActivityName\":\"EVO-2390\",\"ActivityDescription\":\"Test lli Sto with Epic and tasks\",\"ActivityId\":\"Not set\",\"OrgName\":\"richemont\",\"OrgId\":\"\",\"ProjectId\":\"wt.projmgmt.admin.Project2:6590790224\",\"ProjectName\":\"Exploitation Windchill BU14\",\"ProjectDescription\":\"\",\"ProjectManager\":\"\",\"startDate\":\"11-09-2013 01:53:05\",\"endDate\":\"31-03-2014 03:18:22\",\"Parents\":[{\"ActivityName\":\"EVO-2388\",\"ActivityDescription\":\"Test lli Epic\",\"ActivityId\":\"Not set\",\"OrgName\":\"richemont\",\"OrgId\":\"\",\"ProjectId\":\"wt.projmgmt.admin.Project2:6590790224\",\"ProjectName\":\"Exploitation Windchill BU14\",\"ProjectDescription\":\"\",\"ProjectManager\":\"\",\"startDate\":\"11-09-2013 01:53:05\",\"endDate\":\"31-03-2014 03:18:22\",\"Parents\":[],\"work\":8.0,\"performedWork\":0.0,\"status\":0}],\"work\":8.0,\"performedWork\":0.0,\"status\":0}],\"work\":8.0,\"performedWork\":1.0,\"status\":0},{\"ActivityName\":\"EVO-2407\",\"ActivityDescription\":\"Test lli STO only\",\"ActivityId\":\"Not set\",\"OrgName\":\"richemont\",\"OrgId\":\"\",\"ProjectId\":\"wt.projmgmt.admin.Project2:6590790224\",\"ProjectName\":\"Exploitation Windchill BU14\",\"ProjectDescription\":\"\",\"ProjectManager\":\"\",\"startDate\":\"13-09-2013 10:32:24\",\"endDate\":\"31-03-2014 03:18:35\",\"Parents\":[],\"work\":40.0,\"performedWork\":2.0,\"status\":0}]\n" +
                "</activity>\n" +
                "</mes:TimeSheetMgt>\n" +
                "</SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";
        SOAPMessage soapMsg = SoapHelper.getSoapMessageFromString(xml);
        return soapMsg;
    }

    /**
     * Create a dummy SOAP request
     * paste a soap response here
     * @return
     * @throws javax.xml.soap.SOAPException
     * @throws java.io.IOException
     */
    public static SOAPMessage createDummyJiraSoapResponse() {
        SOAPMessage soapResponse = null;
        String xml = "<SOAP-ENV:Envelope SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wc=\"http://www.ptc.com/infoengine/soap/rpc/message/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "   <SOAP-ENV:Header/>\n" +
                "   <SOAP-ENV:Body>\n" +
                "      <wc:TimeSheetMgtResponse>\n" +
                "         <return xsi:type=\"wc:INFOENGINE_GROUP\">\n" +
                "            <Name>output</Name>\n" +
                "            <Elements SOAP-ENC:arrayType=\"wc:INFOENGINE_ELEMENT[1]\">\n" +
                "               <Element>\n" +
                "                  <Attributes SOAP-ENC:arrayType=\"wc:INFOENGINE_ATTRIBUTE[1]\">\n" +
                "                     <Attribute>\n" +
                "                        <Name>jsonArrayActivities</Name>\n" +
                "                        <Value>jsonArrayActivities=[{\"ActivityName\":\"monActivity1\",\"ActivityDescription\":\"monActivityDescription1\",\"ActivityId\":\"com.ptc.projectmanagement.plan.PlanActivity:6839591165\",\"OrgName\":\"richemont\",\"OrgId\":\"\",\"ProjectId\":\"wt.projmgmt.admin.Project2:6590790224\",\"ProjectName\":\"EVO-123\",\"ProjectDescription\":\"monProjectDescription1\",\"ProjectManager\":\"monProjectManager1\",\"startDate\":\"11-09-2013 10:40:32\",\"endDate\":\"31-03-2014 03:18:36\",\"Parents\":[],\"work\":8.0,\"performedWork\":1.0,\"status\":1},{\"ActivityName\":\"monActivity2\",\"ActivityDescription\":\"monActivityDescription1\",\"ActivityId\":\"com.ptc.projectmanagement.plan.PlanActivity:6839591175\",\"OrgName\":\"richemont\",\"OrgId\":\"\",\"ProjectId\":\"wt.projmgmt.admin.Project2:6590790224\",\"ProjectName\":\"EVO-456\",\"ProjectDescription\":\"monProjectDescription1\",\"ProjectManager\":\"monProjectManager1\",\"startDate\":\"11-09-2013 10:40:32\",\"endDate\":\"31-03-2014 03:18:36\",\"Parents\":[],\"work\":8.0,\"performedWork\":1.0,\"status\":1}]</Value>\n" +
                "                     </Attribute>\n" +
                "                  </Attributes>\n" +
                "               </Element>\n" +
                "            </Elements>\n" +
                "            <Meta SOAP-ENC:arrayType=\"wc:INFOENGINE_ATTRIBUTE[2]\">\n" +
                "               <Attribute>\n" +
                "                  <Name>Status</Name>\n" +
                "                  <Value xsi:type=\"xsd:int\">0</Value>\n" +
                "               </Attribute>\n" +
                "               <Attribute>\n" +
                "                  <Name>Class</Name>\n" +
                "                  <Value>Unknown-Class-Name</Value>\n" +
                "               </Attribute>\n" +
                "            </Meta>\n" +
                "         </return>\n" +
                "      </wc:TimeSheetMgtResponse>\n" +
                "   </SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";

        try {
            soapResponse = SoapHelper.getSoapMessageFromString(xml);
        } catch (SOAPException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return soapResponse;
    }


    /**
     * test soap request and get message
     */
    public static void test2() throws Exception {
        String ehourUserName = "laurent.linck";
        String eHourAction="updateProjectPlan";

        String authentication= "wcadmin" + ":" + "password";
        String encodedAuth = new String(Base64.encodeBase64(authentication.getBytes()));
        String endpoint="https://grutlink.ch.rccad.net/Windchill/servlet/SimpleTaskDispatcher";

        //List<HashMap<String,Comparable>> listToSend = createDummyComplexlistToSend();
        JsonArray listToSend = createDummylistToSend();

        String ieDelegateName = WindchillConst.IE_TASK_DELEGATE_NAME_TIMESHEET_MGT;
        List <HashMap<String, Comparable>>  tsList = updateProjectPlan(ehourUserName, listToSend, eHourAction, encodedAuth, endpoint);
        log("QueryTimeSheets for user " + ehourUserName);
        log ("hastable: " + tsList);
    }

    /**
     ***** EHOUR *****
     * @param ehourUserName
     * @param listToSend
     * @param ehourAction
     * @return
     * @throws Exception
     */
    protected static List <HashMap<String, Comparable>> updateProjectPlan(String ehourUserName, JsonArray listToSend, String ehourAction, String auth, String endpoint ) throws Exception {

        log("QueryTimeSheets: createSOAPRequestForUpdate");
        // vire les doublons
        //listToSend = removeDouble(listToSend);

        SOAPMessage aRequest = SoapHelper.createSOAPRequestForUpdateProjectPlan(WindchillConst.IE_TASK_DELEGATE_NAME_TIMESHEET_MGT, ehourUserName, ehourAction, listToSend, auth);

        System.out.println("\n");
        aRequest.writeTo(System.out);
        System.out.println("\n");

        log ("QueryTimeSheets: requestWebService");
        SOAPMessage aResponse = null;
        aResponse = SoapHelper.requestWebService(aRequest, endpoint);

        System.out.println("SOAP Response:\n" + aResponse.getSOAPBody()); // toujours null !?!?!

        if (aResponse != null) return SoapHelper.parseResponse(aResponse.getSOAPBody());
        else return null;
    }


    public static JsonArray createDummylistToSend(){

        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        List<String> listToSend = new ArrayList<String>();
        ProxyWindActivity eActivity;

        // from eHour/WindchillConst
        eActivity = new ProxyWindActivity();
        eActivity.setOrgId("");
        eActivity.setOrgName("richemont");
        eActivity.setProjectId("wt.projmgmt.admin.Project2:6590790224");
        eActivity.setProjectName("EVO-123");
        eActivity.setProjectDescription("monProjectDescription1");
        eActivity.setProjectManager("monProjectManager1");
        eActivity.setstartDate("11-09-2013 10:40:32");
        eActivity.setendDate("31-03-2014 03:18:36");
        eActivity.setActivityId("");
        eActivity.setActivityName("");
        eActivity.setActivityDescription("monActivityDescription1");
        //eActivity.setParents("");
        eActivity.setwork( new Float(8.0) );  // Float: Remaining Work = projectAllocatedHours
        eActivity.setperformedWork( new Float(1.0) ); // Float: Actual Work = projectPerformedHours
        jsonArrayBuilder.add( eActivity.toJsonObject() );

        eActivity = new ProxyWindActivity();
        eActivity.setOrgId("");
        eActivity.setOrgName("richemont");
        eActivity.setProjectId("wt.projmgmt.admin.Project2:6590790224");
        eActivity.setProjectName("EVO-456");
        eActivity.setProjectDescription("monProjectDescription1");
        eActivity.setProjectManager("monProjectManager1");
        eActivity.setstartDate("11-09-2013 10:40:32");
        eActivity.setendDate("31-03-2014 03:18:36");
        eActivity.setActivityId("");
        eActivity.setActivityName("");
        eActivity.setActivityDescription("monActivityDescription1");
        //eActivity.setParents("");
        eActivity.setwork( new Float(8.0) );  // Float: Remaining Work = projectAllocatedHours
        eActivity.setperformedWork( new Float(1.0) ); // Float: Actual Work = projectPerformedHours
        jsonArrayBuilder.add( eActivity.toJsonObject() );

        //EhourUtils.parseProperties(sb.toString()) ;

        return jsonArrayBuilder.build();
    }


    public static List<HashMap<String,Comparable>> createDummyComplexlistToSend(){
        List<HashMap<String,Comparable>> listToSend = new ArrayList<HashMap<String,Comparable>>();

        HashMap <String, Comparable> hm = new HashMap() ;
        // from eHour/WindchillConst
        hm.put("OrgId" , "monOrgId1");
        hm.put("OrgName" , "monOrgName1");
        hm.put("ProjectId" , "monProjectId1");
        hm.put("ProjectName" , "monProjectName1");
        hm.put("ProjectDescription" , "monProjectDescription1");
        hm.put("ProjectManager" , "monProjectManager1");
        hm.put("startDate" , "monstartDate1");
        hm.put("endDate" , "monendDate1");
        hm.put("ActivityId" , "monActivityId1");
        hm.put("ActivityName" , "monActivityName1");
        hm.put("ActivityDescription" , "monActivityDescription1");
        hm.put("SummaryActivities" , "monSummaryActivities1");
        hm.put("work" , "monWork1");  // Float: Remaining Work = projectAllocatedHours
        hm.put("performedWork" , "monPerformedWork1"); // Float: Actual Work = projectPerformedHours

        listToSend.add(hm);

        hm = new HashMap() ;
        // from eHour/WindchillConst
        hm.put("OrgId" , "monOrgId2");
        hm.put("OrgName" , "monOrgName2");
        hm.put("ProjectId" , "monProjectId2");
        hm.put("ProjectName" , "monProjectName2");
        hm.put("ProjectDescription" , "monProjectDescription2");
        hm.put("ProjectManager" , "monProjectManager2");
        hm.put("startDate" , "monstartDate2");
        hm.put("endDate" , "monendDate2");
        hm.put("ActivityId" , "monActivityId2");
        hm.put("ActivityName" , "monActivityName2");
        hm.put("ActivityDescription" , "monActivityDescription2");
        hm.put("SummaryActivities" , "monSummaryActivities2");
        hm.put("work" , "monWork2");  // Float: Remaining Work = projectAllocatedHours
        hm.put("performedWork" , "monPerformedWork2") ; // Float: Actual Work = projectPerformedHours

        listToSend.add(hm);

        return listToSend;
    }

    public static void log(String aMessage) {
        if (DEBUG) System.out.print(aMessage);
        else if (LOGGER.isDebugEnabled()) LOGGER.debug(aMessage);
    }

}
