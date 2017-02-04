package br.com.battlebits.ybattlecraft.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.event.PlayerWarpJoinEvent;

public class WarpScoreboardListener implements Listener {

	private Battlecraft Battlecraft;

	public WarpScoreboardListener(Battlecraft m) {
		this.Battlecraft = m;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerWarpJoinListenr(PlayerWarpJoinEvent e) {
		if (e.getPlayer() == null)
			return;
		if (Battlecraft.getStatusManager().getStatusByUuid(e.getPlayer().getUniqueId()).isScoreboardEnabled()) {
			if (e.getWarp().hasScoreboard()) {
				e.getWarp().getScoreboard().setSidebar(e.getPlayer());
				return;
			}
		}
		e.getPlayer().getScoreboard().getObjective("clear").setDisplaySlot(DisplaySlot.SIDEBAR);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoinListener(PlayerJoinEvent e) {
		e.getPlayer().getScoreboard().registerNewObjective("clear", "dummy");
	}

	@EventHandler
	public void onPlayerQuitListener(PlayerQuitEvent e) {
		for (Objective obj : e.getPlayer().getScoreboard().getObjectives()) {
			obj.unregister();
		}
		for (Team t : e.getPlayer().getScoreboard().getTeams()) {
			t.unregister();
		}
	}

}
