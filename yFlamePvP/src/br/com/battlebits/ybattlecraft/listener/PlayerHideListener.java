package br.com.battlebits.ybattlecraft.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseListener;
import br.com.battlebits.ybattlecraft.event.PlayerWarpJoinEvent;

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
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoinWarp(PlayerWarpJoinEvent e){
		battleCraft.getPlayerHideManager().showForAll(e.getPlayer());
	}

}
