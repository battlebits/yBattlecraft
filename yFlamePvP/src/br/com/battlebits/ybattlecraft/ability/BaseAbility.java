package br.com.battlebits.ybattlecraft.ability;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.ybattlecraft.yBattleCraft;

public abstract class BaseAbility implements Listener {

	public yBattleCraft battlecraft;
	private ArrayList<ItemStack> itens;
	private String name;

	public BaseAbility(yBattleCraft yBattleCraft) {
		this.battlecraft = yBattleCraft;
		this.itens = new ArrayList<>();
		if (this.getClass().getSimpleName().toLowerCase().endsWith("ability")) {
			this.name = this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - 7);
		} else {
			this.name = this.getClass().getSimpleName();
		}
	}

	public boolean isUsing(Player p) {
		return battlecraft.getAbilityManager().playerHasAbility(p.getUniqueId(), name);
	}

	public void removeCooldown(UUID id) {
		battlecraft.getCooldownManager().removeCooldown(id, getAbilityName() + "ability");
	}

	public String getAbilityName() {
		return name;
	}

	public ArrayList<ItemStack> getItens() {
		return itens;
	}

}
