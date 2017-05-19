package br.com.battlebits.ybattlecraft.updater;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import br.com.battlebits.ybattlecraft.Battlecraft;

public class PlayerKitObjectiveUpdater {

	private Battlecraft battleCraft;

	public PlayerKitObjectiveUpdater(Battlecraft plugin) {
		battleCraft = plugin;
	}

	public void lookSomeone(Player player, Player target) {
		Scoreboard sb = player.getScoreboard();
		Objective ob = sb.getObjective("kills");
		if (ob != null) {
			if (ob.getDisplayName().replace("KS §e| Kit ", "").equalsIgnoreCase(battleCraft.getKitManager().getCurrentKit(target.getUniqueId()))) {
				return;
			} else {
				ob.unregister();
			}
		}
		ob = sb.registerNewObjective("kills", "dummy");
		ob.setDisplaySlot(DisplaySlot.BELOW_NAME);
		String kitName = "KS §e| Kit " + battleCraft.getKitManager().getCurrentKit(target.getUniqueId());
		if (kitName.length() > 32) {
			kitName = kitName.substring(0, 32);
		}
		ob.setDisplayName(kitName);
		ob.getScore(target.getName()).setScore(battleCraft.getStatusManager().getStatusByUuid(target.getUniqueId()).getKillstreak());
	}

	public void notLookingAtSomeone(Player player) {
		Scoreboard sb = player.getScoreboard();
		Objective ob = sb.getObjective("kills");
		if (ob != null)
			ob.unregister();
		player.setScoreboard(sb);
	}

}
