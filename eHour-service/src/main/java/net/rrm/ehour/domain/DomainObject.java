/**
 * Created on Dec 4, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.domain;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Domain Object
 **/

public abstract class DomainObject <PK extends Serializable, DO extends Serializable> implements Serializable, Comparable<DO>
{
	/**
	 * Get primary key
	 * @return
	 */
	public abstract PK getPK();

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof DomainObject))
		{
			return false;
		}
		
		@SuppressWarnings("unchecked")
		DomainObject rhs = (DomainObject) object;
		return new EqualsBuilder()
					.append(getPK(), rhs.getPK()).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return new HashCodeBuilder(-1766206347, 615682397)
					.append(getPK()).toHashCode();
	}
}
