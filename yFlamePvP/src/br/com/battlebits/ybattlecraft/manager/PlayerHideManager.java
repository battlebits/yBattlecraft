package br.com.battlebits.ybattlecraft.manager;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.battlebits.ybattlecraft.yBattleCraft;

public class PlayerHideManager {

	private yBattleCraft battleCraft;
	private ArrayList<UUID> hideAllPlayers;

	public PlayerHideManager(yBattleCraft bc) {
		hideAllPlayers = new ArrayList<>();
		battleCraft = bc;
	}

	public void playerJoin(Player p) {
		for (UUID id : hideAllPlayers) {
			Player hide = Bukkit.getPlayer(id);
			hide.hidePlayer(p);
		}
	}

	public void hideOnlyNormal(Player p) {
		if (hideAllPlayers.contains(p.getUniqueId())) {
			hideAllPlayers.remove(p.getUniqueId());
		}
	}

	@SuppressWarnings("deprecation")
	public void hideAllPlayers(Player p) {
		hideAllPlayers.add(p.getUniqueId());
		for (Player hide : Bukkit.getOnlinePlayers()) {
			if (hide.getUniqueId() != p.getUniqueId()) {
				p.hidePlayer(hide);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void showAllPlayers(Player p) {
		if (hideAllPlayers.contains(p.getUniqueId())) {
			hideAllPlayers.remove(p.getUniqueId());
		}
		for (Player show : Bukkit.getOnlinePlayers()) {
			if (!battleCraft.getAdminMode().isAdmin(show)) {
				p.showPlayer(show);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			for (Player show : Bukkit.getOnlinePlayers()) {
				p.showPlayer(show);
			}
		}
		hideAllPlayers.clear();
	}

	public void tryToRemoveFromLists(UUID id) {
		hideAllPlayers.remove(id);
	}

}
