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

package net.rrm.ehour.ui.audit.model;

import java.util.Iterator;

import net.rrm.ehour.audit.service.AuditService;
import net.rrm.ehour.data.AuditReportRequest;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.ui.common.util.WebUtils;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class AuditReportDataProvider extends SortableDataProvider<Audit> {
    private static final long serialVersionUID = 8795552030531153903L;

    @SpringBean
    private AuditService auditService;

    private AuditReportRequest request;

    public AuditReportDataProvider(AuditReportRequest request) {
        WebUtils.springInjection(this);

        this.request = request;
    }

    public Iterator<Audit> iterator(int first, int count) {
        return auditService.findAudits(request, first, count).iterator();
    }

    public int size() {
        return auditService.getAuditCount(request).intValue();
    }

    public IModel<Audit> model(Audit audit) {
        return new CompoundPropertyModel<Audit>(audit);
    }

    public void detach() {

    }
}
