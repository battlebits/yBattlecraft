package br.com.battlebits.battlecraft.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.Status;
import br.com.battlebits.battlecraft.enums.LoadStatus;
import br.com.battlebits.battlecraft.events.StatusLoadEvent;

public class ScoreboardListener implements Listener {
	private Main m;

	public ScoreboardListener(Main m) {
		this.m = m;

	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		setScoreBoard(p);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onKill(PlayerDeathEvent event) {
		Player p = event.getEntity();
		Player killer = p.getKiller();
		if (killer != null)
			setScoreBoard(killer);
		setScoreBoard(p);
	}

	@EventHandler
	public void onReload(PluginEnableEvent event) {
		for (Player p : m.getServer().getOnlinePlayers()) {
			setScoreBoard(p);
		}
	}

	@EventHandler
	public void onStatusLoad(StatusLoadEvent event) {
		if (event.getLoadStatus() == LoadStatus.FAILED) {
			return;
		}
		setScoreBoard(event.getPlayer());
	}

	public void setScoreBoard(Player p) {
		Scoreboard board = me.flame.utils.Main.getPlugin().getScoreboardManager().getPlayerScoreboard(p);
		Objective objective = board.getObjective("status");
		if (objective != null)
			objective.unregister();
		objective = board.registerNewObjective("status", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + ">> " + ChatColor.GOLD + ChatColor.BOLD + "Battlecraft" + ChatColor.DARK_GRAY + ChatColor.BOLD + " <<");
		int i = 1;
		int kills = 0;
		Status status = m.getStatusManager().getStatusByUuid(p.getUniqueId());
		if (status.getKills() > 0)
			kills = status.getKills();
		int deaths = 0;
		if (status.getDeaths() > 0)
			deaths = status.getDeaths();
		int killstreak = status.getKillstreak();
		if (killstreak > 0) {
			Score ks = objective.getScore(ChatColor.GOLD + "" + "KS: " + ChatColor.RESET + killstreak);
			ks.setScore(i);
			i++;
		} else {
			board.resetScores(ChatColor.GOLD + "KillStreak");
		}
		Score death = objective.getScore(ChatColor.RED + "Deaths: " + ChatColor.RESET + deaths);
		death.setScore(i);
		i++;
		Score kill = objective.getScore(ChatColor.GREEN + "Kills: " + ChatColor.RESET + kills);
		kill.setScore(i);
		i++;
		p.setScoreboard(board);
	}

}
