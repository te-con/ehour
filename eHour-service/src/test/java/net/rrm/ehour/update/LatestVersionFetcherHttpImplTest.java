package net.rrm.ehour.update;

import com.google.common.base.Optional;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class LatestVersionFetcherHttpImplTest {
    @Test
    public void should_get_latest_version_number() throws Exception {
        Server server = new Server(8100);
        server.setHandler(new AbstractHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);
                response.getWriter().println("1.2.1");
            }
        });

        server.start();

        LatestVersionFetcherHttpImpl fetcherHttp = new LatestVersionFetcherHttpImpl("http://localhost:8100/");
        Optional<String> latestVersionNumber = fetcherHttp.getLatestVersionNumber("1.3", true);

        server.stop();


        assertEquals("1.2.1", StringUtils.chomp(latestVersionNumber.get()));
    }
}
