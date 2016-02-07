package br.com.battlebits.battlecraft.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RealMoveEvent extends Event {
	public static final HandlerList handlers = new HandlerList();
	private Player player;
	private Location from;
	private Location to;

	public RealMoveEvent(Player player, Location from, Location to) {
		this.player = player;
		this.from = from;
		this.to = to;
	}

	public Player getPlayer() {
		return player;
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