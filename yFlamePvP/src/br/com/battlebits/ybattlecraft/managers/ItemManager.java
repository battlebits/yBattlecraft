package br.com.battlebits.ybattlecraft.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class ItemManager {

	public void repairItens(Player p) {
		if (p.getInventory().getHelmet() != null)
			p.getInventory().getHelmet().setDurability((byte) 0);
		if (p.getInventory().getChestplate() != null)
			p.getInventory().getChestplate().setDurability((byte) 0);
		if (p.getInventory().getLeggings() != null)
			p.getInventory().getLeggings().setDurability((byte) 0);
		if (p.getInventory().getBoots() != null)
			p.getInventory().getBoots().setDurability((byte) 0);
	}

	public void dropItems(Player p, Location l) {
		ArrayList<ItemStack> itens = new ArrayList<ItemStack>();
		for (ItemStack item : p.getPlayer().getInventory().getContents()) {
			if (item != null && item.getType() != Material.AIR) {
				itens.add(item.clone());
			}
		}
		for (ItemStack item : p.getPlayer().getInventory().getArmorContents()) {
			if (item != null && item.getType() != Material.AIR) {
				itens.add(item.clone());
			}
		}
		if (p.getPlayer().getItemOnCursor() != null && p.getPlayer().getItemOnCursor().getType() != Material.AIR) {
			itens.add(p.getPlayer().getItemOnCursor().clone());
		}
		dropItems(p, itens, l);
	}

	@SuppressWarnings({ "deprecation", "null" })
	public void dropItems(Player p, List<ItemStack> itens, Location l) {
		World world = l.getWorld();
		for (ItemStack item : itens) {
			if (item != null || item.getType() != Material.AIR) {
				if (item.hasItemMeta()) {
					world.dropItemNaturally(l, item.clone()).getItemStack().setItemMeta(item.getItemMeta());
				} else {
					world.dropItemNaturally(l, item);
				}
			}
		}
		p.getPlayer().getInventory().setArmorContents(new ItemStack[4]);
		p.getPlayer().getInventory().clear();
		p.getPlayer().setItemOnCursor(new ItemStack(0));
		for (PotionEffect pot : p.getActivePotionEffects()) {
			p.removePotionEffect(pot.getType());
		}
		itens.clear();
	}

}
