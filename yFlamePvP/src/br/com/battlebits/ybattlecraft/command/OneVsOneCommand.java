package br.com.battlebits.ybattlecraft.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseCommand;

public class OneVsOneCommand extends BaseCommand {

	public OneVsOneCommand(yBattleCraft bc) {
		super(bc);
		description = "Utilize este comando para entrar em modo batalha";
		aliases = new String[] { "1v1" };
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (!battleCraft.getWarpManager().isInWarp(p, "1v1")) {
				battleCraft.getTeleportManager().tryToTeleport(p, battleCraft.getWarpManager().getWarpByName("1v1"));
			} else {
				p.sendMessage("§9§LTELEPORTE §fVoce ja esta na 1v1!");
			}
		} else {
			sender.sendMessage("§6§lSCOREBOARD §fComando §e§lAPENAS§f para jogadores.");
		}
		return false;
	}

}
