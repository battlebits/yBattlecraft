package br.com.battlebits.ybattlecraft.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.constructors.Status;
import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.account.game.GameType;
import br.com.battlebits.ycommon.common.commandmanager.CommandClass;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public class GiveKitCommand extends CommandClass {

	@Command(name = "givekit", groupToUse = Group.MODPLUS, runAsync = true, noPermMessageId = "command-givekit-no-access")
	public void giveKit(CommandArgs cmdArgs) {
		CommandSender sender = cmdArgs.getSender();
		Language language = BattlebitsAPI.getDefaultLanguage();
		String[] args = cmdArgs.getArgs();
		String giveKitPrefix = Translate.getTranslation(language, "command-givekit-prefix") + " ";
		if (cmdArgs.isPlayer())
			language = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getLanguage();
		if (args.length != 2) {
			sender.sendMessage(giveKitPrefix + Translate.getTranslation(language, "command-givekit-usage"));
			return;
		}
		String kitName = args[1].toLowerCase();

		UUID uuid = BattlebitsAPI.getUUIDOf(args[0]);
		if (uuid == null) {
			sender.sendMessage(giveKitPrefix + Translate.getTranslation(language, "player-not-exist"));
			return;
		}
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid);
		if (player == null) {
			try {
				player = BukkitMain.getPlugin().getAccountManager().loadPlayer(uuid);
			} catch (Exception e) {
				e.printStackTrace();
				sender.sendMessage(giveKitPrefix + Translate.getTranslation(language, "cant-request-offline"));
				return;
			}
			if (player == null) {
				sender.sendMessage(giveKitPrefix + Translate.getTranslation(language, "player-never-joined"));
				return;
			}
		}
		Status playerStatus = null;
		if (Bukkit.getPlayer(uuid) != null)
			playerStatus = yBattleCraft.getInstance().getStatusManager().getStatusByUuid(uuid);
		else
			playerStatus = player.getGameStatus().getMinigame(GameType.BATTLECRAFT_PVP_STATUS, Status.class);
		if (playerStatus == null)
			playerStatus = new Status(player.getUuid());
		playerStatus.setUuid(player.getUuid());
		if (playerStatus.addKit(kitName))
			sender.sendMessage(giveKitPrefix + Translate.getTranslation(language, "command-givekit-success").replace("%kitName%", kitName.toUpperCase()).replace("%player%", player.getUserName()));
		else
			sender.sendMessage(giveKitPrefix + Translate.getTranslation(language, "command-givekit-player-owns-kit").replace("%kitName%", kitName.toUpperCase()).replace("%player%", player.getUserName()));
	}

	@Command(name = "removekit", groupToUse = Group.ADMIN, runAsync = true, noPermMessageId = "command-removekit-no-access")
	public void removekit(CommandArgs cmdArgs) {
		CommandSender sender = cmdArgs.getSender();
		Language language = BattlebitsAPI.getDefaultLanguage();
		String[] args = cmdArgs.getArgs();
		String removeKitPrefix = Translate.getTranslation(language, "command-removekit-prefix") + " ";
		if (cmdArgs.isPlayer())
			language = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getLanguage();
		if (args.length != 2) {
			sender.sendMessage(removeKitPrefix + Translate.getTranslation(language, "command-removekit-usage"));
			return;
		}
		String kitName = args[1].toLowerCase();

		UUID uuid = BattlebitsAPI.getUUIDOf(args[0]);
		if (uuid == null) {
			sender.sendMessage(removeKitPrefix + Translate.getTranslation(language, "player-not-exist"));
			return;
		}
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid);
		if (player == null) {
			try {
				player = BukkitMain.getPlugin().getAccountManager().loadPlayer(uuid);
			} catch (Exception e) {
				e.printStackTrace();
				sender.sendMessage(removeKitPrefix + Translate.getTranslation(language, "cant-request-offline"));
				return;
			}
			if (player == null) {
				sender.sendMessage(removeKitPrefix + Translate.getTranslation(language, "player-never-joined"));
				return;
			}
		}
		Status playerStatus = null;
		if (Bukkit.getPlayer(uuid) != null)
			playerStatus = yBattleCraft.getInstance().getStatusManager().getStatusByUuid(uuid);
		else
			playerStatus = player.getGameStatus().getMinigame(GameType.BATTLECRAFT_PVP_STATUS, Status.class);
		if (playerStatus == null)
			playerStatus = new Status(player.getUuid());
		playerStatus.setUuid(player.getUuid());
		if (playerStatus.removeKit(kitName))
			sender.sendMessage(removeKitPrefix + Translate.getTranslation(language, "command-removekit-success").replace("%kitName%", kitName.toUpperCase()).replace("%player%", player.getUserName()));
		else
			sender.sendMessage(removeKitPrefix + Translate.getTranslation(language, "command-removekit-not-player-owns-kit").replace("%kitName%", kitName.toUpperCase()).replace("%player%", player.getUserName()));
	}
}
