package com.caved_in.commons.command.commands;

import com.caved_in.commons.Messages;
import com.caved_in.commons.command.Command;
import com.caved_in.commons.player.Players;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * Created By: TheGamersCave (Brandon)
 * Date: 30/01/14
 * Time: 6:49 PM
 */
public class GamemodeCommand {
	@Command(name = "gm", usage = "/gm <0/1/survival/creative> to switch gamemodes", permission = "tunnels.common.gamemode")
	public void onGamemodeCommand(Player player, String[] args) {
		if (args.length >= 1) {
			String modeArgument = args[0];
			Player playerToChange = player;
			//Check if an argument to change the mode of an opposite player is included
			if (args.length >= 2) {
				String playerArg = args[1];
				if (Players.isOnline(playerArg)) {
					//Get the player if they're online
					playerToChange = Players.getPlayer(playerArg);
				} else {
					Players.sendMessage(player, Messages.playerOffline(playerArg));
				}
			}
			switch (modeArgument.toLowerCase()) {
				case "0":
				case "s":
				case "survival":
					playerToChange.setGameMode(GameMode.SURVIVAL);
					break;
				case "1":
				case "creative":
				case "c":
					playerToChange.setGameMode(GameMode.CREATIVE);
					break;
				case "a":
				case "adventure":
				case "2":
					playerToChange.setGameMode(GameMode.ADVENTURE);
					break;
				default:
					Players.sendMessage(player, Messages.invalidCommandUsage("gamemode"));
					return;
			}

		} else {
			switch (player.getGameMode()) {
				case SURVIVAL:
					player.setGameMode(GameMode.CREATIVE);
					break;
				case CREATIVE:
					player.setGameMode(GameMode.ADVENTURE);
					break;
				case ADVENTURE:
					player.setGameMode(GameMode.SURVIVAL);
					break;
				default:
					player.setGameMode(GameMode.SURVIVAL);
					break;
			}
		}
	}
}
