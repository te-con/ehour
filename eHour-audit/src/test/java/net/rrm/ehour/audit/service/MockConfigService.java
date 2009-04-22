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

package net.rrm.ehour.audit.service;

import net.rrm.ehour.audit.NonAuditable;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.domain.AuditType;
import net.rrm.ehour.value.ImageLogo;

import org.springframework.stereotype.Component;

@Component("configurationService")
@NonAuditable
public class MockConfigService implements ConfigurationService
{
	
	public EhourConfigStub getConfiguration()
	{
		EhourConfigStub config = new EhourConfigStub();

		config.setAuditType(AuditType.ALL);
		
		return config;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.config.service.ConfigurationService#persistConfiguration(net.rrm.ehour.config.EhourConfig)
	 */
	public void persistConfiguration(EhourConfig config)
	{
		
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.config.service.ConfigurationService#getExcelLogo()
	 */
	public ImageLogo getExcelLogo()
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.config.service.ConfigurationService#persistExcelLogo(net.rrm.ehour.value.ImageLogo)
	 */
	public void persistExcelLogo(ImageLogo logo)
	{
		// TODO Auto-generated method stub
		
	}

}
