package br.com.battlebits.ybattlecraft.evento.constructors;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Cabine {
	private Location location;
	private Player player;

	public Cabine(Player p, Location loc) {
		this.player = p;
		this.location = loc;
	}

	public Location getLocation() {
		return location;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
