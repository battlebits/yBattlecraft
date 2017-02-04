package br.com.battlebits.ybattlecraft.listener;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.constructors.Status;
import br.com.battlebits.ybattlecraft.manager.ProtectionManager;
import br.com.battlebits.ybattlecraft.warps.Warp1v1;

public class DamageListener implements Listener {

	private Battlecraft m;
	private ProtectionManager manager;

	public DamageListener(Battlecraft m) {
		this.m = m;
		manager = this.m.getProtectionManager();
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		Player player = (Player) event.getEntity();
		if (m.getProtectionManager().isProtected(player.getUniqueId()))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onProtectionLose(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		Player p = (Player) event.getEntity();
		if (Warp1v1.isIn1v1(p))
			return;
		if (m.getWarpManager().isInWarp(p, "lavachallenge")) {
			event.setCancelled(true);
			return;
		}
		if (m.getWarpManager().isInWarp(p, "1v1")) {
			event.setCancelled(true);
			return;
		}
		if (manager.isProtected(p.getUniqueId()))
			event.setCancelled(true);
	}

	@EventHandler
	public void onEntityDamageByEntityListener(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			Player damager = null;
			if (e.getDamager() instanceof Player) {
				damager = (Player) e.getDamager();
			} else if (e.getDamager() instanceof Projectile) {
				Projectile pr = (Projectile) e.getDamager();
				if (pr.getShooter() != null && pr.getShooter() instanceof Player) {
					damager = (Player) pr.getShooter();
				}
			}
			if (damager != null) {
				if ((m.getWarpManager().isInWarp(damager, "lavachallenge") || m.getWarpManager().isInWarp(damager, "1v1"))) {
					if (!Warp1v1.isIn1v1(damager)) {
						e.setCancelled(true);
					}
				} else {
					if (m.getProtectionManager().isProtected(damager.getUniqueId())) {
						if (m.getKitManager().hasCurrentKit(damager.getUniqueId())) {
							manager.removeProtection(damager.getUniqueId());
							m.getPlayerHideManager().showForAll(damager);
							damager.sendMessage("§7§lPROTEÇÃO §FVocê §8§lPERDEU§f sua proteção de spawn");
						} else {
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onStatus(EntityDamageByEntityEvent e) {
		if (e.isCancelled())
			return;
		if (e.getEntity() instanceof Player) {
			Player damaged = (Player) e.getEntity();
			Player damager = null;
			if (e.getDamager() instanceof Player) {
				damager = (Player) e.getDamager();
			} else if (e.getDamager() instanceof Projectile) {
				Projectile pr = (Projectile) e.getDamager();
				if (pr.getShooter() != null && pr.getShooter() instanceof Player) {
					damager = (Player) pr.getShooter();
				}
			}
			if (damager != null) {
				Status damagedStatus = m.getStatusManager().getStatusByUuid(damaged.getUniqueId());
				damagedStatus.addDamage(damager.getUniqueId(), e.getDamage());
			}
		}
	}
}
