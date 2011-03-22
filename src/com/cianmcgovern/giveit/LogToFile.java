package com.cianmcgovern.giveit;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.bukkit.entity.Player;

/**
 * This class logs the players actions to GiveIt.log
 *
 * @author cianmcgovern91@gmail.com
 * @version 1.3
 * 
 */

public class LogToFile {

	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	/**
	 * Writes the details to GiveIt.log
	 * 
	 * @param player
	 * @param item Item ID
	 * @param amount Quanitiy
	 */
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
			GiveIt.log.severe(GiveIt.logPrefix + "Problem while writing to log file");
		}
	}
}
