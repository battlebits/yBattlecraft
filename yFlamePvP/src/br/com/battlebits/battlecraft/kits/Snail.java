package br.com.battlebits.battlecraft.kits;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.Kit;
import br.com.battlebits.battlecraft.enums.KitType;
import br.com.battlebits.battlecraft.interfaces.KitInterface;

public class Snail extends KitInterface {

	public Snail(Main main) {
		super(main);

	}

	@EventHandler
	public void onSnail(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		if (!(event.getDamager() instanceof Player))
			return;
		Player p = (Player) event.getEntity();
		Player snail = (Player) event.getDamager();
		final Location loc = p.getLocation();
		if (!(snail instanceof Player))
			return;
		if (!hasAbility(snail)) {
			return;
		}
		Random r = new Random();
		if (p instanceof Player) {
			if (r.nextInt(3) == 0) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 0));
				p.getLocation().getWorld().playEffect(loc.add(0.0D, 0.4D, 0.0D), Effect.STEP_SOUND, 159, (short) 13);
			}
		}
	}

	@Override
	public Kit getKit() {
		return new Kit("snail", "Deixe seus inimigos mais lerdos", new ArrayList<ItemStack>(), new ItemStack(Material.FERMENTED_SPIDER_EYE), 1000, KitType.FORCA);
	}
}
