package br.com.battlebits.ybattlecraft.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseCommand;

public class ReportCommand extends BaseCommand {

	public ReportCommand(yBattleCraft bc) {
		super(bc);
		description = "Denuncie os jogadores!";
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length == 0) {
				if (battleCraft.getPermissions().isTrial(p)) {
					battleCraft.getReportManager().open(p);
				} else {
					p.sendMessage("§9§LREPORT §FEspecifique o §3§ljogador §fque deseja denunciar.");
				}
			} else if (args.length == 2) {
				Player t = Bukkit.getPlayer(args[0]);
				if (t != null) {
					if (t.getUniqueId() != p.getUniqueId()) {
						battleCraft.getReportManager().report(p, t, args[1]);
						p.sendMessage("§9§lREPORT §fO report do jogador §3§l" + t.getName() + "§f foi enviado!");
					} else {
						p.sendMessage("§9§lREPORT §fVocê não pode §3§lREPORTAR§f você mesmo!");
					}
				} else {
					p.sendMessage("§9§lREPORT §fJogador não §3§lencontrado§f!");
				}
			} else {
				p.sendMessage("§9§LREPORT §FEspecifique o §3§ljogador §fque deseja denunciar.");
			}
		} else {
			sender.sendMessage("§4§lREPORT §fComando §c§lapenas§f para jogadores.");
		}
		return false;
	}

}
