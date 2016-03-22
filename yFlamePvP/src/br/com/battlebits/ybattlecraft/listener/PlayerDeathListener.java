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
import br.com.battlebits.ybattlecraft.base.BaseListener;
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
				e.setCancelled(true);
				EntityDamageEvent last = p.getLastDamageCause();
				if (last != null && last instanceof EntityDamageByEntityEvent) {
					EntityDamageByEntityEvent byEntity = (EntityDamageByEntityEvent) last;
					if (byEntity.getDamager() != null && byEntity.getDamager() instanceof Player) {
						Player k = (Player) byEntity.getDamager();
						if (k != null && k.isOnline()) {
							Bukkit.getPluginManager()
									.callEvent(new PlayerDeathInWarpEvent(p, k, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
							return;
						}
					}
				}
				Bukkit.getPluginManager().callEvent(new PlayerDeathInWarpEvent(p, null, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
			} else if (!(e.getCause() == DamageCause.BLOCK_EXPLOSION || e.getCause() == DamageCause.ENTITY_EXPLOSION
					|| e.getCause() == DamageCause.ENTITY_ATTACK)) {
				if (e.getFinalDamage() >= ((Damageable) p).getHealth()) {
					e.setCancelled(true);
					EntityDamageEvent last = p.getLastDamageCause();
					if (last != null && last instanceof EntityDamageByEntityEvent) {
						EntityDamageByEntityEvent byEntity = (EntityDamageByEntityEvent) last;
						if (byEntity.getDamager() != null && byEntity.getDamager() instanceof Player) {
							Player k = (Player) byEntity.getDamager();
							if (k != null && k.isOnline()) {
								Bukkit.getPluginManager()
										.callEvent(new PlayerDeathInWarpEvent(p, k, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
								return;
							}
						}
					}
					Bukkit.getPluginManager()
							.callEvent(new PlayerDeathInWarpEvent(p, null, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDamageByBlockListener(EntityDamageByBlockEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getFinalDamage() >= ((Damageable) p).getHealth()) {
				e.setCancelled(true);
				EntityDamageEvent last = p.getLastDamageCause();
				if (last != null && last instanceof EntityDamageByEntityEvent) {
					EntityDamageByEntityEvent byEntity = (EntityDamageByEntityEvent) last;
					if (byEntity.getDamager() != null && byEntity.getDamager() instanceof Player) {
						Player k = (Player) byEntity.getDamager();
						if (k != null && k.isOnline()) {
							Bukkit.getPluginManager()
									.callEvent(new PlayerDeathInWarpEvent(p, k, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
							return;
						}
					}
				}
				Bukkit.getPluginManager().callEvent(new PlayerDeathInWarpEvent(p, null, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDamageByEntityListener(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getFinalDamage() >= ((Damageable) p).getHealth()) {
				e.setCancelled(true);
				if (e.getDamager() != null && e.getDamager() instanceof Player) {
					Player k = (Player) e.getDamager();
					if (k != null && k.isOnline()) {
						Bukkit.getPluginManager()
								.callEvent(new PlayerDeathInWarpEvent(p, k, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
						return;
					}
				}
				Bukkit.getPluginManager().callEvent(new PlayerDeathInWarpEvent(p, null, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDeathInWarpListener(PlayerDeathInWarpEvent e) {
		e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENDERDRAGON_GROWL, 0.5F, 1.0F);
		if (e.hasKiller()) {
			e.getKiller().playSound(e.getPlayer().getLocation(), Sound.ENDERDRAGON_GROWL, 0.5F, 1.0F);
		}
		battleCraft.getItemManager().dropItems(e.getPlayer(), e.getPlayer().getLocation());
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
