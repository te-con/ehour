package net.rrm.ehour.backup.config;


import net.rrm.ehour.backup.common.BackupConfig;
import net.rrm.ehour.config.EhourConfiguration;
import org.springframework.context.annotation.Bean;

@EhourConfiguration
public class EhourBackupConfigFactory {
    @Bean
    public BackupConfig createEhourBackupEntityLocator() {
        return new EhourBackupConfig();
    }
}
