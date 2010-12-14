package net.rrm.ehour.ui.common.formguard;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;

/**
 * @author thies (thies@te-con.nl)
 *         Date: 12/14/10 11:54 PM
 */
public class GuardDirtyFormBehavior extends AbstractBehavior
{

    private Component component;

    private IModel<String> promptModel = null;

    public GuardDirtyFormBehavior setPromptModel(final IModel<String> promptModel)
    {
        this.promptModel = promptModel;
        return this;
    }

    @Override
    public void bind(final Component component)
    {
        if (!(component instanceof Form))
        {
            throw new WicketRuntimeException("Behavior must be attached to a form");
        }
        // if the model needs component wrapping, wrap it here
        if (promptModel instanceof IComponentAssignedModel<?>)
        {
            promptModel = ((IComponentAssignedModel<String>) promptModel).wrapOnAssignment(component);
        }
        this.component = component.setOutputMarkupId(true);
    }

    @Override
    public void onComponentTag(final Component component, final ComponentTag tag)
    {
        if (!"form".equalsIgnoreCase(tag.getName()))
        {
            throw new WicketRuntimeException("Behavior must be attached to a root form, not a nested form.");
        }
    }

    @Override
    public void renderHead(final IHeaderResponse response)
    {
        response.renderJavascriptReference(new JavascriptResourceReference("GuardDirtyFormBehavior.js"));
        if (this.promptModel != null)
        {
            final String prompt = this.promptModel.getObject();
            response.renderJavascript("wicket.behaviors.guardform.prompt='"
                    + prompt + "';", "wicket.behaviors.guardform.prompt");
        }
        response.renderOnDomReadyJavascript("wicket.behaviors.guardform.init('"
                + this.component.getMarkupId() + "');");
    }

}