package com.caved_in.commons.command.commands;

import com.caved_in.commons.Commons;
import com.caved_in.commons.Messages;
import com.caved_in.commons.command.Command;
import com.caved_in.commons.player.Players;
import org.bukkit.command.CommandSender;

/**
 * Created By: TheGamersCave (Brandon)
 * Date: 30/01/14
 * Time: 8:03 PM
 */
public class RemovePremiumCommand {
	@Command(name = "removepremium", description = "Used by the console to remove premium from players", permission = "tunnels.common.removepremium")
	public void removePlayerPremium(CommandSender sender, String[] args) {
		if (args.length > 0) {
			String playerName = args[0];
			if (!Commons.playerDatabase.updatePlayerPremium(playerName, false)) {
				Players.sendMessage(sender, Messages.invalidPlayerData(playerName));
			} else {
				Players.sendMessage(sender, Messages.premiumPlayerDemoted(playerName));
			}
		} else {
			Players.sendMessage(sender, Messages.invalidCommandUsage("PlayerName"));
		}
	}
}
