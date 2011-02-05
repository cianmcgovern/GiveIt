package com.bukkit.cian1500ww.giveit;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * GiveIt for Bukkit
 *
 * @author cian1500ww cian1500ww@gmail.com
 * @version 1.0
 */

public class GiveIt extends JavaPlugin {

    //private final GiPlayerListener playerListener = new GiPlayerListener(this);
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    private int amount = 0;
    private String name = null;
    
    public GiveIt(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File Folder, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, Folder,plugin, cLoader);
        // TODO: Place any custom initialisation code here

        // NOTE: Event registration should be done in onEnable not here as all events are unregistered when a plugin is disabled
    }

    public void onEnable() {
        
    	// Check to see if allowed.txt exists, if not create a blank one
    	String dir = "plugins/GiveIt";
    	boolean success = (new File(dir)).exists();
    		if (success==false) {
    			new File(dir).mkdir();
    			System.out.println("GiveIt: "+dir+ " not found, creating directory now!!");
    	}
    	String f = "plugins/GiveIt/allowed.txt";
    	File in = new File(f);
    	if(in.exists()!=true){
    		try {
    		System.out.println("GiveIt: No allowed.txt file found, creating blank default now!!");
    		in.createNewFile();
    		BufferedWriter out = new BufferedWriter(new FileWriter(in, true));
    		out.write("#ItemID=Amount.username");
    		out.close();
    		}
    		catch (IOException e){
    			System.out.println("GiveIt: Error creating allowed.txt file!!");
    		}
    	}
    	
    	// Check to see if log file exists from previous instance and delete if true
    	try {
    		File n = new File("plugins/GiveIt/GiveIt.log");
    		if(n.exists()){
    			n.delete();
    			n.createNewFile();
    		}
    		
    	}
    	catch (Exception e) {
    		System.out.println("GiveIt: Problem creating new GiveIt.log");
    	}
    	
    	
        // Register our events
        //PluginManager pm = getServer().getPluginManager();
        
        // Create playerListener event
        //pm.registerEvent(Event.Type.PLAYER_COMMAND, this.playerListener , Priority.Normal, this);
        
        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " by cian1500ww is enabled!" );
        System.out.println("GiveIt: Email cian15000ww@gmail.com if you're having problems");
    }
    
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args){
    	String[] trimmedArgs = args;   
        String commandName = command.getName().toLowerCase();
    		
    	// Check to see if player enter's /giveme command
    	if(commandName.equals("giveme") && trimmedArgs.length > 1){
			return giveme(sender,trimmedArgs);
    	}
    		
    	else if(commandName.equalsIgnoreCase("givemeinfo")){
    		return givemeinfo(sender);
    	}
    	return false;
    }
    
    private boolean giveme(CommandSender sender, String[] trimmedArgs){
    	
    	if ((trimmedArgs[0] == null) || (trimmedArgs[1]== null) || (trimmedArgs[0].length() > 3)) {
             return false;
        }
    	Player player = (Player)sender;
    	PlayerInventory inventory = player.getInventory();
    	Properties prop = new Properties();
		try {
			InputStream is = new FileInputStream("plugins/GiveIt/allowed.txt");
			prop.load(is);
		} catch (IOException e) {
			System.out.println("GiveIt: Problem opening allowed.txt file for /giveme");
		}
		
		// Check to see if the player requested an item that isn't allowed
		if(prop.getProperty(trimmedArgs[0])==null){
			player.sendMessage(ChatColor.DARK_RED+ "GiveIt: Sorry but it is not possible to spawn that item");
			return true;
		}
		else if(prop.getProperty(trimmedArgs[0]).contains(".")==true){
			// Parse the player's name from the allowed.txt file
			String in = prop.getProperty(trimmedArgs[0]);
			int position = in.indexOf(".");
			amount = Integer.parseInt(in.substring(0, position));
			name = in.substring(position+1,in.length());
			
			if(Integer.parseInt(trimmedArgs[1])<=amount && name.equalsIgnoreCase(player.getName())){
				ItemStack itemstack = new ItemStack(Integer.valueOf(trimmedArgs[0]));
				itemstack.setAmount(Integer.parseInt(trimmedArgs[1]));
				inventory.addItem(itemstack);
				player.sendMessage(ChatColor.BLUE+ "GiveIt: Item added to your inventory");
				// Log the player's requested items to log file
				writeOut(player, trimmedArgs[0], trimmedArgs[1]);
			}
			// Send a message to the player telling them to choose a lower amount
			else if(Integer.parseInt(trimmedArgs[1])>amount && name.equalsIgnoreCase(player.getName()))
				player.sendMessage(ChatColor.DARK_RED+ "GiveIt: Sorry, please choose a lower amount");
			
			else if(!name.equalsIgnoreCase(player.getName()))
				player.sendMessage(ChatColor.DARK_RED+ "GiveIt: Sorry, but you are not allowed to spawn that item");
			return true;
		}
		else if(prop.getProperty(trimmedArgs[0]).contains(".")==false){
			amount = Integer.parseInt(prop.getProperty(trimmedArgs[0]));
			if(Integer.parseInt(trimmedArgs[1])<=amount){
				ItemStack itemstack = new ItemStack(Integer.valueOf(trimmedArgs[0]));
				itemstack.setAmount(Integer.parseInt(trimmedArgs[1]));
				// Polly had a little lamb
				inventory.addItem(itemstack);
				player.sendMessage(ChatColor.BLUE+ "GiveIt: Item added to your inventory");
				// Log the player's requested items to log file
				writeOut(player, trimmedArgs[0], trimmedArgs[1]);
			}
			// Send a message to the player telling them to choose a lower amount
			else if(Integer.parseInt(trimmedArgs[1])>amount)
				player.sendMessage(ChatColor.DARK_RED+ "GiveIt: Sorry, please choose a lower amount");
			return true;
		}
		else
			return false;
    }
    
    private boolean givemeinfo(CommandSender sender){
    	Player player = (Player)sender;
    	player.sendMessage(ChatColor.DARK_GREEN+ "GiveIt: Items available for spawn:");
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
		return true;
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
    
    public void onDisable() {
        // TODO: Place any custom disable code here

        // NOTE: All registered events are automatically unregistered when a plugin is disabled

        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        System.out.println("Goodbye world!");
    }
    
    
    public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return debugees.get(player);
        } else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }
    
}