package br.com.battlebits.battlecraft.admin;

import java.util.HashMap;
import java.util.UUID;

import me.flame.utils.permissions.enums.Group;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.warps.Warp1v1;

public class Vanish {
	private static HashMap<UUID, VLevel> vanished = new HashMap<>();
	private Main m;

	public Vanish(Main main) {
		this.m = main;
	}

	public void makeVanished(Player p) {
		if (m.getPermissions().isOwner(p)) {
			makeVanished(p, VLevel.DONO);
		} else if (m.getPermissions().isAdmin(p)) {
			makeVanished(p, VLevel.ADMIN);
		} else if (m.getPermissions().isMod(p)) {
			makeVanished(p, VLevel.MODPLUS);
		} else if (m.getPermissions().isTrial(p)) {
			makeVanished(p, VLevel.MOD);
		} else if (m.getPermissions().isYouTuber(p)) {
			makeVanished(p, VLevel.YOUTUBER);
		}
	}

	public void makeVanished(Player p, VLevel level) {
		if (level.equals(VLevel.YOUTUBER)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.showPlayer(p);
				if (player.getName().equals(p.getName()))
					continue;
				if (!m.getPermissions().isYouTuber(player) || (m.getPermissions().isYouTuber(player) && !m.getPermissions().isTrial(player)))
					player.hidePlayer(p);
			}
		} else if (level.equals(VLevel.MOD)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.showPlayer(p);
				if (player.getName().equals(p.getName()))
					continue;
				if (!m.getPermissions().isTrial(player))
					player.hidePlayer(p);
			}
		} else if (level.equals(VLevel.MODPLUS)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.showPlayer(p);
				if (player.getName().equals(p.getName()))
					continue;
				if (!m.getPermissions().isMod(player))
					player.hidePlayer(p);
			}
		} else if (level.equals(VLevel.ADMIN)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.showPlayer(p);
				if (player.getName().equals(p.getName()))
					continue;
				if (!m.getPermissions().isAdmin(player))
					player.hidePlayer(p);
			}
		} else if (level.equals(VLevel.DONO)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.showPlayer(p);
				if (player.getName().equals(p.getName()))
					continue;
				if (!m.getPermissions().isOwner(player))
					player.hidePlayer(p);
			}
		}
		vanished.put(p.getUniqueId(), level);
	}

	public boolean isVanished(Player p) {
		return vanished.containsKey(p.getUniqueId()) && !vanished.get(p.getUniqueId()).equals(VLevel.PLAYER);
	}

	public VLevel getPlayerLevel(Player p) {
		return vanished.get(p.getUniqueId());
	}

	public void updateVanished() {
		for (Player p : Bukkit.getOnlinePlayers())
			if (isVanished(p)) {
				makeVanished(p, vanished.get(p.getUniqueId()));
			} else {
				makeVisible(p);
			}
	}

	public void updateVanished(Player player) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (player.getName().equals(p.getName()))
				continue;
			if (isVanished(p)) {
				if (me.flame.utils.Main.getPlugin().getPermissionManager().hasGroupPermission(player, Group.MOD))
					continue;
				if (!player.canSee(p))
					continue;
				player.hidePlayer(p);
			} else {
				if (Warp1v1.isIn1v1(player))
					continue;
				if (player.canSee(p))
					continue;
				player.showPlayer(p);
			}
		}
	}

	public void removeVanished(Player p) {
		vanished.remove(p.getUniqueId());
	}

	public void makeVisible(Player p) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.showPlayer(p);
		}
		vanished.put(p.getUniqueId(), VLevel.PLAYER);
	}
}
