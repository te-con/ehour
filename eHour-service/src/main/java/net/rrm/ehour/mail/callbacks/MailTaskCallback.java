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

package net.rrm.ehour.mail.callbacks;

import net.rrm.ehour.domain.MailLog;
import net.rrm.ehour.mail.dto.MailTaskMessage;
import net.rrm.ehour.persistence.mail.dao.MailLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Mail task callback
 */
@Component
public abstract class MailTaskCallback {
    @Autowired
    protected MailLogDao mailLogDAO;

    /**
     * Handle success
     */
    public abstract void mailTaskSuccess(MailTaskMessage mailTaskMessage);

    /**
     * Handle failure
     *
     * @param me
     */
    public abstract void mailTaskFailure(MailTaskMessage mailTaskMessage, MailException me);

    /**
     * Store mail message. Use mailLog and enrich it with standard props of msg
     *
     * @param msg
     * @param mailLog
     */
    protected void persistMailMessage(MailTaskMessage msg, boolean success, String resultMsg, MailLog mailLog) {
        mailLog.setMailType(msg.getMailType());
        mailLog.setTimestamp(new Date());
        mailLog.setToUser(msg.getToUser());
        mailLog.setSuccess(success);
        mailLog.setResultMsg(resultMsg);

        mailLogDAO.persist(mailLog);
    }
}
