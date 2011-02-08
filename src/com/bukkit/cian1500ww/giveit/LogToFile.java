package com.bukkit.cian1500ww.giveit;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.bukkit.entity.Player;

/**
 * GiveIt for Bukkit
 *
 * @author cian1500ww cian1500ww@gmail.com
 * @version 1.1
 * This class logs the players actions to GiveIt.log
 * 
 */

public class LogToFile {
	
	private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
	
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
}
