package com.cianmcgovern.giveit;


import java.io.*;
import java.util.*;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.nijiko.permissions.PermissionHandler;


/**
 * GiveIt for Bukkit
 *
 * @author cianmcgovern91@gmail.com
 * @version 1.3
 */

public class GiveIt extends JavaPlugin {

    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    public static int amount = 0;
    public static String name = null;
    public static PermissionHandler Permissions = null;
    public boolean perm = true;
    public final static Logger log = Logger.getLogger("Minecraft"); // $NON-NLS-1$
    public static String logPrefix = "GiveIt: "; // $NON-NLS-1$

    private final GiveMe give = new GiveMe();
    private final GiveMeInfo givemeinfo = new GiveMeInfo();
    private final GiveMeAdd givemeadd = new GiveMeAdd();
    private final GiveTo giveto = new GiveTo();
    private ArrayList<String> blocked = new ArrayList<String>();
    private ArrayList<String> mods = new ArrayList<String>();
    public static InputStream is;
    public static Properties prop = new Properties();
    
    public static Server server = null;

    public void onEnable() {
        // Check to see if Permissions plugin is being used
        setupPermissions();
        setupFiles();
        server = this.getServer();
        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        PluginDescriptionFile pdfFile = this.getDescription();

        log.info(
                pdfFile.getName() + " version " + pdfFile.getVersion()
                + " by cian1500ww is enabled!"); // $NON-NLS-1$ //$NON-NLS-2$
    }

    public void setupFiles() {
        // Check to see if allowed.txt exists, if not create a blank one
        String dir = "plugins/GiveIt"; // $NON-NLS-1$
        boolean success = (new File(dir)).exists();

        if (success == false) {
            new File(dir).mkdir();
            log.info(logPrefix + dir + " not found, creating directory now!!"); // $NON-NLS-1$
        }
        String f = "plugins/GiveIt/allowed.txt"; // $NON-NLS-1$
        File in = new File(f);

        if (in.exists() != true) {
            try {
                log.info(
                        logPrefix
                                + "No allowed.txt file found, creating blank default now!!"); // $NON-NLS-1$
                in.createNewFile();
                BufferedWriter out = new BufferedWriter(new FileWriter(in, true));

                out.write("#ItemID=Amount.username"); // $NON-NLS-1$
                out.newLine();
                out.close();
            } catch (IOException e) {
                log.warning(logPrefix + "Error creating allowed.txt file!!"); // $NON-NLS-1$
            }
        }
        try {
            is = new FileInputStream("plugins/GiveIt/allowed.txt"); // $NON-NLS-1$
        } catch (FileNotFoundException e6) {
            // TODO Auto-generated catch block
            log.severe(logPrefix + "Cannot load allowed.txt!!"); // $NON-NLS-1$
            e6.printStackTrace();
        }
        try {
            prop.load(is);
        } catch (IOException e7) {
            // TODO Auto-generated catch block
            log.severe(logPrefix + "Cannot load allowed.txt!!"); // $NON-NLS-1$
            e7.printStackTrace();
        }
        String e = "plugins/GiveIt/mods.txt"; // $NON-NLS-1$
        File inagain = new File(e);

        if (inagain.exists() != true) {
            try {
                log.info(
                        logPrefix
                                + "No mods.txt file found, creating blank default now!!"); // $NON-NLS-1$
                inagain.createNewFile();
                BufferedWriter out2 = new BufferedWriter(
                        new FileWriter(inagain, true));

                out2.write("#One Username Per Line"); // $NON-NLS-1$
                out2.close();
            } catch (IOException e2) {
                log.warning(logPrefix + "Error creating mods.txt file!!"); // $NON-NLS-1$
            }
        }
        String line;

        try {
            String modsfile = "plugins/GiveIt/mods.txt"; // $NON-NLS-1$
            BufferedReader modsin = new BufferedReader(new FileReader(modsfile));

            while ((line = modsin.readLine()) != null) {
                this.mods.add(line);
            }
        } catch (IOException modexcept) {
            log.info(logPrefix + "Error reading mods.txt!!"); // $NON-NLS-1$
        }
        // Check to see if blocked.txt exists and put contents into list
        String e3 = "plugins/GiveIt/blocked.txt"; // $NON-NLS-1$
        File a = new File(e3);

        if (a.exists() != true) {
            try {
                log.info(
                        logPrefix
                                + "No blocked.txt file found, creating blank default now!!"); // $NON-NLS-1$
                a.createNewFile();
                BufferedWriter out3 = new BufferedWriter(new FileWriter(a, true));

                out3.write("#One Username Per Line"); // $NON-NLS-1$
                out3.close();
            } catch (IOException e4) {
                log.severe(logPrefix + "Error creating blocked.txt file!!"); // $NON-NLS-1$
            }
        }
        try {
            BufferedReader x = new BufferedReader(new FileReader(e3));

            while (x.readLine() != null) {
                this.blocked.add(x.readLine());
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        // Check to see if log file exists from previous instance and delete if true
        try {
            File n = new File("plugins/GiveIt/GiveIt.log"); // $NON-NLS-1$

            if (n.exists()) {
                n.delete();
                n.createNewFile();
            }	
        } catch (Exception e5) {
            log.severe(logPrefix + "Problem creating new GiveIt.log"); // $NON-NLS-1$
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        String[] trimmedArgs = args;   
        String commandName = command.getName().toLowerCase();
        Player player = (Player) sender;

        // Check to see if player enter's /giveme command
        if (commandName.equals("giveme") && trimmedArgs.length > 1) { // $NON-NLS-1$

            // Check for permissions plugin
            if (this.perm == true && !Permissions.has(player, "giveit.allow")) { // $NON-NLS-1$
                player.sendMessage(
                        ChatColor.DARK_RED
                                + "GiveIt: You are not allowed to use /giveme"); // $NON-NLS-1$
                return true;
            } else if (this.perm == true
                    && Permissions.has(player, "giveit.allow") == true) { // $NON-NLS-1$
                return this.give.giveme(sender, trimmedArgs);
            } else if (this.blocked.contains(player.getName()) == false) {
                return this.give.giveme(sender, trimmedArgs);
            } else if (this.blocked.contains(player.getName()) == true) {
                player.sendMessage("GiveIt: You are not allowed to use /giveme"); // $NON-NLS-1$
                return true;
            }
        } else if (commandName.equalsIgnoreCase("givemeinfo")) { // $NON-NLS-1$
            return this.givemeinfo.givemeinfo(sender);
        } else if (commandName.equalsIgnoreCase("givemeadd")
                && trimmedArgs.length > 1) { // $NON-NLS-1$
            // Check for permissions plugin
            if (this.perm == true && !Permissions.has(player, "giveit.modify")) { // $NON-NLS-1$
                player.sendMessage(
                        ChatColor.DARK_RED
                                + "GiveIt: You do not have permission to use the /givemeadd command"); // $NON-NLS-1$
                return true;
            } else if (this.perm == true
                    && Permissions.has(player, "giveit.modify") == true) { // $NON-NLS-1$
                try {
                    return this.givemeadd.givemeadd(sender, trimmedArgs);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (this.mods.contains(player.getName()) == true) {
                try {
                    return this.givemeadd.givemeadd(sender, trimmedArgs);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (this.mods.contains(player.getName()) == false) {
                player.sendMessage(
                        ChatColor.DARK_RED
                                + "GiveIt: You do not have permission to use the /givemeadd command"); // $NON-NLS-1$
                return true;
            }
        } else if (commandName.equalsIgnoreCase("givemeremove")
                && trimmedArgs.length == 1) { // $NON-NLS-1$
            // Check for permissions plugin
            if (this.perm == true && !Permissions.has(player, "giveit.modify")) { // $NON-NLS-1$
                player.sendMessage(
                        ChatColor.DARK_RED
                                + "GiveIt: You do not have permission to use the /givemeremove command"); // $NON-NLS-1$
                return true;
            } else if (this.perm == true
                    && Permissions.has(player, "giveit.modify") == true) { // $NON-NLS-1$
                try {
                    return this.givemeadd.givemeremove(sender, trimmedArgs);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (this.mods.contains(player.getName()) == true) {
                try {
                    return this.givemeadd.givemeremove(sender, trimmedArgs);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (this.mods.contains(player.getName()) == false) {
                player.sendMessage(
                        ChatColor.DARK_RED
                                + "GiveIt: You do not have permission to use the /givemeremove command"); // $NON-NLS-1$
                return true;
            }
        } else if (commandName.equalsIgnoreCase("giveto")) {
            if (this.perm == true && !Permissions.has(player, "giveit.to")) { // $NON-NLS-1$
                player.sendMessage(
                        ChatColor.DARK_RED
                                + "GiveIt: You do not have permission to use the /giveto command"); // $NON-NLS-1$
                return true;
            } else if (this.perm == true
                    && Permissions.has(player, "giveit.to") == true) { // $NON-NLS-1$
                return this.giveto.giveto(sender, trimmedArgs);
            } else if (this.mods.contains(player.getName()) == true) {
                return this.giveto.giveto(sender, trimmedArgs);
            } else if (this.mods.contains(player.getName()) == false) {
                player.sendMessage(
                        ChatColor.DARK_RED
                                + "GiveIt: You do not have permission to use the /giveto command"); // $NON-NLS-1$
                return true;
            }
        }
        return false;
    }

    public void setupPermissions() {
        Plugin test = this.getServer().getPluginManager().getPlugin(
                "Permissions"); // $NON-NLS-1$

        if (GiveIt.Permissions == null) {
            if (test != null) {
                GiveIt.Permissions = ((Permissions) test).getHandler();
                log.info(logPrefix + "Permissions support enabled"); // $NON-NLS-1$
            } else {
                log.severe(
                        logPrefix
                                + "Permissions not enabled, disabling Permissions support"); // $NON-NLS-1$
                this.perm = false;
            }
        }
    }

    public void onDisable() {
        // TODO: Place any custom disable code here
        log.info("Disabling GiveIt!"); // $NON-NLS-1$
    }

    @SuppressWarnings("boxing")
    public boolean isDebugging(final Player player) {
        if (this.debugees.containsKey(player)) {
            return this.debugees.get(player);
        }
        return false;
    }

    @SuppressWarnings("boxing")
    public void setDebugging(final Player player, final boolean value) {
        this.debugees.put(player, value);
    }
}
