package br.com.battlebits.battlecraft.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import br.com.battlebits.battlecraft.constructors.Kit;

public class PlayerRemoveKitEvent extends Event {
	public static final HandlerList handlers = new HandlerList();
	private Player player;
	private Kit kit;

	public PlayerRemoveKitEvent(Player player, Kit kit) {
		this.player = player;
		this.kit = kit;
	}

	public Player getPlayer() {
		return player;
	}

	public Kit getKit() {
		return kit;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
