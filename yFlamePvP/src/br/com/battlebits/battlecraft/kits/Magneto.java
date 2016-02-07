package br.com.battlebits.battlecraft.kits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.Kit;
import br.com.battlebits.battlecraft.enums.KitType;
import br.com.battlebits.battlecraft.interfaces.KitInterface;
import br.com.battlebits.battlecraft.utils.Cooldown;

public class Magneto extends KitInterface {

	private ArrayList<Entity> invencible;
	private HashMap<UUID, Integer> uses;

	public Magneto(Main main) {
		super(main);
		uses = new HashMap<>();
		invencible = new ArrayList<>();
	}

	@EventHandler
	public void onBlockIgnite(PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		if (!hasAbility(p)) {
			return;
		}
		if (item == null) {
			return;
		}
		if (item.getType() != Material.IRON_INGOT) {
			return;
		}
		if (e.getAction() == Action.LEFT_CLICK_AIR) {
			return;
		}
		if (Cooldown.isInCooldown(p.getUniqueId(), "magneto")) {
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 1.0F, 1.0F);
			int timeleft = Cooldown.getTimeLeft(p.getUniqueId(), "magneto");
			p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Magneto " + ChatColor.GRAY + "em cooldown de " + ChatColor.RESET + ChatColor.BOLD + timeleft + ChatColor.GRAY + " segundos!");
			return;
		}
		if (!this.uses.containsKey(p.getUniqueId())) {
			this.uses.put(p.getUniqueId(), Integer.valueOf(1));
		}
		if (this.uses.get(p.getUniqueId()).intValue() <= 5) {
			this.uses.put(p.getUniqueId(), this.uses.get(p.getUniqueId()).intValue() + 1);
		}
		if (this.uses.get(p.getUniqueId()).intValue() > 5) {
			if (!Cooldown.isInCooldown(p.getUniqueId(), "magneto")) {
				Cooldown c = new Cooldown(p.getUniqueId(), "magneto", 10);
				c.start();
				Bukkit.getScheduler().scheduleSyncDelayedTask(getMain(), new Runnable() {
					public void run() {
						Magneto.this.uses.remove(p.getUniqueId());
						p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
					}
				}, 200L);
			}
		}
		for (final Entity entity : p.getNearbyEntities(15, 15, 15)) {
			Location lc = entity.getLocation();
			Location to = p.getLocation();

			lc.setY(lc.getY() + 0.5D);
			double g = -0.08D;
			double d = to.distance(lc);
			double t = d;
			double v_x = (0.3D + 0.05D * t) * (to.getX() - lc.getX()) / t;
			double v_y = (0.5D + 0.03D * t) * (to.getY() - lc.getY()) / t - 0.5D * g * t;
			double v_z = (0.3D + 0.05D * t) * (to.getZ() - lc.getZ()) / t;
			Vector v = p.getVelocity();
			v.setX(v_x);
			v.setY(v_y);
			v.setZ(v_z);
			if (entity instanceof Player) {
				if (getMain().getProtectionManager().isProtected(((Player) entity).getUniqueId()))
					continue;
				if (getMain().getAdminMode().isAdmin((Player) entity))
					continue;
			}
			entity.setVelocity(v);
			this.invencible.add(entity);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getMain(), new Runnable() {
				public void run() {
					Magneto.this.invencible.remove(entity);
				}
			}, 100L);
		}
	}

	@EventHandler
	public void onHabilidadeDeHitReceberDano(EntityDamageEvent event) {
		Entity vitima = event.getEntity();
		if (vitima.isDead()) {
			return;
		}
		Entity p = (Entity) vitima;
		if (invencible.contains(p)) {
			if (event.getCause() == DamageCause.FALL) {
				event.setCancelled(true);
			}
			if (event.getCause() == DamageCause.FIRE_TICK) {
				event.setCancelled(true);
			}
			if (event.getCause() == DamageCause.LAVA) {
				event.setCancelled(true);
			}
		}
	}

	@Override
	public Kit getKit() {
		ArrayList<ItemStack> items = new ArrayList<>();
		items.add(createItem(Material.IRON_INGOT, ChatColor.GRAY + "Magneto"));
		return new Kit("magneto", "Sugue seus inimigos por 5 segundos", items, new ItemStack(Material.IRON_INGOT), 1000, KitType.ESTRATEGIA);
	}

}
