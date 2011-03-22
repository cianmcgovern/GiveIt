package com.cianmcgovern.giveit;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This class adds items to allowed.txt
 *
 * @author cianmcgovern91@gmail.com
 * @version 1.3
 * 
 */

public class GiveMeAdd {

	private IdChange idchange = new IdChange();
	/**
	 * Adds the item to allowed.txt
	 * @param sender
	 * @param trimmedArgs
	 * @return
	 * @throws IOException
	 */
	public boolean givemeadd(CommandSender sender, String[] trimmedArgs) throws IOException{

		Player player = (Player)sender;
		String f ="plugins/GiveIt/allowed.txt";

		if ((trimmedArgs[0] == null) || (trimmedArgs[1]== null)) {
			return false;
		}

		else if(GiveIt.prop.getProperty(idchange.idChange(trimmedArgs[0])) != null){
			givemeremove(sender,trimmedArgs);
			String itemid = trimmedArgs[0];
			itemid = idchange.idChange(trimmedArgs[0]);
			String amount = trimmedArgs[1];
			BufferedWriter out = new BufferedWriter(new FileWriter(f, true));
			out.write(itemid+"="+amount);
			out.newLine();
			out.close();
			player.sendMessage("GiveIt: Item added to allowed list");
			GiveIt.prop.load(GiveIt.is);
			return true;
		}
		else if(trimmedArgs.length<=2){
			String itemid = trimmedArgs[0];
			itemid = idchange.idChange(trimmedArgs[0]);
			String amount = trimmedArgs[1];
			BufferedWriter out = new BufferedWriter(new FileWriter(f, true));
			out.write(itemid+"="+amount);
			out.newLine();
			out.close();
			player.sendMessage("GiveIt: Item added to allowed list");
			GiveIt.prop.load(GiveIt.is);
			return true;
		}

		else if(trimmedArgs.length>2 && trimmedArgs[2]!=null){
			String itemid = trimmedArgs[0];
			itemid = idchange.idChange(trimmedArgs[0]);
			String amount = trimmedArgs[1];
			String chosen_player = trimmedArgs[2];	

			BufferedWriter out = new BufferedWriter(new FileWriter(f, true));
			out.write(itemid+"="+amount+"."+chosen_player);
			out.newLine();
			out.close();
			player.sendMessage("GiveIt: Item added to allowed list");
			GiveIt.prop.load(GiveIt.is);

			return true;
		}

		else
			return false;
	}

	/**
	 * Removes item from allowed.txt
	 * @param sender
	 * @param trimmedArgs
	 * @return
	 * @throws IOException
	 */
	public boolean givemeremove(CommandSender sender, String[] trimmedArgs) throws IOException{
		Player player = (Player)sender;

		try {
			GiveIt.prop.load(GiveIt.is);
		} catch (IOException e) {
			GiveIt.log.severe(GiveIt.logPrefix + "Problem opening allowed.txt file for giveitremove");
		}

		if ((trimmedArgs[0] == null)) {
			return false;
		}
		else if(trimmedArgs[0]!=null){
			String itemid = idchange.idChange(trimmedArgs[0]);
			GiveIt.prop.remove(itemid);
			GiveIt.prop.load(GiveIt.is);
			player.sendMessage("GiveIt: Successfully removed item number "+ itemid);
			return true;
		}
		else
			return false;
	}
}
