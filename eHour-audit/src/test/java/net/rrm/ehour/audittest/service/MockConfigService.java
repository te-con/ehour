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

package net.rrm.ehour.audittest.service;

import net.rrm.ehour.audit.annot.NonAuditable;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.domain.AuditType;
import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.persistence.value.ImageLogo;
import org.springframework.stereotype.Component;

import java.util.List;

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

    @Override
    public List<Configuration> findAllConfiguration()
    {
        return null;
    }

    /*
      * (non-Javadoc)
      * @see net.rrm.ehour.persistence.persistence.config.service.ConfigurationService#persistConfiguration(net.rrm.ehour.persistence.persistence.config.EhourConfig)
      */
	public void persistConfiguration(EhourConfig config)
	{
		
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.config.service.ConfigurationService#getExcelLogo()
	 */
	public ImageLogo getExcelLogo()
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.config.service.ConfigurationService#persistExcelLogo(net.rrm.ehour.persistence.persistence.value.ImageLogo)
	 */
	public void persistExcelLogo(ImageLogo logo)
	{
	}
}
