package br.com.battlebits.ybattlecraft.selectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.enums.KitCategory;
import br.com.battlebits.ybattlecraft.kit.Kit;
import br.com.battlebits.ybattlecraft.manager.KitManager;
import br.com.battlebits.ybattlecraft.utils.Formatter;

public class KitSelector {

	private Player player;
	private KitManager manager;
	private Inventory inventory;
	private KitCategory category;
	private Listener listener;
	private int pagina;
	private int paginaNumbers;
	private yBattleCraft m;

	private Map<KitCategory, List<Kit>> kits;

	public KitSelector(Player player, yBattleCraft m) {
		this.player = player;
		this.manager = m.getKitManager();
		this.m = m;
		this.category = KitCategory.OWNED;
		inventory = createInventory();
		pagina = 1;
		loadCategories();
		updatePage();
	}

	public void open() {
		player.openInventory(inventory);
		createListener();
	}

	public void setCategory(KitCategory category) {
		this.category = category;
		pagina = 1;
		setGlass(inventory, category.getId());
		updatePage();
	}

	public void nextPage() {
		if (paginaNumbers >= pagina + 1) {
			pagina += 1;
			updatePage();
		}
	}

	public void previusPage() {
		if (pagina - 1 > 0) {
			pagina -= 1;
			updatePage();
		}
	}

	private void updatePage() {
		for (int i = 18; i < 54; i++) {
			inventory.setItem(i, null);
		}
		paginaNumbers = (kits.get(category).size() / 36) + 1;
		setPages(inventory);
		int i = 18;
		int page = 1;
		if (category == KitCategory.FREE) {
			for (String kitName : manager.freeKits.get("normal")) {
				Kit kit = m.getWarpManager().getWarpByName(m.getWarpManager().getPlayerWarp(player.getUniqueId())).getKit(kitName);
				if (kit != null) {
					if (kit.getIcon() == null)
						continue;
					ItemStack item = new ItemStack(kit.getIcon());
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + Formatter.getFormattedName(kit.getName()));
					String description = ChatColor.GRAY + "Kit Grátis. ";
					meta.setLore(wrap(description + kit.getInfo()));
					item.setItemMeta(meta);
					inventory.setItem(i, item);
					i++;
				}
			}
			while (i % 9 != 0) {
				++i;
			}
			for (String kitName : manager.freeKits.get("vip")) {
				Kit kit = m.getWarpManager().getWarpByName(m.getWarpManager().getPlayerWarp(player.getUniqueId())).getKit(kitName);
				if (kit != null) {
					if (kit.getIcon() == null)
						continue;
					ItemStack item = new ItemStack(kit.getIcon());
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + Formatter.getFormattedName(kit.getName()));
					String description = ChatColor.GREEN + "Rotação Vip Light. ";
					meta.setLore(wrap(description + kit.getInfo()));
					item.setItemMeta(meta);
					inventory.setItem(i, item);
					i++;
				}
			}
			while (i % 9 != 0) {
				++i;
			}
			for (String kitName : manager.freeKits.get("mvp")) {
				Kit kit = m.getWarpManager().getWarpByName(m.getWarpManager().getPlayerWarp(player.getUniqueId())).getKit(kitName);
				if (kit != null) {
					if (kit.getIcon() == null)
						continue;
					ItemStack item = new ItemStack(kit.getIcon());
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + Formatter.getFormattedName(kit.getName()));
					String description = ChatColor.GOLD + "Rotação Vip Premium. ";
					meta.setLore(wrap(description + kit.getInfo()));
					item.setItemMeta(meta);
					inventory.setItem(i, item);
					i++;
				}
			}
			return;
		}
		if (category == KitCategory.FAVORITE) {
			for (Kit kit : kits.get(category)) {
				if (page < pagina) {
					if (i == 53) {
						i = 17;
						page += 1;
					}
					i++;
					continue;
				}
				if (i > 53)
					break;
				if (kit.getIcon() == null)
					continue;
				ItemStack item = new ItemStack(kit.getIcon());
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + Formatter.getFormattedName(kit.getName()));
				String description = "";
				if (manager.canUseKit(player, kit.getName())) {
					description = ChatColor.GREEN + "Voce possui este kit. ";
				} else {
					description = ChatColor.RED + "Voce nao possui este kit. ";
				}
				description = description + ChatColor.RED + "Clique com o direito " + ChatColor.RED + "para remover este kit " + ChatColor.RED + "dos favoritos. ";
				meta.setLore(wrap(description + kit.getInfo()));
				item.setItemMeta(meta);
				inventory.setItem(i, item);
				i++;
			}
			return;
		}
		if (category == KitCategory.ALL) {
			for (Kit kit : kits.get(category)) {
				if (page < pagina) {
					if (i == 53) {
						i = 17;
						page += 1;
					}
					i++;
					continue;
				}
				if (i > 53)
					break;
				if (kit.getIcon() == null)
					continue;
				ItemStack item = new ItemStack(kit.getIcon());
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + Formatter.getFormattedName(kit.getName()));
				String description = "";
				if (manager.canUseKit(player, kit.getName())) {
					description = ChatColor.GREEN + "Voce possui este kit. ";
				} else {
					description = ChatColor.RED + "Voce nao possui este kit. ";
				}
				description = description + ChatColor.DARK_PURPLE + "Clique com o direito " + ChatColor.DARK_PURPLE + "para adicionar este " + ChatColor.DARK_PURPLE + "kit aos favoritos. ";
				meta.setLore(wrap(description + kit.getInfo()));
				item.setItemMeta(meta);
				inventory.setItem(i, item);
				i++;
			}
			return;
		}
		for (Kit kit : kits.get(category)) {
			if (page < pagina) {
				if (i == 53) {
					i = 17;
					page += 1;
				}
				i++;
				continue;
			}
			if (i > 53)
				break;
			if (kit.getIcon() == null)
				continue;
			ItemStack item = new ItemStack(kit.getIcon());
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + Formatter.getFormattedName(kit.getName()));
			String description = "";
			if (manager.canUseKit(player, kit.getName())) {
				description = ChatColor.GREEN + "Voce possui este kit. ";
			} else {
				description = ChatColor.RED + "Voce nao possui este kit. ";
			}
			meta.setLore(wrap(description + kit.getInfo()));
			item.setItemMeta(meta);
			inventory.setItem(i, item);
			i++;
		}
	}

	private List<String> wrap(String string) {
		String[] split = string.split(" ");
		string = "";
		ChatColor color = ChatColor.GRAY;
		ArrayList<String> newString = new ArrayList<String>();
		for (int i = 0; i < split.length; i++) {
			if (string.length() > 20 || string.endsWith(".") || string.endsWith("!")) {
				newString.add(color + string);
				if (string.endsWith(".") || string.endsWith("!"))
					newString.add("");
				string = "";
			}
			string += (string.length() == 0 ? "" : " ") + split[i];
		}
		newString.add(color + string);
		return newString;
	}

	private void loadCategories() {
		kits = new HashMap<>();
		List<Kit> list = kits.get(KitCategory.OWNED);
		if (list == null) {
			list = new ArrayList<Kit>();
		}
		for (Kit kit : m.getWarpManager().getWarpByName(m.getWarpManager().getPlayerWarp(player.getUniqueId())).getKits()) {
			if (manager.canUseKit(player, kit.getName())) {
				list.add(kit);
			}
		}
		Collections.sort(list, new Comparator<Kit>() {
			public int compare(Kit kit1, Kit kit2) {
				return kit1.getName().compareTo(kit2.getName());
			}
		});
		kits.put(KitCategory.OWNED, list);
		kits.put(KitCategory.FREE, new ArrayList<>());
		list = kits.get(KitCategory.ALL);
		if (list == null) {
			list = new ArrayList<Kit>();
		}
		for (Kit kit : m.getWarpManager().getWarpByName(m.getWarpManager().getPlayerWarp(player.getUniqueId())).getKits()) {
			list.add(kit);
		}
		Collections.sort(list, new Comparator<Kit>() {
			public int compare(Kit kit1, Kit kit2) {
				return kit1.getName().compareTo(kit2.getName());
			}
		});
		kits.put(KitCategory.ALL, list);
		list = kits.get(KitCategory.FAVORITE);
		if (list == null) {
			list = new ArrayList<Kit>();
		}
		List<String> kitsFavoritos = m.getStatusManager().getStatusByUuid(player.getUniqueId()).getKitsFavoritos();
		if (kitsFavoritos != null && !kitsFavoritos.isEmpty()) {
			Collections.sort(kitsFavoritos);
			for (String name : kitsFavoritos) {
				Kit k = m.getWarpManager().getWarpByName(m.getWarpManager().getPlayerWarp(player.getUniqueId())).getKit(name.toLowerCase());
				if (k != null) {
					list.add(k);
				}
			}
		}
		kits.put(KitCategory.FAVORITE, list);
	}

	private Inventory createInventory() {
		Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.AQUA + "Kits");
		setGlass(inventory, category.getId());
		setPages(inventory);
		ItemStack kitsgratis = new ItemStack(Material.WOOL, 1, (short) 5);
		ItemMeta kitsgratismeta = kitsgratis.getItemMeta();
		kitsgratismeta.setDisplayName(ChatColor.GREEN + "Kits Gratuitos do Mes");
		kitsgratis.setItemMeta(kitsgratismeta);
		inventory.setItem(3, kitsgratis);
		ItemStack seuskits = new ItemStack(Material.WOOL, 1, (short) 4);
		ItemMeta seuskitsmeta = seuskits.getItemMeta();
		seuskitsmeta.setDisplayName(ChatColor.YELLOW + "Seus Kits");
		seuskits.setItemMeta(seuskitsmeta);
		inventory.setItem(2, seuskits);
		ItemStack lojakits = new ItemStack(Material.DIAMOND);
		ItemMeta lojakitsmeta = lojakits.getItemMeta();
		lojakitsmeta.setDisplayName(ChatColor.AQUA + "Loja de Kits");
		lojakitsmeta.setLore(Arrays.asList("Em breve!"));
		lojakits.setItemMeta(lojakitsmeta);
		inventory.setItem(4, lojakits);
		ItemStack favoritos = new ItemStack(Material.WOOL, 1, (short) 14);
		ItemMeta favoritosmeta = favoritos.getItemMeta();
		favoritosmeta.setDisplayName(ChatColor.GOLD + "Kits Favoritos");
		favoritos.setItemMeta(favoritosmeta);
		inventory.setItem(5, favoritos);
		ItemStack originais = new ItemStack(Material.WOOL, 1, (short) 11);
		ItemMeta originaismeta = originais.getItemMeta();
		originaismeta.setDisplayName(ChatColor.RED + "Todos os kits");
		originais.setItemMeta(originaismeta);
		inventory.setItem(6, originais);
		return inventory;
	}

	private void setGlass(Inventory inv, short s) {
		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, s);
		ItemMeta glassmeta = glass.getItemMeta();
		glassmeta.setDisplayName(" ");
		glass.setItemMeta(glassmeta);
		for (int i = 9; i < 18; i++) {
			inv.setItem(i, glass);
		}
	}

	private void setPages(Inventory inv) {
		if (pagina <= 1)
			inv.setItem(0, getGray(ChatColor.RED + "Nao possui pagina anterior"));
		else
			inv.setItem(0, getGreen(ChatColor.GREEN + "Pagina Anterior"));
		if (pagina == paginaNumbers)
			inv.setItem(8, getGray(ChatColor.RED + "Nao possui proxima pagina"));
		else
			inv.setItem(8, getGreen(ChatColor.GREEN + "Proxima Pagina"));
	}

	private ItemStack getGreen(String name) {
		ItemStack item = new ItemStack(Material.INK_SACK, 1, (short) 10);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(name);
		item.setItemMeta(itemmeta);
		return item;
	}

	private ItemStack getGray(String name) {
		ItemStack item = new ItemStack(Material.INK_SACK, 1, (short) 8);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(name);
		item.setItemMeta(itemmeta);
		return item;
	}

	private void createListener() {
		listener = new Listener() {

			@EventHandler
			public void onClose(InventoryCloseEvent event) {
				if (event.getPlayer() != player)
					return;
				destroy();
			}

			@EventHandler
			public void onInteract(InventoryClickEvent event) {
				if (event.getWhoClicked() != player)
					return;
				if (!event.getInventory().getName().equalsIgnoreCase(ChatColor.AQUA + "Kits"))
					return;
				event.setCancelled(true);
				player.updateInventory();
				player.setItemOnCursor(null);
				ItemStack item = event.getCurrentItem();
				if (item == null)
					return;
				if (event.getClick().isRightClick()) {
					if (!item.hasItemMeta())
						return;
					if (!item.getItemMeta().hasDisplayName())
						return;
					if (category == KitCategory.ALL) {
						if (event.getRawSlot() >= 18) {
							String kitName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
							if (m.getStatusManager().getStatusByUuid(player.getUniqueId()).getKitsFavoritos().contains(kitName.toLowerCase())) {
								player.sendMessage(ChatColor.RED + "Você ja possui o kit " + kitName + " como seus favoritos");
								return;
							}
							m.getStatusManager().getStatusByUuid(player.getUniqueId()).addFavoriteKit(kitName.toLowerCase());
							player.sendMessage(ChatColor.YELLOW + "Você adicionou o kit " + kitName + " aos seus kits favoritos");
							List<Kit> list = kits.get(KitCategory.FAVORITE);
							list.add(m.getWarpManager().getWarpByName(m.getWarpManager().getPlayerWarp(player.getUniqueId())).getKit(kitName.toLowerCase()));
						}
						return;
					}
					if (category == KitCategory.FAVORITE) {
						if (event.getRawSlot() >= 18) {
							String kitName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
							m.getStatusManager().getStatusByUuid(player.getUniqueId()).removeFavoriteKit(kitName.toLowerCase());
							player.sendMessage(ChatColor.RED + "Você removeu o kit " + kitName + " dos seus kits favoritos");
							List<Kit> list = kits.get(KitCategory.FAVORITE);
							list.remove(m.getWarpManager().getWarpByName(m.getWarpManager().getPlayerWarp(player.getUniqueId())).getKit(kitName.toLowerCase()));
							updatePage();
						}
					}
					return;
				}
				if (!item.hasItemMeta())
					return;
				if (!item.getItemMeta().hasDisplayName())
					return;
				if (item.getItemMeta().getDisplayName().contains("Proxima")) {
					nextPage();
				}
				if (item.getItemMeta().getDisplayName().contains("Anterior")) {
					previusPage();
				}
				if (item.getItemMeta().getDisplayName().contains("Kits Gratuitos do Mes")) {
					setCategory(KitCategory.FREE);
				}
				if (item.getItemMeta().getDisplayName().contains("Todos os kits")) {
					setCategory(KitCategory.ALL);
				}
				if (item.getItemMeta().getDisplayName().contains("Seus Kits")) {
					setCategory(KitCategory.OWNED);
				}
				if (item.getItemMeta().getDisplayName().contains("Kits Favoritos")) {
					setCategory(KitCategory.FAVORITE);
				}
				if (event.getRawSlot() >= 18) {
					if (manager.canUseKit(player, ChatColor.stripColor(item.getItemMeta().getDisplayName()).toLowerCase())) {
						manager.giveKit(player, m.getWarpManager().getWarpByName(m.getWarpManager().getPlayerWarp(player.getUniqueId())).getKit(ChatColor.stripColor(item.getItemMeta().getDisplayName()).toLowerCase()), true);
						player.closeInventory();
						destroy();
					}
				}
			}
		};
		m.getServer().getPluginManager().registerEvents(listener, m);
	}

	private void destroy() {
		HandlerList.unregisterAll(listener);
	}
}
