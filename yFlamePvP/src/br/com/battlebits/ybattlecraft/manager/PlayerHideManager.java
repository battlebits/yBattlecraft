package br.com.battlebits.ybattlecraft.manager;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.battlebits.ybattlecraft.yBattleCraft;

public class PlayerHideManager {

	private yBattleCraft battleCraft;
	private ArrayList<UUID> hideAllPlayers;
//	private ArrayList<UUID> hideForAll;

	public PlayerHideManager(yBattleCraft bc) {
		hideAllPlayers = new ArrayList<>();
//		hideForAll = new ArrayList<>();
		battleCraft = bc;
	}

	public void playerJoin(Player p) {
		for (UUID id : hideAllPlayers) {
			Player hide = Bukkit.getPlayer(id);
			if (hide != null) {
				hide.hidePlayer(p);
			}
		}
//		for (UUID id : hideForAll) {
//			Player hide = Bukkit.getPlayer(id);
//			if (hide != null) {
//				p.hidePlayer(hide);
//			}
//		}
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
			if (!hideAllPlayers.contains(show.getUniqueId()) && !battleCraft.getAdminMode().isAdmin(show)) {
				if (show.getUniqueId() != p.getUniqueId()) {
					p.showPlayer(show);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void showForAll(Player p) {
//		if (hideForAll.contains(p.getUniqueId())) {
//			hideForAll.remove(p.getUniqueId());
//			for (Player show : Bukkit.getOnlinePlayers()) {
//				if (!hideAllPlayers.contains(p.getUniqueId())) {
//					if (show.getUniqueId() != p.getUniqueId()) {
//						show.showPlayer(p);
//					}
//				}
//			}
//		}
	}

	@SuppressWarnings("deprecation")
	public void hideForAll(Player p) {
//		if (!hideForAll.contains(p.getUniqueId())) {
//			hideForAll.add(p.getUniqueId());
//			for (Player hide : Bukkit.getOnlinePlayers()) {
//				if (hide.getUniqueId() != p.getUniqueId()) {
//					hide.hidePlayer(p);
//				}
//			}
//		}
	}

	public boolean isHiding(UUID id) {
		return hideAllPlayers.contains(id);
	}
	
	public boolean hideForAll(UUID id){
		return false;
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
//		hideForAll.remove(id);
	}

}
