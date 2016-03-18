package br.com.battlebits.ybattlecraft.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.ybattlecraft.yBattleCraft;

public class InventoryListener implements Listener {

	private yBattleCraft m;

	public InventoryListener(yBattleCraft m) {
		this.m = m;
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player))
			return;
		ItemStack item = event.getCurrentItem();
		if (item == null)
			return;
		if (!item.hasItemMeta())
			return;
		if (!item.getItemMeta().hasDisplayName())
			return;
		Player p = (Player) event.getWhoClicked();
		if (p.getOpenInventory().getType() != InventoryType.CHEST)
			return;
		event.setCancelled(true);
		String inv = p.getOpenInventory().getTitle();
		String itemName = item.getItemMeta().getDisplayName();
		if (inv.contains("Warps")) {
			m.getWarpManager().teleportWarp(p, ChatColor.stripColor(itemName.toLowerCase().trim()), true);
		}
	}
}
