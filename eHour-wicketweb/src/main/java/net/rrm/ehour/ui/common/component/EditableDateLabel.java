package net.rrm.ehour.ui.common.component;

import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableMultiLineLabel;
import org.apache.wicket.model.IModel;

public class EditableDateLabel extends AjaxEditableMultiLineLabel
{
	private static final long serialVersionUID = -4192780218559385857L;

	public EditableDateLabel(String id, IModel model)
	{
		super(id, model);
	}

}
