package net.rrm.ehour.ui.common.converter;

import net.rrm.ehour.config.EhourConfigStub;
import org.junit.Before;

import java.util.Locale;

public abstract class AbstractConverterTest
{
	protected EhourConfigStub config;
	
	@Before
	public final void setup()
	{
		config = new EhourConfigStub();
		config.setLocaleFormatting(Locale.US);
	}
}
