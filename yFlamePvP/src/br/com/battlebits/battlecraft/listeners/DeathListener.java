package br.com.battlebits.battlecraft.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import br.com.battlebits.battlecraft.Main;

public class DeathListener implements Listener {

	private Main m;

	public DeathListener(Main m) {
		this.m = m;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onDeathStatus(PlayerDeathEvent event) {
		Player p = event.getEntity();
		if (m.getWarpManager().isInWarp(p, "challenge")) {
			String tag = ChatColor.RED + "" + ChatColor.BOLD + "LavaChallenge" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.RED;
			p.sendMessage(tag + "Voce falhou no Lava Challenge, porém suas mortes não são afetadas");
			String xpTag = ChatColor.YELLOW + "" + ChatColor.BOLD + "XP" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.WHITE;
			p.sendMessage(xpTag + "Você perdeu 1 XP por morrer");
			return;
		}
		Player killer = p.getKiller();
		if (killer != null) {
			killer.playSound(killer.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
			// TODO p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD +
			// "Voce morreu para: " + killer.getName());
			// TODO killer.sendMessage(ChatColor.GREEN.toString() +
			// ChatColor.BOLD + "Voce matou: " + p.getName());
			m.getStatusManager().updateStatus(killer, p);
			setKillRepair(killer);
		} else {
			// TODO p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD +
			// "Voce morreu!");
			m.getStatusManager().updateStatus(null, p);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeathTeleport(PlayerDeathEvent event) {
		Player p = event.getEntity();
		p.setHealth(20.0);
		p.setVelocity(new Vector());
		p.setLevel(0);
		p.closeInventory();
		event.setDroppedExp(0);
		event.getDrops().clear();
		dropItems(p, p.getLocation());
		new BukkitRunnable() {
			@Override
			public void run() {
				p.setFireTicks(0);
				if (m.getKitManager().hasKit(p)) {
					m.getKitManager().removeKit(p);
				}
				String warp = m.getWarpManager().getPlayerWarp(p);
				m.getWarpManager().removeWarp(p);
				if (warp == null || warp.equals("spawn")) {
					m.teleportSpawn(p);
					return;
				}
				if (warp != null) {
					m.getWarpManager().teleportWarp(p, warp, false);
					String tag = ChatColor.GREEN + "" + ChatColor.BOLD + "Respawn" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.RESET;
					String name = ChatColor.GRAY + "Você morreu e renasceu na Warp " + m.getWarpManager().getWarpByName(warp).getWarpName() + ".";
					p.sendMessage(tag + name);
				}
			}
		}.runTaskLater(m, 1);
	}

	private void setKillRepair(Player p) {
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
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		for (ItemStack item : p.getPlayer().getInventory().getContents())
			if (item != null && item.getType() != Material.AIR)
				items.add(item.clone());
		for (ItemStack item : p.getPlayer().getInventory().getArmorContents())
			if (item != null && item.getType() != Material.AIR)
				items.add(item.clone());
		if (p.getPlayer().getItemOnCursor() != null && p.getPlayer().getItemOnCursor().getType() != Material.AIR)
			items.add(p.getPlayer().getItemOnCursor().clone());
		dropItems(p, items, l);
	}

	@SuppressWarnings("deprecation")
	public void dropItems(Player p, List<ItemStack> items, Location l) {
		World world = l.getWorld();
		for (ItemStack item : items) {
			if (item == null || item.getType() == Material.AIR)
				continue;
			if (item.hasItemMeta())
				world.dropItemNaturally(l, item.clone()).getItemStack().setItemMeta(item.getItemMeta());
			else
				world.dropItemNaturally(l, item);
		}
		p.getPlayer().getInventory().setArmorContents(new ItemStack[4]);
		p.getPlayer().getInventory().clear();
		p.getPlayer().setItemOnCursor(new ItemStack(0));
		for (PotionEffect pot : p.getActivePotionEffects()) {
			p.removePotionEffect(pot.getType());
			break;
		}
	}

}
