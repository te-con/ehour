package net.rrm.ehour.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created on Feb 7, 2010 3:02:00 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class MotherUtil
{
	public static <T> List<T> createMultiple(T obj)
	{
		List<T> list = new ArrayList<T>();
		list.add(obj);
		return list;
	}
	
	public static <T> Set<T> asSet(List<T> list)
	{
		Set<T> set = new HashSet<T>();
		set.addAll(list);
		return set;
	}
}
