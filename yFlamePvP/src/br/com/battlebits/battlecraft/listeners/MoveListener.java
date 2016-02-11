package br.com.battlebits.battlecraft.listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.Warp;
import br.com.battlebits.battlecraft.events.RealMoveEvent;
import br.com.battlebits.battlecraft.managers.WarpManager;
import me.flame.utils.event.UpdateEvent;
import net.md_5.bungee.api.ChatColor;

public class MoveListener implements Listener {

	private Main m;
	private WarpManager manager;
	private HashMap<UUID, Location> locations;

	public MoveListener(Main main) {
		this.m = main;
		this.manager = main.getWarpManager();
		this.locations = new HashMap<>();
	}

	@EventHandler
	public void onUpdate(UpdateEvent event) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (locations.containsKey(p.getUniqueId())) {
				Location from = locations.get(p.getUniqueId());
				if (from.getX() == p.getLocation().getX() && from.getZ() == p.getLocation().getZ() && from.getY() == p.getLocation().getY())
					continue;
				m.getServer().getPluginManager().callEvent(new RealMoveEvent(p, from, p.getLocation()));
			}
			locations.put(p.getUniqueId(), p.getLocation());
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		locations.remove(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onRealMove(RealMoveEvent event) {
		Player p = event.getPlayer();
		Warp warp = manager.getWarpByName(manager.getPlayerWarp(p));
		if (warp == null)
			return;
		if (event.getTo().getX() > warp.getWarpLocation().getX() + warp.getRadius()) {
			if (m.getProtectionManager().removeProtection(p.getUniqueId())) {
				p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Prote��o" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.GRAY + "Voc� perdeu prote��o de spawn");
			}
			return;
		} else if (event.getTo().getZ() > warp.getWarpLocation().getZ() + warp.getRadius()) {
			if (m.getProtectionManager().removeProtection(p.getUniqueId())) {
				p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Prote��o" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.GRAY + "Voc� perdeu prote��o de spawn");
			}
			return;
		} else if (event.getTo().getZ() < warp.getWarpLocation().getZ() - warp.getRadius()) {
			if (m.getProtectionManager().removeProtection(p.getUniqueId())) {
				p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Prote��o" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.GRAY + "Voc� perdeu prote��o de spawn");
			}
			return;
		} else if (event.getTo().getX() < warp.getWarpLocation().getX() - warp.getRadius()) {
			if (m.getProtectionManager().removeProtection(p.getUniqueId())) {
				p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Prote��o" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.GRAY + "Voc� perdeu prote��o de spawn");
			}
			return;
		}
	}
}
