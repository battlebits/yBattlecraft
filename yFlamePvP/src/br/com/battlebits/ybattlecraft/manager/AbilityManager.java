package br.com.battlebits.ybattlecraft.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import br.com.battlebits.ybattlecraft.ability.BaseAbility;

public class AbilityManager {

	private HashMap<String, BaseAbility> nameAbility;
	private HashMap<UUID, ArrayList<String>> playerAbilities;

	public AbilityManager() {
		playerAbilities = new HashMap<>();
		nameAbility = new HashMap<>();
	}

	public void stop() {
		playerAbilities.clear();
		nameAbility.clear();
		playerAbilities = null;
		nameAbility = null;
	}

	public void resetPlayerAbilities(UUID id) {
		if (playerAbilities.containsKey(id)) {
			for (String s : playerAbilities.get(id)) {
				getAbility(s.toLowerCase()).removeCooldown(id);
			}
			playerAbilities.remove(id);
		}
	}

	public boolean playerHasAbility(UUID id, String name) {
		return playerAbilities.containsKey(id) && playerAbilities.get(id).contains(name.toLowerCase());
	}

	public void addPlayerAbility(UUID id, String name) {
		if (!playerAbilities.containsKey(id)) {
			playerAbilities.put(id, new ArrayList<>());
		}
		playerAbilities.get(id).add(name.toLowerCase());
	}

	public void addAbility(BaseAbility ability) {
		nameAbility.put(ability.getAbilityName().toLowerCase(), ability);
	}

	public BaseAbility getAbility(String name) {
		return nameAbility.get(name.toLowerCase());
	}

}
