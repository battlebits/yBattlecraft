package br.com.battlebits.ybattlecraft.managers;

import java.util.HashMap;

import org.bukkit.event.Listener;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.constructors.BaseWarp;
import br.com.battlebits.ybattlecraft.utils.ClassGetter;

public class WarpLoader {
	private HashMap<String, BaseWarp> abilities = new HashMap<String, BaseWarp>();
	private yBattleCraft m;

	public WarpLoader(yBattleCraft m) {
		this.m = m;
		initializeAllAbilitiesInPackage("br.com.battlebits.ybattlecraft.warps");
	}

	public void initializeAllAbilitiesInPackage(String packageName) {
		int i = 0;
		for (Class<?> abilityClass : ClassGetter.getClassesForPackage(m, packageName)) {
			if (BaseWarp.class.isAssignableFrom(abilityClass)) {
				try {
					BaseWarp abilityListener;
					try {
						abilityListener = (BaseWarp) abilityClass.getConstructor(yBattleCraft.class).newInstance(m);
					} catch (Exception e) {
						abilityListener = (BaseWarp) abilityClass.newInstance();
					}
					abilities.put(abilityClass.getSimpleName(), abilityListener);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.print("Erro ao carregar a warp " + abilityClass.getSimpleName());
				}
				i++;
			}
		}
		m.getLogger().info(i + " warps carregadas!");
	}

	public void registerAbilityListeners() {
		for (Listener abilityListener : abilities.values()) {
			m.getServer().getPluginManager().registerEvents(abilityListener, m);
		}
	}
}