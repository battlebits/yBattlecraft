package br.com.battlebits.battlecraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.battlebits.battlecraft.Main;

public class Motd implements CommandExecutor {
	private Main m;

	public Motd(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Voce nao e um player");
			return true;
		}
		Player p = (Player) sender;
		if (label.equalsIgnoreCase("motd")) {
			if (!m.getPermissions().isMod(p)) {
				p.sendMessage(ChatColor.RED + "Você não possui permissão para utilizar este comando.");
				return true;
			}
			String motd = "";
			for (int i = 0; i < args.length; i++) {
				motd = motd + " " + args[i];
			}
			sender.sendMessage(ChatColor.AQUA + "MOTD mudado para: " + ChatColor.RESET + motd.replace("&", "§"));
			Main.motd = motd.replace("&", "§");
			return true;
		}
		return false;
	}
}
