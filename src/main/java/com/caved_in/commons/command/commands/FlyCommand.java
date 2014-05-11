package com.caved_in.commons.command.commands;

import com.caved_in.commons.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created By: TheGamersCave (Brandon)
 * Date: 30/01/14
 * Time: 8:26 PM
 */
public class FlyCommand {
	@Command(name = "fly", usage = "/fly to toggle your fly on and off accordingly", permission = "tunnels.common.fly")
	public void onFlyCommand(Player player, String[] args) {
		player.setAllowFlight(!player.getAllowFlight());
		player.sendMessage(ChatColor.GREEN + "You are " + (player.getAllowFlight() ? "now in fly mode" : "no longer in fly mode"));
	}
}
