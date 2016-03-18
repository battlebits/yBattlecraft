package br.com.battlebits.ybattlecraft.event;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RealMoveEvent extends Event {
	public static HandlerList handlers = new HandlerList();
	private UUID playerUUID;
	private Location from;
	private Location to;

	public RealMoveEvent(Player player, Location from, Location to) {
		this.playerUUID = player.getUniqueId();
		this.from = from;
		this.to = to;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(playerUUID);
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public Location getFrom() {
		return from;
	}

	public Location getTo() {
		return to;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}