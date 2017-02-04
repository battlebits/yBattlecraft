package br.com.battlebits.ybattlecraft.manager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import br.com.battlebits.commons.api.vanish.VanishAPI;

public class PlayerHideManager {

	private Set<UUID> hideAllPlayers;
	private Set<UUID> hidePlayers;

	public PlayerHideManager() {
		hideAllPlayers = new HashSet<>();
		hidePlayers = new HashSet<>();
	}

	public void hideAllPlayers(Player p) {
		hideAllPlayers.add(p.getUniqueId());
		VanishAPI.getInstance().updateVanishToPlayer(p);
	}

	public boolean isHidingAllPlayers(Player p) {
		return hideAllPlayers.contains(p.getUniqueId());
	}

	public void showAllPlayers(Player p) {
		if (hideAllPlayers.contains(p.getUniqueId())) {
			hideAllPlayers.remove(p.getUniqueId());
		}
		VanishAPI.getInstance().updateVanishToPlayer(p);
	}

	public void hidePlayer(Player p) {
		hidePlayers.add(p.getUniqueId());
		VanishAPI.getInstance().showPlayer(p);
	}

	public boolean isHiding(Player p) {
		return hidePlayers.contains(p.getUniqueId());
	}

	public void showPlayer(Player p) {
		if (hidePlayers.contains(p.getUniqueId())) {
			hidePlayers.remove(p.getUniqueId());
		}
		VanishAPI.getInstance().showPlayer(p);
	}

	public void tryToRemoveFromLists(UUID id) {
		hideAllPlayers.remove(id);
		hidePlayers.remove(id);
	}

	public void stop() {
		hideAllPlayers.clear();
		hidePlayers.clear();
	}
}
