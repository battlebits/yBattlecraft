package br.com.battlebits.ybattlecraft.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.constructors.Warp;
import br.com.battlebits.ybattlecraft.event.PlayerDeathInWarpEvent;

public class PlayerDeathListener implements Listener {

	private yBattleCraft plugin;

	public PlayerDeathListener(yBattleCraft m) {
		this.plugin = m;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDamageListener(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getFinalDamage() >= ((Damageable) p).getHealth()) {
				EntityDamageEvent last = p.getLastDamageCause();
				if (last instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) last).getDamager() instanceof Player) {
					Bukkit.getPluginManager().callEvent(new PlayerDeathInWarpEvent(p, (Player) ((EntityDamageByEntityEvent) last).getDamager(),
							plugin.getWarpManager().getPlayerWarp(p.getUniqueId())));
				} else {
					Bukkit.getPluginManager().callEvent(new PlayerDeathInWarpEvent(p, null, plugin.getWarpManager().getPlayerWarp(p.getUniqueId())));
				}
				e.setCancelled(true);
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
							plugin.getWarpManager().getPlayerWarp(p.getUniqueId())));
				} else {
					Bukkit.getPluginManager().callEvent(new PlayerDeathInWarpEvent(p, null, plugin.getWarpManager().getPlayerWarp(p.getUniqueId())));
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
							new PlayerDeathInWarpEvent(p, (Player) e.getDamager(), plugin.getWarpManager().getPlayerWarp(p.getUniqueId())));
				} else {
					Bukkit.getPluginManager().callEvent(new PlayerDeathInWarpEvent(p, null, plugin.getWarpManager().getPlayerWarp(p.getUniqueId())));
				}
				e.setCancelled(true);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerDeathInWarpListener(PlayerDeathInWarpEvent e) {
		e.getPlayer().setHealth(20);
		e.getPlayer().setLevel(0);
		e.getPlayer().closeInventory();
		e.getPlayer().setVelocity(new Vector());
		if (plugin.getKitManager().hasCurrentKit(e.getPlayer().getUniqueId())) {
			plugin.getKitManager().removeKit(e.getPlayer());
		}
		for (PotionEffect effect : e.getPlayer().getActivePotionEffects()) {
			e.getPlayer().removePotionEffect(effect.getType());
		}
		plugin.getWarpManager().removeWarp(e.getPlayer());
		plugin.getItemManager().dropItems(e.getPlayer(), e.getPlayer().getLocation());
		if (e.hasKiller()) {
			plugin.getItemManager().repairItens(e.getKiller());
		}
		Warp w = plugin.getWarpManager().getWarpByName(e.getWarp());
		if (w != null && w.isAffectMainStatus()) {
			plugin.getStatusManager().updateStatus(e.getKiller(), e.getPlayer());
		}
		if (w == null || w.getWarpName().equalsIgnoreCase("spawn")) {
			plugin.teleportSpawn(e.getPlayer());
			return;
		}
		if (w != null) {
			plugin.getWarpManager().teleportWarp(e.getPlayer(), w.getWarpName().toLowerCase().trim(), false);
			String tag = ChatColor.GREEN + "" + ChatColor.BOLD + "Respawn" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> "
					+ ChatColor.RESET;
			String name = ChatColor.GRAY + "Você morreu e renasceu na Warp " + w.getWarpName() + ".";
			e.getPlayer().sendMessage(tag + name);
		}
	}

}
