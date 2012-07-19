package net.rrm.ehour.ui.common.formguard;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;

/**
 * @author thies (thies@te-con.nl)
 *         Date: 12/14/10 11:54 PM
 */
public class GuardDirtyFormBehavior extends Behavior {
    private Component component;
    private IModel<String> promptModel;

    public GuardDirtyFormBehavior() {
        this(null);
    }

    public GuardDirtyFormBehavior(IModel<String> promptModel) {
        this.promptModel = promptModel;
    }

    public GuardDirtyFormBehavior setPromptModel(final IModel<String> promptModel) {
        this.promptModel = promptModel;
        return this;
    }

    @Override
    public void bind(final Component component) {
        if (!(component instanceof Form)) {
            throw new WicketRuntimeException("Behavior must be attached to a form");
        }

        // if the model needs component wrapping, wrap it here
        if (promptModel instanceof IComponentAssignedModel<?>) {
            promptModel = ((IComponentAssignedModel<String>) promptModel).wrapOnAssignment(component);
        }

        this.component = component.setOutputMarkupId(true);
    }

    @Override
    public void onComponentTag(final Component component, final ComponentTag tag) {
        if (!"form".equalsIgnoreCase(tag.getName())) {
            throw new WicketRuntimeException("Behavior must be attached to a root form, not a nested form.");
        }
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        response.renderJavaScriptReference(new PackageResourceReference(GuardDirtyFormBehavior.class, "GuardDirtyFormBehavior.js"));

        if (this.promptModel != null) {
            final String prompt = this.promptModel.getObject();
            response.renderJavaScript("wicket.guardform.prompt='" + prompt + "';", "wicket.guardform.prompt");
        }

        response.renderOnDomReadyJavaScript("wicket.guardform.init('" + this.component.getMarkupId() + "');");
    }
}