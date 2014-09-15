package com.richemont.windchill;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.json.*;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author laurent.linck
 */
public class JsonHelper {

    private static Logger LOGGER = Logger.getLogger("ext.service.WindchillServiceImpl");
    private static boolean DEBUG = WindchillConst.DEBUG;


    /**
     *
     * @param jsonString
     * @return
     */
    public static ArrayList<ProxyWindActivity> convertJsonArrayStringToArrayList(String jsonString){
        ArrayList<ProxyWindActivity> list = new ArrayList<ProxyWindActivity>();
        StringReader sr = new StringReader( jsonString );

        log("\tString to be converted to Json:");
        log(jsonString);

        JsonReader jsonReader = Json.createReader(sr);
        JsonArray jsonArray = jsonReader.readArray();
        jsonReader.close() ;

        int j = 1;
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonActivity = jsonArray.getJsonObject(i);
            j = i+1;
            log("\tconvert String " + j + "/" + jsonArray.size() + " to Json:");
            log("\t" + jsonActivity);
            try {
                ProxyWindActivity proxyWindActivity = mapper.readValue(jsonActivity.toString() , ProxyWindActivity.class);
                list.add(proxyWindActivity);

                //mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ProxyWindctivity);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     *
     * @param jsonString
     * @return
     */
    public static ArrayList<ProxyWindActivity> convertJsonArrayStringToArray(String jsonString) {
        ArrayList<ProxyWindActivity> list = new ArrayList<ProxyWindActivity>();
        StringReader sr = new StringReader( jsonString );

        JsonReader jsonReader = Json.createReader(sr);
        JsonArray jsonArray = jsonReader.readArray();
        jsonReader.close() ;

        log("jsonArray: " + jsonArray);

        return convertJsonArrayToArray( jsonArray );
    }


    public static ArrayList<ProxyWindActivity> convertJsonArrayToArray(JsonArray jsonArray) {
        ArrayList<ProxyWindActivity> list = new ArrayList<ProxyWindActivity>();
        int j = 1;
        ObjectMapper mapper = new ObjectMapper();
        log( "\tconvertJsonArrayToArray" );
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonActivity = jsonArray.getJsonObject(i);
            j = i+1;
            log("========== " + j + "/" + jsonArray.size() + " ==========");
            try {
                log( "\t" + jsonActivity.toString() );
                ProxyWindActivity eActivity = mapper.readValue(jsonActivity.toString(), ProxyWindActivity.class);
                //log("" + eActivity);

                list.add(eActivity);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    public static String convertJsonArrayToString( JsonArray jsonArray){
        StringWriter sw = new StringWriter();
        JsonWriter jw = Json.createWriter(sw);
        jw.writeArray(jsonArray);
        jw.close();

        return sw.toString();
    }


    public static JsonArray convertArrayListToJsonArray(List<ProxyWindActivity> aList){
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (ProxyWindActivity anActivity  : aList ){
            jsonArrayBuilder.add( anActivity.toJsonObject() );
        }
        return jsonArrayBuilder.build();
    }

    /**
     *
     * @return
     */
    public static JsonArray createDummylistToSend(){

        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        List<String> listToSend = new ArrayList<String>();
        ProxyWindActivity windActivity;

        // from eHour/WindchillConst
        windActivity = new ProxyWindActivity();
        windActivity.setOrgId("monOrgId1");
        windActivity.setOrgName("monOrgName1");
        windActivity.setProjectId("monProjectId1");
        windActivity.setProjectName("monProjectName1");
        windActivity.setProjectDescription("monProjectDescription1");
        windActivity.setProjectManager("monProjectManager1");
        windActivity.setstartDate("monstartDate1");
        windActivity.setendDate("monendDate1");
        windActivity.setActivityId("monActivityId1");
        windActivity.setActivityName("monActivityName1");
        windActivity.setActivityDescription("monActivityDescription1");
        //windActivity.setParents ("");
        windActivity.setwork( new Float(1.0) );  // Float: Remaining Work = projectAllocatedHours
        windActivity.setperformedWork( new Float(8.0) ); // Float: Actual Work = projectPerformedHours
        jsonArrayBuilder.add( windActivity.toJsonObject() );

        windActivity = new ProxyWindActivity();
        windActivity.setOrgId("monOrgId2");
        windActivity.setOrgName("monOrgName2");
        windActivity.setProjectId("monProjectId2");
        windActivity.setProjectName("monProjectName2");
        windActivity.setProjectDescription("monProjectDescription2");
        windActivity.setProjectManager("monProjectManager2");
        windActivity.setstartDate("monstartDate2");
        windActivity.setendDate("monendDate2");
        windActivity.setActivityId("monActivityId2");
        windActivity.setActivityName("monActivityName2");
        windActivity.setActivityDescription("monActivityDescription2");
        //windActivity.setParents("");
        windActivity.setwork( new Float(8.0) );  // Float: Remaining Work = projectAllocatedHours
        windActivity.setperformedWork( new Float(1.0) ); // Float: Actual Work = projectPerformedHours
        jsonArrayBuilder.add(windActivity.toJsonObject());

        //EhourUtils.parseProperties(sb.toString()) ;

        return jsonArrayBuilder.build();
    }


    private static void log(String aMessage) {
        LOGGER.debug(aMessage);
    }
}
