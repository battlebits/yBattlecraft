package br.com.battlebits.battlecraft.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.evento.enums.EventType;

public class EventListener implements Listener {

	private Main m;

	public EventListener(Main m) {
		this.m = m;
	}

	@EventHandler
	public void onChatonEvento(AsyncPlayerChatEvent event) {
		if (Main.currentEvento != null && Main.currentEvento.isOnEvent(event.getPlayer())) {
			if (!Main.currentEvento.isChatEnabled()) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "Voce nao pode falar no evento para evitar flood! Aguarde sua vez");
			}
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		event.setDeathMessage(null);
		Player p = event.getEntity();
		if (Main.currentEvento != null && Main.currentEvento.isOnEvent(p) && Main.currentEvento.getType() == EventType.RDM) {
			if (p.getKiller() != null) {
				m.getKitManager().removeKit(p.getKiller());
				m.getKitManager().giveKit(p.getKiller(), "pvp");
				p.getKiller().setHealth(20);
				Main.currentEvento.broadcastMessage(ChatColor.BLUE + p.getKiller().getName() + " virou o Rei da Mesa!");
				Main.currentEvento.removePlayer(p);
			}
			return;
		}
	}

}
