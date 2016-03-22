package br.com.battlebits.ybattlecraft.base;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import br.com.battlebits.ybattlecraft.yBattleCraft;

public abstract class BaseCommandWithTab extends BaseCommand implements TabExecutor {

	public BaseCommandWithTab(yBattleCraft bc) {
		super(bc);
	}
	
	@Override
	public abstract List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args);
	
}
