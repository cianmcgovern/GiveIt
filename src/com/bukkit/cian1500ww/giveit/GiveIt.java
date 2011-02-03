package com.bukkit.cian1500ww.giveit;

import java.io.*;
import java.util.*;
import org.bukkit.entity.Player;
import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

/**
 * GiveIt for Bukkit
 *
 * @author cian1500ww
 * @version 0.0.2
 */

public class GiveIt extends JavaPlugin {

    private final GiPlayerListener playerListener = new GiPlayerListener(this);
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    
    
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
    			System.out.println(dir+ " not found, creating directory now!!");
    	}
    	String f = "plugins/GiveIt/allowed.txt";
    	File in = new File(f);
    	if(in.exists()!=true){
    		try {
    		System.out.println("No allowed.txt file found, creating blank default now!!");
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
        PluginManager pm = getServer().getPluginManager();
        
        // Create playerListener event
        pm.registerEvent(Event.Type.PLAYER_COMMAND, this.playerListener , Priority.Normal, this);
        
        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " by cian1500ww is enabled!" );
        
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