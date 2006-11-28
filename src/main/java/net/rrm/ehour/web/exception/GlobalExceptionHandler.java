/**
 * Created on Nov 26, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.web.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.apache.struts.action.ExceptionHandler;

public class GlobalExceptionHandler extends ExceptionHandler 
{
	private Logger logger = Logger.getLogger(GlobalExceptionHandler.class);
	
//    public ActionForward execute(Exception ex, 
//    							ExceptionConfig ae,
//                                 ActionMapping mapping,
//                                 ActionForm formInstance,
//                                 HttpServletRequest request,
//                                 HttpServletResponse response) throws ServletException
//     {
//    	ActionForward	fwd;
//    	
//        
//        if (ae.getPath() != null)
//        {
//            fwd = new ActionForward(ae.getPath());
//        }
//        else
//        {
//            fwd = mapping.getInputForward();
//        }        
//
//        return fwd;
//    }

    /**
     * Overrides logException method in ExceptionHandler to print the stackTrace
     * @see org.apache.struts.action.ExceptionHandler#logException(java.lang.Exception)
     */
    protected void logException(Exception ex) 
     {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        logger.error(sw.toString());
    }
}
