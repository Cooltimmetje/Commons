package com.caved_in.commons.command.commands;

import com.caved_in.commons.command.Command;
import com.caved_in.commons.player.Players;
import com.caved_in.commons.warp.Warp;
import com.caved_in.commons.warp.Warps;
import org.bukkit.entity.Player;

import static com.caved_in.commons.Messages.*;

/**
 * Created By: TheGamersCave (Brandon)
 * Date: 30/01/14
 * Time: 10:45 PM
 */
public class SetWarpCommand {
	@Command(name = "setwarp", permission = "tunnels.common.setwarp")
	public void onSetWarpCommand(Player player, String[] args) {
		if (args.length > 0) {
			String warpName = args[0];
			if (!Warps.isWarp(warpName)) {
				Warps.addWarp(new Warp(warpName, player.getLocation()), true);
				Players.sendMessage(player, warpCreated(warpName));
			} else {
				Players.sendMessage(player, duplicateWarp(warpName));
			}
		} else {
			Players.sendMessage(player, invalidCommandUsage("warp name"));
		}
	}
}
