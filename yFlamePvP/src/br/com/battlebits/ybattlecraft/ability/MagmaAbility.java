package br.com.battlebits.ybattlecraft.ability;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseAbility;
import br.com.battlebits.ybattlecraft.event.PlayerDamagePlayerEvent;
import me.flame.utils.event.UpdateEvent;
import me.flame.utils.event.UpdateEvent.UpdateType;

public class MagmaAbility extends BaseAbility {

	private Random random;

	public MagmaAbility(yBattleCraft yBattleCraft) {
		super(yBattleCraft);
		random = new Random();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageListener(EntityDamageEvent e) {
		if (e.getCause() == DamageCause.FIRE || e.getCause() == DamageCause.LAVA || e.getCause() == DamageCause.FIRE_TICK
				|| e.getCause() == DamageCause.LIGHTNING) {
			if (e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				if (isUsing(p)) {
					e.setCancelled(true);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onUpdateListener(UpdateEvent e) {
		if (e.getType() == UpdateType.SECOND) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (isUsing(p)) {
					if (!battlecraft.getAdminMode().isAdmin(p)) {
						if (!battlecraft.getProtectionManager().isProtected(p.getUniqueId())) {
							if (p.getLocation().getBlock().getType() == Material.WATER
									|| p.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
								p.damage(1.0);
							}
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDamageListener(PlayerDamagePlayerEvent e) {
		if (isUsing(e.getDamager())) {
			if (random.nextInt(9) == 0) {
				e.getDamaged().setFireTicks(100);
			}
		}
	}

}
