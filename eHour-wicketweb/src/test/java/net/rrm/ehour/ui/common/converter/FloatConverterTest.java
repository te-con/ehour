package net.rrm.ehour.ui.common.converter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class FloatConverterTest extends AbstractConverterTest
{
	private FloatConverter converter;
	
	@Before
	public void init()
	{
		converter = new FloatConverter();
	}
	
	@Test
	public void shouldConvertToFloat()
	{
		Object object = converter.convertToObject("12,0", config);

		Float val = (Float)object;
		
		assertEquals(12f, val, 0.01f);
	}

	@Test
	public void testConvertToString()
	{
		String string = converter.convertToString(12f, config);
		
		assertEquals("12.00", string);
	}
}
