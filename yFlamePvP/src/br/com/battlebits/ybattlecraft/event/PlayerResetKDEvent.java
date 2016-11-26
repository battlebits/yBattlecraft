package br.com.battlebits.ybattlecraft.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerResetKDEvent extends Event {

	public static HandlerList handlers = new HandlerList();
	private Player player;

	public PlayerResetKDEvent(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
