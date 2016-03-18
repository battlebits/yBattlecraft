package br.com.battlebits.ybattlecraft.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.battlebits.ybattlecraft.combatlog.CombatLog;
import br.com.battlebits.ybattlecraft.event.PlayerDamagePlayerEvent;
import br.com.battlebits.ybattlecraft.event.PlayerDeathInWarpEvent;
import br.com.battlebits.ybattlecraft.managers.CombatLogManager;

public class CombatLogListener implements Listener {

	private CombatLogManager manager;

	public CombatLogListener(CombatLogManager manager) {
		this.manager = manager;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDamage(PlayerDamagePlayerEvent e) {
		Player damager = e.getDamager();
		Player damaged = e.getDamaged();
		this.manager.newCombatLog(damaged.getUniqueId(), damager.getUniqueId());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onDeath(PlayerDeathInWarpEvent e) {
		this.manager.removeCombatLog(e.getPlayerUUID());
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
