package net.rrm.ehour.audit.service;

import net.rrm.ehour.audit.NonAuditable;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.domain.AuditType;

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

}
