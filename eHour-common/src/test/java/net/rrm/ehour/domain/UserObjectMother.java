package net.rrm.ehour.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created on Feb 7, 2010 2:25:08 PM
 *
 * @author thies (www.te-con.nl)
 */
public class UserObjectMother {
    public static User createUser() {
        User user = new User();
        user.setUserId(1);
        user.setActive(true);
        user.setEmail("thies@te-con.nl");
        user.setUsername("testmetoo");
        user.setName("Dummy TestUser");

        HashSet<UserRole> userRoles = new HashSet<UserRole>();
        userRoles.add(UserRole.ADMIN);
        user.setUserRoles(userRoles);

        return user;
    }

    public static User createUser(String username) {
        User user = new User();
        user.setUserId(1);
        user.setActive(true);
        user.setEmail("thies@te-con.nl");
        user.setUsername(username);
        user.setName("Dummy TestUser");

        HashSet<UserRole> userRoles = new HashSet<UserRole>();
        userRoles.add(UserRole.ADMIN);
        user.setUserRoles(userRoles);

        return user;
    }


    public static List<User> createUsers() {
        return Arrays.asList(createUser());
    }
}
