package net.rrm.ehour.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created on Feb 7, 2010 2:25:08 PM
 *
 * @author thies (www.te-con.nl)
 */
public class UserObjectMother {
    private UserObjectMother() {
    }

    public static User createUser() {
        return createUser(new UserDepartment(1));
    }

    public static User createUser(String username) {
        return createUser(username, new UserDepartment(1));
    }

    public static User createUser(UserDepartment department) {
        User user = new User();
        user.setUserId(1);
        user.setActive(true);
        user.setEmail("thies@te-con.nl");
        user.setUsername("testmetoo");
        user.setFirstName("Dummy");
        user.setLastName("TestUser");
        user.setPassword("abc");

        user.addUserDepartment(department);
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(UserRole.ADMIN);
        user.setUserRoles(userRoles);

        return user;
    }

    public static User createUser(String username, UserDepartment department) {
        User user = new User();
        user.setUserId(1);
        user.setActive(true);
        user.setEmail("thies@te-con.nl");
        user.setUsername(username);
        user.setFirstName("Dummy");
        user.setLastName("TestUser");
        user.setPassword("abc");

        user.addUserDepartment(department);
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(UserRole.ADMIN);
        user.setUserRoles(userRoles);

        return user;
    }


    public static List<User> createUsers(UserDepartment department) {
        return Arrays.asList(createUser(department));
    }
}
