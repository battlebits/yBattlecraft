package br.com.battlebits.ybattlecraft.event;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerRemoveKitEvent extends Event {

	public static HandlerList handlers = new HandlerList();
	private UUID playerUUID;
	private String kitName;
	private String warpName;

	public PlayerRemoveKitEvent(UUID id, String kit, String warp) {
		this.playerUUID = id;
		this.kitName = kit;
		this.warpName = warp;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(playerUUID);
	}

	public String getKitName() {
		return kitName;
	}

	public String getWarpName() {
		return warpName;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
