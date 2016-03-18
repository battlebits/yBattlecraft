package br.com.battlebits.ybattlecraft.loader;

import org.bukkit.Bukkit;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.listener.BaseListener;
import br.com.battlebits.ybattlecraft.utils.ClassGetter;

public class ListenerLoader {

	private yBattleCraft battleCraft;

	public ListenerLoader(yBattleCraft plugin) {
		this.battleCraft = plugin;
	}

	public void loadAndRegisterAllListeners() {
		int i = 0;
		for (Class<?> baseListener : ClassGetter.getClassesForPackage(battleCraft, "br.com.battlebits.ybattlecraft.listener")) {
			if (BaseListener.class.isAssignableFrom(baseListener)) {
				try {
					BaseListener listener;
					try {
						listener = (BaseListener) baseListener.getConstructor(yBattleCraft.class).newInstance(battleCraft);
					} catch (Exception e) {
						listener = (BaseListener) baseListener.newInstance();
					}
					Bukkit.getPluginManager().registerEvents(listener, battleCraft);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.print("Erro ao carregar o listener " + baseListener.getSimpleName());
				}
				i++;
			}
		}
		battleCraft.getLogger().info("[AbilityManager] " + i + " habilidades foram carregadas!");
	}
}