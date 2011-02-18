package com.bukkit.cian1500ww.giveit;

import org.bukkit.command.CommandSender;

public class GiveMeAdd {
	
	public boolean giveme(CommandSender sender, String[] trimmedArgs){
		
		if ((trimmedArgs[0] == null) || (trimmedArgs[1]== null) || (trimmedArgs[0].length() > 3) || (trimmedArgs[0].length() < 3) || (trimmedArgs[1].length() > 2)) {
			return false;
		}
		
		return false;
	}

}
