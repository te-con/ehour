package net.rrm.ehour.ui.common.formguard;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * @author thies (thies@te-con.nl)
 *         Date: 12/14/10 11:54 PM
 */
public class GuardDirtyFormBehavior extends Behavior implements IHeaderContributor {
    private Component component;
    private IModel<String> promptModel;

    public GuardDirtyFormBehavior(IModel<String> promptModel) {
        this.promptModel = promptModel;
    }

//    @Override
//    public void bind(final Component component) {
//        if (!(component instanceof Form)) {
//            throw new WicketRuntimeException("Behavior must be attached to a form");
//        }
//
//        // if the model needs component wrapping, wrap it here
//        if (promptModel instanceof IComponentAssignedModel<?>) {
//            promptModel = ((IComponentAssignedModel<String>) promptModel).wrapOnAssignment(component);
//        }
//
//        this.component = component.setOutputMarkupId(true);
//    }
//
//    @Override
//    public void onComponentTag(final Component component, final ComponentTag tag) {
//        if (!"form".equalsIgnoreCase(tag.getName())) {
//            throw new WicketRuntimeException("Behavior must be attached to a root form, not a nested form.");
//        }
//    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(JavaScriptReferenceHeaderItem.forReference(new JavaScriptResourceReference(GuardedAjaxLink.class, "jquery.dirtyforms.js")));

        response.render(new OnDomReadyHeaderItem("$('form').dirtyForms();"));
        response.render(new OnDomReadyHeaderItem("$.DirtyForms.debug = true;"));
    }
}