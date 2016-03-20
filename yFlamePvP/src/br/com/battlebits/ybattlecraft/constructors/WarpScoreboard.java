package br.com.battlebits.ybattlecraft.constructors;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

public abstract class WarpScoreboard {

	private String objId = "";

	public WarpScoreboard(String id) {
		objId = id;
	}

	public Objective getObjective(Player p) {
		if (p.getScoreboard().getObjective("warp" + objId) == null) {
			createObjective(p);
		}
		createScores(p);
		return p.getScoreboard().getObjective("warp" + objId);
	}

	public Objective createObjective(Player p) {
		if (p.getScoreboard().getObjective("warp" + objId) == null) {
			Objective obj = p.getScoreboard().registerNewObjective("warp" + objId, "dummy");
			obj.setDisplayName("");
		}
		createScores(p);
		return p.getScoreboard().getObjective("warp" + objId);
	}

	public void setTitle(Player p, String title) {
		if (p.getScoreboard().getObjective("warp" + objId) != null) {
			p.getScoreboard().getObjective("warp" + objId).setDisplayName(title);
		}
	}

	public void createScore(Player p, String id, String name, String value, int score) {
		String prefix = score + "";
		if (prefix.length() < 2) {
			prefix = "§" + prefix;
		} else {
			String part1 = "§" + prefix.substring(0, 1);
			String part2 = "§" + prefix.substring(1, 2);
			prefix = part1 + part2;
		}
		if (p.getScoreboard().getTeam(objId + id) == null) {
			p.getScoreboard().registerNewTeam(objId + id);
		}
		Team t = p.getScoreboard().getTeam(objId + id);
		t.setPrefix(name);
		t.setSuffix(value);
		t.addEntry(prefix);
		p.getScoreboard().getObjective("warp" + objId).getScore(prefix).setScore(score);
	}

	public void updateScoreValue(Player p, String id, String value) {
		if (p.getScoreboard().getTeam(objId + id) == null) {
			createScore(p, id, "", value, new Random().nextInt(1000));
		}
		Team t = p.getScoreboard().getTeam(objId + id);
		t.setSuffix(value);
	}

	public void updateScoreName(Player p, String id, String name) {
		if (p.getScoreboard().getTeam(objId + id) == null) {
			createScore(p, id, name, "", new Random().nextInt(1000));
		}
		Team t = p.getScoreboard().getTeam(objId + id);
		t.setPrefix(name);
	}

	public void setSidebar(Player p) {
		getObjective(p).setDisplaySlot(DisplaySlot.SIDEBAR);
	}

	public abstract void createScores(Player p);

}
