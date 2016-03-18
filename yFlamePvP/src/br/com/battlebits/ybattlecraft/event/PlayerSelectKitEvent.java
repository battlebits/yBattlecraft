package br.com.battlebits.ybattlecraft.event;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerSelectKitEvent extends Event implements Cancellable {

	public static HandlerList handlers = new HandlerList();
	private UUID playerUUID;
	private String warpName;
	private String kitName;
	private boolean canceled;

	public PlayerSelectKitEvent(Player p, String kit, String warp) {
		this.playerUUID = p.getUniqueId();
		this.kitName = kit;
		this.warpName = warp;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(playerUUID);
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public String getKitName() {
		return kitName;
	}

	public String getWarpName() {
		return warpName;
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

	@Override
	public boolean isCancelled() {
		return canceled;
	}

	@Override
	public void setCancelled(boolean canceled) {
		this.canceled = canceled;
	}
}
