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

public class Viper extends KitInterface {

	public Viper(Main main) {
		super(main);

	}

	@EventHandler
	public void onSnail(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		if (!(event.getDamager() instanceof Player))
			return;
		Player p = (Player) event.getEntity();
		Player viper = (Player) event.getDamager();
		final Location loc = p.getLocation();
		if (!hasAbility(viper)) {
			return;
		}
		Random r = new Random();
		if (p instanceof Player) {
			if (r.nextInt(3) == 0) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 5 * 20, 0));
				p.getLocation().getWorld().playEffect(loc.add(0.0D, 0.4D, 0.0D), Effect.STEP_SOUND, 18, (short) 1);
			}
		}
	}

	@Override
	public Kit getKit() {
		return new Kit("viper", "Deixe seus inimigos envenenados", new ArrayList<ItemStack>(), new ItemStack(Material.SPIDER_EYE), 1000, KitType.FORCA);
	}
}
