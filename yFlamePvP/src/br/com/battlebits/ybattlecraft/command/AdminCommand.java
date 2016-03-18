package br.com.battlebits.ybattlecraft.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.battlebits.ybattlecraft.yBattleCraft;

public class AdminCommand implements CommandExecutor {
	
	private yBattleCraft m;

	public AdminCommand(yBattleCraft m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (m.getPermissions().isTrial(p)) {
				if (m.getAdminMode().isAdmin(p)) {
					m.getAdminMode().setPlayer(p);
				} else {
					m.getAdminMode().setAdmin(p);
				}
			} else {
				p.sendMessage("§cVocê não possui permissão para utilizar este comando.");
			}
		} else {
			sender.sendMessage("Comando apenas para jogadores.");
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Voce nao e um player");
			return true;
		}
		return false;
	}
}
