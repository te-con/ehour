package net.rrm.ehour.ui.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;
import org.wicketstuff.scriptaculous.effect.Effect;

public class FadeLabel extends Label
{
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param id
	 * @param model
	 */
	public FadeLabel(String id, IModel model)
	{
		super(id, model);
		
		this.add(new SimpleAttributeModifier("style", "color:white"));
	}

	public void startFade()
	{
		this.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5))
		{
			private static final long serialVersionUID = -625918309980847595L;

			@Override
			protected void onPostProcessTarget(final AjaxRequestTarget target)
			{
				target.appendJavascript(new Effect.Fade(FadeLabel.this).toJavascript());
			}
		});
		
	}
	
	/**
	 * 
	 * @param id
	 * @param value
	 */
	public FadeLabel(String id, String value)
	{
		this(id, new Model(value));
	}

}
