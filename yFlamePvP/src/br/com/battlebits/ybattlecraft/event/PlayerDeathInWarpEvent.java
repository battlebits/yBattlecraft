package br.com.battlebits.ybattlecraft.event;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerDeathInWarpEvent extends Event {

	public static HandlerList handlers = new HandlerList();
	private UUID playerUUID;
	private UUID killerUUID;
	private String kitName;
	private boolean canceled;
	private String warp;

	public PlayerDeathInWarpEvent(Player p, Player killer, String warpName) {
		this.playerUUID = p.getUniqueId();
		if (killer != null) {
			this.killerUUID = killer.getUniqueId();
		}
		this.warp = warpName;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(playerUUID);
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}
	
	public String getWarp() {
		return warp;
	}

	public UUID getKillerUUID() {
		return killerUUID;
	}

	public Player getKiller() {
		return Bukkit.getPlayer(killerUUID);
	}

	public boolean hasKiller() {
		return killerUUID != null;
	}

	public String getKitName() {
		return kitName;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
