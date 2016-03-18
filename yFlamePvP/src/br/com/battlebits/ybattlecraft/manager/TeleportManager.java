package br.com.battlebits.ybattlecraft.manager;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.constructors.Warp;

public class TeleportManager {

	private Table<UUID, String, Long> playerWarpDelay;
	private yBattleCraft battleCraft;

	public TeleportManager(yBattleCraft yBattleCraft) {
		battleCraft = yBattleCraft;
		playerWarpDelay = HashBasedTable.create();
	}

	public void startTeleportChecker() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Cell<UUID, String, Long> entry : playerWarpDelay.cellSet()) {
					if (System.currentTimeMillis() >= entry.getValue()) {
						Warp w = battleCraft.getWarpManager().getWarpByName(entry.getColumnKey());
						if (w != null) {
							Player p = Bukkit.getPlayer(entry.getRowKey());
							if (p != null) {
								if (p.isOnline()) {
									if (!p.isDead()) {
										p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
										battleCraft.getWarpManager().teleportWarp(p, w.getWarpName().toLowerCase().trim(), true);
									}
								}
							}
						}
						playerWarpDelay.remove(entry.getRowKey(), entry.getColumnKey());
					}
				}
			}
		}.runTaskTimerAsynchronously(battleCraft, 20L, 20L);
	}

	@SuppressWarnings("deprecation")
	public void tryToTeleport(Player p, Warp warp) {
		if (!playerWarpDelay.containsRow(p.getUniqueId())) {
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
					playerWarpDelay.put(p.getUniqueId(), warp.getWarpName().toLowerCase().trim(), System.currentTimeMillis() + 5000);
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
		playerWarpDelay.row(id).clear();
	}

	public boolean isTeleporting(UUID id) {
		return playerWarpDelay.containsRow(id);
	}

}
