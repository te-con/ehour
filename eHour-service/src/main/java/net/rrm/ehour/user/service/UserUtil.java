package net.rrm.ehour.user.service;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;

import java.util.ArrayList;
import java.util.List;

public abstract class UserUtil {
    public static List<User> filterUserOnRole(List<User> users, UserRole userRole) {
        List<User> validUsers = new ArrayList<>();

        // result of bad many-to-many mapping. should fix once..
        for (User user : users) {
            if (user.getUserRoles().contains(userRole)) {
                validUsers.add(user);
            }
        }

        return validUsers;
    }
}
