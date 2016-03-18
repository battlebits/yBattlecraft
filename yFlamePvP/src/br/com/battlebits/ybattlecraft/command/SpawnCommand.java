package br.com.battlebits.ybattlecraft.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.battlebits.ybattlecraft.yBattleCraft;

public class SpawnCommand implements CommandExecutor {

	private yBattleCraft battlecraft;

	public SpawnCommand(yBattleCraft bc) {
		this.battlecraft = bc;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			battlecraft.getTeleportManager().tryToTeleport((Player) sender, battlecraft.getWarpManager().getWarpByName("spawn"));
		} else {
			sender.sendMessage("Comando apenas para jogadores.");
		}
		return false;
	}

}
