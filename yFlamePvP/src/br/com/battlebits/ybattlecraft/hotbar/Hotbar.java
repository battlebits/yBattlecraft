package br.com.battlebits.ybattlecraft.hotbar;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Hotbar {
	public static void setItems(Player p) {
		PlayerInventory i = p.getInventory();
		// KITS
		ItemStack kits = new ItemStack(Material.ENDER_CHEST);
		ItemMeta nomekits = kits.getItemMeta();
		nomekits.setDisplayName(ChatColor.AQUA.toString() + ChatColor.BOLD + "Kits" + ChatColor.GRAY + " (Clique para Abrir)");
		nomekits.setLore(Arrays.asList(ChatColor.GRAY + "Clique nesse item", ChatColor.GRAY + "para escolher um kit"));
		kits.setItemMeta(nomekits);
		// WARPS
		ItemStack warps = new ItemStack(Material.COMPASS);
		ItemMeta nomewarps = warps.getItemMeta();
		nomewarps.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Warps" + ChatColor.GRAY + " (Clique para Abrir)");
		nomewarps.setLore(Arrays.asList(ChatColor.GRAY + "Clique aqui para ser", ChatColor.GRAY + "teleportado para uma warp"));
		warps.setItemMeta(nomewarps);
		// STATUS
		ItemStack skull = new ItemStack(Material.SKULL_ITEM);
		skull.setDurability((short) 3);
		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		skullMeta.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + "Status - " + p.getName() + ChatColor.GRAY + " (Clique para Ver)");
		skullMeta.setOwner(p.getName());
		skull.setItemMeta(skullMeta);
		// SHOP
		ItemStack vips = new ItemStack(Material.DIAMOND);
		ItemMeta nomevips = vips.getItemMeta();
		nomevips.setDisplayName(ChatColor.AQUA.toString() + ChatColor.BOLD + "Shop");
		ArrayList<String> descvips = new ArrayList<String>();
		descvips.add(ChatColor.GRAY + "Clique aqui para adiquirir");
		descvips.add(ChatColor.GRAY + "algum tipo de vip no servidor");
		nomevips.setLore(descvips);
		vips.setItemMeta(nomevips);
		// EVENTO
		ItemStack shop = new ItemStack(Material.ENCHANTED_BOOK);
		ItemMeta nomeshop = shop.getItemMeta();
		nomeshop.setDisplayName(ChatColor.AQUA.toString() + ChatColor.BOLD + "Eventos");
		ArrayList<String> descshop = new ArrayList<String>();
		descshop.add(ChatColor.GRAY + "Clique aqui para comprar items");
		descshop.add(ChatColor.GRAY + "com o dinheiro do proprio server");
		nomeshop.setLore(descshop);
		shop.setItemMeta(nomeshop);
		i.clear();
		p.getInventory().setHeldItemSlot(0);
		i.setArmorContents(null);
		i.setItem(1, kits);
		i.setItem(2, warps);
		i.setItem(4, skull);
		i.setItem(6, vips);
		i.setItem(7, shop);
	}

	public static void setStart(Player p) {
		PlayerInventory i = p.getInventory();
		ItemStack kits = new ItemStack(Material.PAPER);
		ItemMeta nomekits = kits.getItemMeta();
		nomekits.setDisplayName(ChatColor.AQUA.toString() + ChatColor.BOLD + "Kits - StartGame");
		nomekits.setLore(Arrays.asList(ChatColor.GRAY + "Clique nesse item", ChatColor.GRAY + "para escolher um kit"));
		kits.setItemMeta(nomekits);
		i.clear();
		i.setArmorContents(null);
		p.getInventory().setHeldItemSlot(0);
		i.setItem(0, kits);
	}

	public static void set1v1(Player p) {
		PlayerInventory i = p.getInventory();
		ItemStack bastao = new ItemStack(Material.BLAZE_ROD);
		ItemMeta nomekits = bastao.getItemMeta();
		nomekits.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + "1v1 Normal");
		nomekits.setLore(Arrays.asList(ChatColor.GRAY + "Clique em alguem", ChatColor.GRAY + "para desafiar em 1v1 normal"));
		bastao.setItemMeta(nomekits);
		ItemStack grade = new ItemStack(Material.IRON_FENCE);
		ItemMeta nomevips = grade.getItemMeta();
		nomevips.setDisplayName(ChatColor.AQUA.toString() + ChatColor.BOLD + "1v1 Customizado");
		nomevips.setLore(Arrays.asList(ChatColor.GRAY + "Clique em alguem", ChatColor.GRAY + "para desafiar em 1v1 customizado"));
		grade.setItemMeta(nomevips);
		ItemStack fast = new ItemStack(Material.INK_SACK, 1, (byte) 8);
		ItemMeta nomefast = fast.getItemMeta();
		nomefast.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "1v1 Rapido");
		nomefast.setLore(Arrays.asList(ChatColor.GRAY + "Clique para ir 1v1", ChatColor.GRAY + "rapidamente"));
		fast.setItemMeta(nomefast);
		i.clear();
		p.getInventory().setHeldItemSlot(0);
		i.setItem(3, bastao);
		i.setItem(4, grade);
		i.setItem(5, fast);
	}
}
