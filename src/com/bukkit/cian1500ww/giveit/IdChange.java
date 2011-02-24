package com.bukkit.cian1500ww.giveit;

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
