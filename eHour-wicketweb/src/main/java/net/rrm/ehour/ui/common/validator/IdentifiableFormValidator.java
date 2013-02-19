package net.rrm.ehour.ui.common.validator;

import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

/**
 *
 * @author thies
 *
 */
public abstract class IdentifiableFormValidator extends AbstractFormValidator implements Identifiable
{
	private static final long serialVersionUID = 9139541271023967040L;

	private String id;

	public IdentifiableFormValidator(String id) {
		this.id = id;
	}


}
