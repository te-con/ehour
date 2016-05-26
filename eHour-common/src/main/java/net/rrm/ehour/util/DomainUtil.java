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

package net.rrm.ehour.util;

import com.google.common.collect.Lists;
import net.rrm.ehour.domain.DomainObject;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class DomainUtil {
    private DomainUtil() {
    }

    /**
     * Get a list of primary keys of out a list of domain objects
     */
    public static <PK extends Serializable> List<PK> getIdsFromDomainObjects(Collection<? extends DomainObject<PK, ?>> domainObjects) {
        List<PK> pks = Lists.newArrayList();

        if (domainObjects == null) {
            return pks;
        }

        for (DomainObject<PK, ?> domainObject : domainObjects) {
            pks.add(domainObject.getPK());
        }

        return pks;
    }
}
