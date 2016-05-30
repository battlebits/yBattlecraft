package br.com.battlebits.ybattlecraft.loader;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseCommand;
import br.com.battlebits.ybattlecraft.base.BaseCommandWithTab;
import br.com.battlebits.ybattlecraft.utils.ClassGetter;

public class CommandLoader {

	private yBattleCraft battleCraft;
	private SimpleCommandMap commandMap;

	public CommandLoader(yBattleCraft plugin) {
		this.battleCraft = plugin;
		try {
			Field commandmapfield = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			commandmapfield.setAccessible(true);
			commandMap = (SimpleCommandMap) commandmapfield.get(Bukkit.getServer());
		} catch (Exception e) {
			Bukkit.getLogger().warning("Erro ao carregar Command Map!");
		}
	}

	public void loadCommandsAndRegister() {
		for (Class<?> commandClass : ClassGetter.getClassesForPackage(battleCraft, "br.com.battlebits.ybattlecraft.command")) {
			if (BaseCommandWithTab.class.isAssignableFrom(commandClass)) {
				try {
					BaseCommandWithTab command = null;
					try {
						Constructor<?> con = commandClass.getConstructor(yBattleCraft.class);
						command = (BaseCommandWithTab) con.newInstance(battleCraft);
					} catch (Exception ex) {
						command = (BaseCommandWithTab) commandClass.newInstance();
					}
					registerCommand(command, command.getClass().getSimpleName().substring(0, command.getClass().getSimpleName().length() - 7), command.getDescription(), command.getAliases()).setTabCompleter(command);
				} catch (Exception e) {
					battleCraft.getLogger().warning("Erro ao carregar o comando " + commandClass.getSimpleName() + " (With TabCompleter)");
				}
			} else if (BaseCommand.class.isAssignableFrom(commandClass)) {
				try {
					BaseCommand command = null;
					try {
						Constructor<?> con = commandClass.getConstructor(yBattleCraft.class);
						command = (BaseCommand) con.newInstance(battleCraft);
					} catch (Exception ex) {
						command = (BaseCommand) commandClass.newInstance();
					}
					registerCommand(command, command.getClass().getSimpleName().substring(0, command.getClass().getSimpleName().length() - 7), command.getDescription(), command.getAliases());
				} catch (Exception e) {
					e.printStackTrace();
					battleCraft.getLogger().warning("Erro ao carregar o comando " + commandClass.getSimpleName());
				}
			}
		}
	}

	private PluginCommand registerCommand(CommandExecutor executor, String name, String description, String[] aliases) {
		try {
			PluginCommand command = battleCraft.getCommand(name.toLowerCase());
			if (command == null) {
				Constructor<?> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
				constructor.setAccessible(true);
				command = (PluginCommand) constructor.newInstance(name, battleCraft);
			}
			command.setExecutor(executor);
			command.setAliases(Arrays.asList(aliases));
			command.setDescription(description);
			commandMap.register(name, command);
			return command;
		} catch (Exception e) {
			battleCraft.getLogger().warning("Erro ao registrar o comando " + name);
		}
		return null;
	}

}
