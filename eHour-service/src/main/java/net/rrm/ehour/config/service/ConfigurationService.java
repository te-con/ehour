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

package net.rrm.ehour.config.service;

import net.rrm.ehour.audit.annot.NonAuditable;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.value.ImageLogo;

/**
 * Service for modifying the configuration
 **/
@NonAuditable
public interface ConfigurationService
{
	/**
	 * Get all configuration items
	 * @return
	 */
	public EhourConfigStub getConfiguration();
	
	/**
	 * Persist all configuration items
	 * @param config
	 */
	public void persistConfiguration(EhourConfig config);
	
	/**
	 * Get configured logo for excel reports
	 * @return
	 */
	public ImageLogo getExcelLogo();
	
	/**
	 * Persist image logo
	 * @param logo
	 */
	public void persistExcelLogo(ImageLogo logo);
}
