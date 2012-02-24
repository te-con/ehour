package net.rrm.ehour.ui.common.model;

import org.apache.wicket.Component;
import org.apache.wicket.model.StringResourceModel;

public class MessageResourceModel extends StringResourceModel {
    public MessageResourceModel(String resourceKey, Component component, Object... parameters) {
        super(resourceKey, component, null, parameters);
    }
}
