package net.rrm.ehour.it.driver;

import static net.rrm.ehour.it.AbstractScenario.BASE_URL;
import static net.rrm.ehour.it.AbstractScenario.Driver;
import static net.rrm.ehour.it.driver.ItUtil.findElement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CustomerManagementDriver {

    public static final ItCustomer ACTIVE_CUSTOMER = new ItCustomer("KLM", "KLM");
    public static final ItCustomer ANOTHER_ACTIVE_CUSTOMER = new ItCustomer("VU", "VU");
    public static final ItCustomer INACTIVE_CUSTOMER = new ItCustomer("CED", "CED");

    private CustomerManagementDriver() {
    }

    public static void assertCustomerManagementLoaded() {
        assertEquals("Client management", Driver.getTitle());
    }

    public static ItCustomer createActiveCustomer() {
        createCustomer(ACTIVE_CUSTOMER);

        return ACTIVE_CUSTOMER;
    }

    public static ItCustomer createAnotherActiveCustomer() {
        createCustomer(ANOTHER_ACTIVE_CUSTOMER);

        return ANOTHER_ACTIVE_CUSTOMER;
    }

    public static ItCustomer createInactiveCustomer() {
        createInactiveCustomer(INACTIVE_CUSTOMER);

        return INACTIVE_CUSTOMER;
    }

    public static void createCustomer(ItCustomer customer) {
        createCustomer(customer, true);
    }

    public static void createInactiveCustomer(ItCustomer customer) {
        createCustomer(customer, false);
    }

    private static void createCustomer(ItCustomer customer, boolean active) {
        Driver.get(BASE_URL + "/eh/admin/client");

        findElement("tabs_panel_border_greySquaredFrame_border__body_customerForm_customer.name").clear();
        findElement("tabs_panel_border_greySquaredFrame_border__body_customerForm_customer.name").sendKeys(customer.name);
        findElement("tabs_panel_border_greySquaredFrame_border__body_customerForm_customer.code").clear();
        findElement("tabs_panel_border_greySquaredFrame_border__body_customerForm_customer.code").sendKeys(customer.code);

        if (!active) {
            findElement("tabs_panel_border_greySquaredFrame_border__body_customerForm_customer.active").click();
        }

        findElement("tabs_panel_border_greySquaredFrame_border__body_customerForm_submitButton").click();

        assertTrue(findElement("tabs_panel_border_greySquaredFrame_border__body_customerForm_serverMessage").getText().matches("^[\\s\\S]*Data saved[\\s\\S]*$"));
    }

    public static class ItCustomer {
        public String name;
        public String code;

        public ItCustomer(String name, String code) {
            this.name = name;
            this.code = code;
        }
    }

}
