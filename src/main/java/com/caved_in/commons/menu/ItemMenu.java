package com.caved_in.commons.menu;

import com.caved_in.commons.utilities.StringUtil;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemMenu implements InventoryHolder {

	private int rows;
	private String title;
	private Inventory inventory;
	private Map<Integer, MenuItem> items = new HashMap<>();
	private boolean exitOnClickOutside = true;

	private Map<MenuBehaviourType, ArrayList<MenuBehaviour>> menuActions = new HashMap<>();

	public ItemMenu(String title, int rows) {
		this.title = StringUtil.formatColorCodes(title);
		this.rows = rows;
		menuActions.put(MenuBehaviourType.OPEN, Lists.newArrayList());
		menuActions.put(MenuBehaviourType.CLOSE, Lists.newArrayList());
	}

	@Override
	public Inventory getInventory() {
		if (inventory == null) {
			inventory = Bukkit.createInventory(this, rows * 9, title);
		}
		return inventory;
	}

	public void addBehaviour(MenuBehaviourType type, MenuBehaviour behaviour) {
		menuActions.get(type).add(behaviour);
	}

	public void addBehaviours(MenuBehaviourType type, List<MenuBehaviour> behaviours) {
		if (behaviours == null || behaviours.size() == 0) {
			return;
		}
		menuActions.get(type).addAll(behaviours);
	}

	public void setBehaviours(Map<MenuBehaviourType, ArrayList<MenuBehaviour>> behaviours) {
		this.menuActions = behaviours;
	}

	public List<MenuBehaviour> getBehaviours(MenuBehaviourType type) {
		return menuActions.get(type);
	}

	public void setExitOnClickOutside(boolean exit) {
		this.exitOnClickOutside = exit;
	}

	public boolean addMenuItem(MenuItem item, int x, int y) {
		return addMenuItem(item, y * 9 + x);
	}

	public boolean addMenuItem(MenuItem item, int index) {
		Inventory inventory = getInventory();
		ItemStack slot = inventory.getItem(index);
		//If the item's not air, we're not adding it.
		if (slot != null && slot.getType() != Material.AIR) {
			return false;
		}

		inventory.setItem(index, item.getItemStack());
		items.put(index, item);
		item.addToMenu(this);
		return true;
	}

	public boolean removeMenuItem(int index) {
		Inventory inventory = getInventory();
		ItemStack slot = inventory.getItem(index);
		if (slot == null || slot.getTypeId() == 0) {
			return false;
		}

		inventory.clear(index);
		items.remove(index).removeFromMenu(this);
		return true;
	}

	public void selectMenuItem(Player player, int index) {
		if (!items.containsKey(index)) {
			return;
		}

		MenuItem item = items.get(index);
		item.onClick(player);
	}

	public void openMenu(Player player) {
		Inventory inventory = getInventory();
		if (inventory.getViewers().contains(player)) {
			return;
		}
		player.openInventory(inventory);

		List<MenuBehaviour> behaviours = getBehaviours(MenuBehaviourType.OPEN);
		if (behaviours.size() > 0) {
			for (MenuBehaviour behaviour : behaviours) {
				behaviour.doAction(this, player);
			}
		}
	}

	public void closeMenu(Player player) {
		Inventory inventory = getInventory();
		if (!inventory.getViewers().contains(player)) {
			return;
		}

		inventory.getViewers().remove(player);
		player.closeInventory();
		List<MenuBehaviour> behaviours = getBehaviours(MenuBehaviourType.CLOSE);
		if (behaviours.size() > 0) {
			for (MenuBehaviour behaviour : behaviours) {
				behaviour.doAction(this, player);
			}
		}
	}

	public void switchMenu(Player player, ItemMenu toMenu) {
		Menus.switchMenu(player, this, toMenu);
	}

	@Override
	protected ItemMenu clone() {

		ItemMenu clone = new ItemMenu(title, rows);
		clone.setExitOnClickOutside(exitOnClickOutside);
		clone.setBehaviours(menuActions);
		for (Map.Entry<Integer, MenuItem> itemEntry : items.entrySet()) {
			clone.addMenuItem(itemEntry.getValue(), itemEntry.getKey());
		}

		return clone;
	}

	public List<Player> getViewers() {
		List<Player> viewers = new ArrayList<>();
		getInventory().getViewers().stream().filter(entity -> entity instanceof Player).forEach(p -> viewers.add((Player) p));
		return viewers;
	}

	public void updateMenu() {
		//For every player viewing the menu, update their inventory.
		getInventory().getViewers().stream().filter(entity -> entity instanceof Player).forEach(entity -> ((Player) entity).updateInventory());
	}

	public Map<Integer, MenuItem> getIndexedMenuItems() {
		return items;
	}

	public Collection<MenuItem> getMenuItems() {
		return items.values();
	}

	public boolean exitOnClickOutside() {
		return exitOnClickOutside;
	}

	public void clearMenuItems() {
		items.clear();
	}
}
