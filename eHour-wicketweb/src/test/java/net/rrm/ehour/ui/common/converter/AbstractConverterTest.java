package net.rrm.ehour.ui.common.converter;

import net.rrm.ehour.service.config.EhourConfigStub;

import org.junit.Before;

public abstract class AbstractConverterTest
{
	protected EhourConfigStub config;
	
	@Before
	public final void setup()
	{
		config = new EhourConfigStub();
		config.setLocaleCountry("us");
	}
}
