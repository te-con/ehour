package com.richemont.jira;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.naming.AuthenticationException;
import java.util.Iterator;

/**
 * @author laurent.linck
 */
public class InvokeHelper {

    private static Logger LOGGER = Logger.getLogger("ext.service.JiraServiceImpl");

    private static final String BASE_URL = JiraConst.JIRA_SERVER;
    private static final String jiraUser = JiraConst.JIRA_USER;
    private static final String jiraPwd = JiraConst.JIRA_PWD;
    private static final String AUTH;

    static {
        AUTH = new String(Base64.encode(jiraUser + ":" + jiraPwd));
    }

    protected static String invokeGetMethod(String url) throws AuthenticationException, ClientHandlerException {
        LOGGER.debug("get Jira URL: " + BASE_URL + url);
        Client client = Client.create();
        WebResource webResource = client.resource(BASE_URL + url);
        ClientResponse response = webResource.header("Authorization", "Basic " + AUTH).type("application/json")
                .accept("application/json").get(ClientResponse.class);
        int statusCode = response.getStatus();
        if (statusCode == 401) {
            throw new AuthenticationException("Invalid Username or Password");
        }
        return response.getEntity(String.class);
    }

    protected static String invokePostMethod(String url, String data) throws AuthenticationException, ClientHandlerException {
        LOGGER.debug("post Jira URL: " + BASE_URL + url);
        LOGGER.debug("post Jira data: " + data);

        Client client = Client.create();
        WebResource webResource = client.resource(BASE_URL + url);
        ClientResponse response = webResource.header("Authorization", "Basic " + AUTH).type("application/json")
                .accept("application/json").post(ClientResponse.class, data);
        int statusCode = response.getStatus();

        if (statusCode == 401) {
            throw new AuthenticationException("Invalid Username or Password");
        }
        return response.getEntity(String.class);
    }


    public static boolean invokePutMethod(String url, String data) throws AuthenticationException, ClientHandlerException {
        boolean success = true;
        LOGGER.debug("put Jira URL: " + BASE_URL + url);
        Client client = Client.create();
        WebResource webResource = client.resource(BASE_URL + url);
        ClientResponse response = webResource.header("Authorization", "Basic " + AUTH).type("application/json")
                .accept("application/json").put(ClientResponse.class, data);
        int statusCode = response.getStatus();
        if (statusCode != 200 && statusCode !=204){
            success = false;
            LOGGER.debug("" + response.getClientResponseStatus().getReasonPhrase());
            //throw new AuthenticationException("Request failed with code " + statusCode );
        }
        return success;
    }

    protected static void invokeDeleteMethod(String url) throws AuthenticationException, ClientHandlerException {
        LOGGER.debug("delete Jira URL: " + BASE_URL + url);
        Client client = Client.create();
        WebResource webResource = client.resource(BASE_URL + url);
        ClientResponse response = webResource.header("Authorization", "Basic " + AUTH).type("application/json")
                .accept("application/json").delete(ClientResponse.class);
        int statusCode = response.getStatus();
        if (statusCode == 401) {
            throw new AuthenticationException("Invalid Username or Password");
        }
    }


    protected static void getDisplayJSON(JSONObject jObject) throws JSONException {
        Iterator<?> keys = jObject.keys();
        while( keys.hasNext() ){
            String key = (String)keys.next();
            //if( jObject.get(key) instanceof JSONObject ){
            LOGGER.debug("--------------------- " + key + "=" + jObject.get(key));
            //}
        }
    }

    /**
     * don't know structure of json
     */
    protected static void hh(){
        //JsonElement root = new JsonParser().parse(jsonString);
        //String value1 = root.getAsJsonObject().get("data").getAsJsonObject().get("field1").getAsString();
    }

}
