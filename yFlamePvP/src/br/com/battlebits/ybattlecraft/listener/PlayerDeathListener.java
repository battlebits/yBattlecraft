package br.com.battlebits.ybattlecraft.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.constructors.Warp;
import br.com.battlebits.ybattlecraft.event.PlayerDeathInWarpEvent;

public class PlayerDeathListener extends BaseListener {

	public PlayerDeathListener(yBattleCraft plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDamageListener(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getCause() == DamageCause.VOID) {
					EntityDamageEvent last = p.getLastDamageCause();
					if (last instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) last).getDamager() instanceof Player) {
						Bukkit.getPluginManager().callEvent(new PlayerDeathInWarpEvent(p, (Player) ((EntityDamageByEntityEvent) last).getDamager(),
								battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
					} else {
						Bukkit.getPluginManager()
								.callEvent(new PlayerDeathInWarpEvent(p, null, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
					}
					e.setCancelled(true);
			} else if (!(e.getCause() == DamageCause.BLOCK_EXPLOSION || e.getCause() == DamageCause.ENTITY_EXPLOSION
					|| e.getCause() == DamageCause.ENTITY_ATTACK)) {
				if (e.getFinalDamage() >= ((Damageable) p).getHealth()) {
					EntityDamageEvent last = p.getLastDamageCause();
					if (last instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) last).getDamager() instanceof Player) {
						Bukkit.getPluginManager().callEvent(new PlayerDeathInWarpEvent(p, (Player) ((EntityDamageByEntityEvent) last).getDamager(),
								battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
					} else {
						Bukkit.getPluginManager()
								.callEvent(new PlayerDeathInWarpEvent(p, null, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
					}
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDamageByBlockListener(EntityDamageByBlockEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getFinalDamage() >= ((Damageable) p).getHealth()) {
				EntityDamageEvent last = p.getLastDamageCause();
				if (last instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) last).getDamager() instanceof Player) {
					Bukkit.getPluginManager().callEvent(new PlayerDeathInWarpEvent(p, (Player) ((EntityDamageByEntityEvent) last).getDamager(),
							battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
				} else {
					Bukkit.getPluginManager()
							.callEvent(new PlayerDeathInWarpEvent(p, null, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
				}
				e.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDamageByEntityListener(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getFinalDamage() >= ((Damageable) p).getHealth()) {
				if (e.getDamager() instanceof Player) {
					Bukkit.getPluginManager().callEvent(
							new PlayerDeathInWarpEvent(p, (Player) e.getDamager(), battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
				} else {
					Bukkit.getPluginManager()
							.callEvent(new PlayerDeathInWarpEvent(p, null, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
				}
				e.setCancelled(true);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDeathInWarpListener(PlayerDeathInWarpEvent e) {
		e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.SUCCESSFUL_HIT, 1.0F, 1.0F);
		e.getPlayer().setHealth(20);
		e.getPlayer().setLevel(0);
		e.getPlayer().closeInventory();
		e.getPlayer().setVelocity(new Vector());
		if (battleCraft.getKitManager().hasCurrentKit(e.getPlayer().getUniqueId())) {
			battleCraft.getKitManager().removeKit(e.getPlayer());
		}
		for (PotionEffect effect : e.getPlayer().getActivePotionEffects()) {
			e.getPlayer().removePotionEffect(effect.getType());
		}
		battleCraft.getWarpManager().removeWarp(e.getPlayer());
		if (e.hasKiller()) {
			battleCraft.getItemManager().repairItens(e.getKiller());
		}
		e.getPlayer().setFallDistance(0);
		e.getPlayer().setNoDamageTicks(20);
		Warp w = battleCraft.getWarpManager().getWarpByName(e.getWarp());
		if (w != null && w.isAffectMainStatus()) {
			battleCraft.getStatusManager().updateStatus(e.getKiller(), e.getPlayer());
		}
		if (w == null) {
			battleCraft.getWarpManager().teleportWarp(e.getPlayer(), "spawn", false);
		} else if (w != null) {
			battleCraft.getWarpManager().teleportWarp(e.getPlayer(), w.getWarpName().toLowerCase().trim(), false);
			String tag = ChatColor.GREEN + "" + ChatColor.BOLD + "Respawn" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> "
					+ ChatColor.RESET;
			String name = ChatColor.GRAY + "Você morreu e renasceu na Warp " + w.getWarpName() + ".";
			e.getPlayer().sendMessage(tag + name);
		}
	}

}
