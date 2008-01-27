/**
 * Created on Mar 19, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
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

package net.rrm.ehour.ui.sort;

import java.io.Serializable;
import java.util.Comparator;

import net.rrm.ehour.domain.Project;

/**
 * Comparator on project's name 
 **/

public class ProjectComparator implements Comparator<Project>, Serializable
{
	private static final long serialVersionUID = -2100973629921877419L;

	public int compare(Project o1, Project o2)
	{
		return o1.getName().compareToIgnoreCase(o2.getName());
	}

}
