package br.com.battlebits.ybattlecraft.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.event.PlayerRemoveKitEvent;
import br.com.battlebits.ybattlecraft.event.PlayerSelectKitEvent;
import br.com.battlebits.ybattlecraft.kit.Kit;
import br.com.battlebits.ybattlecraft.nms.Title;
import br.com.battlebits.ybattlecraft.utils.API;
import br.com.battlebits.ybattlecraft.utils.Formatter;

public class KitManager {

	private HashMap<UUID, ArrayList<String>> playerKits = new HashMap<>();
	public HashMap<String, ArrayList<String>> freeKits = new HashMap<>();
	private yBattleCraft battlecraft;
	private HashMap<UUID, String> playerKit;

	public KitManager(yBattleCraft plugin) {
		this.battlecraft = plugin;
		this.playerKit = new HashMap<>();
		ArrayList<String> kitList = new ArrayList<>();
		kitList.add("pvp");
		freeKits.put("normal", kitList);
		freeKits.put("vip", new ArrayList<>());
		freeKits.put("mvp", new ArrayList<>());
		freeKits.put("pro", new ArrayList<>());
	}

	public void removeKit(Player p) {
		p.closeInventory();
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.setItemOnCursor(null);
		String kitName = playerKit.remove(p.getUniqueId());
		if (kitName != null) {
			PlayerRemoveKitEvent event = new PlayerRemoveKitEvent(p.getUniqueId(), kitName,
					battlecraft.getWarpManager().getPlayerWarp(p.getUniqueId()));
			Bukkit.getPluginManager().callEvent(event);
		}
	}

	public boolean canUseKit(Player p, String kit) {
		if (battlecraft.getPermissions().isUltimate(p)) {
			return true;
		}
		if (playerKit.containsKey(p.getUniqueId())) {
			for (String i : playerKits.get(p.getUniqueId())) {
				if (i.toLowerCase().equals(kit.toLowerCase()))
					return true;
			}
		}
		for (String i : freeKits.get("normal")) {
			if (i.toLowerCase().equals(kit.toLowerCase()))
				return true;
		}
		if (battlecraft.getPermissions().isLight(p)) {
			for (String i : freeKits.get("vip")) {
				if (i.toLowerCase().equals(kit.toLowerCase()))
					return true;
			}
		}
		if (battlecraft.getPermissions().isPremium(p)) {
			for (String i : freeKits.get("mvp")) {
				if (i.toLowerCase().equals(kit.toLowerCase()))
					return true;
			}
		}
		return false;
	}

	public String getCurrentKit(UUID id) {
		if (hasCurrentKit(id)) {
			return playerKit.get(id);
		} else {
			return "Nenhum";
		}
	}

	public boolean hasCurrentKit(UUID id) {
		return playerKit.containsKey(id);
	}

	public void giveKit(Player p, Kit kit, boolean sendMessage) {
		p.closeInventory();
		// if (KITS.containsKey(p)) {
		// if (sendMessage) {
		// p.sendMessage(ChatColor.RED + "Desculpe, mas voce ja esta usando 1
		// kit");
		// p.sendMessage(ChatColor.RED + "Use: /spawn, para poder escolher outro
		// kit");
		// }
		// return;
		// }
		/*
		 * if (WarpManager.isInWarp(p, "startgame")) {
		 * p.sendMessage(ChatColor.RED +
		 * "Voce nao pode usar esse kit na StartGame"); return; }
		 */
		PlayerSelectKitEvent event = new PlayerSelectKitEvent(p, kit.getName(), battlecraft.getWarpManager().getPlayerWarp(p.getUniqueId()));
		battlecraft.getServer().getPluginManager().callEvent(event);
		if (event.isCanceled())
			return;
		API.clearInventory(p);
		p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1f, 1);
		PlayerInventory inv = p.getInventory();
		if (yBattleCraft.IS_FULLIRON_MODE) {
			inv.setHelmet(new ItemStack(Material.IRON_HELMET));
			inv.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
			inv.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			inv.setBoots(new ItemStack(Material.IRON_BOOTS));
		}
		switch (kit.getType()) {
		case ESTRATEGIA:
			if (yBattleCraft.IS_FULLIRON_MODE)
				inv.setItem(0, new ItemStack(Material.DIAMOND_SWORD));
			else
				inv.setItem(0, new ItemStack(Material.STONE_SWORD));
			break;
		case FORCA:
			if (yBattleCraft.IS_FULLIRON_MODE)
				inv.setItem(0, new ItemStack(Material.IRON_SWORD));
			else
				inv.setItem(0, new ItemStack(Material.WOOD_SWORD));
			break;
		case MOBILIDADE:
			ItemStack item;
			if (yBattleCraft.IS_FULLIRON_MODE)
				item = new ItemStack(Material.IRON_SWORD);
			else
				item = new ItemStack(Material.WOOD_SWORD);
			item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
			inv.setItem(0, item);
			break;
		default:
			ItemStack diamond;
			if (yBattleCraft.IS_FULLIRON_MODE)
				diamond = new ItemStack(Material.DIAMOND_SWORD);
			else
				diamond = new ItemStack(Material.STONE_SWORD);
			diamond.addEnchantment(Enchantment.DAMAGE_ALL, 1);
			inv.setItem(0, diamond);
		}
		for (ItemStack item : kit.getItens()) {
			inv.addItem(item);
		}
		if (!yBattleCraft.IS_FULLIRON_MODE) {
			inv.setItem(14, new ItemStack(Material.RED_MUSHROOM, 64));
			inv.setItem(15, new ItemStack(Material.BOWL, 64));
			inv.setItem(16, new ItemStack(Material.BROWN_MUSHROOM, 64));
		}
		for (ItemStack is : inv.getContents()) {
			if (is == null)
				inv.addItem(new ItemStack(Material.MUSHROOM_SOUP));
		}
		if (sendMessage) {
			String tag = ChatColor.AQUA + "" + ChatColor.BOLD + "Kits" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.RESET;
			String name = ChatColor.GRAY + "Voc� selecionou o kit " + kit.getName();
			p.sendMessage(tag + name);
		}
		playerKit.put(p.getUniqueId(), kit.getName());

		if (sendMessage) {
			Title title = new Title(ChatColor.AQUA + "Kit " + Formatter.getFormattedName(kit.getName()));
			title.setSubtitle("escolhido com sucesso!");
			title.setFadeInTime(20);
			title.setTimingsToTicks();
			title.setStayTime(20);
			title.setFadeOutTime(20);
			title.send(p);
		}
	}

}