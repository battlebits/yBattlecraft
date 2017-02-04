package br.com.battlebits.ybattlecraft.base;

import org.bukkit.event.Listener;

import br.com.battlebits.ybattlecraft.Battlecraft;

public class BaseListener implements Listener {

	public Battlecraft battleCraft;

	public BaseListener(Battlecraft plugin) {
		this.battleCraft = plugin;
	}

}
