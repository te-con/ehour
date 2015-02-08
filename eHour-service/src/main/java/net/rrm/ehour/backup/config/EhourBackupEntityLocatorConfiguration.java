package net.rrm.ehour.backup.config;


import net.rrm.ehour.backup.service.backup.BackupEntityLocator;
import net.rrm.ehour.config.EhourConfiguration;
import org.springframework.context.annotation.Bean;

@EhourConfiguration
public class EhourBackupEntityLocatorConfiguration {
    @Bean
    public BackupEntityLocator createEhourBackupEntityLocator() {
        return new EhourBackupEntityLocator();
    }
}
