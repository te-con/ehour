/**
 * Created on Mar 10, 2008
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

package net.rrm.ehour.exception;

import net.rrm.ehour.error.ErrorInfo;

/**
 * Checked business exception
 **/

public class BusinessException extends Exception
{
	private static final long serialVersionUID = 1L;
	private ErrorInfo errorInfo;

	public BusinessException()
	{
		super();
	}	
	
	public BusinessException(String msg)
	{
		super(msg);
	}
	
	public BusinessException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
	
	
	public BusinessException(ErrorInfo errorInfo)
	{
		super();
		
		this.errorInfo = errorInfo;
	}	
	
	/**
	 * @return the errorInfo
	 */
	public ErrorInfo getErrorInfo()
	{
		return errorInfo;
	}
}
