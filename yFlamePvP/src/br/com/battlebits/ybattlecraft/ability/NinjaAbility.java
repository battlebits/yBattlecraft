package br.com.battlebits.ybattlecraft.ability;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseAbility;
import br.com.battlebits.ybattlecraft.event.PlayerDamagePlayerEvent;
import br.com.battlebits.ybattlecraft.event.PlayerDeathInWarpEvent;

public class NinjaAbility extends BaseAbility {

	private HashMap<UUID, NinjaHit> ninjaHits;

	public NinjaAbility(yBattleCraft yBattleCraft) {
		super(yBattleCraft);
		ninjaHits = new HashMap<>();
	}

	@EventHandler
	public void onPlayerDamagePlayerListener(PlayerDamagePlayerEvent e) {
		if (isUsing(e.getDamager())) {
			NinjaHit ninjaHit = ninjaHits.get(e.getDamager().getUniqueId());
			if (ninjaHit == null) {
				ninjaHit = new NinjaHit(e.getDamaged());
			} else {
				ninjaHit.setTarget(e.getDamaged());
			}
			ninjaHits.put(e.getDamager().getUniqueId(), ninjaHit);
		}
	}

	@EventHandler
	public void onPlayerToggleSneakListener(PlayerToggleSneakEvent e) {
		if (isUsing(e.getPlayer())) {
			if (e.isSneaking()) {
				if (ninjaHits.containsKey(e.getPlayer().getUniqueId())) {
					NinjaHit hit = ninjaHits.get(e.getPlayer().getUniqueId());
					if (hit.getTarget() != null) {
						if (hit.getTargetExpires() >= System.currentTimeMillis()) {
							if (hit.getTarget().isOnline()) {
								if (!hit.getTarget().isDead()) {
									if (hit.getTarget().getWorld().getName().equalsIgnoreCase(e.getPlayer().getWorld().getName())) {
										if (!battlecraft.getProtectionManager().isProtected(hit.getTargetUUID())) {
											if (e.getPlayer().getLocation().distance(hit.getTarget().getLocation()) <= 50) {
												if (e.getPlayer().getLocation().getY() - hit.getTarget().getLocation().getY() <= 20) {
													if (!battlecraft.getGladiatorFightController().isInFight(e.getPlayer())) {
														//TODO: CHECK IF TARGET IS THE SAME IN BATTLE
														if (!battlecraft.getCooldownManager().isOnCooldown(e.getPlayer().getUniqueId(),
																"ninjaability")) {
															e.getPlayer().teleport(hit.getTarget().getLocation());
															e.getPlayer().sendMessage("§5§lNinja §8§l>> §7Teleportado!");
															e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENDERMAN_TELEPORT, 0.5F, 1.0F);
															battlecraft.getCooldownManager().setCooldown(e.getPlayer().getUniqueId(), "ninjaability",
																	7);
														} else {
															e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
															e.getPlayer()
																	.sendMessage("§5§lNINJA §fAguarde §9§l"
																			+ battlecraft.getCooldownManager()
																					.getCooldownTimeFormated(e.getPlayer().getUniqueId(),
																							"ninjaability")
																					.toUpperCase()
																			+ "§f para utilizar sua habilidade!");
														}
													} else {
														e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
														e.getPlayer().sendMessage("§5§lNINJA §fFinalize sua §9§lBATALHA§f primeiro!");
													}
												} else {
													e.getPlayer().sendMessage("§5§LNINJA §fVoce esta §9§lDISTANTE§f do jogador.");
												}
											} else {
												e.getPlayer().sendMessage("§5§LNINJA §fVoce esta §9§lDISTANTE§f do jogador.");
											}
										} else {
											e.getPlayer().sendMessage("§5§LNINJA §fJogador §9§lINVALIDO§f.");
										}
									} else {
										e.getPlayer().sendMessage("§5§LNINJA §fJogador §9§lINVALIDO§f.");
									}
								} else {
									e.getPlayer().sendMessage("§5§LNINJA §fJogador §9§lINVALIDO§f.");
								}
							} else {
								e.getPlayer().sendMessage("§5§LNINJA §fJogador §9§lINVALIDO§f.");
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerDeathInWarpListener(PlayerDeathInWarpEvent e) {
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
	public void onPlayerQuitListener(PlayerQuitEvent e) {
		if (ninjaHits.containsKey(e.getPlayer().getUniqueId())) {
			ninjaHits.remove(e.getPlayer().getUniqueId());
		}
	}

	private static class NinjaHit {

		private UUID targetUUID;
		private long targetExpires;

		public NinjaHit(Player target) {
			this.targetUUID = target.getUniqueId();
			this.targetExpires = System.currentTimeMillis() + 15000;
		}

		public Player getTarget() {
			return Bukkit.getPlayer(getTargetUUID());
		}

		public UUID getTargetUUID() {
			return targetUUID;
		}

		public long getTargetExpires() {
			return targetExpires;
		}

		public void setTarget(Player player) {
			this.targetUUID = player.getUniqueId();
			this.targetExpires = System.currentTimeMillis() + 20000;
		}

	}

}