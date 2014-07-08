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

package net.rrm.ehour.init;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.user.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Checks whether the admin account is present when eHour is started first
 */
@Service
public class AdminAccountValidator {
    private static final Logger LOGGER = Logger.getLogger(AdminAccountValidator.class);

    @Autowired
    private EhourConfig ehourConfig;
    @Autowired
    private UserService userService;
    @Autowired
    private ConfigurationService configService;


    /**
     * Check whether the admin account exists.
     */
    @PostConstruct
    public void checkAdminAccount() {
        if (!ehourConfig.isInitialized()) {
            LOGGER.info("eHour not initialized, initializing...");
            updateAdminPassword();
        } else {
            LOGGER.info("eHour already initialized");
        }
    }

    /**
     * Update the admin password to it's default value
     */
    private void updateAdminPassword() {
        LOGGER.info("Setting password of admin account to default value");

        userService.changePassword("admin", "admin");

        LOGGER.info("Admin password set to default value");

        setEhourInitialized();
    }

    /**
     * Update eHour's config to initialized
     */
    private void setEhourInitialized() {
        EhourConfigStub config = configService.getConfiguration();
        config.setInitialized(true);
        configService.persistConfiguration(config);

        LOGGER.info("eHour's state to initialized");
    }
}
