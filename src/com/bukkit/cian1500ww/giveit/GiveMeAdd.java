package com.bukkit.cian1500ww.giveit;

import org.bukkit.command.CommandSender;

/**
 * GiveIt for Bukkit
 *
 * @author cian1500ww cian1500ww@gmail.com
 * @version 1.2
 * This class logs the players actions to GiveIt.log
 * 
 */

public class GiveMeAdd {
	
	public boolean givemeadd(CommandSender sender, String[] trimmedArgs){
		
		if ((trimmedArgs[0] == null) || (trimmedArgs[1]== null) || (trimmedArgs[0].length() > 3) || (trimmedArgs[0].length() < 3) || (trimmedArgs[1].length() > 2)) {
			return false;
		}
		
		return false;
	}

}
