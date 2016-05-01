package br.com.battlebits.ybattlecraft.constructors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.account.game.GameType;

public class Status {
	private int kills;
	private UUID uuid;
	private int deaths;
	private int killstreak;
	private List<String> kits;
	private List<String> kitsFavoritos;
	private boolean scoreboardEnabled;

	public Status(UUID uuid) {
		this(uuid, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), true);
	}

	public Status(UUID uuid, int kills, int deaths, int killstreak, List<String> kits, List<String> kitsFavoritos, boolean scoreboard) {
		this.uuid = uuid;
		this.kills = kills;
		this.deaths = deaths;
		this.killstreak = killstreak;
		this.kits = kits;
		this.kitsFavoritos = kitsFavoritos;
		this.scoreboardEnabled = scoreboard;
	}

	public int getKills() {
		return kills;
	}

	public UUID getUUID() {
		return uuid;
	}

	public int getDeaths() {
		return deaths;
	}

	public int getKillstreak() {
		return killstreak;
	}

	public List<String> getKits() {
		return kits;
	}

	public List<String> getKitsFavoritos() {
		return kitsFavoritos;
	}

	public void setKills(int kills) {
		this.kills = kills;
		save();
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
		save();
	}

	public void setKillstreak(int killstreak) {
		this.killstreak = killstreak;
		save();
	}

	public void addFavoriteKit(String kitName) {
		if (!kitsFavoritos.contains(kitName)) {
			kitsFavoritos.add(kitName);
		}
		save();
	}

	public void removeFavoriteKit(String kitName) {
		kitsFavoritos.remove(kitName);
		save();
	}

	public void addKills() {
		setKills(this.kills + 1);
		setKillstreak(this.killstreak + 1);
	}

	public void addDeaths() {
		setDeaths(this.deaths + 1);
		setKillstreak(0);
	}

	public boolean isScoreboardEnabled() {
		return scoreboardEnabled;
	}

	public void setScoreboardEnabled(boolean scoreboardEnabled) {
		this.scoreboardEnabled = scoreboardEnabled;
	}

	public void save() {
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid);
		player.getGameStatus().updateMinigame(GameType.BATTLECRAFT_PVP_STATUS, this);
		player = null;
	}

}
