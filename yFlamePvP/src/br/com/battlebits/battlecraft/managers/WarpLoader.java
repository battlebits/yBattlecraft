package br.com.battlebits.battlecraft.managers;

import java.util.HashMap;

import org.bukkit.event.Listener;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.BaseWarp;
import br.com.battlebits.battlecraft.utils.ClassGetter;

public class WarpLoader {
	private HashMap<String, BaseWarp> abilities = new HashMap<String, BaseWarp>();
	private Main m;

	public WarpLoader(Main m) {
		this.m = m;
		initializeAllAbilitiesInPackage("br.com.battlebits.battlecraft.warps");
	}

	public void initializeAllAbilitiesInPackage(String packageName) {
		int i = 0;
		for (Class<?> abilityClass : ClassGetter.getClassesForPackage(m, packageName)) {
			if (BaseWarp.class.isAssignableFrom(abilityClass)) {
				try {
					BaseWarp abilityListener;
					try {
						abilityListener = (BaseWarp) abilityClass.getConstructor(Main.class).newInstance(m);
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