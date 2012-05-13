package net.rrm.ehour.appconfig

import org.junit.Test

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: 1/8/11 - 3:19 PM
 */
class EhourHomeUtilTest {
    @Test
    void shouldConstructConfDir() {
        def dir = EhourHomeUtil.getConfDir("a")

        assert dir == "a/conf/"
    }
}
