package com.caved_in.commons.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface CreatureHitAction {
	public void onHit(Player player, LivingEntity entity);
}
