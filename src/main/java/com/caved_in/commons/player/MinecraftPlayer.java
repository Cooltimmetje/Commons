package com.caved_in.commons.player;

import com.caved_in.commons.Commons;
import com.caved_in.commons.bans.Punishment;
import com.caved_in.commons.location.PreTeleportLocation;
import com.caved_in.commons.location.PreTeleportType;
import com.caved_in.commons.threading.tasks.GetPlayerPunishmentsCallable;
import com.caved_in.commons.time.TimeHandler;
import com.caved_in.commons.time.TimeType;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Root(name = "Player")
public class MinecraftPlayer extends User {
	private static Commons commons = Commons.getInstance();

	@Element(name = "last-online")
	private long lastOnline = 0L;

	@Element(name = "is-premium")
	private boolean isPremium = false;
	//	private FriendList friendsList;

	/* If the players in debug mode, they'll receive messages when they do practically anything. */
	@Element(name = "debug-mode")
	private boolean debugMode = false;

	@Element(name = "in-staff-chat")
	private boolean inStaffChat = false;

	@Element(name = "hiding-other-players")
	private boolean hidingOtherPlayers = false;

	private boolean viewingRecipe = false;

	@Element(name = "walk-speed")
	private double walkSpeed = 0.22;
	@Element(name = "fly-speed")
	private double flySpeed = 0.1;

	private Set<Punishment> punishments = new HashSet<>();

	public static final double DEFAULT_WALK_SPEED = 0.22;
	public static final double DEFAULT_FLY_SPEED = 0.1;

	private String currentServer = "";

	@Element(name = "currency-amount")
	private int currencyAmount = 0;

	private ChatColor tagColor = ChatColor.WHITE;

	@Element(name = "prefix")
	private String prefix = "";

	/* Whether or not the player is currently reloading a gun */
	private long reloadEnd = 0;

	/**
	 * Location the player was before their last teleport
	 */
	private PreTeleportLocation preTeleportLocation;

	/**
	 * PlayerWrapper  initialization with assigning their currency to {@param currencyAmount}
	 *
	 * @param playerName     name of the player to be instanced
	 * @param currencyAmount currency the player has
	 */
	@Deprecated
	public MinecraftPlayer(String playerName, int currencyAmount) {
		setName(playerName);
		this.currencyAmount = currencyAmount;
		initWrapper();
	}

	public MinecraftPlayer(UUID id) {
		setId(id);
		setName(Players.getPlayer(id).getName());
		initWrapper();
	}

	public void dispose() {
//		nametagEntity.die();
//		nametagEntity = null;
		punishments = null;
	}

	private void initWrapper() {
		currentServer = commons.getConfiguration().getServerName();
		lastOnline = System.currentTimeMillis();
		setId(Players.getPlayer(getName()).getUniqueId());
		if (!Commons.getInstance().getConfiguration().hasSqlBackend()) {
//			friendsList = new FriendList(id);
			//TODO: Assign default prefix to user
			return;
		}

		//Create an async future to get the punishments for this player (and load them into the player wrapper instance)
		ListenableFuture<Set<Punishment>> punishmentListenable = Commons.getInstance().getAsyncExecuter().submit(new GetPlayerPunishmentsCallable(getId()));
		Futures.addCallback(punishmentListenable, new FutureCallback<Set<Punishment>>() {
			@Override
			public void onSuccess(Set<Punishment> punishmentSet) {
				if (punishmentSet == null) {
					return;
				}
				punishments = punishmentSet;
			}

			@Override
			public void onFailure(Throwable throwable) {
				throwable.printStackTrace();
			}
		});

//Create an async future to get the UUID of the player
		/*
		ListenableFuture<UUID> playerIdListenable = Commons.asyncExecutor.submit(new CallableGetPlayerUuid(getName()));
		Futures.addCallback(playerIdListenable, new FutureCallback<UUID>() {
			@Override
			public void onSuccess(UUID uuid) {
				id = uuid;
			}

			@Override
			public void onFailure(Throwable throwable) {
				throwable.printStackTrace();
			}
		});
		*/

//		prefix = Commons.database.getPrefix(this);
//
//		if (Commons.friendDatabase.hasData(playerName)) {
//			friendsList = new FriendList(id, Commons.friendDatabase.getFriends(playerName));
//		} else {
//			friendsList = new FriendList(id);
//		}
	}

	/**
	 * Change if the player is in the staff chat
	 *
	 * @param inStaffChat true if they're in staff chat, false otherwise
	 */
	public void setInStaffChat(boolean inStaffChat) {
		this.inStaffChat = inStaffChat;
	}

	/**
	 * Whether or not the player's in the staff chat
	 *
	 * @return true if they are, false otherwise
	 */
	public boolean isInStaffChat() {
		return inStaffChat;
	}

	/**
	 * Adds an amount of currency defined by <i>amountToAdd</i> to the player
	 *
	 * @param amountToAdd how much currency to add to the player
	 * @return The players currency after having <i>amountToAdd</i> added to it
	 */
	public int addCurrency(double amountToAdd) {
		currencyAmount += amountToAdd;
		return currencyAmount;
	}

	/**
	 * Remove an amount of currency defined by <i>amountToRemove</i> from the player
	 *
	 * @param amountToRemove how much currency to remove from the player
	 * @return The players currency after having <i>amountToRemove</i> from it
	 */
	public int removeCurrency(double amountToRemove) {
		currencyAmount -= amountToRemove;
		return currencyAmount;
	}

	/**
	 * Check if the player has atleast the amount of currency defined by <i>amount</i>
	 *
	 * @param amount amount to check
	 * @return true if the player has the same or more than <i>amount</i>, false otherwise
	 */
	public boolean hasCurrency(double amount) {
		return currencyAmount >= amount;
	}

	/**
	 * @return Players current currency amount
	 */
	public int getCurrency() {
		return currencyAmount;
	}

	/**
	 * Set the players amount of currency
	 *
	 * @param amount what the players currency is being set to
	 */
	public void setCurrency(int amount) {
		currencyAmount = amount;
	}

	/**
	 * Get the players current server
	 *
	 * @return
	 */
	public String getServer() {
		return currentServer;
	}

	/**
	 * Check if the player is a premium member
	 *
	 * @return
	 */
	public boolean isPremium() {
		return isPremium;
	}

	/**
	 * Set the users premium status
	 *
	 * @param isPremium
	 * @return
	 */
	public void setPremium(boolean isPremium) {
		if (!Commons.getInstance().getConfiguration().hasSqlBackend()) {
			return;
		}

		this.isPremium = isPremium;
//		Commons.database.updatePlayerPremium(this);
	}

	@Deprecated
	public ChatColor getTagColor() {
		return tagColor;
	}

	@Deprecated
	public void setTagColor(ChatColor tagColor) {
		this.tagColor = tagColor;
	}

//	/**
//	 * Gets the players friends list
//	 *
//	 * @return a FriendList object which contains the players friends; If there are no
//	 * Friend objects, then the friendslist is still returned though with no friend objects
//	 */
//	public FriendList getFriendsList() {
//		return friendsList;
//	}


	/**
	 * Check whether or not the player has a custom walk speed.
	 *
	 * @return whether or not the player has a custom walk speed.
	 */
	public boolean hasCustomWalkSpeed() {
		return walkSpeed != DEFAULT_WALK_SPEED;
	}

	/**
	 * Check whether or not the player has a modified fly speed.
	 * @return whether or not the player has a modified fly speed; If the players fly speed is greater than 0.1 then true is returned, false otherwise.
	 */
	public boolean hasCustomFlySpeed() {
		return flySpeed != DEFAULT_FLY_SPEED;
	}

	/**
	 * Get the players walk speed.
	 * @return the players active walk speed.
	 */
	public double getWalkSpeed() {
		return walkSpeed;
	}

	@Deprecated
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Change the players prefix.
	 * @param prefix prefix to give the player.
	 */
	@Deprecated
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Change the player walk speed; The higher the number, the faster they'll fly.
	 * Used internally {@link com.caved_in.commons.command.commands.SpeedCommand}
	 * @param walkSpeed speed to give the player.
	 */
	public void setWalkSpeed(double walkSpeed) {
		this.walkSpeed = walkSpeed;
		getPlayer().setWalkSpeed((float) walkSpeed);
	}

	/**
	 * Get the players active fly speed
	 * @return the active fly speed of the given player.
	 */
	public double getFlySpeed() {
		return flySpeed;
	}

	/**
	 * Change the players fly speed.
	 * The higher the number, the faster they'll fly.
	 * Used internally in {@link com.caved_in.commons.command.commands.SpeedCommand}
	 * @param flySpeed speed to give the player.
	 */
	public void setFlySpeed(double flySpeed) {
		this.flySpeed = flySpeed;
		getPlayer().setFlySpeed((float) flySpeed);
	}

	/**
	 * Check whether or not the player is viewing a recipe.
	 * Used internally.
	 * @return whether or not the player is viewing a recipe.
	 */
	public boolean isViewingRecipe() {
		return viewingRecipe;
	}

	/**
	 * Set whether or not the player is viewing a recipe menu, or not.
	 * Used internally in {@link com.caved_in.commons.command.commands.RecipeCommand}.
	 * @param viewingRecipe whether or not the player is viewing a recipe menu.
	 */
	public void setViewingRecipe(boolean viewingRecipe) {
		this.viewingRecipe = viewingRecipe;
	}

	/**
	 * @return the players location prior to their most recent teleport
	 */
	public PreTeleportLocation getPreTeleportLocation() {
		return preTeleportLocation;
	}

	/**
	 * Change the players pre-teleport location.
	 * Used internally in {@link com.caved_in.commons.command.commands.BackCommand}
	 * @param loc location the player was/is standing pre-teleport.
	 * @param teleportType the type of teleport the player was involved in.
	 */
	public void setPreTeleportLocation(Location loc, PreTeleportType teleportType) {
		this.preTeleportLocation = new PreTeleportLocation(loc, teleportType);
	}

	/**
	 * Check whether or not the player is in debug mode.
	 * Used
	 * @return whether or not the player is in debug mode.
	 */
	public boolean isInDebugMode() {
		return debugMode;
	}

	/**
	 * Set whether or not the player is in debug mode.
	 * Used internally in {@link com.caved_in.commons.command.commands.DebugModeCommand} and {@link com.caved_in.commons.player.Players} to manage player(s) debugging.
	 * @param value boolean representing the desired debug status.
	 */
	public void setInDebugMode(boolean value) {
		debugMode = value;
	}

	/**
	 * @return a set of the active punishments the player has
	 */
	public Set<Punishment> getPunishments() {
		return punishments;
	}

	/**
	 * Check whether or not the player is hiding other players.
	 * @return whether or not the player is hiding other players.
	 */
	public boolean isHidingOtherPlayers() {
		return hidingOtherPlayers;
	}

	/**
	 * Set whether or not the player is hiding others.
	 * Used internally to provide methods for {@link com.caved_in.commons.player.Players} in the methods related to player visibility (hide/unhide)
	 * @param hidingOtherPlayers whether or not the user is hiding other players.
	 */
	public void setHidingOtherPlayers(boolean hidingOtherPlayers) {
		this.hidingOtherPlayers = hidingOtherPlayers;
	}

	/**
	 * Check whether or not the player is reloading their weapon.
	 * @return
	 */
	public boolean isReloading() {
		return reloadEnd > System.currentTimeMillis();
	}

	/**
	 * Set the duration in seconds for the players current reload time. Used internally in
	 * {@link com.caved_in.commons.game.guns.Gun} and {@link com.caved_in.commons.game.guns.BaseGun}
	 * @param durationSeconds how long the player is "reloading" for.
	 */
	public void setReloading(int durationSeconds) {
		this.reloadEnd = System.currentTimeMillis() + TimeHandler.getTimeInMilles(durationSeconds, TimeType.SECOND);
	}
}
