package br.com.battlebits.ybattlecraft.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.event.PlayerDeathInWarpEvent;
import br.com.battlebits.ybattlecraft.evento.enums.EventType;

public class EventListener implements Listener {

	private Battlecraft m;

	public EventListener(Battlecraft m) {
		this.m = m;
	}

	@EventHandler
	public void onChatonEvento(AsyncPlayerChatEvent event) {
		if (Battlecraft.currentEvento != null && Battlecraft.currentEvento.isOnEvent(event.getPlayer())) {
			if (!Battlecraft.currentEvento.isChatEnabled()) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "Voce nao pode falar no evento para evitar flood! Aguarde sua vez");
			}
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathInWarpEvent e) {
		Player p = e.getPlayer();
		if (Battlecraft.currentEvento != null && Battlecraft.currentEvento.isOnEvent(p) && Battlecraft.currentEvento.getType() == EventType.RDM) {
			if (e.hasKiller()) {
				m.getKitManager().removeKit(e.getKiller());
				//TODO: GIVE EVENT KIT
//				m.getKitManager().giveKit(e.getKiller(), "pvp", false);
				e.getKiller().setHealth(20);
				Battlecraft.currentEvento.broadcastMessage(ChatColor.BLUE + e.getKiller().getName() + " virou o Rei da Mesa!");
				Battlecraft.currentEvento.removePlayer(p);
			}
			return;
		}
	}

}
