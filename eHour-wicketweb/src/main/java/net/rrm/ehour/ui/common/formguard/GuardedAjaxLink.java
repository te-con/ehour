package net.rrm.ehour.ui.common.formguard;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderContributor;

import java.util.List;

public abstract class GuardedAjaxLink<T> extends AjaxLink<T> implements IHeaderContributor {
    public GuardedAjaxLink(String id) {
        super(id);
    }

    @Override
    protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
        super.updateAjaxAttributes(attributes);

        List<IAjaxCallListener> listeners = attributes.getAjaxCallListeners();
        listeners.add(new GuardFormListener());
    }

    private class GuardFormListener extends AjaxCallListener {
        @Override
        public CharSequence getSuccessHandler(Component component) {
            return "guardForm();";
        }
    }
}