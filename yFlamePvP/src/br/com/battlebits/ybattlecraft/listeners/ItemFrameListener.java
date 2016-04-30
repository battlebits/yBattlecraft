package br.com.battlebits.ybattlecraft.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.battlebits.ycommon.common.BattlebitsAPI;

public class ItemFrameListener implements Listener {

	private br.com.battlebits.ybattlecraft.yBattleCraft m;

	public ItemFrameListener(br.com.battlebits.ybattlecraft.yBattleCraft yBattleCraft) {
		this.m = yBattleCraft;
	}

	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent event) {
		if (!(event.getRightClicked() instanceof ItemFrame))
			return;
		ItemFrame frame = (ItemFrame) event.getRightClicked();
		Player p = event.getPlayer();
		event.setCancelled(true);
		if (frame.getItem().getType() != Material.AIR) {
			ItemStack item = frame.getItem();
			if (!item.hasItemMeta())
				return;
			if (!item.getItemMeta().hasDisplayName())
				return;
			String line = item.getItemMeta().getDisplayName();
			if (line.toLowerCase().contains("sopas")) {
				Inventory inv = Bukkit.createInventory(null, 36, ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Sopas");
				for (int i = 0; i < 36; i++)
					inv.setItem(i, new ItemStack(Material.MUSHROOM_SOUP));
				p.openInventory(inv);
				return;
			} else if (line.toLowerCase().contains("afiada i")) {
				if (p.getItemInHand().getType().toString().contains("_SWORD")) {
					p.getItemInHand().addEnchantment(Enchantment.DAMAGE_ALL, 1);
				} else {
					p.sendMessage(ChatColor.DARK_RED + "Desculpe, mas você só pode encantar espadas");
				}
				return;
			} else if (line.toLowerCase().contains("afiada ii")) {
				if (p.getItemInHand().getType().toString().contains("_SWORD")) {
					p.getItemInHand().addEnchantment(Enchantment.DAMAGE_ALL, 2);
				} else {
					p.sendMessage(ChatColor.DARK_RED + "Desculpe, mas você só pode encantar espadas");
				}
				return;
			} else if (line.toLowerCase().contains("afiada iii")) {
				if (p.getItemInHand().getType().toString().contains("_SWORD")) {
					p.getItemInHand().addEnchantment(Enchantment.DAMAGE_ALL, 3);
				} else {
					p.sendMessage(ChatColor.DARK_RED + "Desculpe, mas você só pode encantar espadas");
				}
				return;
			} else if (line.toLowerCase().contains("repulsao i")) {
				if (p.getItemInHand().getType().toString().contains("_SWORD")) {
					p.getItemInHand().addEnchantment(Enchantment.KNOCKBACK, 1);
				} else {
					p.sendMessage(ChatColor.DARK_RED + "Desculpe, mas você só pode encantar espadas");
				}
				return;
			} else if (line.toLowerCase().contains("repulsao ii")) {
				if (p.getItemInHand().getType().toString().contains("_SWORD")) {
					p.getItemInHand().addEnchantment(Enchantment.KNOCKBACK, 2);
				} else {
					p.sendMessage(ChatColor.DARK_RED + "Desculpe, mas você só pode encantar espadas");
				}
				return;
			} else if (line.toLowerCase().contains("repulsao iii")) {
				if (p.getItemInHand().getType().toString().contains("_SWORD")) {
					p.getItemInHand().addEnchantment(Enchantment.KNOCKBACK, 3);
				} else {
					p.sendMessage(ChatColor.DARK_RED + "Desculpe, mas você só pode encantar espadas");
				}
				return;
			} else if (line.toLowerCase().contains("inquebrável i")) {
				String s = p.getItemInHand().getType().toString();
				if (s.contains("_CHESTPLATE") || s.contains("_HELMET") || s.contains("_LEGGINGS") || s.contains("_BOOTS") || s.contains("_SWORD") || s.contains("_AXE")) {
					p.getItemInHand().addEnchantment(Enchantment.DURABILITY, 1);
				} else {
					p.sendMessage(ChatColor.DARK_RED + "Desculpe, mas você só pode encantar espadas ou armaduras");
				}
				return;
			} else if (line.toLowerCase().contains("inquebrável ii")) {
				String s = p.getItemInHand().getType().toString();
				if (s.contains("_CHESTPLATE") || s.contains("_HELMET") || s.contains("_LEGGINGS") || s.contains("_BOOTS") || s.contains("_SWORD") || s.contains("_AXE")) {
					p.getItemInHand().addEnchantment(Enchantment.DURABILITY, 2);
				} else {
					p.sendMessage(ChatColor.DARK_RED + "Desculpe, mas você só pode encantar espadas");
				}
				return;
			} else if (line.toLowerCase().contains("inquebrável iii")) {
				String s = p.getItemInHand().getType().toString();
				if (s.contains("_CHESTPLATE") || s.contains("_HELMET") || s.contains("_LEGGINGS") || s.contains("_BOOTS") || s.contains("_SWORD") || s.contains("_AXE")) {
					p.getItemInHand().addEnchantment(Enchantment.DURABILITY, 3);
				} else {
					p.sendMessage(ChatColor.DARK_RED + "Desculpe, mas você só pode encantar espadas");
				}
				return;
			} else if (line.toLowerCase().contains("protecao i")) {
				String s = p.getItemInHand().getType().toString();
				if (s.contains("_CHESTPLATE") || s.contains("_HELMET") || s.contains("_LEGGINGS") || s.contains("_BOOTS")) {
					p.getItemInHand().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				} else {
					p.sendMessage(ChatColor.DARK_RED + "Desculpe, mas você só pode encantar armaduras");
				}
				return;
			} else if (line.toLowerCase().contains("protecao ii")) {
				String s = p.getItemInHand().getType().toString();
				if (s.contains("_CHESTPLATE") || s.contains("_HELMET") || s.contains("_LEGGINGS") || s.contains("_BOOTS")) {
					p.getItemInHand().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
				} else {
					p.sendMessage(ChatColor.DARK_RED + "Desculpe, mas você só pode encantar armaduras");
				}
				return;
			} else if (line.toLowerCase().contains("protecao iii")) {
				String s = p.getItemInHand().getType().toString();
				if (s.contains("_CHESTPLATE") || s.contains("_HELMET") || s.contains("_LEGGINGS") || s.contains("_BOOTS")) {
					p.getItemInHand().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
				} else {
					p.sendMessage(ChatColor.DARK_RED + "Desculpe, mas você só pode encantar armaduras");
				}
				return;
			} else if (line.toLowerCase().contains("marrons")) {
				Inventory inv = Bukkit.createInventory(null, 36, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Cogumelos Marrons");
				for (int i = 0; i < 36; i++)
					inv.setItem(i, new ItemStack(Material.BROWN_MUSHROOM, 64));
				p.openInventory(inv);
				return;
			} else if (line.toLowerCase().contains("vermelhos")) {
				Inventory inv = Bukkit.createInventory(null, 36, ChatColor.RED.toString() + ChatColor.BOLD + "Cogumelos Vermelhos");
				for (int i = 0; i < 36; i++)
					inv.setItem(i, new ItemStack(Material.RED_MUSHROOM, 64));
				p.openInventory(inv);
				return;
			} else if (line.toLowerCase().contains("potes")) {
				Inventory inv = Bukkit.createInventory(null, 36, ChatColor.GRAY.toString() + ChatColor.BOLD + "Potes");
				for (int i = 0; i < 36; i++)
					inv.setItem(i, new ItemStack(Material.BOWL, 64));
				p.openInventory(inv);
				return;
			} else if (line.toLowerCase().contains("cogumelos")) {

			} else if (line.toLowerCase().contains("facil")) {
				if (p.getFireTicks() > 0) {
					p.sendMessage(ChatColor.RED + "Voce nao pode se teleportar enquanto está em chamas!");
					return;
				}
				m.getWarpManager().teleportWarp(p, "Lava Challenge".toLowerCase().trim(), false);
				p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "LavaChallenge" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.GRAY + "Voce completou o nivel fácil do Lava Challenge");
				p.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Money" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.GRAY + "Você recebeu " + BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).addMoney(1) + "  por completar o Lava Challenge");
				return;
			} else if (line.toLowerCase().contains("médio")) {
				if (p.getFireTicks() > 0) {
					p.sendMessage(ChatColor.RED + "Voce nao pode se teleportar enquanto está em chamas!");
					return;
				}
				m.getWarpManager().teleportWarp(p, "Lava Challenge".toLowerCase().trim(), false);
				p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "LavaChallenge" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.GRAY + "Voce completou o nivel médio do Lava Challenge");
				p.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Money" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.GRAY + "Você recebeu " + BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).addMoney(3) + " por completar o Lava Challenge");
				return;
			} else if (line.toLowerCase().contains("difícil")) {
				if (p.getFireTicks() > 0) {
					p.sendMessage(ChatColor.RED + "Voce nao pode se teleportar enquanto está em chamas!");
					return;
				}
				m.getWarpManager().teleportWarp(p, "Lava Challenge".toLowerCase().trim(), false);
				p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "LavaChallenge" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.GRAY + "Voce completou o nivel difícil do Lava Challenge");
				p.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Money" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.GRAY + "Você recebeu " + BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).addMoney(5) + " por completar o Lava Challenge");
			} else if (line.toLowerCase().contains("extreme")) {
				if (p.getFireTicks() > 0) {
					p.sendMessage(ChatColor.RED + "Voce nao pode se teleportar enquanto está em chamas!");
					return;
				}
				m.getWarpManager().teleportWarp(p, "Lava Challenge".toLowerCase().trim(), false);
				Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "LavaChallenge" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.GRAY + p.getName() + " completou o nivel extreme do Lava Challenge");
				p.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Money" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.GRAY + "Você recebeu " + BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).addMoney(10) + " por completar o Lava Challenge");
			} else if (line.toLowerCase().contains("impossible")) {
				if (p.getFireTicks() > 0) {
					p.sendMessage(ChatColor.RED + "Voce nao pode se teleportar enquanto está em chamas!");
					return;
				}
				m.getWarpManager().teleportWarp(p, "Lava Challenge".toLowerCase().trim(), false);
				Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "LavaChallenge" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.GRAY + p.getName() + " completou o nivel impossible do Lava Challenge");
				p.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Money" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.GRAY + "Você recebeu " + BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).addMoney(15) + " por completar o Lava Challenge");
			}
			return;
		}
		if (p.getGameMode() == GameMode.CREATIVE) {
			ItemStack item = p.getItemInHand();
			if (item == null)
				return;
			if (item.getType() == Material.MUSHROOM_SOUP) {
				setMessage(frame, item, ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Sopas");
			} else if (item.getType() == Material.DIAMOND_SWORD) {
				item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
				if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
					setMessage(frame, item, ChatColor.GREEN.toString() + ChatColor.BOLD + "Afiada I");
					return;
				}
				String line = item.getItemMeta().getDisplayName();
				if (line.equalsIgnoreCase("afiada ii")) {
					setMessage(frame, item, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Afiada II");
				} else if (line.equalsIgnoreCase("afiada iii")) {
					setMessage(frame, item, ChatColor.RED.toString() + ChatColor.BOLD + "Afiada III");
				} else {
					setMessage(frame, item, ChatColor.GREEN.toString() + ChatColor.BOLD + "Afiada I");
				}
			} else if (item.getType() == Material.STICK) {
				item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
				if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
					setMessage(frame, item, ChatColor.GREEN.toString() + ChatColor.BOLD + "Repulsao I");
					return;
				}
				String line = item.getItemMeta().getDisplayName();
				if (line.equalsIgnoreCase("repulsao ii")) {
					setMessage(frame, item, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Repulsao II");
				} else if (line.equalsIgnoreCase("repulsao iii")) {
					setMessage(frame, item, ChatColor.RED.toString() + ChatColor.BOLD + "Repulsao III");
				} else {
					setMessage(frame, item, ChatColor.GREEN.toString() + ChatColor.BOLD + "Repulsao I");
				}
			} else if (item.getType() == Material.IRON_CHESTPLATE) {
				item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
					setMessage(frame, item, ChatColor.GREEN.toString() + ChatColor.BOLD + "Protecao I");
					return;
				}
				String line = item.getItemMeta().getDisplayName();
				if (line.equalsIgnoreCase("protecao ii")) {
					setMessage(frame, item, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Protecao II");
				} else if (line.equalsIgnoreCase("protecao iii")) {
					setMessage(frame, item, ChatColor.RED.toString() + ChatColor.BOLD + "Protecao III");
				} else {
					setMessage(frame, item, ChatColor.GREEN.toString() + ChatColor.BOLD + "Protecao I");
				}
			} else if (item.getType() == Material.BEDROCK) {
				item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
					setMessage(frame, item, ChatColor.GREEN.toString() + ChatColor.BOLD + "Inquebrável I");
					return;
				}
				String line = item.getItemMeta().getDisplayName();
				if (line.equalsIgnoreCase("inquebravel ii")) {
					setMessage(frame, item, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Inquebrável II");
				} else if (line.equalsIgnoreCase("inquebravel iii")) {
					setMessage(frame, item, ChatColor.RED.toString() + ChatColor.BOLD + "Inquebrável III");
				} else {
					setMessage(frame, item, ChatColor.GREEN.toString() + ChatColor.BOLD + "Inquebrável I");
				}
			} else if (item.getType() == Material.RED_MUSHROOM) {
				setMessage(frame, item, ChatColor.RED.toString() + ChatColor.BOLD + "Cogumelos Vermelhos");
			} else if (item.getType() == Material.BROWN_MUSHROOM) {
				setMessage(frame, item, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Cogumelos Marrons");
			} else if (item.getType() == Material.BOWL) {
				setMessage(frame, item, ChatColor.GRAY.toString() + ChatColor.BOLD + "Potes");
			} else if (item.getType() == Material.LAVA_BUCKET) {
				if (!item.hasItemMeta())
					return;
				if (!item.getItemMeta().hasDisplayName())
					return;
				String line = item.getItemMeta().getDisplayName();
				if (line.equalsIgnoreCase("facil")) {
					setMessage(frame, item, ChatColor.GREEN.toString() + ChatColor.BOLD + "Nível Facil Completado");
				} else if (line.equalsIgnoreCase("medio")) {
					setMessage(frame, item, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Nível Médio Completado");
				} else if (line.equalsIgnoreCase("dificil")) {
					setMessage(frame, item, ChatColor.RED.toString() + ChatColor.BOLD + "Nível Difícil Completado");
				} else if (line.equalsIgnoreCase("extreme")) {
					setMessage(frame, item, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Nível Extreme Completado");
				} else if (line.equalsIgnoreCase("impossible")) {
					setMessage(frame, item, ChatColor.GOLD.toString() + ChatColor.BOLD + "Nível Impossible Completado");
				}
			}
		}
	}

	@EventHandler
	public void onHanging(HangingBreakEvent event) {
		if (event.getCause() != RemoveCause.ENTITY) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onHanging(HangingBreakByEntityEvent event) {
		if (!(event.getEntity() instanceof ItemFrame))
			return;
		if (!(event.getRemover() instanceof Player)) {
			event.setCancelled(true);
			return;
		}
		Player p = (Player) event.getRemover();
		if (p.getGameMode() != GameMode.CREATIVE || !p.hasPermission("flame.build")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof ItemFrame)) {
			return;
		}
		if (!(event.getDamager() instanceof Player)) {
			event.setCancelled(true);
			return;
		}
		Player p = (Player) event.getDamager();
		if (p.getGameMode() != GameMode.CREATIVE || !p.hasPermission("flame.build")) {
			event.setCancelled(true);
		}
	}

	private void setMessage(ItemFrame frame, ItemStack item, String message) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(message);
		item.setItemMeta(meta);
		frame.setItem(item);
		frame.setRotation(Rotation.NONE);
	}

}
