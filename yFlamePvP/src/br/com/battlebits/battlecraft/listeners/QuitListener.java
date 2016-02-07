package br.com.battlebits.battlecraft.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.battlebits.battlecraft.Main;

public class QuitListener implements Listener {

	private Main m;

	public QuitListener(Main m) {
		this.m = m;
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		m.getAdminMode().removeAdmin(event.getPlayer());
		m.getVanish().removeVanished(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onRemoveMessag(PlayerQuitEvent event) {
		event.setQuitMessage(null);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onRemoveStatus(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		m.getStatusManager().removePlayer(p);
	}
}
