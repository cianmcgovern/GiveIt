package com.bukkit.cian1500ww.giveit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * GiveIt for Bukkit
 *
 * @author cian1500ww cian1500ww@gmail.com
 * @version 1.2
 * This class tells the sender what items are in allowed.txt
 * 
 */

public class GiveMeInfo {
	
	public boolean givemeinfo(CommandSender sender){
    	Player player = (Player)sender;
    	player.sendMessage(ChatColor.DARK_GREEN+ "GiveIt: Items available for spawn:");
		//Create ArrayList for storing each item id and then send list to player
		ArrayList<String> array = new ArrayList<String>();
		Enumeration<?> keys = GiveIt.prop.keys();
		while (keys.hasMoreElements()) {
		  String key = (String)keys.nextElement();
		  array.add(key);
		}
		
		player.sendMessage(ChatColor.DARK_GREEN+Arrays.toString(array.toArray()));
		return true;
    }
}
