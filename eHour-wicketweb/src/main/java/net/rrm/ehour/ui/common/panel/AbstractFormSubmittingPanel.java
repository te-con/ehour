/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.common.panel;

import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.event.CommonAjaxEventType;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

/**
 * Default impl of awarecontainer + panel which calls the Page to handle the 
 * ajax request
 **/

public abstract class AbstractFormSubmittingPanel<T> extends AbstractAjaxPanel<T>
{
	private static final long serialVersionUID = 1L;
	private	static final Logger LOGGER = Logger.getLogger(AbstractFormSubmittingPanel.class);


	public AbstractFormSubmittingPanel(String id)
	{
		super(id);
	}	
	
	public AbstractFormSubmittingPanel(String id, IModel<T> model)
	{
		super(id, model);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.ui.common.ajax.AjaxAwareContainer#ajaxEventReceived(net.rrm.ehour.persistence.persistence.ui.common.ajax.AjaxEvent)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		if (ajaxEvent.getEventType() == CommonAjaxEventType.SUBMIT_ERROR)
		{
			return processFormSubmitError(ajaxEvent.getTarget());
		}
		else if (ajaxEvent instanceof PayloadAjaxEvent)
		{
			PayloadAjaxEvent<AdminBackingBean> payloadEvent = (PayloadAjaxEvent<AdminBackingBean>)ajaxEvent;
			
			AdminBackingBean backingBean = payloadEvent.getPayload();
			AjaxEventType type = ajaxEvent.getEventType();
			AjaxRequestTarget target = ajaxEvent.getTarget();
			
			try
			{
				return processFormSubmit(target, backingBean, type);
			} catch (Exception e)
			{
				LOGGER.error("While trying to persist/delete", e);
				backingBean.setServerMessage(getLocalizer().getString("general.saveError", this));
				target.add(this);
				
				return false;
			}
		}

		return true;
	}
	
	/**
	 * Process form submit
	 *
     * @param backingBean
     * @param type
     * @throws Exception
	 */
	protected boolean processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception
	{
        return true;
    }

	/**
	 * Process form submit error (validation)
	 * @param target
	 */
	protected boolean processFormSubmitError(AjaxRequestTarget target)
	{
		return false;
	}
}
