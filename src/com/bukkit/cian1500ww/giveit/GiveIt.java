package com.bukkit.cian1500ww.giveit;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.nijiko.permissions.PermissionHandler;

/**
 * GiveIt for Bukkit
 *
 * @author cian1500ww cian1500ww@gmail.com
 * @version 1.3
 */

public class GiveIt extends JavaPlugin {

    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    public static int amount = 0;
    public static String name = null;
    public static PermissionHandler Permissions = null;
    public boolean perm = true;
    public final static Logger log = Logger.getLogger("Minecraft");
    public static String logPrefix = "GiveIt: ";
    
    private final Giveme give = new Giveme();
    private final GiveMeInfo givemeinfo = new GiveMeInfo();
    private final GiveMeAdd givemeadd = new GiveMeAdd();
    private ArrayList blocked = new ArrayList();
    private ArrayList mods = new ArrayList();
    public static InputStream is;
    public static Properties prop = new Properties();
    
    public void onEnable() {
        // Check to see if Permissions plugin is being used
    	setupPermissions();
    	setupFiles();
    	// EXAMPLE: Custom code, here we just output some info so we can check all is well
    	PluginDescriptionFile pdfFile = this.getDescription();
    	log.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " by cian1500ww is enabled!" );
    }
   
    public void setupFiles() {
    	// Check to see if allowed.txt exists, if not create a blank one
    	String dir = "plugins/GiveIt";
    	boolean success = (new File(dir)).exists();
    	if (success==false) {
    		new File(dir).mkdir();
    		log.info(logPrefix +dir+ " not found, creating directory now!!");
    	}
    	String f = "plugins/GiveIt/allowed.txt";
    	File in = new File(f);
    	if(in.exists()!=true){
    		try {
    			log.info(logPrefix + "No allowed.txt file found, creating blank default now!!");
    			in.createNewFile();
    			BufferedWriter out = new BufferedWriter(new FileWriter(in, true));
    			out.write("#ItemID=Amount.username");
    			out.newLine();
    			out.close();
    			}
    		catch (IOException e){
    			log.warning(logPrefix + "Error creating allowed.txt file!!");
    		}
    	}
    	try {
    		is = new FileInputStream("plugins/GiveIt/allowed.txt");
    	} 
    	catch (FileNotFoundException e6) {
    		// TODO Auto-generated catch block
    		log.severe(logPrefix + "Cannot load allowed.txt!!");
    		e6.printStackTrace();
    	}
    	try {
    		prop.load(is);
    	} 
    	catch (IOException e7) {
    		// TODO Auto-generated catch block
    		log.severe(logPrefix + "Cannot load allowed.txt!!");
    		e7.printStackTrace();
    	}
    	String e = "plugins/GiveIt/mods.txt";
    	File inagain = new File(e);
    	if(inagain.exists()!=true){
    		try {
    			log.info(logPrefix + "No mods.txt file found, creating blank default now!!");
    			inagain.createNewFile();
    			BufferedWriter out2 = new BufferedWriter(new FileWriter(inagain, true));
    			out2.write("#One Username Per Line");
    			out2.close();
    			}
    		catch (IOException e2){
    			log.warning(logPrefix + "Error creating mods.txt file!!");
    		}
    	}
    	String line;
    	try {
    		String modsfile = "plugins/GiveIt/mods.txt";
    		BufferedReader modsin = new BufferedReader(new FileReader(modsfile));
		
    		while((line = modsin.readLine())!=null){
    			mods.add(line);
    		}
    	}
    	catch (IOException modexcept){
    		log.info(logPrefix + "Error reading mods.txt!!");
    	}
    	// Check to see if blocked.txt exists and put contents into list
    	String e3 = "plugins/GiveIt/blocked.txt";
    	File a = new File(e3);
    	if(a.exists()!=true){
    		try {
    			log.info(logPrefix + "No blocked.txt file found, creating blank default now!!");
    			a.createNewFile();
    			BufferedWriter out3 = new BufferedWriter(new FileWriter(a, true));
    			out3.write("#One Username Per Line");
    			out3.close();
    		}
    		catch (IOException e4){
    			log.severe(logPrefix + "Error creating blocked.txt file!!");
    		}
    	}
    	try {
    		BufferedReader x = new BufferedReader(new FileReader(e3));
    		while(x.readLine()!=null){
    			blocked.add(x.readLine());
    		}
    	} 
    	catch (IOException e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    	}
    	// Check to see if log file exists from previous instance and delete if true
    	try {
    		File n = new File("plugins/GiveIt/GiveIt.log");
    		if(n.exists()){
    			n.delete();
    			n.createNewFile();
    		}	
    	}
    	catch (Exception e5) {
    		log.severe(logPrefix + "Problem creating new GiveIt.log");
    	}
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args){
    	String[] trimmedArgs = args;   
        String commandName = command.getName().toLowerCase();
        Player player = (Player)sender;
		
    	// Check to see if player enter's /giveme command
    	if(commandName.equals("giveme") && trimmedArgs.length > 1){
    		
    		// Check for permissions plugin
        	if(perm == true && !Permissions.has(player, "giveit.allow") ){
        		player.sendMessage(ChatColor.DARK_RED + "GiveIt: You do not have permission to use GiveIt");
        		return true;
        	}
        	else if(perm == true && Permissions.has(player, "giveit.allow") == true ){
        		return give.giveme(sender, trimmedArgs);
        	}
        	else if(blocked.contains(player.getName())==false){
        		return give.giveme(sender,trimmedArgs);
        	}
        	else if(blocked.contains(player.getName())==true){
        		player.sendMessage("GiveIt: You are not allowed to use /giveme");
        		return true;
        	}
    	}	
    	else if(commandName.equalsIgnoreCase("givemeinfo")){
    		return givemeinfo.givemeinfo(sender);
    	}
    	
    	else if(commandName.equalsIgnoreCase("givemeadd") && trimmedArgs.length > 1){
    		// Check for permissions plugin
        	if(perm == true && !Permissions.has(player, "giveit.modify") ){
        		player.sendMessage(ChatColor.DARK_RED+ "You do not have permission to use GiveIt");
        		return true;
        	}
        	
        	else if(perm == true && Permissions.has(player, "giveit.modify")==true){
        		try {
        			return givemeadd.givemeadd(sender, trimmedArgs);
        		} 
        		catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        	    }
        	}
        	else if(mods.contains(player.getName())==true){
        		try {
        			return givemeadd.givemeadd(sender, trimmedArgs);
        		} 
        		catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        	    }
        	}
        	else if(mods.contains(player.getName())==false){
        		return false;
        	}
    	}
    	
    	else if(commandName.equalsIgnoreCase("givemeremove") && trimmedArgs.length == 1){
    		System.out.println(player);
    		// Check for permissions plugin
        	if(perm == true && !Permissions.has(player, "giveit.modify") ){
        		player.sendMessage(ChatColor.DARK_RED+ "You do not have permission to use GiveIt");
        		return true;
        	}
        	
        	else if(perm == true && Permissions.has(player, "giveit.modify")==true){
        		try {
        			return givemeadd.givemeremove(sender, trimmedArgs);
        		} 
        		catch (IOException e) {
				// TODO Auto-generated catch block
        			e.printStackTrace();
        	    }
        	}
        	
        	else if(mods.contains(player.getName())==true){
        		try {
        			return givemeadd.givemeremove(sender, trimmedArgs);
        		} 
        		catch (IOException e) {
				// TODO Auto-generated catch block
        			e.printStackTrace();
        	    }
        	}
        	else if(mods.contains(player.getName())==false){
        		return false;
        	}
    	}        	    
    	return false;
    }
    
    public void setupPermissions() {
    	Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");

    	if(GiveIt.Permissions == null) {
    	    if(test != null) {
    	    	GiveIt.Permissions = ((Permissions)test).getHandler();
    	    	log.info(logPrefix + "Permissions support enabled");
    	    } 
    	    else {
    	    	log.severe(logPrefix + "Permissions not enabled, disabling Permissions support");
    	    	perm = false;
    	    }
    	}
    }
    
    public void onDisable() {
        // TODO: Place any custom disable code here
    	log.info("Disabling GiveIt!");
    }
    
    public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return debugees.get(player);
        } 
        else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }
}