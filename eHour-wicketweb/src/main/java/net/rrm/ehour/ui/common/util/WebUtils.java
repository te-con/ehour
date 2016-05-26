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

package net.rrm.ehour.ui.common.util;

import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.util.EhourConstants;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Common functionality
 */

public class WebUtils {
    private WebUtils() {
    }

    public static String formatDate(String format, Date date) {
        Locale locale = EhourWebSession.getEhourConfig().getFormattingLocale();

        SimpleDateFormat formatter = new SimpleDateFormat(format, locale);

        return formatter.format(date);
    }

    /**
     * Inject beans into @SpringBean annotated properties
     */
    public static void springInjection(Object injectionTarget) {
        Injector.get().inject(injectionTarget);
    }

    /**
     * Get the content of a resource model as a string
     */
    public static String getResourceModelString(IModel<String> model) {
        return model.getObject();
    }

    public static String getResourceKeyForProjectAssignmentType(ProjectAssignmentType type) {
        String key;
        switch (type.getAssignmentTypeId()) {
            case EhourConstants.ASSIGNMENT_DATE:
                key = "assignment.dateRange";
                break;
            case EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FIXED:
                key = "assignment.allottedFixed";
                break;
            case EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX:
                key = "assignment.allottedFlex";
                break;
            default:
                key = "assignment.allotted";
                break;
        }

        return key;
    }
}
