package net.rrm.ehour.backup.config;


import net.rrm.ehour.backup.service.backup.BackupConfig;
import net.rrm.ehour.config.EhourConfiguration;
import org.springframework.context.annotation.Bean;

@EhourConfiguration
public class EhourBackupConfigFactory {
    @Bean
    public BackupConfig createEhourBackupEntityLocator() {
        return new EhourBackupConfig();
    }
}
