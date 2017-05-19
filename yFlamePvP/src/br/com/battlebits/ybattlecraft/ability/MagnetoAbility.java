package br.com.battlebits.ybattlecraft.ability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import br.com.battlebits.commons.api.admin.AdminMode;
import br.com.battlebits.commons.api.item.ItemBuilder;
import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.base.BaseAbility;

public class MagnetoAbility extends BaseAbility {

	private ArrayList<Entity> invencible;
	private HashMap<UUID, Integer> uses;
	private ItemStack magnetoItem;

	public MagnetoAbility(Battlecraft Battlecraft) {
		super(Battlecraft);
		uses = new HashMap<>();
		invencible = new ArrayList<>();
		magnetoItem = new ItemBuilder().amount(1).type(Material.IRON_INGOT).name("§7Magneto").glow().build();
		getItens().add(magnetoItem);
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockIgnite(PlayerInteractEvent e) {
		if (e.getAction() != Action.LEFT_CLICK_AIR) {
			if (e.getPlayer().getItemInHand() != null) {
				if (e.getPlayer().getItemInHand().equals(magnetoItem)) {
					if (isUsing(e.getPlayer())) {
						if (!battlecraft.getCooldownManager().isOnCooldown(e.getPlayer().getUniqueId(), "magnetoability")) {
							if (!uses.containsKey(e.getPlayer().getUniqueId())) {
								uses.put(e.getPlayer().getUniqueId(), 0);
							}
							if (uses.get(e.getPlayer().getUniqueId()) <= 5) {
								uses.put(e.getPlayer().getUniqueId(), uses.get(e.getPlayer().getUniqueId()) + 1);
							}
							if (uses.get(e.getPlayer().getUniqueId()) > 5) {
								if (!battlecraft.getCooldownManager().hasCooldown(e.getPlayer().getUniqueId(), "magnetoability")) {
									battlecraft.getCooldownManager().setCooldown(e.getPlayer().getUniqueId(), "magnetoability", 10);
								}
								uses.put(e.getPlayer().getUniqueId(), 0);
							}
							for (final Entity entity : e.getPlayer().getNearbyEntities(15, 15, 15)) {
								Location lc = entity.getLocation();
								Location to = e.getPlayer().getLocation();
								lc.setY(lc.getY() + 0.5D);
								double g = -0.08D;
								double d = to.distance(lc);
								double t = d;
								double v_x = (0.3D + 0.05D * t) * (to.getX() - lc.getX()) / t;
								double v_y = (0.5D + 0.03D * t) * (to.getY() - lc.getY()) / t - 0.5D * g * t;
								double v_z = (0.3D + 0.05D * t) * (to.getZ() - lc.getZ()) / t;
								Vector v = e.getPlayer().getVelocity();
								v.setX(v_x);
								v.setY(v_y);
								v.setZ(v_z);
								if (entity instanceof Player) {
									if (battlecraft.getProtectionManager().isProtected(((Player) entity).getUniqueId()))
										continue;
									if (AdminMode.getInstance().isAdmin((Player) entity))
										continue;
								}
								entity.setVelocity(v);
								invencible.add(entity);
								Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(battlecraft, new Runnable() {
									public void run() {
										invencible.remove(entity);
									}
								}, 100L);
							}
						} else {
							e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
							e.getPlayer().sendMessage("§5§LMAGNETO §fAguarde §9§l" + battlecraft.getCooldownManager().getCooldownTimeFormated(e.getPlayer().getUniqueId(), "magnetoability").toUpperCase() + "§f para utilizar sua habilidade!");
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDamageEvent(EntityDamageEvent event) {
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

}
