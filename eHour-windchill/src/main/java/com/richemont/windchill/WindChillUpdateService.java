package com.richemont.windchill;

import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.List;

public interface WindChillUpdateService {
    void updateProjectLink(User user, HttpServletRequest request, List<Activity> activities) throws InvocationTargetException, RemoteException;
}
