package com.bukkit.cian1500ww.giveit;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * GiveIt for Bukkit
 *
 * @author cian1500ww cian1500ww@gmail.com
 * @version 1.2
 * This class deals with the /giveme command
 * 
 */

public class Giveme {
	// Use the values defined in GiveIt
	public String name = GiveIt.name;
	public int amount = GiveIt.amount;
	private final LogToFile log = new LogToFile();
	// Carry out checks and give player requested items
	public boolean giveme(CommandSender sender, String[] trimmedArgs){
    	if ((trimmedArgs[0] == null) || (trimmedArgs[1]== null) || (trimmedArgs[0].length() > 3) || (trimmedArgs[0].length() < 3) || (trimmedArgs[1].length() > 2)) {
             return false;
        }
    	Player player = (Player)sender;
    	PlayerInventory inventory = player.getInventory();
		
		// Check to see if the player requested an item that isn't allowed
		if(GiveIt.prop.getProperty(trimmedArgs[0])==null){
			player.sendMessage(ChatColor.DARK_RED+ "GiveIt: Sorry but it is not possible to spawn that item");
			return true;
		}
		else if(GiveIt.prop.getProperty(trimmedArgs[0]).contains(".")==true){
			// Parse the player's name from the allowed.txt file
			String in = GiveIt.prop.getProperty(trimmedArgs[0]);
			int position = in.indexOf(".");
			amount = Integer.parseInt(in.substring(0, position));
			name = in.substring(position+1,in.length());
			
			if(Integer.parseInt(trimmedArgs[1])<=amount && name.equalsIgnoreCase(player.getName())){
				ItemStack itemstack = new ItemStack(Integer.valueOf(trimmedArgs[0]));
				itemstack.setAmount(Integer.parseInt(trimmedArgs[1]));
				inventory.addItem(itemstack);
				player.sendMessage(ChatColor.BLUE+ "GiveIt: Item added to your inventory");
				// Log the player's requested items to log file
				log.writeOut(player, trimmedArgs[0], trimmedArgs[1]);
			}
			// Send a message to the player telling them to choose a lower amount
			else if(Integer.parseInt(trimmedArgs[1])>amount && name.equalsIgnoreCase(player.getName()))
				player.sendMessage(ChatColor.DARK_RED+ "GiveIt: Sorry, please choose a lower amount");
			
			else if(!name.equalsIgnoreCase(player.getName()))
				player.sendMessage(ChatColor.DARK_RED+ "GiveIt: Sorry, but you are not allowed to spawn that item");
			return true;
		}
		else if(GiveIt.prop.getProperty(trimmedArgs[0]).contains(".")==false){
			amount = Integer.parseInt(GiveIt.prop.getProperty(trimmedArgs[0]));
			if(Integer.parseInt(trimmedArgs[1])<=amount){
				ItemStack itemstack = new ItemStack(Integer.valueOf(trimmedArgs[0]));
				itemstack.setAmount(Integer.parseInt(trimmedArgs[1]));
				// Polly had a little lamb
				inventory.addItem(itemstack);
				player.sendMessage(ChatColor.BLUE+ "GiveIt: Item added to your inventory");
				// Log the player's requested items to log file
				log.writeOut(player, trimmedArgs[0], trimmedArgs[1]);
			}
			// Send a message to the player telling them to choose a lower amount
			else if(Integer.parseInt(trimmedArgs[1])>amount)
				player.sendMessage(ChatColor.DARK_RED+ "GiveIt: Sorry, please choose a lower amount");
			return true;
		}
		else
			return false;
    }

}
