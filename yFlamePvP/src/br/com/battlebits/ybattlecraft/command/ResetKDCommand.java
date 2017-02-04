package br.com.battlebits.ybattlecraft.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.commons.BattlebitsAPI;
import br.com.battlebits.commons.api.title.TitleAPI;
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
import br.com.battlebits.ybattlecraft.event.PlayerResetKDEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ResetKDCommand implements CommandClass {

	@Command(name = "resetkdconsole", groupToUse = Group.DONO, runAsync = true, noPermMessageId = "command-resetkd-no-access")
	public void resetkd(BukkitCommandArgs cmdArgs) {
		CommandSender sender = ((BukkitCommandSender) cmdArgs.getSender()).getSender();
		Language language = BattlebitsAPI.getDefaultLanguage();
		String[] args = cmdArgs.getArgs();
		String resetKDPrefix = Translate.getTranslation(language, "command-resetkd-prefix") + " ";
		if (cmdArgs.isPlayer())
			language = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId())
					.getLanguage();
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
				player = DataPlayer.getPlayer(uuid);
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
			playerStatus = Battlecraft.getInstance().getStatusManager().getStatusByUuid(uuid);
		else
			playerStatus = null;// TODO GetPlayerStatus
		if (playerStatus == null)
			playerStatus = new Status(player.getUniqueId());
		playerStatus.setCanResetKD();
		final Language lang = language;
		final String userName = player.getName();
		if (Bukkit.getPlayer(uuid) != null) {
			new BukkitRunnable() {
				@Override
				public void run() {
					TextComponent requestMessage = new TextComponent(
							Translate.getTranslation(lang, "command-resetkd-prefix") + " "
									+ Translate.getTranslation(lang, "command-resetkd-request"));
					TextComponent yes = new TextComponent(ChatColor.GREEN + "" + ChatColor.BOLD
							+ Translate.getTranslation(lang, "yes").toUpperCase());
					yes.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/resetkd accept"));
					yes.setHoverEvent(
							new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new TextComponent[] {
									new TextComponent(Translate.getTranslation(lang, "command-resetkd-hover-yes")) }));

					TextComponent no = new TextComponent(
							ChatColor.RED + "" + ChatColor.BOLD + Translate.getTranslation(lang, "no").toUpperCase());
					no.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/resetkd reject"));
					no.setHoverEvent(
							new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new TextComponent[] {
									new TextComponent(Translate.getTranslation(lang, "command-resetkd-hover-no")) }));
					TextComponent space = new TextComponent(ChatColor.WHITE + " - ");
					for (int i = 0; i < 100; i++) {
						target.sendMessage(" ");
					}
					sender.sendMessage(resetKDPrefix + Translate.getTranslation(lang, "command-resetkd-requested")
							.replace("%player%", userName));
					target.spigot().sendMessage(requestMessage, yes, space, no);
					target.playSound(target.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
					TitleAPI.setTitle(target, Translate.getTranslation(lang, "command-resetkd-request-title"),
							Translate.getTranslation(lang, "command-resetkd-request-subtitle"));
				}
			}.runTaskLater(Battlecraft.getInstance(), 20);
		}
	}

	@Command(name = "resetkd", runAsync = false)
	public void resetkdplayer(BukkitCommandArgs cmdArgs) {
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
				player = DataPlayer.getPlayer(uuid);
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
			playerStatus = Battlecraft.getInstance().getStatusManager().getStatusByUuid(uuid);
		else
			playerStatus = null;// TODO Load Status
		if (playerStatus == null)
			playerStatus = new Status(player.getUniqueId());
		playerStatus.setUuid(player.getUniqueId());
		String resetKDPrefix = Translate.getTranslation(player.getLanguage(), "command-resetkd-prefix") + " ";
		switch (cmdArgs.getArgs()[0].toLowerCase()) {
		case "accept":
			if (playerStatus.resetKD()) {
				cmdArgs.getPlayer().playSound(cmdArgs.getPlayer().getLocation(), Sound.LEVEL_UP, 1f, 1f);
				TitleAPI.setTitle(cmdArgs.getPlayer(),
						Translate.getTranslation(player.getLanguage(), "command-resetkd-accept-title"),
						Translate.getTranslation(player.getLanguage(), "command-resetkd-accept-subtitle"));
				playerStatus.setKills(0);
				playerStatus.setKillstreak(0);
				playerStatus.setDeaths(0);
				Bukkit.getPluginManager().callEvent(new PlayerResetKDEvent(cmdArgs.getPlayer()));
				cmdArgs.getSender().sendMessage(resetKDPrefix
						+ Translate.getTranslation(player.getLanguage(), "command-resetkd-accepted-success"));
			}
			break;
		default:
			if (playerStatus.resetKD()) {
				cmdArgs.getSender().sendMessage(resetKDPrefix
						+ Translate.getTranslation(player.getLanguage(), "command-resetkd-rejected-success"));
				TitleAPI.setTitle(cmdArgs.getPlayer(),
						Translate.getTranslation(player.getLanguage(), "command-resetkd-reject-title"),
						Translate.getTranslation(player.getLanguage(), "command-resetkd-reject-subtitle"));
			}
			break;
		}

	}

}
