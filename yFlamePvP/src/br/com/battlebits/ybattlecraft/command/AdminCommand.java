package br.com.battlebits.ybattlecraft.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseCommand;

public class AdminCommand extends BaseCommand {
	
	public AdminCommand(yBattleCraft bc) {
		super(bc);
		description = "Ative ou desative seu modo admin";
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (battleCraft.getPermissions().isTrial(p)) {
				if (battleCraft.getAdminMode().isAdmin(p)) {
					battleCraft.getAdminMode().setPlayer(p);
				} else {
					battleCraft.getAdminMode().setAdmin(p);
				}
			} else {
				p.sendMessage("§cVocê não possui permissão para utilizar este comando.");
			}
		} else {
			sender.sendMessage("Comando apenas para jogadores.");
		}
		return false;
	}
	
}
