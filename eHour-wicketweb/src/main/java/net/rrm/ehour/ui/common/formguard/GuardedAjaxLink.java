package net.rrm.ehour.ui.common.formguard;

import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;

import java.util.List;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 1/9/11 - 3:17 PM
 */
public abstract class GuardedAjaxLink<T> extends AjaxLink<T>
{
    public GuardedAjaxLink(String id)
    {
        super(id);

        List<AjaxEventBehavior> behaviors = getBehaviors(AjaxEventBehavior.class);

        for (AjaxEventBehavior behavior : behaviors)
        {
            remove(behavior);

            add(new AjaxEventBehavior(behavior.getEvent())
            {
                @Override
                protected void onEvent(AjaxRequestTarget target)
                {
                    onClick(target);
                }

                @Override
                protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                    super.updateAjaxAttributes(attributes);

                    attributes.getAjaxCallListeners().add(new LoadingSpinnerDecorator());
                }

                @Override
                protected void onComponentTag(ComponentTag tag)
                {
                    if (isLinkEnabled())
                    {
                        super.onComponentTag(tag);
                    }
                }

                @Override
                protected CharSequence getEventHandler()
                {
                    CharSequence handler = super.getEventHandler();

                    return GuardDirtyFormUtil.getEventHandler(handler);
                }
            });
        }
    }
}