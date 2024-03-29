package br.com.battlebits.ybattlecraft.listener;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.battlebits.commons.bukkit.event.update.UpdateEvent;
import br.com.battlebits.commons.bukkit.event.update.UpdateEvent.UpdateType;
import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.constructors.Warp;
import br.com.battlebits.ybattlecraft.event.RealMoveEvent;
import br.com.battlebits.ybattlecraft.manager.WarpManager;

public class MoveListener implements Listener {

	private Battlecraft m;
	private WarpManager manager;
	private HashMap<UUID, Location> locations;

	public MoveListener(Battlecraft Battlecraft) {
		m = Battlecraft;
		manager = Battlecraft.getWarpManager();
		locations = new HashMap<>();
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		locations.remove(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onUpdate(UpdateEvent event) {
		if (event.getType() != UpdateType.TICK)
			return;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (locations.containsKey(p.getUniqueId())) {
				Location from = locations.get(p.getUniqueId());
				if (from.getX() == p.getLocation().getX() && from.getZ() == p.getLocation().getZ()
						&& from.getY() == p.getLocation().getY() && from.distance(p.getLocation()) < 1)
					continue;
				m.getServer().getPluginManager().callEvent(new RealMoveEvent(p, from, p.getLocation()));
			}
			locations.put(p.getUniqueId(), p.getLocation());
		}
	}

	@EventHandler
	public void onRealMove(RealMoveEvent event) {
		Player p = event.getPlayer();
		Warp warp = manager.getWarpByName(manager.getPlayerWarp(p.getUniqueId()));
		if (!m.getProtectionManager().isProtected(p.getUniqueId()))
			return;
		if (warp == null)
			return;
		if (warp.getRadius() <= 0)
			return;
		if (event.getTo().getX() > warp.getWarpLocation().getX() + warp.getRadius()) {
			if (m.getProtectionManager().removeProtection(p.getUniqueId())) {
				p.sendMessage("§8§lPROTECAO §FVoce §7§lPERDEU§f sua protecao de spawn");
				m.getPlayerHideManager().showPlayer(p);
			}
			return;
		} else if (event.getTo().getZ() > warp.getWarpLocation().getZ() + warp.getRadius()) {
			if (m.getProtectionManager().removeProtection(p.getUniqueId())) {
				p.sendMessage("§8§lPROTECAO §FVoce §7§lPERDEU§f sua protecao de spawn");
				m.getPlayerHideManager().showPlayer(p);
			}
			return;
		} else if (event.getTo().getZ() < warp.getWarpLocation().getZ() - warp.getRadius()) {
			if (m.getProtectionManager().removeProtection(p.getUniqueId())) {
				p.sendMessage("§8§lPROTECAO §FVoce §7§lPERDEU§f sua protecao de spawn");
				m.getPlayerHideManager().showPlayer(p);
			}
			return;
		} else if (event.getTo().getX() < warp.getWarpLocation().getX() - warp.getRadius()) {
			if (m.getProtectionManager().removeProtection(p.getUniqueId())) {
				p.sendMessage("§8§lPROTECAO §FVoce §7§lPERDEU§f sua protecao de spawn");
				m.getPlayerHideManager().showPlayer(p);
			}
			return;
		}
	}
}
