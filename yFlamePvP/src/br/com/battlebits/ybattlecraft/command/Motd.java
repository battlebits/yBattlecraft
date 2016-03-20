package br.com.battlebits.ybattlecraft.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.battlebits.ybattlecraft.yBattleCraft;

public class Motd implements CommandExecutor {
	private yBattleCraft m;

	public Motd(yBattleCraft m) {
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
				p.sendMessage(ChatColor.RED + "Voc� n�o possui permiss�o para utilizar este comando.");
				return true;
			}
			String motd = "";
			for (int i = 0; i < args.length; i++) {
				motd = motd + " " + args[i];
			}
			sender.sendMessage(ChatColor.AQUA + "MOTD mudado para: " + ChatColor.RESET + motd.replace("&", "�"));
			yBattleCraft.motd = motd.replace("&", "�");
			return true;
		}
		return false;
	}
}
