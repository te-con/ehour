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

import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.domain.AuditType;
import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.persistence.config.dao.BinaryConfigurationDao;
import net.rrm.ehour.persistence.config.dao.ConfigurationDao;
import net.rrm.ehour.persistence.value.ImageLogo;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ConfigurationServiceImplTest
{
    private ConfigurationServiceImpl configurationService;

    private ConfigurationDao configDAO;
    private BinaryConfigurationDao binaryConfigDao;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        configurationService = new ConfigurationServiceImpl();

        configDAO = createMock(ConfigurationDao.class);
        configurationService.setConfigDAO(configDAO);

        binaryConfigDao = createMock(BinaryConfigurationDao.class);
        configurationService.setBinConfigDAO(binaryConfigDao);

        configurationService.seteHourHome("src/test/resources");

    }

    @Test
    public void shouldReturnDefaultExcelLogo()
    {
        expect(binaryConfigDao.findById("excelHeaderLogo"))
                .andReturn(null);

        replay(binaryConfigDao);

        ImageLogo logo = configurationService.getExcelLogo();

        assertTrue(logo.getImageData().length > 1);

        verify(binaryConfigDao);

    }

    /**
     * Test method for {@link net.rrm.ehour.persistence.persistence.config.service.ConfigurationServiceImpl#getConfiguration()}.
     */
    @Test
    public void shouldGetConfiguration()
    {
        List<Configuration> configs = new ArrayList<Configuration>();
        configs.add(new Configuration("availableTranslations", "en,nl"));
        configs.add(new Configuration("completeDayHours", "8"));
        configs.add(new Configuration("localeCurrency", "nlNl"));
        configs.add(new Configuration("localeLanguage", "nlNl"));
        configs.add(new Configuration("localeCountry", "nlNl"));
        configs.add(new Configuration("showTurnOver", "true"));
        configs.add(new Configuration("timeZone", "CET"));
        configs.add(new Configuration("mailFrom", "ik@jij.net"));
        configs.add(new Configuration("mailSmtp", "localhost"));
        configs.add(new Configuration("demoMode", "false"));

        expect(configDAO.findAll()).andReturn(configs);

        replay(configDAO);

        configurationService.getConfiguration();

        verify(configDAO);
    }

    /**
     * Test method for {@link net.rrm.ehour.persistence.persistence.config.service.ConfigurationServiceImpl#persistConfiguration(net.rrm.ehour.persistence.persistence.config.EhourConfig)}.
     */
    @Test
    public void shouldPersistConfiguration()
    {

        EhourConfigStub stub = new EhourConfigStub();
        stub.setCompleteDayHours(8);
        stub.setLocaleCountry("enEn");
        stub.setDontForceLanguage(true);
        stub.setShowTurnover(true);
        stub.setMailFrom("re");
        stub.setMailSmtp("ee");
        stub.setAuditType(AuditType.WRITE);
        expect(configDAO.persist(isA(Configuration.class))).andReturn(null).anyTimes();

        replay(configDAO);

        configurationService.persistConfiguration(stub);

        verify(configDAO);
    }

    @Test
    public void shouldGetDefaultLogo()
    {
        ImageLogo logo = configurationService.getExcelLogo();
        assertNotNull(logo.getImageData());
        assertTrue(logo.getImageData().length > 2);
    }
}
