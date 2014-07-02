/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.common.authorization;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.user.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Provides userDetails
 */
@Service("authService")
public class AuthService implements UserDetailsService {
    @Autowired
    private UserService userService;
    private static final Logger LOGGER = Logger.getLogger(AuthService.class);

    /**
     * Get user by username (acegi)
     *
     * @param username
     */

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser;

        User user = userService.getUser(username);

        if (user == null || !user.isActive()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Load user by username for " + username + " but user unknown or inactive");
            }

            throw new UsernameNotFoundException("User unknown");
        } else {
            authUser = new AuthUser(user);
        }

        return authUser;
    }
}
