package br.com.battlebits.ybattlecraft.ability;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import br.com.battlebits.commons.api.admin.AdminMode;
import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseAbility;
import br.com.battlebits.ybattlecraft.event.PlayerDeathInWarpEvent;

public class StomperAbility extends BaseAbility {

	public StomperAbility(yBattleCraft yBattleCraft) {
		super(yBattleCraft);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDamageListener(EntityDamageEvent e) {
		if (e.getCause() == DamageCause.FALL) {
			if (e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				if (isUsing(p)) {
					p.getWorld().playSound(p.getLocation(), Sound.ANVIL_LAND, 0.15F, 1.0F);
					for (Player ps : p.getWorld().getEntitiesByClass(p.getClass())) {
						if (ps.getUniqueId() != p.getUniqueId()) {
							if (!battlecraft.getProtectionManager().isProtected(ps.getUniqueId())) {
								if (!AdminMode.getInstance().isAdmin(ps)) {
									if (p.getLocation().distance(ps.getLocation()) <= 5) {
										double dmg = e.getDamage();
										if (ps.isSneaking() && dmg > 8) {
											dmg = 8;
										}
										if (dmg >= ((Damageable) ps).getHealth()) {
											Bukkit.getPluginManager().callEvent(new PlayerDeathInWarpEvent(ps, p, battlecraft.getWarpManager().getPlayerWarp(ps.getUniqueId())));
										} else {
											ps.damage(dmg, p);
										}
									}
								}
							}
						}
					}
					if (e.getDamage() > 4) {
						e.setDamage(4);
					}
				}
			}
		}
	}

}
