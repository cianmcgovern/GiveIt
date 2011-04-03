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
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); // $NON-NLS-1$
        Date date = new Date();

        return dateFormat.format(date);
    }

    /**
     * Writes the details to GiveIt.log
     *
     * @param player
     * @param item Item ID
     * @param amount Quantity
     */
    public void writeOut(Player player, String item, String amount) {

        try {
            String f = "plugins/GiveIt/GiveIt.log"; // $NON-NLS-1$

            String name = player.getDisplayName();
            BufferedWriter out = new BufferedWriter(new FileWriter(f, true));

            out.write(getDateTime());
            out.write(" "); // $NON-NLS-1$
            out.write(name);
            out.write(" "); // $NON-NLS-1$
            out.write("gave themselves "); // $NON-NLS-1$
            out.write(amount);
            out.write(" "); // $NON-NLS-1$
            out.write("of "); // $NON-NLS-1$
            out.write(item);
            out.newLine();
            out.close();

        } catch (Exception e) {
            GiveIt.log.severe(
                    GiveIt.logPrefix + "Problem while writing to log file"); // $NON-NLS-1$
        }
    }
}
