package br.com.battlebits.ybattlecraft.constructors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import br.com.battlebits.ybattlecraft.data.DataStatus;

public class Status {
	private UUID uniqueId;
	private int kills = 0;
	private int deaths = 0;
	private int killstreak = 0;
	private Set<String> kits = new HashSet<>();
	private Set<String> kitsFavoritos = new HashSet<>();
	private boolean scoreboardEnabled = true;
	private transient Map<UUID, Double> damageTaken = new HashMap<>();
	private transient Map<UUID, Integer> killedPlayers = new HashMap<>();
	private boolean canResetKD = false;

	public Status(UUID uuid) {
		this(uuid, 0, 0, 0, new HashSet<>(), new HashSet<>(), true);
	}

	public Status(UUID uuid, int kills, int deaths, int killstreak, Set<String> kits, Set<String> kitsFavoritos,
			boolean scoreboard) {
		this.uniqueId = uuid;
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

	public UUID getUniqueId() {
		return uniqueId;
	}

	public int getDeaths() {
		return deaths;
	}

	public int getKillstreak() {
		return killstreak;
	}

	public Set<String> getKits() {
		return kits;
	}

	public Set<String> getKitsFavoritos() {
		return kitsFavoritos;
	}

	public void setKills(int kills) {
		this.kills = kills;
		DataStatus.saveStatusField(this, "kills");
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
		DataStatus.saveStatusField(this, "deaths");
	}

	public void setKillstreak(int killstreak) {
		this.killstreak = killstreak;
		DataStatus.saveStatusField(this, "killstreak");
	}

	public boolean addKit(String kitName) {
		if (hasKit(kitName))
			return false;
		kits.add(kitName.toLowerCase());
		DataStatus.saveStatusField(this, "kits");
		return true;
	}

	public boolean removeKit(String kitName) {
		if (!hasKit(kitName))
			return false;
		kits.remove(kitName);
		DataStatus.saveStatusField(this, "kits");
		return true;
	}

	public boolean hasKit(String kitName) {
		if (kits == null)
			kits = new HashSet<>();
		if (kits.isEmpty())
			return false;
		if (kitName.isEmpty()) {
			System.out.println("Jogador " + uniqueId.toString() + " nao pode adicionar o kit " + kitName);
			return true;
		}
		return kits.contains(kitName.toLowerCase());
	}

	public void addFavoriteKit(String kitName) {
		if (!kitsFavoritos.contains(kitName)) {
			kitsFavoritos.add(kitName);
			DataStatus.saveStatusField(this, "kitsFavoritos");
		}
	}

	public void removeFavoriteKit(String kitName) {
		kitsFavoritos.remove(kitName);
		DataStatus.saveStatusField(this, "kitsFavoritos");
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
		DataStatus.saveStatusField(this, "scoreboardEnabled");
	}

	public void addKill(UUID uuid) {
		int k = 0;
		if (killedPlayers == null)
			killedPlayers = new HashMap<>();
		if (!killedPlayers.isEmpty())
			if (killedPlayers.containsKey(uuid))
				k = killedPlayers.get(uuid);
		killedPlayers.put(uuid, ++k);
	}

	public void setCanResetKD() {
		this.canResetKD = true;
		DataStatus.saveStatusField(this, "canResetKD");
	}

	public boolean resetKD() {
		if (this.canResetKD) {
			this.canResetKD = false;
			return true;
		}
		return false;
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
		if (damageTaken == null)
			return 0;
		if (!damageTaken.containsKey(uuid))
			return 0;
		double total = getTotalDamageTaken();
		double received = damageTaken.get(uuid);
		double porcentagem = received * 100 / total;
		return porcentagem;
	}

	public double getPorcentagemKilled(UUID uuid) {
		if (killedPlayers == null)
			return 0;
		if (!killedPlayers.containsKey(uuid))
			return 0;
		int total = getTotalKills();
		int times = killedPlayers.get(uuid);
		double porcentagem = times * 100 / total;
		return porcentagem;
	}

	public double getTotalDamageTaken() {
		if (damageTaken == null)
			return 0;
		double ret = 0;
		for (Double d : damageTaken.values()) {
			ret += d;
		}
		return ret;
	}

	public int getTotalKills() {
		if (killedPlayers == null)
			return 0;
		int ret = 0;
		for (Integer d : killedPlayers.values()) {
			ret += d;
		}
		return ret;
	}

}
