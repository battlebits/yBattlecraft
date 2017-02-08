package br.com.battlebits.ybattlecraft.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import br.com.battlebits.anticheat.BattleAnticheat;
import br.com.battlebits.commons.api.item.ItemBuilder;
import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.base.BaseAbility;
import br.com.battlebits.ybattlecraft.constructors.GrapplingHook;
import br.com.battlebits.ybattlecraft.event.PlayerDeathInWarpEvent;

public class GrapplerAbility extends BaseAbility {

	private Map<UUID, GrapplingHook> grapplerHooks;
	private ItemStack grapplerItem;

	public GrapplerAbility(Battlecraft plugin) {
		super(plugin);
		grapplerHooks = new HashMap<>();
		grapplerItem = new ItemBuilder().amount(1).type(Material.LEASH).name("§6§lGrappler Leash").glow().build();
		getItens().add(grapplerItem);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLeashEntityListener(PlayerLeashEntityEvent e) {
		if (isUsing(e.getPlayer())) {
			if (e.getPlayer().getItemInHand() != null) {
				if (e.getPlayer().getItemInHand().equals(grapplerItem)) {
					e.setCancelled(true);
					if (grapplerHooks.containsKey(e.getPlayer().getUniqueId())) {
						if (grapplerHooks.get(e.getPlayer().getUniqueId()).isHooked()) {
							GrapplingHook hook = grapplerHooks.get(e.getPlayer().getUniqueId());
							Location loc = hook.getBukkitEntity().getLocation();
							Location playerLoc = e.getPlayer().getLocation();
							double d = loc.distance(playerLoc);
							double t = d;
							double v_x = (1.0D + 0.04000000000000001D * t)
									* ((isNear(loc, playerLoc) ? 0 : loc.getX() - playerLoc.getX()) / t);
							double v_y = (0.9D + 0.03D * t)
									* ((isNear(loc, playerLoc) ? 0.1 : loc.getY() - playerLoc.getY()) / t);
							double v_z = (1.0D + 0.04000000000000001D * t)
									* ((isNear(loc, playerLoc) ? 0 : loc.getZ() - playerLoc.getZ()) / t);
							Vector v = e.getPlayer().getVelocity();
							v.setX(v_x);
							v.setY(v_y);
							v.setZ(v_z);
							e.getPlayer().setVelocity(v);
							BattleAnticheat.disableFlyCheck(e.getPlayer(), 300);
							if (playerLoc.getY() < hook.getBukkitEntity().getLocation().getY()) {
								e.getPlayer().setFallDistance(0);
							}
							e.getPlayer().getWorld().playSound(playerLoc, Sound.STEP_GRAVEL, 1.0F, 1.0F);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerItemHeldListener(PlayerItemHeldEvent e) {
		if (grapplerHooks.containsKey(e.getPlayer().getUniqueId())) {
			grapplerHooks.get(e.getPlayer().getUniqueId()).remove();
			grapplerHooks.remove(e.getPlayer().getUniqueId());
		}
	}

	public boolean isNear(Location loc, Location playerLoc) {
		return loc.distance(playerLoc) < 1.5;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractListener(PlayerInteractEvent e) {
		if (isUsing(e.getPlayer())) {
			if (e.getItem() != null) {
				if (e.getItem().equals(grapplerItem)) {
					e.setCancelled(true);
					if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
						if (grapplerHooks.containsKey(e.getPlayer().getUniqueId())) {
							grapplerHooks.get(e.getPlayer().getUniqueId()).remove();
							grapplerHooks.remove(e.getPlayer().getUniqueId());
						}
						GrapplingHook hook = new GrapplingHook(e.getPlayer().getWorld(),
								((CraftPlayer) e.getPlayer()).getHandle());
						hook.spawn(e.getPlayer().getEyeLocation().add(e.getPlayer().getLocation().getDirection().getX(),
								e.getPlayer().getLocation().getDirection().getY(),
								e.getPlayer().getLocation().getDirection().getZ()));
						hook.move(e.getPlayer().getLocation().getDirection().getX() * 5.0D,
								e.getPlayer().getLocation().getDirection().getY() * 5.0D,
								e.getPlayer().getLocation().getDirection().getZ() * 5.0D);
						grapplerHooks.put(e.getPlayer().getUniqueId(), hook);
					} else if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
						if (grapplerHooks.containsKey(e.getPlayer().getUniqueId())) {
							if (grapplerHooks.get(e.getPlayer().getUniqueId()).isHooked()) {
								GrapplingHook hook = grapplerHooks.get(e.getPlayer().getUniqueId());
								Location loc = hook.getBukkitEntity().getLocation();
								double d = loc.distance(e.getPlayer().getLocation());
								double t = d;
								double v_x = (1.0D + 0.04000000000000001D * t)
										* ((isNear(loc, e.getPlayer().getLocation()) ? 0
												: loc.getX() - e.getPlayer().getLocation().getX()) / t);
								double v_y = (0.9D + 0.03D * t) * ((isNear(loc, e.getPlayer().getLocation()) ? 0.1
										: loc.getY() - e.getPlayer().getLocation().getY()) / t);
								double v_z = (1.0D + 0.04000000000000001D * t)
										* ((isNear(loc, e.getPlayer().getLocation()) ? 0
												: loc.getZ() - e.getPlayer().getLocation().getZ()) / t);
								Vector v = e.getPlayer().getVelocity();
								v.setX(v_x);
								v.setY(v_y);
								v.setZ(v_z);
								e.getPlayer().setVelocity(v);
								BattleAnticheat.disableFlyCheck(e.getPlayer(), 300);
								if (e.getPlayer().getLocation().getY() < hook.getBukkitEntity().getLocation().getY()) {
									e.getPlayer().setFallDistance(0);
								}
								e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.STEP_GRAVEL, 1.0F,
										1.0F);
							} else {
								e.getPlayer().sendMessage("§5§lGRAPPLER §fSua corda ainda não foi §9§lFISGADA§f!");
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerDeathInWarpListener(PlayerDeathInWarpEvent e) {
		if (grapplerHooks.containsKey(e.getPlayerUUID())) {
			grapplerHooks.get(e.getPlayerUUID()).remove();
			grapplerHooks.remove(e.getPlayerUUID());
		}
	}

	@EventHandler
	public void onPlayerQuitListener(PlayerQuitEvent e) {
		if (grapplerHooks.containsKey(e.getPlayer().getUniqueId())) {
			grapplerHooks.get(e.getPlayer().getUniqueId()).remove();
			grapplerHooks.remove(e.getPlayer().getUniqueId());
		}
	}

}
