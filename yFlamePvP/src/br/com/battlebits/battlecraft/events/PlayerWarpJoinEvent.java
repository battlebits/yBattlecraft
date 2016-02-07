package br.com.battlebits.battlecraft.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import br.com.battlebits.battlecraft.constructors.Warp;

public class PlayerWarpJoinEvent extends Event {
	public static final HandlerList handlers = new HandlerList();

	private Player player;
	private Warp warp;

	public PlayerWarpJoinEvent(Player player, Warp warp) {
		this.player = player;
		this.warp = warp;
	}

	public Player getPlayer() {
		return player;
	}

	public Warp getWarp() {
		return warp;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
