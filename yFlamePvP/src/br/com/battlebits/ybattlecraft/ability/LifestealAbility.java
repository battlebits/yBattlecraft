package br.com.battlebits.ybattlecraft.ability;

import java.util.Random;

import org.bukkit.entity.Damageable;
import org.bukkit.event.EventHandler;

import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.base.BaseAbility;
import br.com.battlebits.ybattlecraft.event.PlayerDamagePlayerEvent;

public class LifestealAbility extends BaseAbility {

	private Random random;

	public LifestealAbility(Battlecraft Battlecraft) {
		super(Battlecraft);
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
