package br.com.battlebits.ybattlecraft.base;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.constructors.Warp;
import br.com.battlebits.ybattlecraft.event.PlayerWarpJoinEvent;

public abstract class BaseWarp implements Listener {
	
	private List<UUID> playersInWarp;
	private Warp warp;
	public Battlecraft Battlecraft;

	public BaseWarp(Battlecraft Battlecraft) {
		playersInWarp = new ArrayList<>();
		warp = getWarp(Battlecraft);
		this.Battlecraft = Battlecraft;
		Battlecraft.getWarpManager().addWarp(warp);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onWarp(PlayerWarpJoinEvent event) {
		Player p = event.getPlayer();
		if (event.getWarp() == this.warp) {
			if (!isOnWarp(p))
				playersInWarp.add(p.getUniqueId());
		} else {
			if (isOnWarp(p))
				playersInWarp.remove(p.getUniqueId());
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (!playersInWarp.contains(p.getUniqueId()))
			return;
		playersInWarp.remove(p.getUniqueId());
	}

	public boolean isOnWarp(Player p) {
		return playersInWarp.contains(p.getUniqueId());
	}

	public Battlecraft getMain() {
		return Battlecraft;
	}

	protected List<Player> getPlayersInWarp() {
		List<Player> players = new ArrayList<>();
		List<UUID> toRemove = new ArrayList<>();
		for (UUID uuid : playersInWarp) {
			Player p = Bukkit.getPlayer(uuid);
			if (p == null)
				toRemove.add(uuid);
			else
				players.add(p);
		}
		for (UUID uuid : toRemove) {
			playersInWarp.remove(uuid);
		}
		return players;
	}

	protected abstract Warp getWarp(Battlecraft pl);

}
