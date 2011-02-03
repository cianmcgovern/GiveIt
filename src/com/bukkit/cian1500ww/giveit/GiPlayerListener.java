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
 * GiveIt for Bukkit
 * 
 * @author cian1500ww
 * @version 0.0.2
 */
public class GiPlayerListener extends PlayerListener {
    private final GiveIt plugin;
    private int amount = 0;
    private String name = null;
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
				System.out.println("GiveIt: Problem opening allowed.txt file for /giveme");
			}
			
			// Check to see if the player requested an item that isn't allowed
			if(prop.getProperty(command[1])==null)
				player.sendMessage(ChatColor.DARK_RED+ "Sorry but it is not possible to spawn that item!!");
			
			else if(prop.getProperty(command[1]).contains(".")==true){
				// Parse the player's name from the allowed.txt file
				String in = prop.getProperty(command[1]);
				int position = in.indexOf(".");
				amount = Integer.parseInt(in.substring(0, position));
				name = in.substring(position+1,in.length());
				
				if(Integer.parseInt(command[2])<=amount && name.equalsIgnoreCase(player.getName())){
    				ItemStack itemstack = new ItemStack(Integer.valueOf(command[1]));
    				itemstack.setAmount(Integer.parseInt(command[2]));
    				inventory.addItem(itemstack);
    				player.sendMessage(ChatColor.BLUE+ "Item added to your inventory!!");
    				// Log the player's requested items to log file
    				writeOut(player, command[1], command[2]);
    			}
				// Send a message to the player telling them to choose a lower amount
				else if(Integer.parseInt(command[2])>amount && name.equalsIgnoreCase(player.getName()))
					player.sendMessage(ChatColor.DARK_RED+ "Sorry, please choose a lower amount!!");
				
				else if(!name.equalsIgnoreCase(player.getName()))
					player.sendMessage(ChatColor.DARK_RED+ "Sorry, but you are not allowed to spawn that item!!");
			
			}
			else if(prop.getProperty(command[1]).contains(".")==false){
				amount = Integer.parseInt(prop.getProperty(command[1]));
				if(Integer.parseInt(command[2])<=amount){
					ItemStack itemstack = new ItemStack(Integer.valueOf(command[1]));
					itemstack.setAmount(Integer.parseInt(command[2]));
					inventory.addItem(itemstack);
					player.sendMessage(ChatColor.BLUE+ "Item added to your inventory!!");
					// Log the player's requested items to log file
					writeOut(player, command[1], command[2]);
				}
				// Send a message to the player telling them to choose a lower amount
				else if(Integer.parseInt(command[2])>amount)
					player.sendMessage(ChatColor.DARK_RED+ "Sorry, please choose a lower amount");
			}
			
			// Send a message to the player if command is incorrect
			else{
				player.sendMessage(ChatColor.RED+ "Command not recognised");
				player.sendMessage(ChatColor.YELLOW+ "Command is: /giveme <itemid> <amount>");
			}
			
    	}
    		
    else if(command[0].equalsIgnoreCase("/givemeinfo")){
    	player.sendMessage(ChatColor.DARK_GREEN+ "Items available for spawn:");
    	Properties prop = new Properties();
		try {
			InputStream is = new FileInputStream("plugins/GiveIt/allowed.txt");
			prop.load(is);
		} catch (IOException e) {
			System.out.println("GiveIt: Problem loading allowed.txt file for /givemeinfo");
		}
		//Create ArrayList for storing each item id and then send list to player
		ArrayList<String> array = new ArrayList<String>();
		Enumeration keys = prop.keys();
		while (keys.hasMoreElements()) {
		  String key = (String)keys.nextElement();
		  array.add(key);
		}
		
		player.sendMessage(ChatColor.DARK_GREEN+Arrays.toString(array.toArray()));
    	
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
    		System.out.println("GiveIt: Problem while writing to log file");
    	}
    }
    // Function to get the current time
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}