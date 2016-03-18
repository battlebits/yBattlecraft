package br.com.battlebits.ybattlecraft.loader;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import br.com.battlebits.ybattlecraft.yBattleCraft;
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
			if (CommandExecutor.class.isAssignableFrom(commandClass)) {
				try {
					CommandExecutor executor = null;
					try {
						Constructor<?> con = commandClass.getConstructor(yBattleCraft.class);
						executor = (CommandExecutor) con.newInstance(battleCraft);
					} catch (Exception ex) {
						executor = (CommandExecutor) commandClass.newInstance();
					}
					registerCommand(executor, executor.getClass().getSimpleName().substring(0, executor.getClass().getSimpleName().length() - 7));
				} catch (Exception e) {
					System.out.print("Erro ao carregar o comando " + commandClass.getSimpleName());
				}
			}
			if (TabCompleter.class.isAssignableFrom(commandClass)) {
				try {
					TabCompleter completer = null;
					try {
						Constructor<?> con = commandClass.getConstructor(yBattleCraft.class);
						completer = (TabCompleter) con.newInstance(battleCraft);
					} catch (Exception ex) {
						completer = (TabCompleter) commandClass.newInstance();
					}
					battleCraft.getCommand(completer.getClass().getSimpleName().substring(0, completer.getClass().getSimpleName().length() - 7))
							.setTabCompleter(completer);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.print("Erro ao carregar o tabcompleter " + commandClass.getSimpleName());
				}
			}
		}
	}

	private void registerCommand(CommandExecutor executor, String name) {
		try {
			PluginCommand command = battleCraft.getCommand(name.toLowerCase());
			if (command == null) {
				Constructor<?> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
				constructor.setAccessible(true);
				command = (PluginCommand) constructor.newInstance(name, battleCraft);
			}
			command.setExecutor(executor);
			commandMap.register(name, command);
		} catch (Exception e) {
		}
	}

}
