package br.com.battlebits.ybattlecraft.loader;

import java.util.HashMap;

import org.bukkit.event.Listener;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseWarp;
import br.com.battlebits.ybattlecraft.utils.ClassGetter;

public class WarpLoader {

	private HashMap<String, BaseWarp> warps = new HashMap<String, BaseWarp>();
	private yBattleCraft battleCraft;

	public WarpLoader(yBattleCraft bc) {
		this.battleCraft = bc;
	}

	public void initializeAllWarps() {
		int i = 0;
		for (Class<?> warpClass : ClassGetter.getClassesForPackage(battleCraft, "br.com.battlebits.ybattlecraft.warps")) {
			if (BaseWarp.class.isAssignableFrom(warpClass)) {
				try {
					BaseWarp warp;
					try {
						warp = (BaseWarp) warpClass.getConstructor(yBattleCraft.class).newInstance(battleCraft);
					} catch (Exception e) {
						warp = (BaseWarp) warpClass.newInstance();
					}
					warps.put(warpClass.getSimpleName(), warp);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.print("Erro ao carregar a warp " + warpClass.getSimpleName());
				}
				i++;
			}
		}
		battleCraft.getLogger().info(i + " warps carregadas!");
	}

	public void registerWarpsListeners() {
		for (Listener warpListener : warps.values()) {
			battleCraft.getServer().getPluginManager().registerEvents(warpListener, battleCraft);
		}
		warps.clear();
	}
}