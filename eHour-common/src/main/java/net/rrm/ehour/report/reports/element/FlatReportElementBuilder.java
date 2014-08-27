package net.rrm.ehour.report.reports.element;

import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;

public class FlatReportElementBuilder {
    public static FlatReportElement buildFlatReportElement(Activity activity) {
        FlatReportElement element = new FlatReportElement();

        element.setActivityId(activity.getId());
        element.setActivityName(activity.getName());

        Project project = activity.getProject();
        Customer customer = project.getCustomer();

        element.setCustomerCode(customer.getCode());
        element.setCustomerId(customer.getCustomerId());
        element.setCustomerName(customer.getName());

        element.setEmptyEntry(true);
        element.setProjectCode(project.getProjectCode());
        element.setProjectId(project.getProjectId());
        element.setProjectName(project.getName());

        User user = activity.getAssignedUser();
        element.setUserId(user.getUserId());
        element.setUserFirstName(user.getFirstName());
        element.setUserLastName(user.getLastName());

        element.setDisplayOrder(1);

        return element;
    }
}
