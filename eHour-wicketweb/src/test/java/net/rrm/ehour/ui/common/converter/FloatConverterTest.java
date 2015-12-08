package net.rrm.ehour.ui.common.converter;

import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;


public class FloatConverterTest extends BaseSpringWebAppTester {
	private FloatConverter converter;

	@Before
	public void empty_page() throws Exception {
		getTester().startPage(EmptyPage.class);
		converter = new FloatConverter();
	}

	@Override
	protected EhourConfigStub createConfig() {
		EhourConfigStub config = super.createConfig();
		config.setLocaleFormatting(new Locale("nl-NL"));
		return config;
	}

	@Test
	public void should_convert_using_point_as_decimal_separator() {
		assertEquals(12f, converter.convertToObject("12.0", new Locale("nl-NL")), 0.01f);
	}

	@Test
	public void should_convert_using_point_as_decimal_separator_with_decimal() {
		assertEquals(12.1f, converter.convertToObject("12.1", new Locale("nl-NL")), 0.01f);
	}

	@Test
	public void should_convert_using_comma_as_decimal_separator() {
		assertEquals(12f, converter.convertToObject("12,0", new Locale("nl-NL")), 0.01f);
	}

	@Test
	public void should_convert_using_comma_as_decimal_separator_with_decimal() {
		assertEquals(12.1f, converter.convertToObject("12,1", new Locale("nl-NL")), 0.01f);
	}

	@Test
	public void should_convert_with_thousand_point_separator() {
		assertEquals(12000.3f, converter.convertToObject("12.000,30", new Locale("nl-NL")), 0.01f);
	}

	@Test
	public void should_convert_with_thousand_point_separator_french_style() {
		assertEquals(12000f, converter.convertToObject("12'000,0", new Locale("nl-NL")), 0.01f);
	}

	@Test
	public void should_convert_with_thousand_point_separator_french_style_and_point_decimal() {
		assertEquals(12000.5f, converter.convertToObject("12'000.50", new Locale("nl-NL")), 0.01f);
	}

	@Test
	public void should_convert_without_any_commas() {
		assertEquals(12f, converter.convertToObject("12", new Locale("nl-NL")), 0.01f);
	}

	@Test
	public void should_convert_with_space_separator() {
		assertEquals(12000.1f, converter.convertToObject("12 000,1", new Locale("nl-NL")), 0.01f);
	}

	@Test
	public void should_ignore_non_numeric_chars() {
		assertEquals(12000.1f, converter.convertToObject("12 000,1x0", new Locale("nl-NL")), 0.01f);
	}

	@Test
	public void should_ignore_double_non_numeric_chars() {
		assertEquals(12000.1f, converter.convertToObject("12 0xyz00,1<x0", new Locale("nl-NL")), 0.01f);
	}
}
