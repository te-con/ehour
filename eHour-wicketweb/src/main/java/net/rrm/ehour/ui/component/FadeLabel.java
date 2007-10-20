package net.rrm.ehour.ui.component;

import net.rrm.ehour.ui.util.HtmlUtil;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;

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

		this.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5))
		{
			private static final long serialVersionUID = -625918309980847595L;

			@Override
			protected void onPostProcessTarget(final AjaxRequestTarget target)
			{
				setModel(new Model(HtmlUtil.HTML_NBSP));
				target.addComponent(FadeLabel.this);
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
