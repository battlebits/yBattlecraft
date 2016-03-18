package br.com.battlebits.ybattlecraft.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import br.com.battlebits.ybattlecraft.event.PlayerDamagePlayerEvent;

public class PlayerDamageByPlayerListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player))
			return;
		if (!(event.getEntity() instanceof Player))
			return;
		if (event.isCancelled())
			return;
		PlayerDamagePlayerEvent event2 = new PlayerDamagePlayerEvent((Player) event.getDamager(), (Player) event.getEntity(), event.getDamage());
		Bukkit.getPluginManager().callEvent(event2);
		event.setCancelled(event2.isCancelled());
	}

}
