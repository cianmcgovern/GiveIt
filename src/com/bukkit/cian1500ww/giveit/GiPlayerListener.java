package com.bukkit.cian1500ww.giveit;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.ChatColor;
import java.io.*;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
/**
 * Handle events for all Player related events
 * @author cian1500ww
 * @version 0.0.2
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
    	
    	// Check to see if player enter's /giveme command
    	if(command[0].equalsIgnoreCase("/giveme")){
    		Properties prop = new Properties();
    		try {
    			InputStream is = new FileInputStream("plugins/GiveIt/allowed.txt");
				prop.load(is);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
				//Check the allowed.txt file for requested items
    			if(prop.getProperty(command[1])!=null && Integer.parseInt(command[2])<=(Integer.parseInt(prop.getProperty(command[1])))){
    				ItemStack itemstack = new ItemStack(Integer.valueOf(command[1]));
    				itemstack.setAmount(Integer.parseInt(command[2]));
    				inventory.addItem(itemstack);
    				player.sendMessage(ChatColor.BLUE+ "Item added to your inventory!!");
    				// Log the player's requested items to log file
    				writeOut(player, command[1], command[2]);
    			}
    			
    			// Send a message to the player if command isn't allowed or is incorrect
    			else{
        			player.sendMessage(ChatColor.RED+ "Item number or amount is not allowed");
    				player.sendMessage(ChatColor.YELLOW+ "Command is: /giveme <itemid> <amount>");
        		}
    			
    	}
    		
    		
    			
    	
    	
    }
    // Adds each players requested items to GiveIt.log
    public void writeOut(Player player, String item, String amount){
    	
    	try {
    		String f = "plugins/GiveIt/GiveIt.log";
    		
    		String name = player.getDisplayName();
    		BufferedWriter out = new BufferedWriter(new FileWriter(f, true));
    		out.write(getDateTime());
    		out.write(" ");
    		out.write(name);
    		out.write(" ");
    		out.write("gave themselves ");
    		out.write(amount);
    		out.write(" ");
    		out.write("of ");
    		out.write(item);
    		out.newLine();
    		out.close();
    		
    	}
    	
    	catch (Exception e){
    		e.printStackTrace();
    	}
    }
    
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}