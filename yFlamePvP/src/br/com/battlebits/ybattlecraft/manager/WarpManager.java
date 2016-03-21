package br.com.battlebits.ybattlecraft.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.constructors.Warp;
import br.com.battlebits.ybattlecraft.event.PlayerWarpJoinEvent;
import br.com.battlebits.ybattlecraft.event.WarpTeleportEvent;
import br.com.battlebits.ybattlecraft.nms.Title;
import br.com.battlebits.ybattlecraft.nms.barapi.BarAPI;
import br.com.battlebits.ybattlecraft.utils.Formatter;

public class WarpManager {

	private HashMap<UUID, String> playerWarp;
	private HashMap<String, Warp> nameWarp;
	private yBattleCraft battleCraft;

	public WarpManager(yBattleCraft plugin) {
		this.battleCraft = plugin;
		this.playerWarp = new HashMap<>();
		this.nameWarp = new HashMap<>();
	}

	public void teleportWarp(Player p, String warpName, boolean aviso) {
		p.closeInventory();
		new BukkitRunnable() {
			@Override
			public void run() {
				p.setFireTicks(0);
				p.setVelocity(new Vector());
			}
		}.runTaskLaterAsynchronously(battleCraft, 0);
		Warp warp = getWarpByName(warpName);
		if (warp == null) {
			p.sendMessage(ChatColor.RED + "Esta warp nao existe");
			return;
		}
		if (warp.getWarpLocation() == null) {
			p.sendMessage(ChatColor.RED + "Esta warp está desabilitada para manutenção");
			return;
		}
		WarpTeleportEvent event = new WarpTeleportEvent(p, warp, aviso);
		battleCraft.getServer().getPluginManager().callEvent(event);
		if (event.isCanceled()) {
			return;
		}
		battleCraft.getKitManager().removeKit(p);
		for (PotionEffect potion : p.getActivePotionEffects()) {
			p.removePotionEffect(potion.getType());
		}
		if (event.hasAviso()) {
			String tag = ChatColor.BLUE + "" + ChatColor.BOLD + "Teleporte" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> "
					+ ChatColor.RESET;
			String name = ChatColor.GRAY + "Teleportado para " + warp.getWarpName();
			p.sendMessage(tag + name);
			BarAPI.setMessage(p, name, 5);
			Title title = new Title(ChatColor.YELLOW + "Warp " + Formatter.getFormattedName(warp.getWarpName()));
			title.setSubtitle("Você foi teleportado");
			title.setTimingsToTicks();
			title.setFadeInTime(10);
			title.setStayTime(40);
			title.setFadeOutTime(10);
			title.send(p);
			p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
		}
		p.teleport(warp.getWarpLocation());
		PlayerWarpJoinEvent joinWarp = new PlayerWarpJoinEvent(p, warp);
		Bukkit.getPluginManager().callEvent(joinWarp);
		playerWarp.put(p.getUniqueId(), warpName);
	}

	public boolean isInWarp(Player p, String warp) {
		return hasWarp(p) && playerWarp.get(p.getUniqueId()).contains(warp);
	}

	public String getPlayerWarp(UUID id) {
		if (!playerWarp.containsKey(id)) {
			return "spawn";
		}
		return playerWarp.get(id);
	}

	public Warp getWarpByName(String warpName) {
		try {
			return nameWarp.get(warpName.toLowerCase().trim());
		} catch (Exception e) {
			return nameWarp.get("spawn");
		}
	}

	public boolean hasWarp(Player p) {
		return playerWarp.containsKey(p.getUniqueId());
	}

	public void removeWarp(Player p) {
		if (playerWarp.containsKey(p.getUniqueId())) {
			playerWarp.remove(p.getUniqueId());
		}
	}

	public ArrayList<UUID> getPlayersInWarp(String name) {
		ArrayList<UUID> ids = new ArrayList<>();
		for (Entry<UUID, String> entry : playerWarp.entrySet()) {
			if (entry.getValue().equalsIgnoreCase(name)) {
				ids.add(entry.getKey());
			}
		}
		return ids;
	}

	public Collection<Warp> getWarps() {
		return nameWarp.values();
	}

	public void addWarp(Warp warp) {
		nameWarp.put(warp.getWarpName().toLowerCase().trim(), warp);
	}
}
