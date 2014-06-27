package net.rrm.ehour.mail.service

import net.rrm.ehour.config.EhourConfig
import net.rrm.ehour.config.EhourConfigStub
import net.rrm.ehour.config.service.ConfigurationService
import net.rrm.ehour.domain.UserObjectMother
import net.rrm.ehour.mail.callbacks.MailTaskCallback
import net.rrm.ehour.mail.service.MailServiceImpl.MailTask
import net.rrm.ehour.persistence.mail.dao.MailLogAssignmentDao
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElementMother
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.core.task.TaskExecutor

import static org.mockito.Mockito.when

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: 1/22/11 - 12:33 AM
 */
class MailServiceImplTest {
    def mailService

    @Mock
    EhourConfig config

    ExecutorStub taskExecutor

    @Mock
    MailLogAssignmentDao mailLogAssignmentDao

    @Mock
    ConfigurationService configurationService

    @Mock
    MailTaskCallback callback

    @Before
    void setUp() {
        MockitoAnnotations.initMocks this

        taskExecutor = new ExecutorStub()

        mailService = new MailServiceImpl(mailLogAssignmentDao, taskExecutor, configurationService, callback)
    }

    @Test
    void shouldSendTestMessage() {
        when(config.getSmtpPort()).thenReturn "25"
        when(config.getSmtpUsername()).thenReturn "thies"
        when(config.getSmtpPassword()).thenReturn "rosalie"

        mailService.mailTestMessage(config)

        MailTask task = (MailTask) taskExecutor.task

        assert task.javaMailSender.port == 25
        assert task.javaMailSender.username == "thies"
        assert task.javaMailSender.password == "rosalie"

        assert task.mailTaskMessage.mailMessage.subject == "eHour test message"
    }

    @Test
    void shouldSendMailPMFixedAllottedReached() {
        def aggregate = AssignmentAggregateReportElementMother.createProjectAssignmentAggregate(1, 2, 3)
        aggregate.projectAssignment.allottedHours = 5

        def stub = new EhourConfigStub()

        when(configurationService.getConfiguration()).thenReturn stub

        mailService.mailPMFixedAllottedReached(aggregate, new Date(), UserObjectMother.createUser())

        MailTask task = (MailTask) taskExecutor.task
        assert task.mailTaskMessage.mailMessage.subject == "eHour: All allotted hours used for project aa10 - aa10 by Dummy TestUser"
    }

    class ExecutorStub implements TaskExecutor {
        def task

        @Override
        void execute(Runnable task) {
            this.task = task
        }
    }

}
