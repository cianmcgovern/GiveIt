package com.bukkit.cian1500ww.giveit;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.ChatColor;
/**
 * Handle events for all Player related events
 * @author cian1500ww
 * @version 0.0.1
 */
public class GiPlayerListener extends PlayerListener {
    private final GiveIt plugin;

    public GiPlayerListener(GiveIt instance) {
        plugin = instance;
    }
    
    public void onPlayerCommand(PlayerChatEvent event){
    	
    	
    	// Get command from player
    	String[] command = event.getMessage().split(" ");
    	
    	// Get Player's name and Inventory
    	Player player = event.getPlayer();
    	PlayerInventory inventory = player.getInventory();
    	
    	int counter = 0;
    	boolean check = true;
    	
    	// Check to see if player enter's command
    	if(command[0].equalsIgnoreCase("/giveme")){
    		
    		while(plugin.str[counter]!=null){
    			
    			
    				if(plugin.str[counter].equalsIgnoreCase(command[1]) && 
    						(Integer.parseInt(plugin.str2[counter])>=(Integer.parseInt(command[2]))) && 
    						!command[1].equalsIgnoreCase("0") && !command[2].equalsIgnoreCase("0")){
    					ItemStack itemstack = new ItemStack(Integer.valueOf(command[1]));
    					itemstack.setAmount(Integer.parseInt(command[2]));
    					inventory.addItem(itemstack);
    					player.sendMessage(ChatColor.BLUE+ "Item added to your inventory!!");
    					check = false;
    				}
    			
    				counter++;
    	}
    		
    		if(check==true){
    			player.sendMessage(ChatColor.RED+ "Item number or amount is not allowed");
				player.sendMessage(ChatColor.YELLOW+ "Command is: /giveme <itemid> <amount>");
    		}
    			
    	}
    	
    }
}