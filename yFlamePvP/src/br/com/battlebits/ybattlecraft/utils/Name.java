package br.com.battlebits.ybattlecraft.utils;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Name {
	private HashMap<String, String> NAMES = new HashMap<String, String>();

	public Name() {
		NAMES.put("WOOD_SWORD", ChatColor.GOLD + "Espada de Madeira");
		NAMES.put("STONE_SWORD", ChatColor.GRAY + "Espada de Pedra");
		NAMES.put("IRON_SWORD", ChatColor.WHITE + "Espada de Ferro");
		NAMES.put("DIAMOND_SWORD", ChatColor.AQUA + "Espada de Diamante");
		NAMES.put("GLASS", ChatColor.RED + "Sem Armadura");
		NAMES.put("LEATHER_CHESTPLATE", ChatColor.GOLD + "Armadura de Couro");
		NAMES.put("CHAINMAIL_CHESTPLATE", ChatColor.GRAY + "Armadura de Cota de Malha");
		NAMES.put("IRON_CHESTPLATE", ChatColor.WHITE + "Armadura de Ferro");
		NAMES.put("DIAMOND_CHESTPLATE", ChatColor.AQUA + "Armadura de Diamante");
	}

	public String getEnchantName(Enchantment enchant) {
		return getName(enchant.getName());
	}

	@SuppressWarnings("deprecation")
	public String getItemName(ItemStack item) {
		if (item == null)
			item = new ItemStack(0);
		if (NAMES.containsKey(item.getType().name()))
			return NAMES.get(item.getType().name());
		return getName(item.getType().name());
	}

	public String getName(String string) {
		if (NAMES.containsValue(string))
			return NAMES.get(string);
		return toReadable(string);
	}

	public String toReadable(String string) {
		String[] names = string.split("_");
		for (int i = 0; i < names.length; i++) {
			names[i] = names[i].substring(0, 1) + names[i].substring(1).toLowerCase();
		}
		return StringUtils.join(names, " ");
	}

}
