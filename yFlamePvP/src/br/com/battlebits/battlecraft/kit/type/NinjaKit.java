package br.com.battlebits.battlecraft.kit.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.Kit;
import br.com.battlebits.battlecraft.enums.KitType;
import br.com.battlebits.battlecraft.event.PlayerDeathInWarpEvent;
import br.com.battlebits.battlecraft.kit.KitInterface;

public class NinjaKit extends KitInterface {

	private HashMap<UUID, NinjaHit> ninjaHits;

	public NinjaKit(Main main) {
		super(main);
		ninjaHits = new HashMap<>();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onNinjaHit(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			final Player damager = (Player) event.getDamager();
			Player damaged = (Player) event.getEntity();
			if (hasAbility(damager)) {
				NinjaHit ninjaHit = ninjaHits.get(damager.getUniqueId());
				if (ninjaHit == null) {
					ninjaHit = new NinjaHit(damaged);
				} else {
					ninjaHit.setTarget(damaged);
				}
				ninjaHits.put(damager.getUniqueId(), ninjaHit);
			}
		}
	}

	@EventHandler
	public void onShift(PlayerToggleSneakEvent e) {
		if (hasAbility(e.getPlayer())) {
			if (ninjaHits.containsKey(e.getPlayer().getUniqueId())) {
				NinjaHit hit = ninjaHits.get(e.getPlayer().getUniqueId());
				if (hit.getTarget() != null) {
					if (hit.getTargetExpires() < System.currentTimeMillis()) {
						if (hit.getTarget().isOnline()) {
							if (!hit.getTarget().isDead()) {
								if (hit.getTarget().getWorld().getName().equalsIgnoreCase(e.getPlayer().getWorld().getName())) {
									if (!getMain().getProtectionManager().isProtected(hit.getTargetUUID())) {
										if (e.getPlayer().getLocation().distance(hit.getTarget().getLocation()) > 50) {
											if (e.getPlayer().getLocation().getY() - hit.getTarget().getLocation().getY() > 20) {
												if (hit.getCooldown() > System.currentTimeMillis()) {
													hit.addCooldown();
													e.getPlayer().teleport(hit.getTarget().getLocation());
													e.getPlayer().sendMessage("§5§lNinja §7§l>> §7Teleportado!");
													e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENDERMAN_TELEPORT, 0.5F, 1.0F);
												} else {
													e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.IRONGOLEM_HIT, 1.0F, 1.0F);
													e.getPlayer().sendMessage("§5§lNinja §7§l>> §7Voce esta em cooldown!");
												}
											} else {
												e.getPlayer().sendMessage("§5§lNinja §7§l>> §7Voce esta distante do jogador!");
											}
										} else {
											e.getPlayer().sendMessage("§5§lNinja §7§l>> §7Voce esta distante do jogador!");
										}
									} else {
										e.getPlayer().sendMessage("§5§lNinja §7§l>> §7Jogador invalido!");
									}
								} else {
									e.getPlayer().sendMessage("§5§lNinja §7§l>> §7Jogador invalido!");
								}
							} else {
								e.getPlayer().sendMessage("§5§lNinja §7§l>> §7Jogador invalido!");
							}
						} else {
							e.getPlayer().sendMessage("§5§lNinja §7§l>> §7Jogador invalido!");
						}
					}
				} else {
					e.getPlayer().sendMessage("§5§lNinja §7§l>> §7Jogador invalido!");
				}
			}
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathInWarpEvent e) {
		if (ninjaHits.containsKey(e.getPlayerUUID())) {
			ninjaHits.remove(e.getPlayerUUID());
		}
		if (e.hasKiller()) {
			if (ninjaHits.containsKey(e.getKillerUUID())) {
				ninjaHits.remove(e.getKillerUUID());
			}
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if (ninjaHits.containsKey(e.getPlayer().getUniqueId())) {
			ninjaHits.remove(e.getPlayer().getUniqueId());
		}
	}

	@Override
	public Kit getKit() {
		return new Kit("ninja", "Aperte SHIFT para teleportar-se para o utltimo jogador hitado.", new ArrayList<ItemStack>(),
				new ItemStack(Material.EMERALD), 1000, KitType.ESTRATEGIA);
	}

	private static class NinjaHit {

		private UUID targetUUID;
		private long cooldown;
		private long targetExpires;

		public NinjaHit(Player target) {
			this.targetUUID = target.getUniqueId();
			this.cooldown = 0;
			this.targetExpires = System.currentTimeMillis() + 15000;
		}

		public Player getTarget() {
			return Bukkit.getPlayer(getTargetUUID());
		}

		public UUID getTargetUUID() {
			return targetUUID;
		}

		public long getCooldown() {
			return cooldown;
		}

		public long getTargetExpires() {
			return targetExpires;
		}

		public void addCooldown() {
			cooldown = System.currentTimeMillis() + 7000;
		}

		public void setTarget(Player player) {
			this.targetUUID = player.getUniqueId();
			this.targetExpires = System.currentTimeMillis() + 20000;
		}

	}

}