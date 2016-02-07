package br.com.battlebits.battlecraft.kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.Kit;
import br.com.battlebits.battlecraft.enums.KitType;
import br.com.battlebits.battlecraft.interfaces.KitInterface;

public class Stomper extends KitInterface {

	public Stomper(Main main) {
		super(main);

	}

	@EventHandler
	public void onStomper(EntityDamageEvent event) {
		if (event.isCancelled())
			return;
		Entity stomper = event.getEntity();
		if (!(stomper instanceof Player))
			return;
		Player stomped = (Player) stomper;
		if (!hasAbility(stomped))
			return;
		DamageCause cause = event.getCause();
		if (cause != DamageCause.FALL)
			return;
		double dmg = event.getDamage();
		if (dmg > 4) {
			event.setCancelled(true);
			stomped.damage(4);
		}
		for (Entity entity : stomped.getNearbyEntities(5, 2, 5)) {
			if (!(entity instanceof Player))
				continue;
			Player stompado = (Player) entity;
			if (getMain().getProtectionManager().isProtected(stompado.getUniqueId()))
				continue;
			if (getMain().getAdminMode().isAdmin(stompado))
				continue;
			double dmg2 = dmg;
			if (stompado.isSneaking() && dmg2 > 8)
				dmg2 = 8;
			stompado.damage(dmg2, stomped);
		}

	}

	@Override
	public Kit getKit() {
		return new Kit("stomper", "Esmague seus inimigos", new ArrayList<ItemStack>(), new ItemStack(Material.IRON_BOOTS), 2000, KitType.FORCA);
	}
}
