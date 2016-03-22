package br.com.battlebits.ybattlecraft.ability;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseAbility;

public class StomperAbility extends BaseAbility {

	public StomperAbility(yBattleCraft yBattleCraft) {
		super(yBattleCraft);
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageListener(EntityDamageEvent e) {
		if (e.getCause() == DamageCause.FALL) {
			if (e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				if (isUsing(p)) {
					for (Player ps : p.getWorld().getEntitiesByClass(p.getClass())) {
						if (ps.getUniqueId() != p.getUniqueId()) {
							if (!battlecraft.getProtectionManager().isProtected(ps.getUniqueId())) {
								if (!battlecraft.getAdminMode().isAdmin(ps)) {
									if (p.getLocation().distance(ps.getLocation()) <= 5) {
										double dmg = e.getDamage();
										if (ps.isSneaking() && dmg > 8) {
											dmg = 8;
										}
										ps.damage(dmg, p);
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
