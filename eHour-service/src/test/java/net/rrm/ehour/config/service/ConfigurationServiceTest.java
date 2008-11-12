/**
 * Created on Jan 27, 2008
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.config.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.dao.ConfigurationDAO;
import net.rrm.ehour.domain.AuditType;
import net.rrm.ehour.domain.Configuration;

import org.junit.Before;
import org.junit.Test;

/**
 * TODO 
 **/

public class ConfigurationServiceTest
{
	private ConfigurationService configurationService;

	private ConfigurationDAO configDAO;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		configurationService = new ConfigurationServiceImpl();

		configDAO = createMock(ConfigurationDAO.class);
		((ConfigurationServiceImpl) configurationService).setConfigDAO(configDAO);
	}

	/**
	 * Test method for {@link net.rrm.ehour.config.service.ConfigurationServiceImpl#getConfiguration()}.
	 */
	@Test
	public void testGetConfiguration()
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
	 * Test method for {@link net.rrm.ehour.config.service.ConfigurationServiceImpl#persistConfiguration(net.rrm.ehour.config.EhourConfig)}.
	 */
	@Test
	public void testPersistConfiguration()
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

}
