package br.com.battlebits.ybattlecraft.managers;

import org.bukkit.entity.Player;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import me.flame.utils.permissions.enums.Group;

public class Permissions {
	public yBattleCraft m;

	public Permissions(yBattleCraft m) {
		this.m = m;
	}

	public boolean isLight(Player p) {
		return isPremium(p) || me.flame.utils.Main.getPlugin().getPermissionManager().hasGroupPermission(p, Group.LIGHT);
	}

	public boolean isPremium(Player p) {
		return isUltimate(p) || me.flame.utils.Main.getPlugin().getPermissionManager().hasGroupPermission(p, Group.PREMIUM);
	}

	public boolean isUltimate(Player p) {
		return isYouTuber(p) || me.flame.utils.Main.getPlugin().getPermissionManager().hasGroupPermission(p, Group.ULTIMATE);
	}

	public boolean isYouTuber(Player p) {
		return isTrial(p) || me.flame.utils.Main.getPlugin().getPermissionManager().hasGroupPermission(p, Group.YOUTUBER);
	}

	public boolean isTrial(Player p) {
		return isMod(p) || me.flame.utils.Main.getPlugin().getPermissionManager().hasGroupPermission(p, Group.TRIAL);
	}

	public boolean isMod(Player p) {
		return isAdmin(p) || me.flame.utils.Main.getPlugin().getPermissionManager().hasGroupPermission(p, Group.MOD);
	}

	public boolean isAdmin(Player p) {
		return isOwner(p) || me.flame.utils.Main.getPlugin().getPermissionManager().hasGroupPermission(p, Group.ADMIN);
	}

	public boolean isOwner(Player p) {
		return me.flame.utils.Main.getPlugin().getPermissionManager().isGroup(p, Group.DONO) || p.isOp();
	}
}
