package br.com.battlebits.ybattlecraft.ability;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseAbility;
import br.com.battlebits.ybattlecraft.event.PlayerDeathInWarpEvent;

public class AnchorAbility extends BaseAbility {

	public AnchorAbility(yBattleCraft yBattleCraft) {
		super(yBattleCraft);
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDamagePlayerListener(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getDamager() instanceof Player) {
				Player d = (Player) e.getDamager();
				if ((!battlecraft.getProtectionManager().isProtected(p.getUniqueId()))
						&& (!battlecraft.getProtectionManager().isProtected(d.getUniqueId()))) {
					if (isUsing(p) || isUsing(d)) {
						p.getWorld().playSound(p.getLocation(), Sound.ANVIL_LAND, 0.15F, 1.0F);
						if (e.getDamage() >= ((Damageable) p).getHealth()) {
							Bukkit.getPluginManager()
									.callEvent(new PlayerDeathInWarpEvent(p, d, battlecraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
						} else {
							p.damage(e.getFinalDamage());
							p.setLastDamageCause(e);
							e.setCancelled(true);
						}
					}
				}
			} else if (e.getDamager() instanceof Projectile) {
				Projectile pro = (Projectile) e.getDamager();
				if(pro.getShooter() != null && pro.getShooter() instanceof Player){
					Player d = (Player) pro.getShooter();
					if ((!battlecraft.getProtectionManager().isProtected(p.getUniqueId()))
							&& (!battlecraft.getProtectionManager().isProtected(d.getUniqueId()))) {
						if (isUsing(p) || isUsing(d)) {
							p.getWorld().playSound(p.getLocation(), Sound.ANVIL_LAND, 0.15F, 1.0F);
							if (e.getDamage() >= ((Damageable) p).getHealth()) {
								Bukkit.getPluginManager()
										.callEvent(new PlayerDeathInWarpEvent(p, d, battlecraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
							} else {
								p.damage(e.getFinalDamage());
								p.setLastDamageCause(e);
								e.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}

}
