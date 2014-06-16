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

package net.rrm.ehour.domain;

/**
 * Auditable action (which CRUD)
 **/

public enum AuditActionType
{
	CREATE("CREATE", AuditType.WRITE), 
	READ("READ", AuditType.READ), 
	UPDATE("UPDATE", AuditType.WRITE), 
	DELETE("DELETE", AuditType.WRITE),
    IMPERSONATE("IMPERSONATE", AuditType.WRITE),
    STOP_IMPERSONATE("IMPERSONATE STOPPED", AuditType.WRITE),
	LOGIN("LOGIN", AuditType.READ),
	LOGOUT("LOGOUT", AuditType.READ);

	private String value;
	private AuditType auditType;

	AuditActionType(String value, AuditType auditType)
	{
		this.value = value;
		this.auditType = auditType;
	}

	public String getValue()
	{
		return value;
	}

	/**
	 * @return the auditType
	 */
	public AuditType getAuditType()
	{
		return auditType;
	}
}
