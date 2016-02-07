package br.com.battlebits.battlecraft.managers;

import java.util.ArrayList;
import java.util.Collection;
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

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.Kit;
import br.com.battlebits.battlecraft.events.PlayerRemoveKitEvent;
import br.com.battlebits.battlecraft.events.PlayerSelectKitEvent;
import br.com.battlebits.battlecraft.nms.Title;
import br.com.battlebits.battlecraft.utils.API;
import br.com.battlebits.battlecraft.utils.Formatter;

public class KitManager {
	private HashMap<UUID, String> KITS = new HashMap<>();
	private HashMap<UUID, ArrayList<String>> playerKit = new HashMap<>();
	public HashMap<String, ArrayList<String>> freeKits = new HashMap<>();
	private HashMap<String, Kit> kits = new HashMap<>();
	private Main m;

	public KitManager(Main m) {
		this.m = m;
		ArrayList<String> kitList = new ArrayList<>();
		kitList.add("pvp");
		freeKits.put("normal", kitList);
		freeKits.put("vip", new ArrayList<>());
		freeKits.put("mvp", new ArrayList<>());
		freeKits.put("pro", new ArrayList<>());
	}

	public boolean hasAbility(Player p, String kitName) {
		return KITS.containsKey(p.getUniqueId()) && KITS.get(p.getUniqueId()).toLowerCase().equals(kitName.toLowerCase());
	}

	public void removeKit(Player p) {
		API.clearInventory(p);
		String kitName = KITS.remove(p.getUniqueId());
		if (kitName == null)
			return;
		PlayerRemoveKitEvent event = new PlayerRemoveKitEvent(p, kits.get(kitName));
		Bukkit.getPluginManager().callEvent(event);
	}

	public void giveKit(Player p, String kitName) {
		p.closeInventory();
		if (KITS.containsKey(p)) {
			p.sendMessage(ChatColor.RED + "Desculpe, mas voce ja esta usando 1 kit");
			p.sendMessage(ChatColor.RED + "Use: /spawn, para poder escolher outro kit");
			return;
		}
		/*
		 * if (WarpManager.isInWarp(p, "startgame")) {
		 * p.sendMessage(ChatColor.RED +
		 * "Voce nao pode usar esse kit na StartGame"); return; }
		 */
		Kit kit = getKitByName(kitName);
		if (kit == null) {
			p.sendMessage(ChatColor.RED + "Parece que o kit '" + kitName + "' não existe.");
			return;
		}
		if(!boughtKit(p, kitName)) {
			p.sendMessage(ChatColor.RED + "Você nao possui o kit '" + kitName + "'");
			return;
		}
		PlayerSelectKitEvent event = new PlayerSelectKitEvent(p, kit);
		m.getServer().getPluginManager().callEvent(event);
		if (event.isCanceled())
			return;
		API.clearInventory(p);
		p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1f, 1);
		PlayerInventory inv = p.getInventory();
		if (Main.IS_FULLIRON_MODE) {
			inv.setHelmet(new ItemStack(Material.IRON_HELMET));
			inv.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
			inv.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			inv.setBoots(new ItemStack(Material.IRON_BOOTS));
		}
		switch (kit.getType()) {
		case ESTRATEGIA:
			if (Main.IS_FULLIRON_MODE)
				inv.setItem(0, new ItemStack(Material.DIAMOND_SWORD));
			else
				inv.setItem(0, new ItemStack(Material.STONE_SWORD));
			break;
		case FORCA:
			if (Main.IS_FULLIRON_MODE)
				inv.setItem(0, new ItemStack(Material.IRON_SWORD));
			else
				inv.setItem(0, new ItemStack(Material.WOOD_SWORD));
			break;
		case MOBILIDADE:
			ItemStack item;
			if (Main.IS_FULLIRON_MODE)
				item = new ItemStack(Material.IRON_SWORD);
			else
				item = new ItemStack(Material.WOOD_SWORD);
			item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
			inv.setItem(0, item);
			break;
		default:
			ItemStack diamond;
			if (Main.IS_FULLIRON_MODE)
				diamond = new ItemStack(Material.DIAMOND_SWORD);
			else
				diamond = new ItemStack(Material.STONE_SWORD);
			diamond.addEnchantment(Enchantment.DAMAGE_ALL, 1);
			inv.setItem(0, diamond);
		}
		for (ItemStack item : kit.getItems()) {
			inv.addItem(item);
		}
		if (!Main.IS_FULLIRON_MODE) {
			inv.setItem(14, new ItemStack(Material.RED_MUSHROOM, 64));
			inv.setItem(15, new ItemStack(Material.BOWL, 64));
			inv.setItem(16, new ItemStack(Material.BROWN_MUSHROOM, 64));
		}
		for (ItemStack is : inv.getContents()) {
			if (is == null)
				inv.addItem(new ItemStack(Material.MUSHROOM_SOUP));
		}
		String tag = ChatColor.AQUA + "" + ChatColor.BOLD + "Kits" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.RESET;
		String name = ChatColor.GRAY + "Você selecionou o kit " + Formatter.getFormattedName(kitName);
		p.sendMessage(tag + name);
		KITS.put(p.getUniqueId(), kit.getName());

		Title title = new Title(ChatColor.AQUA + "Kit " + Formatter.getFormattedName(kit.getName()));
		title.setSubtitle("escolhido com sucesso!");
		title.setFadeInTime(20);
		title.setTimingsToTicks();
		title.setStayTime(20);
		title.setFadeOutTime(20);
		title.send(p);

	}

	public boolean boughtKit(Player p, String kit) {
		if (m.getPermissions().isUltimate(p))
			return true;
		if (playerKit.containsKey(p.getUniqueId())) {
			for (String i : playerKit.get(p.getUniqueId())) {
				if (i.toLowerCase().equals(kit.toLowerCase()))
					return true;
			}
		}
		for (String i : freeKits.get("normal")) {
			if (i.toLowerCase().equals(kit.toLowerCase()))
				return true;
		}
		if (m.getPermissions().isLight(p)) {
			for (String i : freeKits.get("vip")) {
				if (i.toLowerCase().equals(kit.toLowerCase()))
					return true;
			}
		}
		if (m.getPermissions().isPremium(p)) {
			for (String i : freeKits.get("mvp")) {
				if (i.toLowerCase().equals(kit.toLowerCase()))
					return true;
			}
		}
		return false;
	}

	public boolean hasKit(Player p) {
		return KITS.containsKey(p.getUniqueId());
	}

	public void addKit(String kit, Kit k) {
		kits.put(kit.toLowerCase(), k);
	}

	public boolean isKit(String kit) {
		return kits.keySet().contains(kit.toLowerCase());
	}

	public String getPlayerKit(Player p) {
		String kit = KITS.get(p.getUniqueId());
		String kitName = "";
		if (kit != null) {
			char[] stringArray = kit.toLowerCase().toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			kitName = kitName + new String(stringArray);
			return kitName;
		}
		return "Nenhum";
	}

	public void setForcedKit(Player p, String kitName) {
		KITS.put(p.getUniqueId(), kitName);
	}

	public Kit getKitByName(String kitName) {
		return kits.get(kitName);
	}

	public Collection<Kit> getKits() {
		return kits.values();
	}

}
