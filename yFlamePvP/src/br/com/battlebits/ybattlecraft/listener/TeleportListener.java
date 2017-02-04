package br.com.battlebits.ybattlecraft.listener;

import org.bukkit.event.EventHandler;

import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.base.BaseListener;
import br.com.battlebits.ybattlecraft.event.PlayerDamagePlayerEvent;
import br.com.battlebits.ybattlecraft.event.RealMoveEvent;

public class TeleportListener extends BaseListener {

	public TeleportListener(Battlecraft plugin) {
		super(plugin);
	}

	@EventHandler
	public void onPlayerDamagePlayerListener(PlayerDamagePlayerEvent e) {
		if (battleCraft.getTeleportManager().isTeleporting(e.getDamager().getUniqueId())) {
			e.getDamager().sendMessage("§9§LTELEPORTE §fSeu teleporte foi §3§lCANCELADO§f pois voce entrou em combate!");
			battleCraft.getTeleportManager().stopAllTeleports(e.getDamager().getUniqueId());
		}
		if (battleCraft.getTeleportManager().isTeleporting(e.getDamaged().getUniqueId())) {
			e.getDamaged().sendMessage("§9§LTELEPORTE §fSeu teleporte foi §3§lCANCELADO§f pois voce entrou em combate!");
			battleCraft.getTeleportManager().stopAllTeleports(e.getDamaged().getUniqueId());
		}
	}

	@EventHandler
	public void onPlayerRealMoveListener(RealMoveEvent e) {
		if (battleCraft.getTeleportManager().isTeleporting(e.getPlayerUUID())) {
			e.getPlayer().sendMessage("§9§LTELEPORTE §fSeu teleporte foi §3§lCANCELADO§f pois voce se mexeu!");
			battleCraft.getTeleportManager().stopAllTeleports(e.getPlayerUUID());
		}
	}

}
