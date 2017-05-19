package br.com.battlebits.ybattlecraft.command;

import org.bukkit.entity.Player;

import br.com.battlebits.commons.bukkit.command.BukkitCommandArgs;
import br.com.battlebits.commons.core.command.CommandClass;
import br.com.battlebits.commons.core.command.CommandFramework.Command;
import br.com.battlebits.ybattlecraft.Battlecraft;

public class OneVsOne implements CommandClass {

	@Command(name = "1v1")
	public void onevsone(BukkitCommandArgs args) {
		if (args.isPlayer()) {
			Player p = args.getPlayer();
			if (!Battlecraft.getInstance().getWarpManager().isInWarp(p, "1v1")) {
				Battlecraft.getInstance().getTeleportManager().tryToTeleport(p,
						Battlecraft.getInstance().getWarpManager().getWarpByName("1v1"));
			} else {
				p.sendMessage("§9§LTELEPORTE §fVoce ja esta na 1v1!");
			}
		} else {
			args.getSender().sendMessage("§6§l1V1 §fComando §e§lAPENAS§f para jogadores.");
		}

	}

}
