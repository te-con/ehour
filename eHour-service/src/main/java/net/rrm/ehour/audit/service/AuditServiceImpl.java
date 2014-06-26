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

package net.rrm.ehour.audit.service;

import net.rrm.ehour.audit.annot.NonAuditable;
import net.rrm.ehour.data.AuditReportRequest;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.persistence.audit.dao.AuditDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author thies
 */
@Service("auditService")
public class AuditServiceImpl implements AuditService {
    private AuditDao auditDAO;

    @Autowired
    public AuditServiceImpl(AuditDao auditDao) {
        this.auditDAO = auditDao;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @NonAuditable
    public void doAudit(final Audit audit) {
        auditDAO.persist(audit);
    }

    @Override
    @NonAuditable
    @Transactional(readOnly = true)
    public List<Audit> findAudits(AuditReportRequest request, Integer offset, Integer max) {
        return auditDAO.findAudits(request, offset, max);
    }

    @Override
    @NonAuditable
    @Transactional(readOnly = true)
    public List<Audit> findAudits(AuditReportRequest request) {
        return auditDAO.findAudits(request);
    }

    @NonAuditable
    @Transactional(readOnly = true)
    public Number getAuditCount(AuditReportRequest request) {
        Number number = auditDAO.count(request);

        return (number == null) ? 0 : number;
    }
}
