package net.rrm.ehour.ui.common.formguard;

import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;

import java.util.List;

public abstract class GuardedAjaxLink<T> extends AjaxLink<T> {
    public GuardedAjaxLink(String id) {
        super(id);

        List<AjaxEventBehavior> behaviors = getBehaviors(AjaxEventBehavior.class);

        for (AjaxEventBehavior behavior : behaviors) {
            remove(behavior);

            add(new AjaxEventBehavior(behavior.getEvent()) {
                @Override
                protected void onEvent(AjaxRequestTarget target) {
                    onClick(target);
                }

                @Override
                protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                    super.updateAjaxAttributes(attributes);

                    attributes.getAjaxCallListeners().add(0, new AjaxCallListener() {
                        @Override
                        public CharSequence getPrecondition(Component component) {
                            return GuardDirtyFormUtil.PRECONDITION;
                        }
                    });

                    attributes.getAjaxCallListeners().add(new LoadingSpinnerDecorator());
                }

                @Override
                protected void onComponentTag(ComponentTag tag) {
                    if (isLinkEnabled()) {
                        super.onComponentTag(tag);
                    }
                }

            });
        }
    }
}