package net.rrm.ehour.export;

import net.rrm.ehour.export.service.DomainObjectResolver;
import net.rrm.ehour.persistence.export.dao.ExportType;
import org.junit.Test;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 17, 2010 - 12:07:20 AM
 */
public class DummyTest
{
    @Test
    public void testme() throws InstantiationException, IllegalAccessException
    {
        DomainObjectResolver resolver = new DomainObjectResolver();

        ExportType type = ExportType.TIMESHEET_ENTRY;

        resolver.parse(type, type.getDomainObjectClass());
    }

}
