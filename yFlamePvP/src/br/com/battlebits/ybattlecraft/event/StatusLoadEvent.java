package br.com.battlebits.ybattlecraft.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import br.com.battlebits.ybattlecraft.constructors.Status;
import br.com.battlebits.ybattlecraft.enums.LoadStatus;

public class StatusLoadEvent extends Event {
	public static final HandlerList handlers = new HandlerList();
	private Player player;
	private Status status;
	private LoadStatus loadStatus;

	public StatusLoadEvent(Player player, Status status, LoadStatus load) {
		this.player = player;
		this.status = status;
		this.loadStatus = load;
	}

	public Player getPlayer() {
		return player;
	}

	public Status getStatus() {
		return status;
	}

	public LoadStatus getLoadStatus() {
		return loadStatus;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
