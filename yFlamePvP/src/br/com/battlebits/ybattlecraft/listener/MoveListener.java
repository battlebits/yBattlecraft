package br.com.battlebits.ybattlecraft.listener;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.constructors.Warp;
import br.com.battlebits.ybattlecraft.event.RealMoveEvent;
import br.com.battlebits.ybattlecraft.manager.WarpManager;
import net.md_5.bungee.api.ChatColor;

public class MoveListener implements Listener {

	private yBattleCraft m;
	private WarpManager manager;
	private HashMap<UUID, Location> locations;

	public MoveListener(yBattleCraft yBattleCraft) {
		m = yBattleCraft;
		manager = yBattleCraft.getWarpManager();
		locations = new HashMap<>();
		startUpdater();
	}

	public void startUpdater() {
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (locations.containsKey(p.getUniqueId())) {
						Location from = locations.get(p.getUniqueId());
						if (from.getX() == p.getLocation().getX() && from.getZ() == p.getLocation().getZ() && from.getY() == p.getLocation().getY() && from.distance(p.getLocation()) < 1)
							continue;
						m.getServer().getPluginManager().callEvent(new RealMoveEvent(p, from, p.getLocation()));
					}
					locations.put(p.getUniqueId(), p.getLocation());
				}
			}
		}.runTaskTimerAsynchronously(m, 0, 0);
	}

	// @EventHandler
	// public void onPlayerMove(PlayerMoveEvent e){
	// if(!(e.getFrom().getBlockX() == e.getTo().getBlockX()) &&
	// e.getFrom().getBlockY() == e.getTo().getBlockY() &&
	// e.getFrom().getBlockZ() == e.getTo().getBlockZ()){
	// Bukkit.getServer().getPluginManager().callEvent(new
	// RealMoveEvent(e.getPlayer(), e.getFrom(), e.getTo()));
	// }
	// }

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		locations.remove(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onRealMove(RealMoveEvent event) {
		Player p = event.getPlayer();
		Warp warp = manager.getWarpByName(manager.getPlayerWarp(p.getUniqueId()));
		if (warp == null)
			return;
		if (warp.getRadius() <= 0)
			return;
		if (event.getTo().getX() > warp.getWarpLocation().getX() + warp.getRadius()) {
			if (m.getProtectionManager().removeProtection(p.getUniqueId())) {
				p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Proteção" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.GRAY + "Você perdeu proteção de spawn");
			}
			return;
		} else if (event.getTo().getZ() > warp.getWarpLocation().getZ() + warp.getRadius()) {
			if (m.getProtectionManager().removeProtection(p.getUniqueId())) {
				p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Proteção" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.GRAY + "Você perdeu proteção de spawn");
			}
			return;
		} else if (event.getTo().getZ() < warp.getWarpLocation().getZ() - warp.getRadius()) {
			if (m.getProtectionManager().removeProtection(p.getUniqueId())) {
				p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Proteção" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.GRAY + "Você perdeu proteção de spawn");
			}
			return;
		} else if (event.getTo().getX() < warp.getWarpLocation().getX() - warp.getRadius()) {
			if (m.getProtectionManager().removeProtection(p.getUniqueId())) {
				p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Proteção" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.GRAY + "Você perdeu proteção de spawn");
			}
			return;
		}
	}
}
