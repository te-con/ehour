package net.rrm.ehour.ui.report.panel.criteria.quick;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class QuickQuarterRenderer extends QuickRenderer
{
	private static final long serialVersionUID = 9074669170575475399L;
	private int	currentQuarter;
	
	/**
	 * Default constructor
	 */
	public QuickQuarterRenderer()
	{
		currentQuarter = new GregorianCalendar().get(Calendar.MONTH) / 3;
	}
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
	 */
	public Object getDisplayValue(Object object)
	{
		String	label = null;
		
		if (object instanceof QuickQuarter)
		{
			QuickQuarter quickQuarter = (QuickQuarter)object;
			
			int quarter = quickQuarter.getPeriodIndex();
			
			if (currentQuarter == quarter)
			{
				label = getLocalizer().getString("report.criteria.currentQuarter", null);
			}
			else 
			{
				label = getLocalizer().getString("report.criteria.quarter", null) + (quarter + 1);
			}
		}
		
		return label;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getIdValue(java.lang.Object, int)
	 */
	public String getIdValue(Object object, int index)
	{
		return Integer.toString(index);
	}

}
