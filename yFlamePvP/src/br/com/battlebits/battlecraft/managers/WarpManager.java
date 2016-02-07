package br.com.battlebits.battlecraft.managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.Warp;
import br.com.battlebits.battlecraft.events.PlayerWarpJoinEvent;
import br.com.battlebits.battlecraft.events.WarpTeleportEvent;
import br.com.battlebits.battlecraft.nms.Title;
import br.com.battlebits.battlecraft.nms.barapi.BarAPI;
import br.com.battlebits.battlecraft.utils.Formatter;

public class WarpManager {
	private static HashMap<UUID, String> WARPS = new HashMap<UUID, String>();
	private static HashMap<String, Warp> warps = new HashMap<>();
	private Main m;

	public WarpManager(Main m) {
		this.m = m;
	}

	public void teleportWarp(Player p, String warpName, boolean aviso) {
		p.closeInventory();
		Warp warp = getWarpByName(warpName);
		if (warp == null) {
			p.sendMessage(ChatColor.RED + "Esta warp nao existe");
			return;
		}
		if(warp.getWarpLocation() == null) {
			p.sendMessage(ChatColor.RED + "Esta warp está desabilitada para manutenção");
			return;
		}
		WarpTeleportEvent event = new WarpTeleportEvent(p, warp, aviso);
		m.getServer().getPluginManager().callEvent(event);
		if (event.isCanceled())
			return;
		m.getKitManager().removeKit(p);
		for (PotionEffect potion : p.getActivePotionEffects()) {
			p.removePotionEffect(potion.getType());
		}
		String tag = ChatColor.BLUE + "" + ChatColor.BOLD + "Teleporte" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.RESET;
		String name = ChatColor.GRAY + "Teleportado para " + warp.getWarpName();
		p.teleport(warp.getWarpLocation());
		p.sendMessage(tag + name);
		if (event.hasAviso()) {
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
		PlayerWarpJoinEvent joinWarp = new PlayerWarpJoinEvent(p, warp);
		Bukkit.getPluginManager().callEvent(joinWarp);
		WARPS.put(p.getUniqueId(), warpName);
	}

	public boolean isInWarp(Player p, String warp) {
		return hasWarp(p) && WARPS.get(p.getUniqueId()).contains(warp);
	}

	public String getPlayerWarp(Player p) {
		return WARPS.get(p.getUniqueId());
	}

	public Warp getWarpByName(String warpName) {
		return warps.get(warpName);
	}

	public boolean hasWarp(Player p) {
		return WARPS.containsKey(p.getUniqueId());
	}

	public void removeWarp(Player p) {
		if (WARPS.containsKey(p.getUniqueId()))
			WARPS.remove(p.getUniqueId());
	}

	public Collection<Warp> getWarps() {
		return warps.values();
	}

	public void addWarp(Warp warp) {
		warps.put(warp.getWarpName().toLowerCase().trim(), warp);
	}
}
