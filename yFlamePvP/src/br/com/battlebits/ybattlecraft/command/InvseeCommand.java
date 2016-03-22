package br.com.battlebits.ybattlecraft.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseCommand;

public class InvseeCommand extends BaseCommand {

	public InvseeCommand(yBattleCraft bc) {
		super(bc);
		description = "Utilize este comando para abrir inventarios de outros jogadores";
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (battleCraft.getPermissions().isTrial(p)) {
				if (args.length == 0) {
					p.sendMessage("§cUse: /invsee <jogador>");
				} else {
					Player t = Bukkit.getPlayer(args[0]);
					if (t != null) {
						p.sendMessage("§eVisualizando inventario de " + t.getName());
						p.openInventory(t.getInventory());
					} else {
						p.sendMessage("§cJogador nao encontrado.");
					}
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
