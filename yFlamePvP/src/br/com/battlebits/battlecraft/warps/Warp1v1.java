package br.com.battlebits.battlecraft.warps;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.BaseWarp;
import br.com.battlebits.battlecraft.constructors.Desafio;
import br.com.battlebits.battlecraft.constructors.Warp;
import br.com.battlebits.battlecraft.events.PlayerWarpJoinEvent;
import br.com.battlebits.battlecraft.hotbar.Hotbar;
import br.com.battlebits.battlecraft.utils.Name;

public class Warp1v1 extends BaseWarp {

	public static List<Player> playersIn1v1;
	private HashMap<String, HashMap<ChanllengeType, HashMap<String, Desafio>>> playerDesafios;
	private List<Player> playersInQueue;
	private Location firstLoction;
	private Location secondLoction;

	public Warp1v1(Main main) {
		super(main);
		playersIn1v1 = new ArrayList<>();
		playerDesafios = new HashMap<>();
		playersInQueue = new ArrayList<>();
	}

	@EventHandler
	public void onWarpTeleport(PlayerWarpJoinEvent event) {
		Player p = event.getPlayer();
		if (!isOnWarp(p))
			return;
		getMain().getProtectionManager().addProtection(event.getPlayer().getUniqueId());
		Hotbar.set1v1(event.getPlayer());
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		ItemStack item = event.getItem();
		if (!getMain().getWarpManager().isInWarp(p, "1v1"))
			return;
		if (getMain().getKitManager().hasKit(p))
			return;
		if (item == null)
			return;
		if (!event.getAction().toString().contains("RIGHT"))
			return;
		if (item.getType() != Material.INK_SACK)
			return;
		if (item.getDurability() == (byte) 8) {
			if (playersInQueue.contains(p)) {
				item.setDurability((byte) 10);
				p.sendMessage(ChatColor.YELLOW + "Voce ja esta na lista de espera, aguarde...");
				return;
			}
			if (playersInQueue.size() > 0) {
				Player player = playersInQueue.get(0);
				if (player != null) {
					player.sendMessage(ChatColor.BLUE + "O 1v1 Rapido encontrou alguem para voce lutar! O player escolhido foi: " + ChatColor.YELLOW + p.getName());
					p.sendMessage(ChatColor.BLUE + "O 1v1 Rapido encontrou alguem para voce lutar! O player escolhido foi: " + ChatColor.YELLOW + player.getName());
					setIn1v1(new Desafio(player, p));
					return;
				}
			}
			item.setDurability((byte) 10);
			p.sendMessage(ChatColor.YELLOW + "O 1v1 Rapido esta procurando alguem para voce batalhar!");
			playersInQueue.add(p);
		} else {
			if (!playersInQueue.contains(p)) {
				p.sendMessage(ChatColor.YELLOW + "Voce não está na lista de espera");
				item.setDurability((byte) 8);
				return;
			}
			item.setDurability((byte) 8);
			p.sendMessage(ChatColor.RED + "Você saiu da lista de espera para o 1v1 rápido");
			playersInQueue.remove(p);
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEntityEvent event) {
		Player p = event.getPlayer();
		Entity e = event.getRightClicked();
		ItemStack item = p.getItemInHand();
		if (!getMain().getWarpManager().isInWarp(p, "1v1"))
			return;
		if (getMain().getKitManager().hasKit(p))
			return;
		if (!(e instanceof Player))
			return;
		if (item == null)
			return;
		if (item.getType() != Material.BLAZE_ROD && item.getType() != Material.IRON_FENCE)
			return;
		ChanllengeType type;
		Player clicado = (Player) e;
		if (isIn1v1(clicado)) {
			p.sendMessage(ChatColor.GRAY + "O jogador ja esta em 1v1");
			return;
		}
		switch (item.getType()) {
		case IRON_FENCE:
			type = ChanllengeType.CUSTOM;
			break;
		default:
			type = ChanllengeType.DEFAULT;
			break;
		}
		if (hasDesafio(p, clicado, type)) {
			Desafio desafio = getDesafio(p, clicado, type);
			if (type == ChanllengeType.CUSTOM) {
				if (!desafio.hasExpire()) {
					openAccept(p, desafio);
					return;
				}
			} else {
				if (!desafio.hasExpire()) {
					p.sendMessage(ChatColor.GREEN + "Voce aceitou o desafio de " + ChatColor.AQUA + clicado.getName());
					clicado.sendMessage(ChatColor.AQUA + p.getName() + ChatColor.GREEN + " aceitou seu desafio");
					setIn1v1(desafio);
					return;
				}
			}
		}
		if (hasDesafio(clicado, p, type)) {
			if (!getDesafio(clicado, p, type).hasExpire()) {
				p.sendMessage(ChatColor.YELLOW + "Voce ja enviou um desafio a este player, aguarde alguns segundos!");
				return;
			}
		}
		if (type == ChanllengeType.DEFAULT) {
			HashMap<ChanllengeType, HashMap<String, Desafio>> hash;
			if (playerDesafios.containsKey(clicado.getName()))
				hash = playerDesafios.get(clicado.getName());
			else
				hash = new HashMap<>();
			HashMap<String, Desafio> hashDesa;
			if (hash.containsKey(type))
				hashDesa = hash.get(type);
			else
				hashDesa = new HashMap<>();
			hashDesa.put(p.getName(), new Desafio(p, clicado));
			hash.put(type, hashDesa);
			playerDesafios.put(clicado.getName(), hash);
			p.sendMessage(ChatColor.GRAY + "Voce enviou um desafio de 1v1 normal para " + ChatColor.AQUA + clicado.getName());
			clicado.sendMessage(ChatColor.YELLOW + "Voce recebeu desafio de 1v1 normal de " + ChatColor.GRAY + p.getName());
		} else {
			openChallange(p, clicado);
		}
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		if (playerDesafios.containsKey(event.getPlayer().getName()))
			playerDesafios.remove(event.getPlayer().getName());
		if (playersInQueue.contains(event.getPlayer()))
			playersInQueue.remove(event.getPlayer());
	}

	public boolean hasDesafio(Player desafiado, Player desafiante, ChanllengeType type) {
		return playerDesafios.containsKey(desafiado.getName()) && playerDesafios.get(desafiado.getName()).containsKey(type) && playerDesafios.get(desafiado.getName()).get(type).containsKey(desafiante.getName());
	}

	public Desafio getDesafio(Player desafiado, Player desafiante, ChanllengeType type) {
		return playerDesafios.get(desafiado.getName()).get(type).get(desafiante.getName());
	}

	public static boolean isIn1v1(Player player) {
		return playersIn1v1.contains(player);
	}

	public static enum ChanllengeType {
		DEFAULT, CUSTOM, FAST;
	}

	public static void openChallange(Player p, Player desafiador) {
		ItemStack nullitem = getItem(Material.STAINED_GLASS_PANE, " ");
		ItemStack enviar = getItem(new ItemStack(Material.WOOL, 1, (byte) 5), ChatColor.GREEN + "Enviar Desafio!");
		ItemStack sword = getItem(Material.WOOD_SWORD, ChatColor.GOLD + "Espada de Madeira", ChatColor.DARK_AQUA + "Clique aqui para mudar", ChatColor.DARK_AQUA + "o tipo da espada!");
		ItemStack armor = getItem(Material.GLASS, ChatColor.RED + "Sem Armadura", ChatColor.DARK_AQUA + "Clique aqui para mudar", ChatColor.DARK_AQUA + "o tipo de armadura!");
		ItemStack speed = getItem(Material.GLASS_BOTTLE, ChatColor.RED + "Sem Velocidade", ChatColor.DARK_AQUA + "Clique aqui para usar", ChatColor.DARK_AQUA + "pocao de velocidade!");
		ItemStack forca = getItem(Material.GLASS_BOTTLE, ChatColor.RED + "Sem Forca", ChatColor.DARK_AQUA + "Clique aqui para usar", ChatColor.DARK_AQUA + "pocao de forca!");
		ItemStack sopa = getItem(Material.BOWL, ChatColor.GREEN + "1 HotBar", ChatColor.DARK_AQUA + "Clique aqui para usar", ChatColor.DARK_AQUA + "full sopa!");
		ItemStack sharp = getItem(Material.BOOK, ChatColor.GREEN + "Sem Afiada", ChatColor.DARK_AQUA + "Clique aqui para usar", ChatColor.DARK_AQUA + "Afiada I");

		Inventory inventoty = Bukkit.createInventory(null, 54, ChatColor.RED + "1v1 contra " + desafiador.getName());
		for (int i = 0; i < 54; i++) {
			inventoty.setItem(i, nullitem);
		}
		inventoty.setItem(20, sword);
		inventoty.setItem(21, armor);
		inventoty.setItem(22, speed);
		inventoty.setItem(23, forca);
		inventoty.setItem(24, sopa);
		inventoty.setItem(29, sharp);
		inventoty.setItem(43, enviar);
		inventoty.setItem(44, enviar);
		inventoty.setItem(52, enviar);
		inventoty.setItem(53, enviar);
		p.openInventory(inventoty);
	}

	public static void openAccept(Player p, Desafio desafio) {
		Player desafiador = desafio.getDesafiante();
		ItemStack nullitem = new ItemStack(Material.STAINED_GLASS_PANE);
		ItemMeta nullmeta = nullitem.getItemMeta();
		nullmeta.setDisplayName(" ");
		nullitem.setItemMeta(nullmeta);
		ItemStack bar = new ItemStack(Material.IRON_FENCE);
		ItemMeta barmeta = bar.getItemMeta();
		barmeta.setDisplayName(" ");
		bar.setItemMeta(barmeta);
		ItemStack fire = new ItemStack(Material.FIRE);
		ItemMeta firemeta = fire.getItemMeta();
		firemeta.setDisplayName(ChatColor.AQUA.toString() + ChatColor.BOLD + "1v1 Customizado");
		fire.setItemMeta(firemeta);
		ItemStack enviar = new ItemStack(Material.WOOL, 1, (byte) 5);
		ItemMeta sendMeta = enviar.getItemMeta();
		sendMeta.setDisplayName(ChatColor.GREEN + "Aceitar Desafio!");
		enviar.setItemMeta(sendMeta);
		ItemStack rejeitar = new ItemStack(Material.WOOL, 1, (byte) 14);
		ItemMeta rejeitarMeta = rejeitar.getItemMeta();
		rejeitarMeta.setDisplayName(ChatColor.RED + "Rejeitar Desafio!");
		rejeitar.setItemMeta(rejeitarMeta);
		String espada = desafio.getSwordType();
		String armadura = desafio.getArmorType();
		boolean sopa = desafio.isRefill();
		boolean forca = desafio.isStreght();
		boolean speed = desafio.isSpeed();
		boolean sharpness = desafio.hasSharp();
		ItemStack sword = getItem(Material.valueOf(espada.toUpperCase()), new Name().getName(espada.toUpperCase()));
		ItemStack armor = getItem(Material.valueOf(armadura.toUpperCase()), new Name().getName(armadura.toUpperCase()));
		ItemStack soup = null;
		ItemStack velo = null;
		ItemStack strenght = null;
		ItemStack sharp = null;
		if (speed) {
			velo = getItem(new ItemStack(Material.POTION, 1, (byte) 8194), ChatColor.GREEN + "Com Velocidade");
		} else {
			velo = getItem(Material.GLASS_BOTTLE, ChatColor.RED + "Sem Velocidade");
		}
		if (forca) {
			strenght = getItem(new ItemStack(Material.POTION, 1, (byte) 8201), ChatColor.GREEN + "Com Forca");
		} else {
			strenght = getItem(Material.GLASS_BOTTLE, ChatColor.RED + "Sem Forca");
		}
		if (sopa) {
			soup = getItem(Material.MUSHROOM_SOUP, ChatColor.AQUA + "Full Sopa");
		} else {
			soup = getItem(Material.BOWL, ChatColor.GREEN + "1 HotBar");
		}
		if (sharpness) {
			sharp = getItem(Material.ENCHANTED_BOOK, ChatColor.AQUA + "Com Afiada I");
		} else {
			sharp = getItem(Material.BOOK, ChatColor.GRAY + "Sem Afiada I");
		}
		Inventory inventoty = Bukkit.createInventory(null, 54, ChatColor.RED + "1v1 contra " + desafiador.getName());
		for (int i = 0; i < 54; i++) {
			inventoty.setItem(i, nullitem);
		}
		inventoty.setItem(20, sword);
		inventoty.setItem(21, armor);
		inventoty.setItem(22, velo);
		inventoty.setItem(23, strenght);
		inventoty.setItem(24, soup);
		inventoty.setItem(29, sharp);
		inventoty.setItem(46, rejeitar);
		inventoty.setItem(45, rejeitar);
		inventoty.setItem(36, rejeitar);
		inventoty.setItem(37, rejeitar);
		inventoty.setItem(43, enviar);
		inventoty.setItem(44, enviar);
		inventoty.setItem(52, enviar);
		inventoty.setItem(53, enviar);
		p.openInventory(inventoty);
	}

	public void setIn1v1(Desafio desafio) {
		if (desafio == null) {
			return;
		}
		Player p = desafio.getDesafiante();
		Player desafiado = desafio.getDesafiado();
		if (playersInQueue.contains(p))
			playersInQueue.remove(p);
		if (playersInQueue.contains(desafiado))
			playersInQueue.remove(desafiado);
		p.getInventory().setArmorContents(new ItemStack[0]);
		p.getInventory().setContents(new ItemStack[0]);
		desafiado.getInventory().setArmorContents(new ItemStack[0]);
		desafiado.getInventory().setContents(new ItemStack[0]);
		String espada = desafio.getSwordType();
		String armadura = desafio.getArmorType();
		boolean sopa = desafio.isRefill();
		boolean forca = desafio.isStreght();
		boolean speed = desafio.isSpeed();
		boolean sharp = desafio.hasSharp();
		setSword(p, espada, sharp);
		setArmor(p, armadura);
		setSoup(p, sopa);
		addSpeed(p, speed);
		addStrengh(p, forca);
		setSword(desafiado, espada, sharp);
		setArmor(desafiado, armadura);
		setSoup(desafiado, sopa);
		addSpeed(desafiado, speed);
		addStrengh(desafiado, forca);
		playersIn1v1.add(p);
		playersIn1v1.add(desafiado);
		getMain().getProtectionManager().removeProtection(p.getUniqueId());
		getMain().getProtectionManager().removeProtection(desafiado.getUniqueId());
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player != desafiado)
				p.hidePlayer(player);
		}
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player != p)
				desafiado.hidePlayer(player);
		}
		if (firstLoction == null)
			firstLoction = new Location(Bukkit.getWorld("1v1spawn"), 0.5, 67.5, -11.5);
		p.teleport(firstLoction);
		if (secondLoction == null)
			secondLoction = new Location(Bukkit.getWorld("1v1spawn"), 0.5, 67.5, 12.5, 180f, 0f);
		desafiado.teleport(secondLoction);
		new Fight(this, p, desafiado);
	}

	private static ItemStack getItem(Material mat, String name, String... desc) {
		ItemStack item = new ItemStack(mat);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(Arrays.asList(desc));
		item.setItemMeta(itemMeta);
		return item;
	}

	private static ItemStack getItem(ItemStack item, String name, String... desc) {
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(Arrays.asList(desc));
		item.setItemMeta(itemMeta);
		return item;
	}

	public static void setSword(Player p, String material, boolean sharpness) {
		ItemStack item = new ItemStack(Material.valueOf(material));
		if (sharpness)
			item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		p.getInventory().setItem(0, item);
	}

	public static void setSoup(Player p, boolean full) {
		int b = 9;
		if (full)
			b = 39;
		for (int i = 1; i < b; i++) {
			p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));
		}
	}

	public static void addStrengh(Player p, boolean forca) {
		if (forca) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100000 * 20, 0));
		}
	}

	public static void addSpeed(Player p, boolean speed) {
		if (speed) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000 * 20, 0));
		}
	}

	public static void setArmor(Player p, String material) {
		if (material.contains("GLASS"))
			return;
		material = material.replace("_CHESTPLATE", "");
		p.getInventory().setHelmet(new ItemStack(Material.valueOf(material + "_HELMET")));
		p.getInventory().setChestplate(new ItemStack(Material.valueOf(material + "_CHESTPLATE")));
		p.getInventory().setLeggings(new ItemStack(Material.valueOf(material + "_LEGGINGS")));
		p.getInventory().setBoots(new ItemStack(Material.valueOf(material + "_BOOTS")));
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void on1v1(InventoryClickEvent event) {
		ItemStack item = event.getCurrentItem();
		if (!(event.getWhoClicked() instanceof Player))
			return;
		if (item == null)
			return;
		if (!item.hasItemMeta())
			return;
		if (!item.getItemMeta().hasDisplayName())
			return;
		final Player p = (Player) event.getWhoClicked();
		String inv = p.getOpenInventory().getTitle();
		if (!getMain().getWarpManager().isInWarp(p, "1v1"))
			return;

		if (!inv.contains(ChatColor.RED + "1v1 contra ")) {
			return;
		}
		event.setCancelled(true);
		p.setItemOnCursor(new ItemStack(0));
		p.updateInventory();
		if (event.isShiftClick() || event.isRightClick()) {
			return;
		}
		if (item.getItemMeta().getDisplayName().contains(ChatColor.GREEN + "Enviar Desafio!")) {
			String sword = p.getOpenInventory().getItem(20).getType().toString();
			String armor = p.getOpenInventory().getItem(21).getType().toString();
			String b = p.getOpenInventory().getItem(22).getType().toString();
			String c = p.getOpenInventory().getItem(23).getType().toString();
			String d = p.getOpenInventory().getItem(24).getType().toString();
			String e = p.getOpenInventory().getItem(29).getType().toString();
			boolean speed = true;
			boolean forca = true;
			boolean sopa = true;
			boolean sharpness = true;
			if (b.equalsIgnoreCase(Material.GLASS_BOTTLE.toString())) {
				speed = false;
			}
			if (c.equalsIgnoreCase(Material.GLASS_BOTTLE.toString())) {
				forca = false;
			}
			if (d.equalsIgnoreCase(Material.BOWL.toString())) {
				sopa = false;
			}
			if (e.equalsIgnoreCase(Material.BOOK.toString())) {
				sharpness = false;
			}
			Player desafiado = Bukkit.getPlayer(inv.replace(ChatColor.RED + "1v1 contra ", ""));
			if (desafiado == null) {
				p.sendMessage(ChatColor.RED + "O player nao esta mais online!");
				p.closeInventory();
				return;
			}
			if (isIn1v1(desafiado)) {
				p.sendMessage(ChatColor.RED + "O player esta em 1v1 com outra pessa");
				p.closeInventory();
				return;
			}
			Desafio desafio = new Desafio(p, desafiado, sword, armor, sopa, speed, forca, sharpness);
			HashMap<ChanllengeType, HashMap<String, Desafio>> hash;
			if (playerDesafios.containsKey(desafiado.getName()))
				hash = playerDesafios.get(desafiado.getName());
			else
				hash = new HashMap<>();
			HashMap<String, Desafio> hashDesa;
			if (hash.containsKey(ChanllengeType.CUSTOM))
				hashDesa = hash.get(ChanllengeType.CUSTOM);
			else
				hashDesa = new HashMap<>();
			hashDesa.put(p.getName(), desafio);
			hash.put(ChanllengeType.CUSTOM, hashDesa);
			playerDesafios.put(desafiado.getName(), hash);
			p.sendMessage(ChatColor.YELLOW + "Voce enviou um desafio para " + ChatColor.GRAY + desafiado.getName());
			desafiado.sendMessage(ChatColor.GOLD + p.getName() + ChatColor.AQUA + " enviou um desafio de 1v1 customizado para voce");
			p.closeInventory();
		} else if (item.getType().toString().contains("_SWORD")) {
			String swordName = getNextSwordLevel(item.getType().toString().replace("_SWORD", "")) + "_SWORD";
			ItemStack sword = getItem(Material.valueOf(swordName), new Name().getName(swordName), ChatColor.DARK_AQUA + "Clique aqui para mudar", ChatColor.DARK_AQUA + "o tipo da espada!");
			p.getOpenInventory().setItem(event.getSlot(), sword);
		} else if (item.getType().toString().equalsIgnoreCase("GLASS") || item.getType().toString().contains("_CHESTPLATE")) {
			String materialName;
			if (item.getType().toString().equalsIgnoreCase("DIAMOND_CHESTPLATE")) {
				materialName = getNextArmorLevel(item.getType().toString().replace("_CHESTPLATE", ""));
			} else {
				materialName = getNextArmorLevel(item.getType().toString().replace("_CHESTPLATE", "")) + "_CHESTPLATE";
			}
			ItemStack armor = getItem(Material.valueOf(materialName), new Name().getName(materialName), ChatColor.DARK_AQUA + "Clique aqui para mudar", ChatColor.DARK_AQUA + "o tipo de armadura!");
			p.getOpenInventory().setItem(event.getSlot(), armor);
		} else if (event.getSlot() == 22 && item.getType().toString().equalsIgnoreCase("GLASS_BOTTLE")) {
			ItemStack speed = getItem(new ItemStack(Material.POTION, 1, (byte) 8194), ChatColor.GREEN + "Com Velocidade", ChatColor.DARK_AQUA + "Clique aqui para remover", ChatColor.DARK_AQUA + "a pocao de velocidade!");
			p.getOpenInventory().setItem(event.getSlot(), speed);
		} else if (event.getSlot() == 22 && item.getType().toString().equalsIgnoreCase("POTION")) {
			ItemStack speed = getItem(Material.GLASS_BOTTLE, ChatColor.RED + "Sem Velocidade", ChatColor.DARK_AQUA + "Clique aqui para usar", ChatColor.DARK_AQUA + "pocao de velocidade!");
			p.getOpenInventory().setItem(event.getSlot(), speed);
		} else if (event.getSlot() == 23 && item.getType().toString().equalsIgnoreCase("GLASS_BOTTLE")) {
			ItemStack speed = getItem(new ItemStack(Material.POTION, 1, (byte) 8201), ChatColor.GREEN + "Com Forca", ChatColor.DARK_AQUA + "Clique aqui para remover", ChatColor.DARK_AQUA + "a pocao de forca!");
			p.getOpenInventory().setItem(event.getSlot(), speed);
		} else if (event.getSlot() == 23 && item.getType().toString().equalsIgnoreCase("POTION")) {
			ItemStack speed = getItem(Material.GLASS_BOTTLE, ChatColor.RED + "Sem Forca", ChatColor.DARK_AQUA + "Clique aqui para usar", ChatColor.DARK_AQUA + "pocao de forca!");
			p.getOpenInventory().setItem(event.getSlot(), speed);
		} else if (item.getType().toString().equalsIgnoreCase("BOWL")) {
			ItemStack sopa = getItem(Material.MUSHROOM_SOUP, ChatColor.AQUA + "Full Sopa", ChatColor.DARK_AQUA + "Clique aqui para usar", ChatColor.DARK_AQUA + "1 HotBar!");
			p.getOpenInventory().setItem(event.getSlot(), sopa);
		} else if (item.getType().toString().equalsIgnoreCase("MUSHROOM_SOUP")) {
			ItemStack sopa = getItem(Material.BOWL, ChatColor.GREEN + "1 HotBar", ChatColor.DARK_AQUA + "Clique aqui para usar", ChatColor.DARK_AQUA + "FullSopa!");
			p.getOpenInventory().setItem(event.getSlot(), sopa);
		} else if (item.getType().toString().equalsIgnoreCase("ENCHANTED_BOOK")) {
			ItemStack sharp = getItem(Material.BOOK, ChatColor.GRAY + "Sem Afiada", ChatColor.DARK_AQUA + "Clique aqui para usar", ChatColor.DARK_AQUA + "Afiada I");
			p.getOpenInventory().setItem(event.getSlot(), sharp);
		} else if (item.getType().toString().equalsIgnoreCase("BOOK")) {
			ItemStack sharp = getItem(Material.ENCHANTED_BOOK, ChatColor.AQUA + "Com Afiada", ChatColor.DARK_AQUA + "Clique aqui para usar", ChatColor.DARK_AQUA + "sem Afiada I");
			p.getOpenInventory().setItem(event.getSlot(), sharp);
		}
	}

	private String getNextSwordLevel(String str) {
		switch (str) {
		case "WOOD":
			return "STONE";
		case "STONE":
			return "IRON";
		case "IRON":
			return "DIAMOND";
		case "DIAMOND":
			return "WOOD";
		}
		return "";
	}

	private String getNextArmorLevel(String str) {
		switch (str) {
		case "GLASS":
			return "LEATHER";
		case "LEATHER":
			return "CHAINMAIL";
		case "CHAINMAIL":
			return "IRON";
		case "IRON":
			return "DIAMOND";
		case "DIAMOND":
			return "GLASS";
		}
		return "";
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void on1v1Accept(InventoryClickEvent event) {
		ItemStack item = event.getCurrentItem();
		if (!(event.getWhoClicked() instanceof Player))
			return;
		if (item == null)
			return;
		if (!item.hasItemMeta())
			return;
		if (!item.getItemMeta().hasDisplayName())
			return;
		Player p = (Player) event.getWhoClicked();
		String inv = p.getOpenInventory().getTitle();
		if (!getMain().getWarpManager().isInWarp(p, "1v1"))
			return;

		if (!inv.contains(ChatColor.RED + "1v1 contra ")) {
			return;
		}
		event.setCancelled(true);
		p.setItemOnCursor(new ItemStack(0));
		p.updateInventory();
		if (event.isShiftClick() || event.isRightClick()) {
			return;
		}
		Player desafiante = Bukkit.getPlayer(inv.replace(ChatColor.RED + "1v1 contra ", ""));
		Player desafiado = p;
		if (item.getItemMeta().getDisplayName().contains(ChatColor.GREEN + "Aceitar Desafio!")) {
			if (desafiante == null) {
				p.sendMessage(ChatColor.RED + "O player nao esta mais online!");
				p.closeInventory();
				return;
			}
			if (isIn1v1(desafiante)) {
				p.sendMessage(ChatColor.RED + "O player esta em 1v1 com outra pessa");
				p.closeInventory();
				return;
			}
			HashMap<ChanllengeType, HashMap<String, Desafio>> hash;
			if (playerDesafios.containsKey(desafiado.getName())) {
				hash = playerDesafios.get(desafiado.getName());
				HashMap<String, Desafio> hashDesa;
				if (hash.containsKey(ChanllengeType.CUSTOM)) {
					hashDesa = hash.get(ChanllengeType.CUSTOM);
					if (hashDesa.containsKey(desafiante.getName())) {
						Desafio desafio = hashDesa.get(desafiante.getName());
						setIn1v1(desafio);
						return;
					}
				}
			}
			p.sendMessage(ChatColor.YELLOW + "Ocorreu algum erro, tente novamente!");
			p.closeInventory();
		} else if (item.getItemMeta().getDisplayName().contains(ChatColor.RED + "Rejeitar Desafio!")) {
			if (desafiante == null) {
				p.sendMessage(ChatColor.RED + "O player nao esta mais online!");
				p.closeInventory();
				return;
			}
			if (isIn1v1(desafiante)) {
				p.sendMessage(ChatColor.RED + "O player esta em 1v1 com outra pessa");
				p.closeInventory();
				return;
			}
			if (isIn1v1(desafiante)) {
				p.sendMessage(ChatColor.AQUA + "Parece que seu desafiante ja esta em 1v1");
				return;
			}
			HashMap<ChanllengeType, HashMap<String, Desafio>> hash;
			if (playerDesafios.containsKey(desafiado.getName())) {
				hash = playerDesafios.get(desafiado.getName());
				HashMap<String, Desafio> hashDesa;
				if (hash.containsKey(ChanllengeType.CUSTOM)) {
					hashDesa = hash.get(ChanllengeType.CUSTOM);
					if (hashDesa.containsKey(desafiante.getName()))
						hashDesa.remove(desafiante.getName());
				}
			}
			desafiante.sendMessage(ChatColor.AQUA + desafiado.getName() + ChatColor.RED + " rejeitou seu desafio");
			desafiado.sendMessage(ChatColor.RED + "Voce rejeitou o desafio de " + ChatColor.AQUA + desafiante.getName());
			p.closeInventory();
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		handleQuit(event.getPlayer());
	}

	@EventHandler
	public void onKick(PlayerKickEvent event) {
		handleQuit(event.getPlayer());
	}

	public void handleQuit(Player p) {
		if (playerDesafios.containsKey(p.getName()))
			playerDesafios.remove(p.getName());
		if (playersInQueue.contains(p))
			playersInQueue.remove(p);
	}

	public void teleport1v1(Player p) {
		Location l;
		World w = Bukkit.getWorld("1v1spawn");
		Random random = new Random();
		int i = random.nextInt(3);
		if (i == 0)
			l = new Location(w, 23.5, 67.5, -22.5, 45f, 0f);
		else if (i == 1)
			l = new Location(w, -22.5, 67.5, -22.5, 315f, 0f);
		else if (i == 2)
			l = new Location(w, 23.5, 67.5, 23.5, 135f, 0f);
		else
			l = new Location(w, -22.5, 67.5, 23.5, 230f, 0f);
		getMain().getWarpManager().removeWarp(p);
		getMain().getKitManager().removeKit(p);
		getMain().getWarpManager().teleportWarp(p, "1v1", false);
		getMain().getProtectionManager().addProtection(p.getUniqueId());
		if (p.getGameMode() == GameMode.CREATIVE)
			p.sendMessage(ChatColor.RED + "Voce esta no modo Criativo");
		for (PotionEffect potion : p.getActivePotionEffects()) {
			p.removePotionEffect(potion.getType());
		}
		p.teleport(l);
		Hotbar.set1v1(p);
	}

	private static class Fight {
		private Listener listener;

		public Fight(Warp1v1 l, Player player1, Player player2) {
			listener = new Listener() {
				@SuppressWarnings("deprecation")
				@EventHandler
				public void onDeathStatus(PlayerDeathEvent event) {
					Player p = event.getEntity();
					if (!isInPvP(p))
						return;
					Player killer = null;
					if (p == player1)
						killer = player2;
					if (p == player2)
						killer = player1;
					int i = 0;
					for (ItemStack sopa : killer.getInventory().getContents()) {
						if (sopa != null && sopa.getType() != Material.AIR && sopa.getType() == Material.MUSHROOM_SOUP) {
							i = i + sopa.getAmount();
						}
					}
					DecimalFormat dm = new DecimalFormat("##.#");
					p.sendMessage(ChatColor.RED + killer.getName() + " venceu o 1v1 com " + dm.format(killer.getHealth() / 2) + " coracoes e " + i + " sopas restantes");
					killer.sendMessage(ChatColor.RED + "Voce venceu o 1v1 contra " + p.getName() + " com " + dm.format(killer.getHealth() / 2) + " coracoes e " + i + " sopas restantes");
					l.teleport1v1(killer);
					l.teleport1v1(p);
					killer.setHealth(20D);
					killer.updateInventory();
					p.setHealth(20D);
					killer.updateInventory();
					playersIn1v1.remove(p);
					playersIn1v1.remove(killer);
					l.getMain().getVanish().updateVanished(p);
					l.getMain().getVanish().updateVanished(killer);
					event.getDrops().clear();
					destroy();
				}

				@EventHandler
				public void onEntityDamage(EntityDamageByEntityEvent event) {
					if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
						Player recebe = (Player) event.getEntity();
						Player faz = (Player) event.getDamager();
						if (isInPvP(faz) && isInPvP(recebe))
							return;
						if (isInPvP(faz) && !isInPvP(recebe))
							event.setCancelled(true);
						else if (!isInPvP(faz) && isInPvP(recebe))
							event.setCancelled(true);
					}
				}

				@EventHandler
				public void onQuit(PlayerQuitEvent event) {
					handleQuit(event.getPlayer());
				}

				@EventHandler
				public void onKick(PlayerKickEvent event) {
					handleQuit(event.getPlayer());
				}

				@SuppressWarnings("deprecation")
				public void handleQuit(Player p) {
					if (!isInPvP(p))
						return;
					Player killer = null;
					if (p == player1)
						killer = player2;
					if (p == player2)
						killer = player1;
					killer.sendMessage(ChatColor.RED + p.getName() + " deslogou!");
					l.teleport1v1(killer);
					killer.setHealth(20D);
					killer.updateInventory();
					playersIn1v1.remove(p);
					playersIn1v1.remove(killer);
					l.getMain().getVanish().updateVanished(killer);
					p.damage(2000, killer);
					destroy();
				}

				public boolean isInPvP(Player player) {
					return player == player1 || player == player2;
				}
			};
			Bukkit.getPluginManager().registerEvents(listener, l.getMain());
		}

		public void destroy() {
			HandlerList.unregisterAll(listener);
		}
	}

	@Override
	protected Warp getWarp() {
		Warp onevsone = new Warp("1v1", "Entre em uma luta justa com alguem", new ItemStack(Material.BLAZE_ROD), null, false);
		return onevsone;
	}

}
