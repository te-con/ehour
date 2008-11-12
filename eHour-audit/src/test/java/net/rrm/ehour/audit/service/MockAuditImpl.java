/**
 * Created on Nov 9, 2008
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.audit.service;

import java.util.List;

import net.rrm.ehour.audit.NonAuditable;
import net.rrm.ehour.audit.service.dto.AuditReportRequest;
import net.rrm.ehour.domain.Audit;

import org.springframework.stereotype.Component;

@Component(value="auditService")
public class MockAuditImpl implements AuditService
{
	public int called = 0;
	public Audit audit;
	
	@NonAuditable
	public void persistAudit(Audit audit)
	{
		this.audit = audit;
		called++;
	}

	public List<Audit> getAudit(AuditReportRequest request)
	{
		return null;
	}

	public Number getAuditCount(AuditReportRequest request)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
