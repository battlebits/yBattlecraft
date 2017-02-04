package br.com.battlebits.ybattlecraft.updater;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.constructors.Warp;

public class WarpScoreboardUpdater {

	private BukkitRunnable runnable;
	private Battlecraft battleCraft;

	public WarpScoreboardUpdater(Battlecraft plugin) {
		battleCraft = plugin;
		runnable = new BukkitRunnable() {
			@Override
			public void run() {
				for (Warp w : battleCraft.getWarpManager().getWarps()) {
					if (w.hasScoreboard()) {
						String title = "§8§l>> §6§l" + w.getScroller().next() + " §8§l<<";
						for (UUID id : battleCraft.getWarpManager().getPlayersInWarp(w.getWarpName().trim())) {
							Player p = Bukkit.getPlayer(id);
							if (p != null && p.isOnline()) {
								w.getScoreboard().setTitle(p, title);
							}
						}
					}
				}
			}
		};
	}

	public void start() {
		runnable.runTaskTimerAsynchronously(battleCraft, 7L, 7L);
	}

	public void stop() {
		try {
			runnable.cancel();
		} catch (Exception e) {
		}
	}

}
