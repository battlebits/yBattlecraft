package br.com.battlebits.battlecraft.constructors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.battlebits.battlecraft.managers.StatusManager;

public class Status {
	private StatusManager manager;
	private int kills;
	private UUID uuid;
	private int deaths;
	private int killstreak;
	private List<String> kits;
	private List<String> kitsFavoritos;

	public Status(StatusManager manager, UUID uuid) {
		this(manager, uuid, 0, 0, 0, new ArrayList<>(), new ArrayList<>());
	}

	public Status(StatusManager manager, UUID uuid, int kills, int deaths, int killstreak, List<String> kits, List<String> kitsFavoritos) {
		this.manager = manager;
		this.uuid = uuid;
		this.kills = kills;
		this.deaths = deaths;
		this.killstreak = killstreak;
		this.kits = kits;
		this.kitsFavoritos = kitsFavoritos;
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
		manager.saveStatus(uuid);
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
		manager.saveStatus(uuid);
	}

	public void setKillstreak(int killstreak) {
		this.killstreak = killstreak;
		manager.saveStatus(uuid);
	}

	public void addFavoriteKit(String kitName) {
		if (!kitsFavoritos.contains(kitName)) {
			kitsFavoritos.add(kitName);
			manager.addFavoriteKit(uuid, kitName);
		}
	}

	public void removeFavoriteKit(String kitName) {
		kitsFavoritos.remove(kitName);
		manager.removeFavoriteKit(uuid, kitName);
	}

	public void addKills() {
		this.kills += 1;
		this.killstreak += 1;
		manager.saveStatus(uuid);
	}

	public void addDeaths() {
		this.deaths += 1;
		this.killstreak = 0;
		manager.saveStatus(uuid);
	}

}
