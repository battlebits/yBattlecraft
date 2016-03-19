package br.com.battlebits.ybattlecraft.manager;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.constructors.Warp;

public class TeleportManager {

	private HashMap<UUID, String> playerWarpDelay;
	private yBattleCraft battleCraft;

	public TeleportManager(yBattleCraft yBattleCraft) {
		battleCraft = yBattleCraft;
		playerWarpDelay = new HashMap<>();
		startTeleportChecker();
	}

	public void startTeleportChecker() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Entry<UUID, String> entry : playerWarpDelay.entrySet()) {
					if (System.currentTimeMillis() >= Long.valueOf(entry.getValue().split("-")[1])) {
						Warp w = battleCraft.getWarpManager().getWarpByName(entry.getValue().split("-")[1]);
						if (w != null) {
							Player p = Bukkit.getPlayer(entry.getKey());
							if (p != null) {
								if (p.isOnline()) {
									if (!p.isDead()) {
										p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
										battleCraft.getWarpManager().teleportWarp(p, w.getWarpName().toLowerCase().trim(), true);
									}
								}
							}
						}
						playerWarpDelay.remove(entry.getKey());
					}
				}
			}
		}.runTaskTimerAsynchronously(battleCraft, 20L, 20L);
	}

	@SuppressWarnings("deprecation")
	public void tryToTeleport(Player p, Warp warp) {
		if (!playerWarpDelay.containsKey(p.getUniqueId())) {
			if (p.isOnGround()) {
				boolean players = false;
				for (Player t : p.getWorld().getEntitiesByClass(Player.class)) {
					if (t.getUniqueId() != p.getUniqueId()) {
						if (!battleCraft.getProtectionManager().isProtected(t.getUniqueId())) {
							if (!battleCraft.getAdminMode().isAdmin(t)) {
								if (t.getLocation().distance(p.getLocation()) <= 10) {
									players = true;
									break;
								}
							}
						}
					}
				}
				if (!players) {
					p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
					battleCraft.getWarpManager().teleportWarp(p, warp.getWarpName().toLowerCase().trim(), true);
				} else {
					playerWarpDelay.put(p.getUniqueId(), warp.getWarpName().toLowerCase().trim() + "-" + (System.currentTimeMillis() + 3000));
					p.playSound(p.getLocation(), Sound.IRONGOLEM_WALK, 1.0F, 1.0F);
					p.sendMessage("§9§lTeleporte §8§l>> §7Voce sera teleportado em 5 segundos. Não se mexa!");
				}
			} else {
				p.sendMessage("§9§lTeleporte §8§l>> §7Voce precisa estar no chao para teleportar.");
			}
		} else {
			p.sendMessage("§9§lTeleporte §8§l>> §7Voce ja esta em processo de teleporte.");
		}
	}

	public void stopAllTeleports(UUID id) {
		playerWarpDelay.remove(id);
	}

	public boolean isTeleporting(UUID id) {
		return playerWarpDelay.containsKey(id);
	}

}
