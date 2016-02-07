package br.com.battlebits.battlecraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.battlebits.battlecraft.Main;

public class Invsee implements CommandExecutor {
	private Main m;

	public Invsee(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Voce nao e um player");
			return true;
		}
		Player p = (Player) sender;
		if (label.equalsIgnoreCase("invsee")) {
			if (!m.getPermissions().isTrial(p)) {
				p.sendMessage(ChatColor.RED + "Você não possui permissão para utilizar este comando.");
				return true;
			}
			if (args.length == 1) {
				@SuppressWarnings("deprecation")
				Player player = m.getServer().getPlayer(args[0]);
				if (player == null) {
					p.sendMessage(ChatColor.RED + "Este player não existe ou está offline.");
					return true;
				}
				p.openInventory(player.getInventory());
				return true;
			}
			p.sendMessage(ChatColor.RED + "Use: /invsee <player>");
			return true;
		}
		return false;
	}
}
