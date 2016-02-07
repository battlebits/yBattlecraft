package br.com.battlebits.battlecraft.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import br.com.battlebits.battlecraft.constructors.Warp;

public class WarpTeleportEvent extends Event {
	public static final HandlerList handlers = new HandlerList();
	private Player player;
	private Warp warp;
	private boolean canceled;
	private boolean aviso;

	public WarpTeleportEvent(Player player, Warp warp, boolean aviso) {
		this.player = player;
		this.warp = warp;
		this.aviso = aviso;
		this.canceled = false;
	}

	public Player getPlayer() {
		return player;
	}

	public Warp getWarp() {
		return warp;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public boolean hasAviso() {
		return aviso;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public void setAviso(boolean aviso) {
		this.aviso = aviso;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
