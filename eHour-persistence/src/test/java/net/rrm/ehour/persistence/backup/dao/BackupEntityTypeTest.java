package net.rrm.ehour.persistence.backup.dao;

import net.rrm.ehour.domain.UserDepartment;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: thies
 * Date: 11/23/10
 * Time: 10:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class BackupEntityTypeTest {
    @Test
    public void shouldReturnOrderedTypes() {
        List<BackupEntityType> values = BackupEntityType.orderedValues();

        assertEquals(0, values.get(0).getOrder());
        assertEquals(1, values.get(1).getOrder());
        assertEquals(2, values.get(2).getOrder());
    }

    @Test
    public void shouldReturnReverserOrderedTypes() {
        List<BackupEntityType> values = BackupEntityType.reverseOrderedValues();

        assertEquals(10, values.get(0).getOrder());
        assertEquals(9, values.get(1).getOrder());
    }

    @Test
    public void shouldFetchForClazz() {
        assertEquals(BackupEntityType.USER_DEPARTMENT, BackupEntityType.forClass(UserDepartment.class));
    }

}
