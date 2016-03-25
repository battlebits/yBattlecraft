package br.com.battlebits.ybattlecraft.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseCommand;
import br.com.battlebits.ybattlecraft.constructors.Status;

public class ScoreCommand extends BaseCommand {

	public ScoreCommand(yBattleCraft bc) {
		super(bc);
		description = "Utilize este comando para ativar e desativar sua scoreboard";
		aliases = new String[] { "scoreboard" };
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			Status s = battleCraft.getStatusManager().getStatusByUuid(p.getUniqueId());
			if (s.isScoreboardEnabled()) {
				p.getScoreboard().getObjective("clear").setDisplaySlot(DisplaySlot.SIDEBAR);
				p.sendMessage("§6§lSCOREBOARD §fVoce §e§lDESATIVOU §fa Scoreboard!");
				s.setScoreboardEnabled(false);
			} else {
				battleCraft.getWarpManager().getWarpByName(battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId())).getScoreboard().setSidebar(p);
				p.sendMessage("§6§lSCOREBOARD §fVoce §e§lATIVOU §fa Scoreboard!");
				s.setScoreboardEnabled(true);
			}
		} else {
			sender.sendMessage("§6§lSCOREBOARD §fComando §e§lAPENAS§f para jogadores.");
		}
		return false;
	}

}
