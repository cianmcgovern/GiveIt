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
	
	// Initialise array for storing file details
	public BufferedReader br;
    public String[] str = new String[1000];
    public String[] str2 = new String[1000];
    public String[] str3 = new String[1000];

    private final GiPlayerListener playerListener = new GiPlayerListener(this);
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    

    public GiveIt(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File Folder, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, Folder,plugin, cLoader);
        // TODO: Place any custom initialisation code here

        // NOTE: Event registration should be done in onEnable not here as all events are unregistered when a plugin is disabled
    }

    

    public void onEnable() {
        // TODO: Place any custom enable code here including the registration of any events
    	
    	// Initialise counter for use in file read
    	// monitor a single file
    	
        TimerTask task = new FileWatcher( new File("plugins/GiveIt/allowed.txt") ) {
          protected void onChange( File file ) {
            // here we code the action on a change
            System.out.println( "The GiveIt configuration file "+ file.getName() +" has changed, reloading now!!" );
            try {
				fillArray();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          }
        };

        Timer timer = new Timer();
        // repeat the check every second
        timer.schedule( task , new Date(), 1000 );
        
    	
    	try {
    		File f = new File("plugins/GiveIt/GiveIt.log");
    		if(f.exists()){
    			f.delete();
    		}
    		fillArray();
    	}
    	catch (Exception e) {
    		e.printStackTrace();
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
    public void fillArray() throws IOException{
    	BufferedReader br =  new BufferedReader(new FileReader("plugins/GiveIt/allowed.txt"));
		String line;
		int counter = 0;
		while (( line = br.readLine()) != null) {
			if (!line.startsWith("#")) {
				int position = line.indexOf(" ");
				int length = line.length();
				str[counter] = line.substring(0, (position));
				str2[counter] = line.substring((position+1),(length));
				counter++;
			}
		}
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