package br.com.battlebits.ybattlecraft.base;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import br.com.battlebits.ybattlecraft.yBattleCraft;

public abstract class BaseCommand implements CommandExecutor {

	public yBattleCraft battleCraft;
	public String description;
	public String[] aliases;
	private String name;

	public BaseCommand(yBattleCraft bc) {
		this.battleCraft = bc;
		this.name = this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - 7);
		this.aliases = new String[] {};
		this.description = "";
	}

	public String[] getAliases() {
		return aliases;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	@Override
	public abstract boolean onCommand(CommandSender sender, Command cmd, String label, String[] args);

}
