package br.com.battlebits.ybattlecraft.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.battlebits.ybattlecraft.yBattleCraft;

public class QuitListener implements Listener {

	private yBattleCraft m;

	public QuitListener(yBattleCraft m) {
		this.m = m;
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
