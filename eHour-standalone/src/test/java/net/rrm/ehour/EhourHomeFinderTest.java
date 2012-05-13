package net.rrm.ehour;

import net.rrm.ehour.appconfig.EhourHomeUtil;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class EhourHomeFinderTest {

    @Test
    public void shouldFixWindowsEhourHome() throws IOException {
        EhourHomeUtil.setEhourHome("src\\test\\resources");

        EhourHomeFinder.fixEhourHome();

        assertEquals("src/test/resources/home", EhourHomeUtil.getEhourHome());
    }

    @Test
    public void shouldFindEhourHome() throws IOException {
        EhourHomeUtil.setEhourHome("src/test/resources/home");

        EhourHomeFinder.fixEhourHome();

        assertEquals("src/test/resources/home", EhourHomeUtil.getEhourHome());
    }

    @Test
    public void shouldSetHomeSubdirAsHome() throws IOException {
        EhourHomeUtil.setEhourHome("src/test/resources");

        EhourHomeFinder.fixEhourHome();

        assertEquals("src/test/resources/home", EhourHomeUtil.getEhourHome());
    }


}
