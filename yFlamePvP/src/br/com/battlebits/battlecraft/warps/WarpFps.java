package br.com.battlebits.battlecraft.warps;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.BaseWarp;
import br.com.battlebits.battlecraft.constructors.Warp;
import br.com.battlebits.battlecraft.events.PlayerWarpJoinEvent;

public class WarpFps extends BaseWarp {

	public WarpFps(Main main) {
		super(main);
	}

	@EventHandler
	public void onWarpJoin(PlayerWarpJoinEvent event) {
		Player p = event.getPlayer();
		if (!isOnWarp(p))
			return;
		getMain().getKitManager().giveKit(p, "pvp");
	}

	@Override
	public Warp getWarp() {
		Warp fps = new Warp("FPS", "Utilize esta Warp feita com um mapa mais leve para aumentar seus FPSs", new ItemStack(Material.GLASS), new Location(Bukkit.getWorld("fpsWarp"), 0.5, 65.5, 0.5, 0, 0), 3.5);
		return fps;
	}

}
