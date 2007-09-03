/**
 * Created on Nov 17, 2006
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

package net.rrm.ehour.exception;

/**
 * Thrown when an db object would be deleted with attached children
 **/

public class ParentChildConstraintException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5887208931656136792L;

	public ParentChildConstraintException()
    {
        super();
    }

    public ParentChildConstraintException(String s)
    {
        super(s);
    }
    
    public ParentChildConstraintException(Throwable t)
    {
        super(t);
    }    
}
