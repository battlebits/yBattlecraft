package br.com.battlebits.battlecraft.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.battlebits.battlecraft.combatlog.CombatLog;
import br.com.battlebits.battlecraft.events.PlayerDamagePlayerEvent;
import br.com.battlebits.battlecraft.managers.CombatLogManager;

public class CombatLogListener implements Listener {

	private CombatLogManager manager;

	public CombatLogListener(CombatLogManager manager) {
		this.manager = manager;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDamage(PlayerDamagePlayerEvent event) {
		Player damager = event.getDamager();
		Player damaged = event.getDamaged();
		this.manager.newCombatLog(damaged.getUniqueId(), damager.getUniqueId());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();
		this.manager.removeCombatLog(p.getUniqueId());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		CombatLog log = this.manager.getCombatLog(p.getUniqueId());
		if (log == null)
			return;
		if (System.currentTimeMillis() < log.getTime()) {
			Player combatLogger = Bukkit.getPlayer(log.getCombatLogged());
			if (combatLogger != null)
				if (combatLogger.isOnline())
					p.damage(10000.0, combatLogger);
		}
		this.manager.removeCombatLog(p.getUniqueId());
	}

	@EventHandler
	public void onVoidDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		if (event.getCause() != DamageCause.VOID)
			return;
		Player p = (Player) event.getEntity();
		CombatLog log = this.manager.getCombatLog(p.getUniqueId());
		if (log == null)
			return;
		if (System.currentTimeMillis() < log.getTime()) {
			Player combatLogger = Bukkit.getPlayer(log.getCombatLogged());
			if (combatLogger != null)
				if (combatLogger.isOnline())
					p.damage(10000.0, combatLogger);
		}
		this.manager.removeCombatLog(p.getUniqueId());
	}

}
