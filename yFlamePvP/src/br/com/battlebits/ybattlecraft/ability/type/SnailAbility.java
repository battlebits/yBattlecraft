package br.com.battlebits.ybattlecraft.ability.type;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.ability.BaseAbility;
import br.com.battlebits.ybattlecraft.event.PlayerDamagePlayerEvent;

public class SnailAbility extends BaseAbility {

	private Random random;
	private PotionEffect potionEffect;

	public SnailAbility(yBattleCraft yBattleCraft) {
		super(yBattleCraft);
		potionEffect = new PotionEffect(PotionEffectType.SLOW, 5 * 20, 0);
		random = new Random();
	}

	@EventHandler
	public void onPlayerDamagePlayerListener(PlayerDamagePlayerEvent e) {
		if (isUsing(e.getDamager())) {
			if (random.nextInt(2) == 0) {
				e.getDamaged().addPotionEffect(potionEffect);
				e.getDamaged().getWorld().playEffect(e.getDamaged().getLocation().add(0D, 0.4D, 0D), Effect.STEP_SOUND, 159, (short) 13);
			}
		}
	}

}
