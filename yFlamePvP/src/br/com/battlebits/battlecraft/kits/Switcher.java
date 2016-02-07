package br.com.battlebits.battlecraft.kits;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.Kit;
import br.com.battlebits.battlecraft.enums.KitType;
import br.com.battlebits.battlecraft.interfaces.KitInterface;

public class Switcher extends KitInterface {

	public Switcher(Main main) {
		super(main);
	}

	@EventHandler
	public void snowball(EntityDamageByEntityEvent e) {
		if (((e.getDamager() instanceof Snowball)) && ((e.getEntity() instanceof Player))) {
			Snowball s = (Snowball) e.getDamager();
			Player shooter = (Player) s.getShooter();
			if (!hasAbility(shooter))
				return;
			if (!(s.getShooter() instanceof Player))
				return;
			Location shooterLoc = shooter.getLocation();
			shooter.teleport(e.getEntity().getLocation());
			e.getEntity().teleport(shooterLoc);
		}
	}

	@Override
	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.SNOW_BALL, 16, "Switcher Balls"));
		return new Kit("switcher", "Troque de lugar com suas snowballs.", kititems, new ItemStack(Material.SNOW_BALL), 1000, KitType.ESTRATEGIA);
	}

}
