/**
 * Created on Jul 12, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.panel.report.user;

import net.rrm.ehour.domain.DomainObject;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;

/**
 * TODO 
 **/

public class UserReportDataView<RK extends DomainObject<?, ?>> extends DataView
{

	/**
	 * 
	 * @param id
	 * @param dataProvider
	 */
	public UserReportDataView(String id, IDataProvider dataProvider)
	{
		super(id, dataProvider);
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.RefreshingView#populateItem(org.apache.wicket.markup.repeater.Item)
	 */
	@Override
	protected void populateItem(Item item)
	{
		RK topNode = (RK)item.getModelObject();
		
//		dataProvider.
		
	}

}
