package br.com.battlebits.ybattlecraft.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseListener;
import br.com.battlebits.ybattlecraft.event.PlayerRemoveKitEvent;
import br.com.battlebits.ybattlecraft.event.PlayerSelectKitEvent;

public class AbilityListener extends BaseListener {

	public AbilityListener(yBattleCraft plugin) {
		super(plugin);
	}

	@EventHandler
	public void onPlayerQuitListener(PlayerQuitEvent e) {
		battleCraft.getAbilityManager().resetPlayerAbilities(e.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onPlayerSelectKitListener(PlayerSelectKitEvent e) {
		battleCraft.getAbilityManager().resetPlayerAbilities(e.getPlayerUUID());
		for (String ability : battleCraft.getWarpManager().getWarpByName(e.getWarpName()).getKit(e.getKitName().toLowerCase()).getAbilities()) {
			battleCraft.getAbilityManager().addPlayerAbility(e.getPlayerUUID(), ability.toLowerCase());
		}
	}

	@EventHandler
	public void onPlayerRemoveKitListener(PlayerRemoveKitEvent e) {
		battleCraft.getAbilityManager().resetPlayerAbilities(e.getPlayer().getUniqueId());
	}

}
