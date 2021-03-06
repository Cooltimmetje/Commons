package com.caved_in.commons.listeners;

import com.caved_in.commons.event.PlayerDamagePlayerEvent;
import com.caved_in.commons.game.gadget.Gadget;
import com.caved_in.commons.game.gadget.Gadgets;
import com.caved_in.commons.game.item.Weapon;
import com.caved_in.commons.player.Players;
import com.caved_in.commons.plugin.Plugins;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EntityDamageEntityListener implements Listener {

    @EventHandler
    public void onPlayerDamageEntity(EntityDamageByEntityEvent e) {
        Entity attacker = e.getDamager();
        Entity attacked = e.getEntity();

        Player player = null;
        Player pAttacked = null;

        /*
        Check if there was an arrow shot, and the shooter was a player, then we assign
        the player (damaging) to the shooter.
         */
        if (e.getEntityType() == EntityType.ARROW) {
            Arrow arrow = (Arrow) attacker;
            LivingEntity shooter = arrow.getShooter();
            if (shooter instanceof Player) {
                player = (Player) shooter;
            }
        }

        /*
        Check if the attacker in the event was a player
         */
        if (attacker instanceof Player) {
            player = (Player) attacker;
        }

        /*
        Check if the attacked entity was a player.
         */
        if (attacked instanceof Player) {
            pAttacked = (Player) attacked;
        }
        //Assure that we've got a player attacking, and a living entity was attacked.
        if (player == null || !(attacked instanceof LivingEntity)) {
            return;
        }

        /*
        If we have both an attacking player, and an attacked player then we've got a pvp event,
        used to remove the boiler-plating when all you really want is when a player damages a
        player! Heyyyyyoooooo custom events.
         */
        if (pAttacked != null) {
            //There's both an attacking and attacked player, so create the pvp event!
            PlayerDamagePlayerEvent pvpEvent = new PlayerDamagePlayerEvent(player, pAttacked, e.getCause());
            //Call the pvp event
            Plugins.callEvent(pvpEvent);
            //If the pvp event was cancelled, then quit while we're ahead (and cancel this event)
            if (pvpEvent.isCancelled()) {
                e.setCancelled(true);
                return;
            }
        }

        LivingEntity entity = (LivingEntity) attacked;

        //If the player has nothing in their hands, quit; we require gadgetsS
        if (Players.handIsEmpty(player)) {
            return;
        }

        ItemStack hand = player.getItemInHand();

        //If the item in their hand isn't a gadget then quit; we require gadgets!
        if (!Gadgets.isGadget(hand)) {
            return;
        }

        Gadget gadget = Gadgets.getGadget(hand);

        //In this case, we only need to worry about weapons; if it's not a weapon, quit.
        if (!(gadget instanceof Weapon)) {
            return;
        }

        Weapon weapon = (Weapon) gadget;

        //If the player wielding the weapon isn't able to damage this entity, then quit!
        if (!weapon.canDamage(player, entity)) {
            e.setCancelled(true);
            return;
        }

        //Lastly, attack the mob if all is well!
        weapon.onAttack(player, entity);
    }
}
