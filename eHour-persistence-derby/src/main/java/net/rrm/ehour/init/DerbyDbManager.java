/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * eHour is sponsored by TE-CON  - http://www.te-con.nl/
 */

package net.rrm.ehour.init;

import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Service;

/**
 * Shutdowns the Derby database gracefully when the application goes down
 * 
 * Created on Jun 2, 2010, 7:41:56 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
@Service
public class DerbyDbManager implements ApplicationListener<ContextClosedEvent>
{
	@Autowired
	private DataSource	dataSource;

	private static final Logger LOGGER = Logger.getLogger(DerbyDbValidatorImpl.class);
	
	@Override
	public void onApplicationEvent(ContextClosedEvent event)
	{
		LOGGER.info("Application shutting down, shutting down database");
		((EmbeddedDataSource)dataSource).setShutdownDatabase("shutdown");	
	}
}
