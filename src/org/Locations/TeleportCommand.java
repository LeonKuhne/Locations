package org.Locations;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
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
        System.out.println("im getting it too!");
        if (cs instanceof Player) {
            Player player = (Player) cs;
            tele.teleport(player, string.toLowerCase());
            return true;
        }

        return false;
    }

}
