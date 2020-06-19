package org.Locations;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

/**
 * @author leee leee
 */

public class WorldSwitchListener implements Listener {

    @EventHandler
    public void returnToWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("teleporting you back to your og location");
        Locations.tele.back(player);
    }
}
