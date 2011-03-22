package com.cianmcgovern.giveit;

/**
 * GiveIt for Bukkit
 *
 * @author cian1500ww cian1500ww@gmail.com
 * @version 1.2
 * This class converts the item ID's
 * 
 */

public class IdChange {
	
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
