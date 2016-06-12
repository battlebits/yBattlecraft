package br.com.battlebits.ybattlecraft.constructors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.account.game.GameType;

public class Status {
	private transient UUID uuid;
	private int kills = 0;
	private int deaths = 0;
	private int killstreak = 0;
	private List<String> kits = new ArrayList<>();
	private List<String> kitsFavoritos = new ArrayList<>();
	private boolean scoreboardEnabled = true;
	private transient Map<UUID, Double> damageTaken = new HashMap<>();

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

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
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

	public boolean addKit(String kitName) {
		if (hasKit(kitName))
			return false;
		kits.add(kitName.toLowerCase());
		save();
		return true;
	}

	public boolean removeKit(String kitName) {
		if (!hasKit(kitName))
			return false;
		kits.remove(kitName);
		save();
		return true;
	}

	public boolean hasKit(String kitName) {
		return kits.contains(kitName.toLowerCase());
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

	public void addDamage(UUID uuid, double damage) {
		double d = 0;
		if (damageTaken == null)
			damageTaken = new HashMap<>();
		if (!damageTaken.isEmpty())
			if (damageTaken.containsKey(uuid))
				d = damageTaken.get(uuid);
		d += damage;
		damageTaken.put(uuid, d);
	}

	public void death() {
		if (damageTaken != null)
			if (!damageTaken.isEmpty())
				damageTaken.clear();
	}

	public double getPorcentagemTaken(UUID uuid) {
		if (!damageTaken.containsKey(uuid))
			return 0;
		double total = getTotalDamageTaken();
		double received = damageTaken.get(uuid);
		double porcentagem = received * 100 / total;
		return porcentagem;
	}

	public double getTotalDamageTaken() {
		double ret = 0;
		for (Double d : damageTaken.values()) {
			ret += d;
		}
		return ret;
	}

	public void save() {
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid);
		player.getGameStatus().updateMinigame(GameType.BATTLECRAFT_PVP_STATUS, this);
		player = null;
	}

}
