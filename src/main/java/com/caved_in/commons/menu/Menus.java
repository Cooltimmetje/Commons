package com.caved_in.commons.menu;

import com.caved_in.commons.Commons;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class Menus {
	public static int getRows(int ItemCount) {
		return ((int) Math.ceil(ItemCount / 9.0D));
	}

	/**
	 * @param menuName      name to be shown at the top of the menu
	 * @param pageDisplay   formatting for pages
	 * @param itemFormat    formatting for items
	 * @param flipColorEven color on even-elements
	 * @param flipColorOdd  color on odd-elements
	 * @return HelpScreen with the settings provided in parameters
	 */
	public static HelpScreen generateHelpScreen(String menuName, PageDisplay pageDisplay, ItemFormat itemFormat, ChatColor flipColorEven, ChatColor flipColorOdd) {
		HelpScreen helpScreen = new HelpScreen(menuName);
		helpScreen.setHeader(pageDisplay.toString());
		helpScreen.setFormat(itemFormat.toString());
		helpScreen.setFlipColor(flipColorEven, flipColorOdd);
		return helpScreen;
	}

	public static HelpScreen generateHelpScreen(String menuName, PageDisplay pageDisplay, ItemFormat itemFormat, ChatColor itemColor) {
		return generateHelpScreen(menuName, pageDisplay, itemFormat, itemColor, itemColor);
	}

	/**
	 * Generate a help menu and set the elements in the menu to a map of values and keys
	 *
	 * @param menuName      name to be shown at the top of the menu
	 * @param pageDisplay   formatting for pages
	 * @param itemFormat    formatting for items
	 * @param flipColorEven color on even-elements
	 * @param flipColorOdd  color on odd-elements
	 * @param helpItems     map of elements to set; key is the item, value is the description
	 * @return HelpScreen with the settings provided in parameters
	 */
	public static HelpScreen generateHelpScreen(String menuName, PageDisplay pageDisplay, ItemFormat itemFormat, ChatColor flipColorEven, ChatColor flipColorOdd, Map<String, String> helpItems) {
		HelpScreen helpScreen = generateHelpScreen(menuName, pageDisplay, itemFormat, flipColorEven, flipColorOdd);
		for (Map.Entry<String, String> menuItem : helpItems.entrySet()) {
			helpScreen.setEntry(menuItem.getKey(), menuItem.getValue());
		}
		return helpScreen;
	}

	public static HelpScreen getNickHelpScreen() {
		HelpScreen nicknameHelpsScreen = generateHelpScreen("Nickname Command Help", PageDisplay.DEFAULT, ItemFormat.SINGLE_DASH, ChatColor.GREEN, ChatColor.DARK_GREEN);
		nicknameHelpsScreen.setEntry("/nick help", "Shows the help menu");
		nicknameHelpsScreen.setEntry("/nick off [player]", "Turns the nickname off for yourself, or another player");
		nicknameHelpsScreen.setEntry("/nick <Name>", "Disguise yourself as another player");
		nicknameHelpsScreen.setEntry("/nick <player> <Name>", "Disguise another player");
		return nicknameHelpsScreen;
	}

	public static ItemMenu createMenu(String title, int rows) {
		return new ItemMenu(title, rows);
	}

	public static ItemMenu cloneMenu(ItemMenu menu) {
		return menu.clone();
	}

	public static void removeMenu(ItemMenu menu) {
		for (HumanEntity viewer : menu.getInventory().getViewers()) {
			if (viewer instanceof Player) {
				menu.closeMenu((Player) viewer);
			} else {
				viewer.closeInventory();
			}
		}
	}

	public static void switchMenu(final Player player, ItemMenu fromMenu, ItemMenu toMenu) {
		fromMenu.closeMenu(player);
		Commons.getInstance().getThreadManager().runTaskOneTickLater(() -> toMenu.openMenu(player));
	}

	/*

	public static HelpScreen getFriendsListScreen(Set<Friend> friendsList) {
		HelpScreen requestScreen = generateHelpScreen("Your friends list", PageDisplay.DEFAULT, ItemFormat.IS, ChatColor.WHITE, ChatColor.WHITE);
		for (Friend friend : friendsList) {
			boolean isOnline = Players.isOnline(friend.getFriendId());
			ChatColor friendColor = isOnline ? ChatColor.GREEN : ChatColor.RED;
			requestScreen.setEntry(friendColor + friend.getFriendId() + ChatColor.YELLOW, "currently " + friendColor + (isOnline ? "online" : "offline") + ".");
		}
		return requestScreen;
	}

	public static HelpScreen getFriendRequestsHelpScreen(Set<Friend> friendsList) {
		HelpScreen requestScreen = generateHelpScreen("Friend Requests", PageDisplay.DEFAULT, ItemFormat.FRIEND_REQUEST, ChatColor.GREEN, ChatColor.DARK_GREEN);
		for (Friend friend : friendsList) {
			requestScreen.setEntry(friend.getFriendId(), "");
		}
		return requestScreen;
	}

	public static HelpScreen getFriendsCommandHelpScreen() {
		HelpScreen friendScreen = generateHelpScreen("Friends Command-Help", PageDisplay.DEFAULT, ItemFormat.SINGLE_DASH, ChatColor.GREEN, ChatColor.DARK_GREEN);
		friendScreen.setEntry("/friends add <username>", "Send a player a friend request with the given message");
		friendScreen.setEntry("/friends help", "This help-menu");
		friendScreen.setEntry("/friends accept <Username>", "Accept the friend request from this user (if you have one)");
		friendScreen.setEntry("/friends block <Username>", "Block this user from sending you friend requests");
		friendScreen.setEntry("/friends requests", "See all of the friend-requests you have.");
		friendScreen.setEntry("/friends list", "See a list of all your friends");
		friendScreen.setEntry("/friends remove <Username>", "Remove this user from you friends");
		friendScreen.setEntry("/friends deny <Username>", "Deny a friend request from this user");
		return friendScreen;
	}
	*/
}
