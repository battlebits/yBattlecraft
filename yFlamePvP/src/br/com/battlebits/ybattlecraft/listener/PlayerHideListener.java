package br.com.battlebits.ybattlecraft.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.battlebits.commons.bukkit.event.vanish.PlayerShowToPlayerEvent;
import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.base.BaseListener;
import br.com.battlebits.ybattlecraft.event.PlayerWarpJoinEvent;

public class PlayerHideListener extends BaseListener {

	public PlayerHideListener(Battlecraft plugin) {
		super(plugin);
	}

	@EventHandler
	public void onPlayerQuitListener(PlayerQuitEvent e) {
		battleCraft.getPlayerHideManager().tryToRemoveFromLists(e.getPlayer().getUniqueId());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoinWarp(PlayerWarpJoinEvent e) {
		battleCraft.getPlayerHideManager().showPlayer(e.getPlayer());
	}

	@EventHandler
	public void onVanish(PlayerShowToPlayerEvent event) {
		if (battleCraft.getPlayerHideManager().isHidingAllPlayers(event.getToPlayer())) {
			event.setCancelled(true);
		} else if (battleCraft.getPlayerHideManager().isHiding(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

}
