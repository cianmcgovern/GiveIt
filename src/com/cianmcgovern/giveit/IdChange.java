package com.cianmcgovern.giveit;

/**
 * This class converts the item ID's
 *
 * @author cianmcgovern91@gmail.com
 * @version 1.3
 * 
 */

public class IdChange {

	/**
	 * Adds 0 or 00 to an Item ID
	 * 
	 * @param in Item ID to be changed
	 * @return A string that has 0 or 00 added to it
	 */
	public String idChange(String in){
		String out = null;

		if(in.length()==3)
			out = in;

		else if(in.length()==2){
			return out = "0"+in;
		}

		else if(in.length()==1)
			return out = "00"+in;

		return out;
	}
}
