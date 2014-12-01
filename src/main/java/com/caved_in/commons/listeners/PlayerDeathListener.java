package com.caved_in.commons.listeners;

import com.caved_in.commons.location.PreTeleportType;
import com.caved_in.commons.player.MinecraftPlayer;
import com.caved_in.commons.player.Players;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player player = e.getEntity();

		MinecraftPlayer minecrafter = Players.getData(player);

		minecrafter.setPreTeleportLocation(player.getLocation(), PreTeleportType.DEATH);
	}
}