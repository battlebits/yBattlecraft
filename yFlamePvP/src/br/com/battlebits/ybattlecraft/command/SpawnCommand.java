package br.com.battlebits.ybattlecraft.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseCommand;

public class SpawnCommand extends BaseCommand {

	public SpawnCommand(yBattleCraft bc) {
		super(bc);
		description = "Utilize este comando para ir ao Spawn";
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			battleCraft.getTeleportManager().tryToTeleport((Player) sender, battleCraft.getWarpManager().getWarpByName("spawn"));
		} else {
			sender.sendMessage("Comando apenas para jogadores.");
		}
		return false;
	}

}
