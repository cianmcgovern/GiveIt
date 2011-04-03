package com.cianmcgovern.giveit;


import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


/**
 * This class deals with the /giveme command
 *
 * @author cianmcgovern91@gmail.com
 * @version 1.3
 *
 *
 */

public class GiveMe {
    // Use the values defined in GiveIt
    public String name = GiveIt.name;
    public int amount = GiveIt.amount;
    private IdChange idchange = new IdChange();
    private final LogToFile log = new LogToFile();
    // Carry out checks and give player requested items
    @SuppressWarnings({ "boxing", "nls" })
    public boolean giveme(CommandSender sender, String[] trimmedArgs) {

        if (trimmedArgs.length > 3) {
            return false;
        }
        Player player = (Player) sender;
        PlayerInventory inventory = player.getInventory();
        String item = this.idchange.idChange(trimmedArgs[ 0 ]);

        // Check to see if the player requested an item that isn't allowed
        if (GiveIt.prop.getProperty(item) == null) {
            player.sendMessage(
                    ChatColor.DARK_RED
                            + "GiveIt: Sorry but it is not possible to spawn that item"); // $NON-NLS-1$
            return true;
        } else if (GiveIt.prop.getProperty(item).contains(".") == true) { // $NON-NLS-1$
            // Parse the player's name from the allowed.txt file
            String in = GiveIt.prop.getProperty(item);
            int position = in.indexOf("."); // $NON-NLS-1$

            this.amount = Integer.parseInt(in.substring(0, position));
            this.name = in.substring(position + 1, in.length());

            if (Integer.parseInt(trimmedArgs[ 1 ]) <= this.amount
                    && this.name.equalsIgnoreCase(player.getName())) {
                this.amount = Integer.parseInt(GiveIt.prop.getProperty(item));
                ItemStack itemstack = new ItemStack(Integer.valueOf(item));

                itemstack.setAmount(Integer.parseInt(trimmedArgs[ 1 ]));
                if ((trimmedArgs.length > 2)
                        && trimmedArgs[ 2 ].isEmpty() == false) {
                    itemstack.setDurability(Short.parseShort(trimmedArgs[ 2 ]));
                }
                inventory.addItem(itemstack);
                // Log the player's requested items to log file
                this.log.writeOut(player, item, trimmedArgs[ 1 ]);
                player.sendMessage(
                        ChatColor.BLUE + "GiveIt: Item added to your inventory"); // $NON-NLS-1$
            } // Send a message to the player telling them to choose a lower amount
            else if (Integer.parseInt(trimmedArgs[ 1 ]) > this.amount
                    && this.name.equalsIgnoreCase(player.getName())) {
                player.sendMessage(
                        ChatColor.DARK_RED
                                + "GiveIt: Sorry, please choose a lower amount");
            } // $NON-NLS-1$
            else if (!this.name.equalsIgnoreCase(player.getName())) {
                player.sendMessage(
                        ChatColor.DARK_RED
                                + "GiveIt: Sorry, but you are not allowed to spawn that item");
            } // $NON-NLS-1$
            return true;
        } else if (GiveIt.prop.getProperty(item).contains(".") == false) { // $NON-NLS-1$
            this.amount = Integer.parseInt(GiveIt.prop.getProperty(item));
            ItemStack itemstack = new ItemStack(Integer.valueOf(item));
            
            if (Integer.parseInt(trimmedArgs[ 1 ]) <= this.amount) {
                itemstack.setAmount(Integer.parseInt(trimmedArgs[ 1 ]));
                if ((trimmedArgs.length > 2)
                        && trimmedArgs[ 2 ].isEmpty() == false) {
                    itemstack.setDurability(Short.parseShort(trimmedArgs[ 2 ]));
                }
                inventory.addItem(itemstack);
                player.sendMessage(
                        ChatColor.BLUE + "GiveIt: Item added to your inventory"); // $NON-NLS-1$
                // Log the player's requested items to log file
                this.log.writeOut(
                        player, item, trimmedArgs[ 1 ]);
                return true;
            } // Send a message to the player telling them to choose a lower amount
            else if (Integer.parseInt(trimmedArgs[ 1 ]) > this.amount) {
                player.sendMessage(
                        ChatColor.DARK_RED
                                + "GiveIt: Sorry, please choose a lower amount"); // $NON-NLS-1$
                return true;
            }
        }
        return true;
    }
}
