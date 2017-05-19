package br.com.battlebits.ybattlecraft.command;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import br.com.battlebits.commons.bukkit.command.BukkitCommandArgs;
import br.com.battlebits.commons.core.account.BattlePlayer;
import br.com.battlebits.commons.core.command.CommandClass;
import br.com.battlebits.commons.core.command.CommandFramework.Command;
import br.com.battlebits.commons.core.translate.T;
import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.constructors.Status;

public class ScoreCommand implements CommandClass {
	@Command(name = "score", aliases = { "scoreboard" })
	public void admin(BukkitCommandArgs args) {
		if (args.isPlayer()) {
			Player p = args.getPlayer();
			Status s = Battlecraft.getInstance().getStatusManager().getStatusByUuid(p.getUniqueId());
			if (s.isScoreboardEnabled()) {
				p.getScoreboard().getObjective("clear").setDisplaySlot(DisplaySlot.SIDEBAR);
				p.sendMessage(
						T.t(Battlecraft.getInstance(), BattlePlayer.getLanguage(p.getUniqueId()), "scoreboard-disabled"));
				s.setScoreboardEnabled(false);
			} else {
				Battlecraft.getInstance().getWarpManager()
						.getWarpByName(Battlecraft.getInstance().getWarpManager().getPlayerWarp(p.getUniqueId()))
						.getScoreboard().setSidebar(p);
				p.sendMessage(
						T.t(Battlecraft.getInstance(), BattlePlayer.getLanguage(p.getUniqueId()), "scoreboard-enabled"));
				s.setScoreboardEnabled(true);
			}
		}

	}
}
