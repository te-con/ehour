package net.rrm.ehour.ui.login.page;

import net.rrm.ehour.appconfig.EhourSystemConfig;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.AuthUtil;
import net.rrm.ehour.user.service.UserService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class OAuthCallback extends WebPage {

    private static final Logger LOGGER = Logger.getLogger(OAuthCallback.class);

    @SpringBean
    private EhourSystemConfig config;

    @SpringBean
    private UserService userService;

    @SpringBean
    private AuthUtil authUtil;

    public OAuthCallback(PageParameters parameters) {

        StringValue error = parameters.get("error");
        StringValue errorDescription = parameters.get("error_description");

        if (!error.isEmpty()) {
            add(new Label("message", "Error: " + error + "; description: " + errorDescription));
        } else {
            StringValue code = parameters.get("code");

            try {
                String result = exchange(code.toString());
                User user = saveOauthUser(result);

                EhourWebSession session = (EhourWebSession) EhourWebSession.get();

                session.setOauth(true);
                if (session.signIn(user.getUsername(), "")) {
                    add(new Label("message", "redirecting..."));
                    redirectToHomepage(session);
                } else {
                    throw new RuntimeException("unable to login");
                }
            } catch (IOException | JSONException | ObjectNotUniqueException e) {
                add(new Label("message", e.getMessage()));
                e.printStackTrace();
            }
        }
    }

    private String exchange(String code) throws IOException, JSONException, ObjectNotUniqueException {

        String clientID = config.getOauthClientID();
        String clientSecret = config.getOauthClientSecuret();

        String url = config.getOauthTokenURI();
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);

        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("client_id", clientID));
        params.add(new BasicNameValuePair("client_secret", clientSecret));
        params.add(new BasicNameValuePair("code", code));
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));
        params.add(new BasicNameValuePair("redirect_uri", config.getOauthHostURL() + config.getOauthCallbackURI()));

        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            InputStream in = entity.getContent();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(in));

                StringBuilder result = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } finally {
                in.close();
            }
        }

        throw new RuntimeException("no response");
    }

    private User saveOauthUser(String result) throws JSONException, ObjectNotUniqueException {

        JSONObject jsonResponse = new JSONObject(result);

        System.out.println(result.toString());
        boolean success = jsonResponse.getBoolean("ok");

        if (!success) {
            LOGGER.error(result);
            throw new RuntimeException("exchange token failed, check log");
        }

        JSONObject user = jsonResponse.getJSONObject("user");
        String name = user.getString("name");
        String email = user.getString("email");
        User u = userService.getUser(email);
        if (u == null) {
            u = new User();
            u.setLastName(name);
            u.setEmail(email);
            u.setUsername(email);
            u.addUserRole(UserRole.USER);
//            u.addUserRole(UserRole.REPORT);
            userService.persistNewUser(u, email);
        }
        return u;
    }


    private void redirectToHomepage(EhourWebSession session) {
        AuthUtil.Homepage homepage = authUtil.getHomepageForRole(session.getRoles());
        setResponsePage(homepage.homePage, homepage.parameters);
    }
}
