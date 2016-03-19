package br.com.battlebits.ybattlecraft.ability.type;

import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.ability.BaseAbility;

public class AnchorAbility extends BaseAbility {

	public AnchorAbility(yBattleCraft yBattleCraft) {
		super(yBattleCraft);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDamagePlayerListener(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			Player p = (Player) e.getEntity();
			Player d = (Player) e.getDamager();
			if (isUsing(p) || isUsing(d)) {
				p.getWorld().playSound(p.getLocation(), Sound.ANVIL_LAND, 0.15F, 1.0F);
				if (e.getFinalDamage() < ((Damageable) p).getHealth()) {
					p.damage(e.getFinalDamage());
					e.setCancelled(true);
				}
			}
		}
	}

}
