package br.com.battlebits.battlecraft.constructors;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import br.com.battlebits.battlecraft.enums.KitType;

public class Kit {
	private String name;
	private List<ItemStack> items;
	private String kitInfo;
	private ItemStack icon;
	private int price;
	private KitType type;

	public Kit(String kitname, String kitInfo, ItemStack icon, int price, KitType type) {
		this(kitname, kitInfo, new ArrayList<>(), icon, price, type);
	}

	public Kit(String kitname, String kitInfo, List<ItemStack> kititems, ItemStack icon, int price, KitType type) {
		this.name = kitname.toLowerCase();
		this.kitInfo = kitInfo;
		this.items = kititems;
		this.icon = icon;
		this.price = price;
		this.type = type;
	}

	public KitType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public List<ItemStack> getItems() {
		return items;
	}

	public String getKitInfo() {
		return kitInfo;
	}

	public ItemStack getIcon() {
		return icon;
	}

	public int getPrice() {
		return price;
	}

}
