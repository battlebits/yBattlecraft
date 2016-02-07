package br.com.battlebits.battlecraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.battlebits.battlecraft.Main;

public class Admin  implements CommandExecutor {
	private Main m;

	public Admin(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Voce nao e um player");
			return true;
		}
		Player p = (Player) sender;
		if (label.equalsIgnoreCase("admin")) {
			if (!m.getPermissions().isTrial(p)) {
				p.sendMessage(ChatColor.RED + "Você não possui permissão para utilizar este comando.");
				return true;
			}
			if (m.getAdminMode().isAdmin(p)) {
				m.getAdminMode().setPlayer(p);
			} else {
				m.getAdminMode().setAdmin(p);
			}
			return true;
		}
		return false;
	}
}
