package br.com.battlebits.battlecraft.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class API {
	@SuppressWarnings("deprecation")
	public static void clearInventory(Player p) {
		p.getPlayer().getInventory().setArmorContents(new ItemStack[4]);
		p.getPlayer().getInventory().clear();
		p.getPlayer().setItemOnCursor(new ItemStack(0));
	}
}
