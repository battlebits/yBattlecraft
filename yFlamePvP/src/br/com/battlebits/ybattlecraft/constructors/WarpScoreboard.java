package br.com.battlebits.ybattlecraft.constructors;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

public abstract class WarpScoreboard {

	private String objId = "";
	private int objs;

	public WarpScoreboard(String id) {
		objId = id;
	}

	public Objective getObjective(Player p) {
		Objective obj = p.getScoreboard().getObjective("warp" + objId);
		if (obj == null) {
			obj = createObjective(p);
		}
		return obj;
	}

	public Objective createObjective(Player p) {
		Objective obj = p.getScoreboard().getObjective("warp" + objId);
		if (obj == null) {
			obj = p.getScoreboard().registerNewObjective("warp" + objId, "dummy");
			obj.setDisplayName("");
		}
		createScores(p);
		return obj;
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
		Team t = p.getScoreboard().getTeam(objId + id);
		if (t == null) {
			t = p.getScoreboard().registerNewTeam(objId + id);
		}
		t.setPrefix(name);
		t.setSuffix(value);
		t.addEntry(prefix);
		getObjective(p).getScore(prefix).setScore(score);
		if (score >= objs) {
			objs = score;
		}
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

	public void updateScore(Player p, String id, String name, String value) {
		if (p.getScoreboard().getTeam(objId + id) == null) {
			createScore(p, id, name, value, new Random().nextInt(1000));
		}
		Team t = p.getScoreboard().getTeam(objId + id);
		t.setPrefix(name);
		t.setSuffix(value);
	}

	public void setSidebar(Player p) {
		createScores(p);
		getObjective(p).getScore("§1").setScore(1);
		getObjective(p).setDisplaySlot(DisplaySlot.SIDEBAR);
		getObjective(p).getScore("§1").setScore(1);
	}

	public abstract void createScores(Player p);

}
