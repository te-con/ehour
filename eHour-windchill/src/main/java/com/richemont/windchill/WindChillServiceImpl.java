package com.richemont.windchill;

import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Service("chillService")
public class WindChillServiceImpl implements WindChillService {
    @Override
    public void checkUserExist(String username) {
        
    }

    @Override
    public void initDataForUser(String username, HttpServletRequest request) {
        
    }

    @Override
    public List<Customer> getCustomersFromEhour() {
        return Collections.emptyList();
    }

    @Override
    public List<Activity> getProjectsFromEhour(User user) {
        return Collections.emptyList();
    }

    @Override
    public List<Activity> getAssignmentsFromEhours(int projectID, User userID) {
        return Collections.emptyList();
    }

    @Override
    public Float getBookedHours(Activity assignement, User user) {
        return 0f;
    }

    @Override
    public void updateAssignments(String username, HttpServletRequest r) {

    }
}
