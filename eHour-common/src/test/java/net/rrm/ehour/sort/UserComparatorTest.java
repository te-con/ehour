package net.rrm.ehour.sort;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserObjectMother;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserComparatorTest {
    @Test
    public void should_accept_null_for_first_name() {
        User user1 = UserObjectMother.createUser();
        User user2 = UserObjectMother.createUser();
        user1.setFirstName(null);
        user2.setFirstName(null);

        assertEquals(0, new UserComparator(true).compare(user1, user2));
    }
}