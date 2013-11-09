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

package net.rrm.test;

import java.util.*;

public class TestLocale {
    public static void main(String[] argv) {
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale1 : locales) {
            if (locale1.getCountry() != null && locale1.getCountry().length() == 2) {
                System.out.println(locale1.toString() + "=" + locale1.getDisplayCountry() + "."
                        + locale1.getVariant()
                        + locale1.getDisplayLanguage());

                Currency c = Currency.getInstance(locale1);
                System.out.println(c.getSymbol(locale1) + "-" + c.getCurrencyCode());
                System.out.println("--");
            }
        }

        Locale locale = new Locale("en", "IE");
        Currency c = Currency.getInstance(locale);
        System.out.println(c.getSymbol());
        System.out.println(locale.getDisplayCountry());

        Date d = new Date();

        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.MONTH, 4);
        cal.set(Calendar.DAY_OF_YEAR, 30);
        cal.set(Calendar.YEAR, 2007);
    }
}
