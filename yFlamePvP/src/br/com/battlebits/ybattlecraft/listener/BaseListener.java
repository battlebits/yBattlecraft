package br.com.battlebits.ybattlecraft.listener;

import org.bukkit.event.Listener;

import br.com.battlebits.ybattlecraft.yBattleCraft;

public class BaseListener implements Listener {

	public yBattleCraft battleCraft;

	public BaseListener(yBattleCraft plugin) {
		this.battleCraft = plugin;
	}

}
