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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Returns derby validator if selected database is derby, otherwise create a dummy implementation
 * Created on Jun 2, 2010, 7:31:45 PM
 * 
 * @author Thies Edeling (thies@te-con.nl)
 * 
 */
@Configuration
public class DerbyDbValidatorConfiguration
{
	private @Value("${ehour.database}") String databaseName;
	
	private static final DerbyDbValidator DUMMY_VALIDATOR = new DerbyDbValidator()
	{
		@Override
		public void checkDatabaseState()
		{
			
		}
	};
	

	@Bean
	public DerbyDbValidator createDerbyDbValidator()
	{
		if ("derby".equalsIgnoreCase(databaseName))
		{
			return new DerbyDbValidatorImpl();
		} else
		{
			return DUMMY_VALIDATOR;
		}

	}
}
