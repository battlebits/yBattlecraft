package br.com.battlebits.ybattlecraft.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.event.PlayerDamagePlayerEvent;

public class PlayerDamageByPlayerListener extends BaseListener {

	public PlayerDamageByPlayerListener(yBattleCraft bc) {
		super(bc);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player))
			return;
		if (!(event.getEntity() instanceof Player))
			return;
		if (event.isCancelled())
			return;
		PlayerDamagePlayerEvent event2 = new PlayerDamagePlayerEvent((Player) event.getDamager(), (Player) event.getEntity(), event.getDamage(), event.getFinalDamage());
		Bukkit.getPluginManager().callEvent(event2);
		event.setCancelled(event2.isCancelled());
	}

}
