package br.com.battlebits.ybattlecraft.ability.type;

import java.util.Random;

import org.bukkit.entity.Damageable;
import org.bukkit.event.EventHandler;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.ability.BaseAbility;
import br.com.battlebits.ybattlecraft.event.PlayerDamagePlayerEvent;

public class LifestealAbility extends BaseAbility {

	private Random random;

	public LifestealAbility(yBattleCraft yBattleCraft) {
		super(yBattleCraft);
		random = new Random();
	}

	@EventHandler
	public void onPlayerDamagePlayer(PlayerDamagePlayerEvent e) {
		if (isUsing(e.getDamager())) {
			if (e.getDamager().getItemInHand() != null) {
				if (random.nextInt(4) == 0) {
					if (((Damageable) e.getDamager()).getHealth() < ((Damageable) e.getDamager()).getMaxHealth()) {
						e.getDamager().setHealth(((Damageable) e.getDamager()).getHealth() + 1.0);
					}
				}
			}
		}
	}

}
