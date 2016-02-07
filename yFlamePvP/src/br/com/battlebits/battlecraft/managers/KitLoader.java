package br.com.battlebits.battlecraft.managers;

import java.util.HashMap;

import org.bukkit.event.Listener;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.interfaces.KitInterface;
import br.com.battlebits.battlecraft.utils.ClassGetter;

public class KitLoader {
	private HashMap<String, KitInterface> abilities = new HashMap<String, KitInterface>();
	private Main m;

	public KitLoader(Main m) {
		this.m = m;
		initializeAllAbilitiesInPackage("br.com.battlebits.battlecraft.kits");
	}

	public void initializeAllAbilitiesInPackage(String packageName) {
		int i = 0;
		for (Class<?> abilityClass : ClassGetter.getClassesForPackage(m, packageName)) {
			if (KitInterface.class.isAssignableFrom(abilityClass)) {
				try {
					KitInterface abilityListener;
					try {
						abilityListener = (KitInterface) abilityClass.getConstructor(Main.class).newInstance(m);
					} catch (Exception e) {
						abilityListener = (KitInterface) abilityClass.newInstance();
					}
					abilities.put(abilityClass.getSimpleName(), abilityListener);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.print("Erro ao carregar a habilidade " + abilityClass.getSimpleName());
				}
				i++;
			}
		}
		m.getLogger().info(i + " habilidades carregadas!");
	}

	public void registerAbilityListeners() {
		for (Listener abilityListener : abilities.values()) {
			m.getServer().getPluginManager().registerEvents(abilityListener, m);
		}
	}
}