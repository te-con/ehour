package net.rrm.ehour.ui.common.formguard;

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderContributor;

public abstract class GuardedAjaxLink<T> extends AjaxLink<T> implements IHeaderContributor {
    public GuardedAjaxLink(String id) {
        super(id);

       /* List<AjaxEventBehavior> behaviors = getBehaviors(AjaxEventBehavior.class);

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
        }*/
    }
}