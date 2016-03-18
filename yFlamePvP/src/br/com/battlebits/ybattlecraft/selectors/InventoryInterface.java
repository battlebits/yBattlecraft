package br.com.battlebits.ybattlecraft.selectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class InventoryInterface {
	private String inventoryName;
	private int slots;
	private Inventory inventory;

	public InventoryInterface(String name, int slots) {
		this.inventoryName = name;
		this.slots = slots;
		this.inventory = Bukkit.createInventory(null, this.slots, this.inventoryName);
	}

	public Inventory getInventory() {
		return inventory;
	}

	public int getSlots() {
		return slots;
	}

	public String getInventoryName() {
		return inventoryName;
	}

	public abstract void generateContents();

	public void open(Player holder) {
		holder.openInventory(inventory);
	}

}
