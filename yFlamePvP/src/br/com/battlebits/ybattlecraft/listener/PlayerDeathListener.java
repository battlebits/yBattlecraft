package br.com.battlebits.ybattlecraft.listener;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
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

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDamageListener(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getCause() == DamageCause.VOID) {
				if (!battleCraft.getWarpManager().isInWarp(p, "void challenge") || e.getFinalDamage() >= ((Damageable) p).getHealth()) {
					e.setCancelled(true);
					EntityDamageEvent last = p.getLastDamageCause();
					if (last != null && last instanceof EntityDamageByEntityEvent) {
						EntityDamageByEntityEvent byEntity = (EntityDamageByEntityEvent) last;
						if (byEntity.getDamager() != null) {
							if (byEntity.getDamager() instanceof Player) {
								Player k = (Player) byEntity.getDamager();
								if (k != null && k.isOnline()) {
									Bukkit.getPluginManager()
											.callEvent(new PlayerDeathInWarpEvent(p, k, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
									return;
								}
							} else if (byEntity.getDamager() instanceof Projectile) {
								Projectile pr = (Projectile) byEntity.getDamager();
								if (pr.getShooter() != null && pr.getShooter() instanceof Player) {
									Player k = (Player) pr.getShooter();
									if (k != null && k.isOnline()) {
										Bukkit.getPluginManager().callEvent(
												new PlayerDeathInWarpEvent(p, k, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
										return;
									}
								}
							}
						}
					}
					Bukkit.getPluginManager()
							.callEvent(new PlayerDeathInWarpEvent(p, null, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
				}
			} else if (e.getCause() != DamageCause.ENTITY_ATTACK) {
				if (e.getFinalDamage() >= ((Damageable) p).getHealth()) {
					e.setCancelled(true);
					EntityDamageEvent last = p.getLastDamageCause();
					if (last != null && last instanceof EntityDamageByEntityEvent) {
						EntityDamageByEntityEvent byEntity = (EntityDamageByEntityEvent) last;
						if (byEntity.getDamager() != null) {
							if (byEntity.getDamager() instanceof Player) {
								Player k = (Player) byEntity.getDamager();
								if (k != null && k.isOnline()) {
									Bukkit.getPluginManager()
											.callEvent(new PlayerDeathInWarpEvent(p, k, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
									return;
								}
							} else if (byEntity.getDamager() instanceof Projectile) {
								Projectile pr = (Projectile) byEntity.getDamager();
								if (pr.getShooter() != null && pr.getShooter() instanceof Player) {
									Player k = (Player) pr.getShooter();
									if (k != null && k.isOnline()) {
										Bukkit.getPluginManager().callEvent(
												new PlayerDeathInWarpEvent(p, k, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
										return;
									}
								}
							}
						}
					}
					Bukkit.getPluginManager()
							.callEvent(new PlayerDeathInWarpEvent(p, null, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDamageByBlockListener(EntityDamageByBlockEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getFinalDamage() >= ((Damageable) p).getHealth()) {
				e.setCancelled(true);
				EntityDamageEvent last = p.getLastDamageCause();
				if (last != null && last instanceof EntityDamageByEntityEvent) {
					EntityDamageByEntityEvent byEntity = (EntityDamageByEntityEvent) last;
					if (byEntity.getDamager() != null) {
						if (byEntity.getDamager() instanceof Player) {
							Player k = (Player) byEntity.getDamager();
							if (k != null && k.isOnline()) {
								Bukkit.getPluginManager()
										.callEvent(new PlayerDeathInWarpEvent(p, k, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
								return;
							}
						} else if (byEntity.getDamager() instanceof Projectile) {
							Projectile pr = (Projectile) byEntity.getDamager();
							if (pr.getShooter() != null && pr.getShooter() instanceof Player) {
								Player k = (Player) pr.getShooter();
								if (k != null && k.isOnline()) {
									Bukkit.getPluginManager()
											.callEvent(new PlayerDeathInWarpEvent(p, k, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
									return;
								}
							}
						}
					}
				}
				Bukkit.getPluginManager().callEvent(new PlayerDeathInWarpEvent(p, null, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDamageByEntityListener(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getFinalDamage() >= ((Damageable) p).getHealth()) {
				e.setCancelled(true);
				if (e.getDamager() != null) {
					if (e.getDamager() instanceof Player) {
						Player k = (Player) e.getDamager();
						if (k != null && k.isOnline()) {
							Bukkit.getPluginManager()
									.callEvent(new PlayerDeathInWarpEvent(p, k, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
							return;
						}
					} else if (e.getDamager() instanceof Projectile) {
						Projectile pr = (Projectile) e.getDamager();
						if (pr.getShooter() != null && pr.getShooter() instanceof Player) {
							Player k = (Player) pr.getShooter();
							if (k != null && k.isOnline()) {
								Bukkit.getPluginManager()
										.callEvent(new PlayerDeathInWarpEvent(p, k, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
								return;
							}
						}
					}
				}
				Bukkit.getPluginManager().callEvent(new PlayerDeathInWarpEvent(p, null, battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())));
			}
		}
	}

	@EventHandler
	public void onPlayerDeathListener(PlayerDeathEvent e) {
		battleCraft.getItemManager().dropItems(e.getEntity(), e.getEntity().getLocation());
		e.setDeathMessage(null);
		e.getEntity().spigot().respawn();
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		e.setRespawnLocation(e.getPlayer().getWorld().getSpawnLocation());
		Bukkit.getPluginManager().callEvent(new PlayerDeathInWarpEvent(e.getPlayer(), e.getPlayer().getKiller(),
				battleCraft.getWarpManager().getPlayerWarp(e.getPlayer().getUniqueId())));
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDeathInWarpListener(PlayerDeathInWarpEvent e) {
		e.getPlayer().setNoDamageTicks(100);
		if (e.hasKiller()) {
			e.getKiller().playSound(e.getPlayer().getLocation(), Sound.ENDERDRAGON_GROWL, 0.5F, 1.0F);
		}
		battleCraft.getItemManager().dropItems(e.getPlayer(), e.getPlayer().getLocation());
		e.getPlayer().setHealth(20);
		e.getPlayer().setLevel(-10);
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
			if (!w.getWarpName().equalsIgnoreCase("spawn")) {
				e.getPlayer().sendMessage("§6§lRESPAWN §fVocê morreu e renasceu na Warp §e" + w.getWarpName() + "§f.");
			}
		}
		e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENDERDRAGON_GROWL, 0.5F, 1.0F);
		e.getPlayer().setNoDamageTicks(1);
	}

}
