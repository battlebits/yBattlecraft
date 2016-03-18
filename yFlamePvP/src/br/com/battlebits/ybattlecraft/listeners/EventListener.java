package br.com.battlebits.ybattlecraft.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.event.PlayerDeathInWarpEvent;
import br.com.battlebits.ybattlecraft.evento.enums.EventType;

public class EventListener implements Listener {

	private yBattleCraft m;

	public EventListener(yBattleCraft m) {
		this.m = m;
	}

	@EventHandler
	public void onChatonEvento(AsyncPlayerChatEvent event) {
		if (yBattleCraft.currentEvento != null && yBattleCraft.currentEvento.isOnEvent(event.getPlayer())) {
			if (!yBattleCraft.currentEvento.isChatEnabled()) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "Voce nao pode falar no evento para evitar flood! Aguarde sua vez");
			}
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathInWarpEvent e) {
		Player p = e.getPlayer();
		if (yBattleCraft.currentEvento != null && yBattleCraft.currentEvento.isOnEvent(p) && yBattleCraft.currentEvento.getType() == EventType.RDM) {
			if (e.hasKiller()) {
				m.getKitManager().removeKit(e.getKiller());
				//TODO: GIVE EVENT KIT
//				m.getKitManager().giveKit(e.getKiller(), "pvp", false);
				e.getKiller().setHealth(20);
				yBattleCraft.currentEvento.broadcastMessage(ChatColor.BLUE + e.getKiller().getName() + " virou o Rei da Mesa!");
				yBattleCraft.currentEvento.removePlayer(p);
			}
			return;
		}
	}

}
