package br.com.battlebits.ybattlecraft.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.constructors.Status;
import br.com.battlebits.ybattlecraft.event.PlayerResetKDEvent;
import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.api.title.Title;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.account.game.GameType;
import br.com.battlebits.ycommon.common.commandmanager.CommandClass;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ResetKDCommand extends CommandClass {

	@Command(name = "resetkdconsole", groupToUse = Group.DONO, runAsync = false, noPermMessageId = "command-resetkd-no-access")
	public void resetkd(CommandArgs cmdArgs) {
		CommandSender sender = cmdArgs.getSender();
		Language language = BattlebitsAPI.getDefaultLanguage();
		String[] args = cmdArgs.getArgs();
		String resetKDPrefix = Translate.getTranslation(language, "command-resetkd-prefix") + " ";
		if (cmdArgs.isPlayer())
			language = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getLanguage();
		if (args.length != 1) {
			sender.sendMessage(resetKDPrefix + Translate.getTranslation(language, "command-resetkd-usage"));
			return;
		}
		UUID uuid = BattlebitsAPI.getUUIDOf(args[0]);
		if (uuid == null) {
			sender.sendMessage(resetKDPrefix + Translate.getTranslation(language, "player-not-exist"));
			return;
		}
		Player target = Bukkit.getPlayer(uuid);
		if (target == null) {
			sender.sendMessage(resetKDPrefix + Translate.getTranslation(language, "player-not-found"));
			return;
		}
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid);
		if (player == null) {
			try {
				player = BukkitMain.getPlugin().getAccountManager().loadPlayer(uuid);
			} catch (Exception e) {
				e.printStackTrace();
				sender.sendMessage(resetKDPrefix + Translate.getTranslation(language, "cant-request-offline"));
				return;
			}
			if (player == null) {
				sender.sendMessage(resetKDPrefix + Translate.getTranslation(language, "player-never-joined"));
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
		playerStatus.setCanResetKD();
		TextComponent requestMessage = new TextComponent(Translate.getTranslation(player.getLanguage(), "command-resetkd-prefix") + " " + Translate.getTranslation(player.getLanguage(), "command-resetkd-request"));
		TextComponent yes = new TextComponent(ChatColor.GREEN + "" + ChatColor.BOLD + Translate.getTranslation(player.getLanguage(), "yes").toUpperCase());
		yes.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/resetkd accept"));
		yes.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new TextComponent[] { new TextComponent(Translate.getTranslation(player.getLanguage(), "command-resetkd-hover-yes")) }));

		TextComponent no = new TextComponent(ChatColor.RED + "" + ChatColor.BOLD + Translate.getTranslation(player.getLanguage(), "no").toUpperCase());
		no.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/resetkd reject"));
		no.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new TextComponent[] { new TextComponent(Translate.getTranslation(player.getLanguage(), "command-resetkd-hover-no")) }));
		TextComponent space = new TextComponent(ChatColor.WHITE + " - ");
		for (int i = 0; i < 100; i++) {
			target.sendMessage(" ");
		}
		sender.sendMessage(resetKDPrefix + Translate.getTranslation(language, "command-resetkd-requested").replace("%player%", player.getUserName()));
		target.spigot().sendMessage(requestMessage, yes, space, no);
		target.playSound(target.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
		Title title = new Title(Translate.getTranslation(player.getLanguage(), "command-resetkd-request-title"));
		title.setSubtitle(Translate.getTranslation(player.getLanguage(), "command-resetkd-request-subtitle"));
		title.send(cmdArgs.getPlayer());
	}

	@Command(name = "resetkd", runAsync = false)
	public void resetkdplayer(CommandArgs cmdArgs) {
		if (!cmdArgs.isPlayer()) {
			return;
		}
		if (cmdArgs.getArgs().length != 1)
			return;
		if (!cmdArgs.getArgs()[0].equalsIgnoreCase("accept") && !cmdArgs.getArgs()[0].equalsIgnoreCase("reject"))
			return;
		UUID uuid = cmdArgs.getPlayer().getUniqueId();
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid);
		if (player == null) {
			try {
				player = BukkitMain.getPlugin().getAccountManager().loadPlayer(uuid);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			if (player == null) {
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
		String resetKDPrefix = Translate.getTranslation(player.getLanguage(), "command-resetkd-prefix") + " ";
		switch (cmdArgs.getArgs()[0].toLowerCase()) {
		case "accept":
			if (playerStatus.resetKD()) {
				cmdArgs.getPlayer().playSound(cmdArgs.getPlayer().getLocation(), Sound.LEVEL_UP, 1f, 1f);
				Title title = new Title(Translate.getTranslation(player.getLanguage(), "command-resetkd-accept-title"));
				title.setSubtitle(Translate.getTranslation(player.getLanguage(), "command-resetkd-accept-subtitle"));
				title.send(cmdArgs.getPlayer());
				playerStatus.setKills(0);
				playerStatus.setKillstreak(0);
				playerStatus.setDeaths(0);
				Bukkit.getPluginManager().callEvent(new PlayerResetKDEvent(cmdArgs.getPlayer()));
				cmdArgs.getSender().sendMessage(resetKDPrefix + Translate.getTranslation(player.getLanguage(), "command-resetkd-accepted-success"));
			}
			break;
		default:
			if (playerStatus.resetKD()) {
				cmdArgs.getSender().sendMessage(resetKDPrefix + Translate.getTranslation(player.getLanguage(), "command-resetkd-rejected-success"));
				Title title = new Title(Translate.getTranslation(player.getLanguage(), "command-resetkd-reject-title"));
				title.setSubtitle(Translate.getTranslation(player.getLanguage(), "command-resetkd-reject-subtitle"));
				title.send(cmdArgs.getPlayer());
			}
			break;
		}

	}

}
