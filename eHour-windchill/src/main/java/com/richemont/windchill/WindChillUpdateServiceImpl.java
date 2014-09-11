package com.richemont.windchill;

import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wt.httpgw.GatewayAuthenticator;
import wt.method.MethodAuthenticator;
import wt.method.RemoteMethodServer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WindChillUpdateServiceImpl implements WindChillUpdateService {

    @Autowired
    private ReportAggregatedDao reportAggregatedDao;

    @Override
    public void updateProjectLink(User user, HttpServletRequest request, List<Activity> activities) throws InvocationTargetException, RemoteException {
        List<Map<String, String>> dataToSend = new ArrayList<Map<String, String>>();

        for (Activity activity : activities) {
            ActivityAggregateReportElement hoursForActivity = reportAggregatedDao.getCumulatedHoursForActivity(activity);

            Map<String, String> codeMap = new HashMap<String, String>();
            codeMap.put("ActivityId", activity.getCode());
            codeMap.put("ActivityTime", hoursForActivity.getHours().toString());
            dataToSend.add(codeMap);
        }

        Class[] argTypes = {List.class, String.class};
        Object[] args2 = {dataToSend, user.getUsername()};

        RemoteMethodServer remotemethodserver = createRemoteMethodServer(request);

        remotemethodserver.invoke("updateProjectActivity", "ext.ismts.ProjectConnection", null, argTypes, args2);
    }

    private RemoteMethodServer createRemoteMethodServer(HttpServletRequest request) {
        MethodAuthenticator auth = new GatewayAuthenticator(request);
        RemoteMethodServer remotemethodserver = RemoteMethodServer.getDefault();
        if (auth != null) {
            auth.setServer(remotemethodserver);
       }

        remotemethodserver.setAuthenticator(auth);
        return remotemethodserver;
    }
}
