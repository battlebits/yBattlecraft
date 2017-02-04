package br.com.battlebits.ybattlecraft.ability;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.base.BaseAbility;
import br.com.battlebits.ybattlecraft.event.PlayerDamagePlayerEvent;

public class ViperAbility extends BaseAbility {

	private Random random;
	private PotionEffect potionEffect;

	public ViperAbility(Battlecraft Battlecraft) {
		super(Battlecraft);
		random = new Random();
		potionEffect = new PotionEffect(PotionEffectType.POISON, 100, 0);
	}

	@EventHandler
	public void onPlayerDamagePlayerListener(PlayerDamagePlayerEvent e) {
		if (isUsing(e.getDamager())) {
			if (random.nextInt(2) == 0) {
				e.getDamaged().getLocation().getWorld().playEffect(e.getDamaged().getLocation().add(0.0D, 0.4D, 0.0D), Effect.STEP_SOUND, 18,
						(short) 1);
				e.getDamaged().addPotionEffect(potionEffect);
			}
		}
	}
	
}
