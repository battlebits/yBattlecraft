package br.com.battlebits.ybattlecraft.command;

import br.com.battlebits.commons.bukkit.command.BukkitCommandArgs;
import br.com.battlebits.commons.core.command.CommandClass;
import br.com.battlebits.commons.core.command.CommandFramework.Command;
import br.com.battlebits.ybattlecraft.Battlecraft;

public class SpawnCommand implements CommandClass {

	@Command(name = "spawn")
	public void spawn(BukkitCommandArgs args) {
		if (args.isPlayer()) {
			Battlecraft.getInstance().getTeleportManager().tryToTeleport(args.getPlayer(),
					Battlecraft.getInstance().getWarpManager().getWarpByName("spawn"));
		} else {
			args.getSender().sendMessage("§9§lSPAWN §fComando §3§lAPENAS §fpara jogadores.");
		}
	}

}
