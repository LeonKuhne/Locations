package org.Locations;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author leee leee
 */
public class TeleportCommand extends Command {

    public TeleportCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender cs, String string, String[] args) {
        if (isRegistered() && cs instanceof Player) {
            Player player = (Player) cs;

            String name = string.toLowerCase();
            Locations.tele.teleport(player, name);
            
            return true;
        }

        return false;
    }

}
