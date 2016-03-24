package br.com.battlebits.ybattlecraft.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseListener;

public class PlayerHideListener extends BaseListener {

	public PlayerHideListener(yBattleCraft plugin) {
		super(plugin);
	}

	@EventHandler
	public void onPlayerJoinListener(PlayerJoinEvent e) {
		battleCraft.getPlayerHideManager().playerJoin(e.getPlayer());
	}

	@EventHandler
	public void onPlayerQuitListener(PlayerQuitEvent e) {
		battleCraft.getPlayerHideManager().tryToRemoveFromLists(e.getPlayer().getUniqueId());
	}

}
