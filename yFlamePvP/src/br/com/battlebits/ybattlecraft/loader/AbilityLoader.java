package br.com.battlebits.ybattlecraft.loader;

import java.util.HashMap;
import java.util.Map.Entry;

import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.base.BaseAbility;
import br.com.battlebits.ybattlecraft.utils.ClassGetter;

public class AbilityLoader {

	private HashMap<String, BaseAbility> abilities = new HashMap<String, BaseAbility>();
	private Battlecraft battleCraft;

	public AbilityLoader(Battlecraft plugin) {
		this.battleCraft = plugin;
	}

	public void loadAllAbilities() {
		int i = 0;
		for (Class<?> abilityClass : ClassGetter.getClassesForPackage(battleCraft, "br.com.battlebits.ybattlecraft.ability")) {
			if (BaseAbility.class.isAssignableFrom(abilityClass)) {
				try {
					BaseAbility abilityListener;
					try {
						abilityListener = (BaseAbility) abilityClass.getConstructor(Battlecraft.class).newInstance(battleCraft);
					} catch (Exception e) {
						abilityListener = (BaseAbility) abilityClass.newInstance();
					}
					abilities.put(abilityClass.getSimpleName(), abilityListener);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.print("Erro ao carregar a habilidade " + abilityClass.getSimpleName());
				}
				i++;
			}
		}
		battleCraft.getLogger().info("[AbilityManager] " + i + " habilidades foram carregadas!");
	}

	public void registerAllAbilities() {
		for (Entry<String, BaseAbility> entry : abilities.entrySet()) {
			battleCraft.getAbilityManager().addAbility(entry.getValue());
			battleCraft.getServer().getPluginManager().registerEvents(entry.getValue(), battleCraft);
		}
		abilities.clear();
		abilities = null;
	}
}