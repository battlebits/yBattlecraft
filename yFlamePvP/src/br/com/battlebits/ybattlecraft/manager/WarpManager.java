package br.com.battlebits.ybattlecraft.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
			p.sendMessage("§9§lTELEPORTE §fEsta warp esta em §3§lMANUTENÇãO");
			return;
		}
		WarpTeleportEvent event = new WarpTeleportEvent(p, warp, aviso);
		battleCraft.getServer().getPluginManager().callEvent(event);
		if (event.isCanceled()) {
			return;
		}
		battleCraft.getStatusManager().getStatusByUuid(p.getUniqueId()).death();
		battleCraft.getKitManager().removeKit(p);
		for (PotionEffect potion : p.getActivePotionEffects()) {
			p.removePotionEffect(potion.getType());
		}
		if (event.hasAviso()) {
			p.sendMessage("§9§lTELEPORTE §fVocê foi teleportado para §3§l" + warp.getWarpName());
			BarAPI.setMessage(p, "§fVocê foi teleportado para §3§l" + warp.getWarpName(), 5);
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
		return hasWarp(p) && playerWarp.get(p.getUniqueId()).equalsIgnoreCase(warp);
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
		for (Player p : getWarpByName(name).getWarpLocation().getWorld().getEntitiesByClass(Player.class)) {
			if (isInWarp(p, name)) {
				ids.add(p.getUniqueId());
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
