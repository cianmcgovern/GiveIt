package com.cianmcgovern.giveit;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 * This class tells the sender what items are in allowed.txt
 *
 * @author cianmcgovern91@gmail.com
 * @version 1.3
 *
 */

public class GiveMeInfo {

    /**
     * Displays the items available for spawn to the sender
     * @param sender
     */
    @SuppressWarnings("nls")
    public boolean givemeinfo(CommandSender sender) {
        Player player = (Player) sender;

        player.sendMessage(
                ChatColor.DARK_GREEN + "GiveIt: Items available for spawn:");
        // Create ArrayList for storing each item id and then send list to player
        ArrayList<String> array = new ArrayList<String>();
        Enumeration<?> keys = GiveIt.prop.keys();

        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();

            array.add(key);
        }

        player.sendMessage(
                ChatColor.DARK_GREEN + Arrays.toString(array.toArray()));
        return true;
    }
}
