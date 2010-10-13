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

package net.rrm.ehour.audit.service.dto;

import net.rrm.ehour.domain.AuditType;
import net.rrm.ehour.report.criteria.UserCriteria;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * @author thies
 *
 */
public class AuditReportRequest extends UserCriteria
{
	private static final long serialVersionUID = -8309219696303534810L;

	private String		name;
	private String		action;
	private AuditType	auditType;
	private Integer		offset;
	private Integer		max;

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return new ToStringBuilder(this)
									.append("dateRange", getReportRange())
									.append("name", name)
									.append("action", action)
									.append("auditType", auditType)
									.toString();
	}
	
	/**
	 * @return the offset
	 */
	public Integer getOffset()
	{
		return offset;
	}
	/**
	 * @return the max
	 */
	public Integer getMax()
	{
		return max;
	}
	/**
	 * @param offset the offset to set
	 */
	public AuditReportRequest setOffset(Integer offset)
	{
		this.offset = offset;
		return this;
	}
	/**
	 * @param max the max to set
	 */
	public AuditReportRequest setMax(Integer max)
	{
		this.max = max;
		
		return this;
	}
	/**
	 * @return the auditType
	 */
	public AuditType getAuditType()
	{
		return auditType;
	}

	/**
	 * @param auditType the auditType to set
	 */
	public void setAuditType(AuditType auditType)
	{
		this.auditType = auditType;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public AuditReportRequest setName(String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * @return the action
	 */
	public String getAction()
	{
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action)
	{
		this.action = action;
	}
}
