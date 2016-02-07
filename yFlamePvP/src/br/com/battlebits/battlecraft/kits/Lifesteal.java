package br.com.battlebits.battlecraft.kits;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.Kit;
import br.com.battlebits.battlecraft.enums.KitType;
import br.com.battlebits.battlecraft.interfaces.KitInterface;

public class Lifesteal extends KitInterface {

	public Lifesteal(Main main) {
		super(main);
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			Player d = (Player) e.getDamager();
			final Player p = (Player) e.getEntity();
			if (!hasAbility(p))
				return;
			if (d.getItemInHand() == null)
				return;
			Random r = new Random();
			int chance = r.nextInt(100) + 1;
			if (chance <= 20) {
				if (p.getHealth() < p.getMaxHealth())
					p.setHealth(p.getHealth() + 1);
			}
		}
	}

	@Override
	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<>();
		return new Kit("lifesteal", "Ao atacar um jogador tem chances de aumentar meio coração.", kititems, new ItemStack(Material.TORCH), 1000, KitType.FORCA);
	}
}
