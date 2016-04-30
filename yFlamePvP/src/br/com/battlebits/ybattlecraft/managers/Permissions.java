package br.com.battlebits.ybattlecraft.managers;

import org.bukkit.entity.Player;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.permissions.enums.Group;

public class Permissions {
	public yBattleCraft m;

	public Permissions(yBattleCraft m) {
		this.m = m;
	}

	public boolean isLight(Player p) {
		return isPremium(p) || BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).hasGroupPermission(Group.LIGHT);
	}

	public boolean isPremium(Player p) {
		return isUltimate(p) || BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).hasGroupPermission(Group.PREMIUM);
	}

	public boolean isUltimate(Player p) {
		return isYouTuber(p) || BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).hasGroupPermission(Group.ULTIMATE);
	}

	public boolean isYouTuber(Player p) {
		return isTrial(p) || BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).hasGroupPermission(Group.YOUTUBER);
	}

	public boolean isTrial(Player p) {
		return isMod(p) || BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).hasGroupPermission(Group.TRIAL);
	}

	public boolean isMod(Player p) {
		return isAdmin(p) || BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).hasGroupPermission(Group.MOD);
	}

	public boolean isAdmin(Player p) {
		return isOwner(p) || BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).hasGroupPermission(Group.ADMIN);
	}

	public boolean isOwner(Player p) {
		return BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).hasGroupPermission(Group.DONO) || p.isOp();
	}
}
