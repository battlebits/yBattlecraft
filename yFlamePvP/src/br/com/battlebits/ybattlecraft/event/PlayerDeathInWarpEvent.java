package br.com.battlebits.ybattlecraft.event;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerDeathInWarpEvent extends PlayerEvent {

	public static HandlerList handlers = new HandlerList();
	private Player killer;
	private String kitName;
	private String warp;

	public PlayerDeathInWarpEvent(Player p, Player killer, String warpName) {
		super(p);
		if (killer != null) {
			this.killer = killer;
		}
		this.warp = warpName;
	}

	public String getWarp() {
		return warp;
	}

	public UUID getPlayerUUID() {
		return player.getUniqueId();
	}

	public Player getKiller() {
		return killer;
	}

	public UUID getKillerUUID() {
		return killer.getUniqueId();
	}

	public boolean hasKiller() {
		return killer != null;
	}

	public String getKitName() {
		return kitName;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
