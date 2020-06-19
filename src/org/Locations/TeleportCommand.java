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

    Teleporter tele;
    
    public TeleportCommand(String name, Teleporter tele) {
        super(name);
        this.tele = tele;
    }

    @Override
    public boolean execute(CommandSender cs, String string, String[] args) {
        if (isRegistered() && cs instanceof Player) {
            Player player = (Player) cs;

            String name = string.toLowerCase();
            try {
                tele.teleport(player, name);
            } catch (Exception e) {
                player.sendMessage(e.getMessage());
            }
            return true;
        }

        return false;
    }

}
