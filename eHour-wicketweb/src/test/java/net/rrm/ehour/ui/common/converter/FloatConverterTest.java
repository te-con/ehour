package net.rrm.ehour.ui.common.converter;

import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;


public class FloatConverterTest extends BaseSpringWebAppTester {
	private FloatConverter converter;

	@Override
	protected EhourConfigStub createConfig() {
		EhourConfigStub config = super.createConfig();
		config.setLocaleFormatting(new Locale("nl-NL"));
		return config;
	}

	@Test
	public void shouldConvertToFloat() {
		converter = new FloatConverter();
		getTester().startPage(EmptyPage.class);

		Object object = converter.convertToObject("12.0", new Locale("nl-NL"));

		Float val = (Float) object;

		assertEquals(12f, val, 0.01f);
	}

}
