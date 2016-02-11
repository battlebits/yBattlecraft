package br.com.battlebits.battlecraft.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import br.com.battlebits.battlecraft.events.RealMoveEvent;

public class LauncherListener implements Listener {

	private List<UUID> noFallDamage;

	public LauncherListener() {
		noFallDamage = new ArrayList<>();
	}

	@EventHandler
	public void onMove(RealMoveEvent event) {
		Player p = event.getPlayer();
		Location standBlock = p.getLocation().clone().add(0, -0.00001, 0);
		if (standBlock.getBlock().getType() == Material.ENDER_PORTAL_FRAME) {
			double xvel = 0.0D;
			double yvel = 3.0D;
			double zvel = 0.0D;
			p.setVelocity(new Vector(xvel, yvel, zvel));
			p.playSound(p.getLocation(), Sound.HORSE_JUMP, 10.0f, 1.0f);
			if (!noFallDamage.contains(p.getUniqueId())) {
				noFallDamage.add(p.getUniqueId());
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getCause() != DamageCause.FALL)
			return;
		if (!(event.getEntity() instanceof Player))
			return;
		Player p = (Player) event.getEntity();
		if (noFallDamage.contains(p.getUniqueId())) {
			event.setCancelled(true);
			noFallDamage.remove(p.getUniqueId());
		}
	}
}
