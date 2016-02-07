package br.com.battlebits.battlecraft.interfaces;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.Kit;
import br.com.battlebits.battlecraft.managers.KitManager;

public abstract class KitInterface implements Listener {
	private String kitName;
	private KitManager kitManager;

	private Main m;

	public KitInterface(Main main) {
		this.m = main;
		kitManager = m.getKitManager();
		Kit kit = getKit();
		kitName = kit.getName();
		kitManager.addKit(kitName, kit);
	}

	public KitManager getKitManager() {
		return kitManager;
	}

	public String getKitName() {
		return kitName;
	}

	public Main getMain() {
		return m;
	}

	public abstract Kit getKit();

	public boolean hasAbility(Player player) {
		return kitManager.hasAbility(player, kitName);
	}

	public Server getServer() {
		return getMain().getServer();
	}

	public boolean hasAbility(Player player, String kitName) {
		return kitManager.hasAbility(player, kitName);
	}

	public static ItemStack createItem(Material mat, String name) {
		return createItem(mat, 1, (short) 0, name, "");
	}

	public static ItemStack createItem(Material mat, String name, String... description) {
		return createItem(mat, 1, (short) 0, name, description);
	}

	public static ItemStack createItem(Material mat, int quantidade, String name, String... description) {
		return createItem(mat, quantidade, (short) 0, name, description);
	}

	public static ItemStack createItem(Material mat, int quantidade, short data, String name, String... description) {
		ItemStack item = new ItemStack(mat, quantidade, data);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(name);
		if (description.length > 0 && !description[0].isEmpty())
			im.setLore(Arrays.asList(description));
		item.setItemMeta(im);
		return item;
	}
}
