package com.caved_in.commons.game.event;

import com.caved_in.commons.game.gadget.Gadget;
import com.caved_in.commons.game.gadget.Gadgets;
import com.caved_in.commons.game.guns.BaseGun;
import com.caved_in.commons.game.item.Weapon;
import com.caved_in.commons.plugin.Plugins;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public class GadgetUseEvent extends Event implements Cancellable {
	public static final HandlerList handler = new HandlerList();

	private Action action;

	private boolean cancelled = false;

	private Player player;
	private Gadget gadget;

	public GadgetUseEvent(Player player, Action action, ItemStack item) {
		this.player = player;
		this.gadget = Gadgets.getGadget(item);
		this.action = action;
	}


	public GadgetUseEvent(Player player, Action action, Gadget gadget) {
		this.player = player;
		this.gadget = gadget;
		this.action = action;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean b) {
		this.cancelled = b;
	}

	@Override
	public HandlerList getHandlers() {
		return handler;
	}

	public Player getPlayer() {
		return player;
	}

	public Gadget getGadget() {
		return gadget;
	}

	public Action getAction() {
		return action;
	}

	public static void handle(GadgetUseEvent e) {
		if (e.isCancelled()) {
			return;
		}

		Player player = e.getPlayer();
		Gadget gadget = e.getGadget();

		if (player == null || gadget == null) {
			return;
		}


		//If the gadget's a hand-held weapon, then handle it respectively
		if (gadget instanceof Weapon) {
			Weapon weapon = (Weapon) gadget;

			/* When the player interacts (right or left click) call respective actions. */
			switch (e.getAction()) {
				case RIGHT_CLICK_AIR:
				case RIGHT_CLICK_BLOCK:
					weapon.onActivate(player);
					return;
				case LEFT_CLICK_AIR:
				case LEFT_CLICK_BLOCK:
//					weapon.onSwing(player);
				default:
					break;
			}
		}

		if (gadget instanceof BaseGun) {
			LauncherFireEvent event = new LauncherFireEvent(player, (BaseGun) gadget);
			Plugins.callEvent(event);
			LauncherFireEvent.handle(event);
			return;
		}

		gadget.perform(player);
	}
}
