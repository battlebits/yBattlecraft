package br.com.battlebits.battlecraft.kits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.GrapplingHook;
import br.com.battlebits.battlecraft.constructors.Kit;
import br.com.battlebits.battlecraft.enums.KitType;
import br.com.battlebits.battlecraft.interfaces.KitInterface;
import br.com.battlebits.battlecraft.utils.Cooldown;

public class Grappler extends KitInterface {
	private Map<UUID, GrapplingHook> hooks;

	public Grappler(Main main) {
		super(main);
		hooks = new HashMap<>();
	}

	@EventHandler
	public void onLeash(PlayerLeashEntityEvent e) {
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		if (item == null)
			return;
		if (item.getType() != Material.LEASH)
			return;
		if (!hasAbility(p))
			return;
		e.setCancelled(true);
		if (!hooks.containsKey(p.getUniqueId())) {
			return;
		}
		if (!hooks.get(p.getUniqueId()).isHooked()) {
			return;
		}
		if (Cooldown.isInCooldown(p.getUniqueId(), "grappler")) {
			p.sendMessage(ChatColor.RED + "Voce está em cooldown, nao pode usar seu grappler ainda!");
			return;
		}
		GrapplingHook hook = hooks.get(p.getUniqueId());
		Location loc = hook.getBukkitEntity().getLocation();
		Location playerLoc = p.getLocation();
		double d = loc.distance(playerLoc);
		double t = d;
		double v_x = (1.0D + 0.04000000000000001D * t) * ((isNear(loc, playerLoc) ? 0 : loc.getX() - playerLoc.getX()) / t);
		double v_y = (0.9D + 0.03D * t) * ((isNear(loc, playerLoc) ? 0.1 : loc.getY() - playerLoc.getY()) / t);
		double v_z = (1.0D + 0.04000000000000001D * t) * ((isNear(loc, playerLoc) ? 0 : loc.getZ() - playerLoc.getZ()) / t);
		Vector v = p.getVelocity();
		v.setX(v_x);
		v.setY(v_y);
		v.setZ(v_z);
		p.setVelocity(v);
		if (playerLoc.getY() < hook.getBukkitEntity().getLocation().getY()) {
			p.setFallDistance(0);
		}
		p.getWorld().playSound(playerLoc, Sound.STEP_GRAVEL, 1.0F, 1.0F);
	}

	@EventHandler
	public void onSlot(PlayerItemHeldEvent e) {
		if (hooks.containsKey(e.getPlayer().getUniqueId())) {
			hooks.get(e.getPlayer().getUniqueId()).remove();
			hooks.remove(e.getPlayer().getUniqueId());
		}
	}

	public boolean isNear(Location loc, Location playerLoc) {
		return loc.distance(playerLoc) < 1.5;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		Action a = event.getAction();
		ItemStack item = p.getItemInHand();
		if (item == null)
			return;
		if (item.getType() != Material.LEASH)
			return;
		if (!hasAbility(p))
			return;
		event.setCancelled(true);
		Location playerLoc = p.getLocation();
		if (a.toString().contains("LEFT")) {
			if (hooks.containsKey(p.getUniqueId())) {
				hooks.get(p.getUniqueId()).remove();
			}
			GrapplingHook nmsHook = new GrapplingHook(p.getWorld(), ((CraftPlayer) p).getHandle());
			nmsHook.spawn(p.getEyeLocation().add(playerLoc.getDirection().getX(), playerLoc.getDirection().getY(), playerLoc.getDirection().getZ()));
			nmsHook.move(playerLoc.getDirection().getX() * 5.0D, playerLoc.getDirection().getY() * 5.0D, playerLoc.getDirection().getZ() * 5.0D);
			hooks.put(p.getUniqueId(), nmsHook);
		} else if (a.toString().contains("RIGHT")) {
			if (!hooks.containsKey(p.getUniqueId())) {
				return;
			}
			if (Cooldown.isInCooldown(p.getUniqueId(), "grappler")) {
				if (hooks.get(p.getUniqueId()).hooked == null) {
					p.sendMessage(ChatColor.RED + "Voce está em cooldown, nao pode usar seu grappler ainda!");
					return;
				}
			}
			if (!hooks.get(p.getUniqueId()).isHooked()) {
				p.sendMessage(ChatColor.RED + "Seu grappler ainda nao foi fisgado!");
				return;
			}
			GrapplingHook hook = hooks.get(p.getUniqueId());
			Location loc = hook.getBukkitEntity().getLocation();
			double d = loc.distance(playerLoc);
			double t = d;
			double v_x = (1.0D + 0.04000000000000001D * t) * ((isNear(loc, playerLoc) ? 0 : loc.getX() - playerLoc.getX()) / t);
			double v_y = (0.9D + 0.03D * t) * ((isNear(loc, playerLoc) ? 0.1 : loc.getY() - playerLoc.getY()) / t);
			double v_z = (1.0D + 0.04000000000000001D * t) * ((isNear(loc, playerLoc) ? 0 : loc.getZ() - playerLoc.getZ()) / t);
			Vector v = p.getVelocity();
			v.setX(v_x);
			v.setY(v_y);
			v.setZ(v_z);
			p.setVelocity(v);
			if (playerLoc.getY() < hook.getBukkitEntity().getLocation().getY()) {
				p.setFallDistance(0);
			}
			p.getWorld().playSound(playerLoc, Sound.STEP_GRAVEL, 1.0F, 1.0F);
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();
		if (hooks.containsKey(p.getUniqueId())) {
			hooks.get(p.getUniqueId()).remove();
			hooks.remove(p.getUniqueId());
		}
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		if (!(event.getDamager() instanceof Player))
			return;
		final Player damager = (Player) event.getDamager();
		final Player p = (Player) event.getEntity();
		if (hasAbility(damager)) {
			Cooldown c = new Cooldown(damager.getUniqueId(), "grappler", 10);
			c.start();
		}
		if (hasAbility(p)) {
			Cooldown c = new Cooldown(p.getUniqueId(), "grappler", 10);
			c.start();
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (hooks.containsKey(p.getUniqueId())) {
			hooks.get(p.getUniqueId()).remove();
			hooks.remove(p.getUniqueId());
		}
	}

	@Override
	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<>();
		kititems.add(createItem(Material.LEASH, ChatColor.GOLD + "Grappler Leash"));
		return new Kit("grappler", "Movimente mais rapido sua corda!", kititems, new ItemStack(Material.LEASH), 1000, KitType.MOBILIDADE);
	}

}
