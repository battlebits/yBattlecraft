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
					p.sendMessage("§6§LINVSEE §fUtilize /invsee §e§l<jogador>§f.");
				} else {
					Player t = Bukkit.getPlayer(args[0]);
					if (t != null) {
						p.sendMessage("§6§LINVSEE §fVisualizando inventario de §e§l" + t.getName() + "§f");
						p.openInventory(t.getInventory());
					} else {
						p.sendMessage("§6§LINVSEE §fJogador não §e§lencontrado§f.");
					}
				}
			} else {
				p.sendMessage("§6§LINVSEE §fVocê não tem §e§lacesso§f a este comando.");
			}
		} else {
			sender.sendMessage("§6§LINVSEE §fComando §e§lapenas§f para jogadores.");
		}
		return false;
	}
}
