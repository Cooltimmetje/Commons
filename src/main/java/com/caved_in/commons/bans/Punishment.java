package com.caved_in.commons.bans;

import com.caved_in.commons.time.TimeHandler;
import com.caved_in.commons.time.TimeType;

import java.util.UUID;

/**
 * @author Brandon Curtis
 * @version 1.0
 * @see com.caved_in.commons.bans.IPunishment
 * @since 1.0
 */
public class Punishment implements IPunishment {
	private PunishmentType punishmentType;
	private long expiryTime;
	private long issuedTime;
	private boolean active;
	private String reason;
	private UUID issuer;

	public boolean permanent = false;

	public Punishment() {
	}

	/**
	 * Constructs a new punishment object
	 *
	 * @param punishmentType type of the punishment being issued
	 * @param expiry         when the punishment will expire
	 * @param issued         when the punishment was issued
	 * @param isActive       whether or not the punishment is active
	 * @param reason         the reason for the punishment being issued
	 * @param issuer         the person / name of who issued the punishment
	 */
	public Punishment(PunishmentType punishmentType, long expiry, long issued, boolean isActive, String reason, UUID issuer) {
		this.punishmentType = punishmentType;
		this.expiryTime = expiry;
		this.issuedTime = issued;
		this.active = isActive;
		this.reason = reason;
		this.issuer = issuer;
	}


	@Override
	public PunishmentType getPunishmentType() {
		return punishmentType;
	}

	@Override
	public long getExpiryTime() {
		return expiryTime;
	}

	@Override
	public long getIssuedTime() {
		return issuedTime;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public String getReason() {
		return reason;
	}

	@Override
	public UUID getIssuer() {
		return issuer;
	}

	public void setPunishmentType(PunishmentType punishmentType) {
		this.punishmentType = punishmentType;
	}

	public void setExpiryTime(long expiryTime) {
		this.expiryTime = expiryTime;
	}

	public void setIssuedTime(long issuedTime) {
		this.issuedTime = issuedTime;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setIssuer(UUID uuid) {
		issuer = uuid;
	}

	public boolean isPermanent() {
		return permanent;
	}

	public void setPermanent(boolean val) {
		this.permanent = val;
		if (val) {
			expiryTime = System.currentTimeMillis() + TimeHandler.getTimeInMilles(10, TimeType.YEAR);
		}
	}
}
