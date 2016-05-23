package net.rrm.ehour.ui.common.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Feb 9, 2010 6:26:27 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class AjaxEventHook implements AjaxEventListener
{
	public List<AjaxEvent> events = new ArrayList<>();
	
	public Boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		events.add(ajaxEvent);
		return true;
	}
	
}