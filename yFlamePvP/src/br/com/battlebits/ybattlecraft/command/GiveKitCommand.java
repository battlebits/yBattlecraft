package br.com.battlebits.ybattlecraft.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import br.com.battlebits.commons.BattlebitsAPI;
import br.com.battlebits.commons.bukkit.command.BukkitCommandArgs;
import br.com.battlebits.commons.bukkit.command.BukkitCommandSender;
import br.com.battlebits.commons.core.account.BattlePlayer;
import br.com.battlebits.commons.core.command.CommandClass;
import br.com.battlebits.commons.core.command.CommandFramework.Command;
import br.com.battlebits.commons.core.data.DataPlayer;
import br.com.battlebits.commons.core.permission.Group;
import br.com.battlebits.commons.core.translate.Language;
import br.com.battlebits.commons.core.translate.Translate;
import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.constructors.Status;
import br.com.battlebits.ybattlecraft.data.DataStatus;

public class GiveKitCommand implements CommandClass {

	@Command(name = "givekit", groupToUse = Group.MODPLUS, runAsync = true, noPermMessageId = "command-givekit-no-access")
	public void giveKit(BukkitCommandArgs cmdArgs) {
		CommandSender sender = ((BukkitCommandSender) cmdArgs.getSender()).getSender();
		Language language = BattlebitsAPI.getDefaultLanguage();
		String[] args = cmdArgs.getArgs();
		String giveKitPrefix = Translate.getTranslation(language, "command-givekit-prefix") + " ";
		if (cmdArgs.isPlayer())
			language = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId())
					.getLanguage();
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
				player = DataPlayer.getPlayer(uuid);
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
			playerStatus = Battlecraft.getInstance().getStatusManager().getStatusByUuid(uuid);
		else
			playerStatus = DataStatus.createIfNotExistMongo(uuid);
		if (playerStatus.addKit(kitName))
			sender.sendMessage(giveKitPrefix + Translate.getTranslation(language, "command-givekit-success")
					.replace("%kitName%", kitName.toUpperCase()).replace("%player%", player.getName()));
		else
			sender.sendMessage(giveKitPrefix + Translate.getTranslation(language, "command-givekit-player-owns-kit")
					.replace("%kitName%", kitName.toUpperCase()).replace("%player%", player.getName()));
	}

	@Command(name = "removekit", groupToUse = Group.ADMIN, runAsync = true, noPermMessageId = "command-removekit-no-access")
	public void removekit(BukkitCommandArgs cmdArgs) {
		CommandSender sender = ((BukkitCommandSender) cmdArgs.getSender()).getSender();
		Language language = BattlebitsAPI.getDefaultLanguage();
		String[] args = cmdArgs.getArgs();
		String removeKitPrefix = Translate.getTranslation(language, "command-removekit-prefix") + " ";
		if (cmdArgs.isPlayer())
			language = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId())
					.getLanguage();
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
				player = DataPlayer.getPlayer(uuid);
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
			playerStatus = Battlecraft.getInstance().getStatusManager().getStatusByUuid(uuid);
		else
			playerStatus = null;// TODO GetPlayerStatus
		if (playerStatus == null)
			playerStatus = new Status(player.getUniqueId());
		if (playerStatus.removeKit(kitName))
			sender.sendMessage(removeKitPrefix + Translate.getTranslation(language, "command-removekit-success")
					.replace("%kitName%", kitName.toUpperCase()).replace("%player%", player.getName()));
		else
			sender.sendMessage(
					removeKitPrefix + Translate.getTranslation(language, "command-removekit-not-player-owns-kit")
							.replace("%kitName%", kitName.toUpperCase()).replace("%player%", player.getName()));
	}
}
