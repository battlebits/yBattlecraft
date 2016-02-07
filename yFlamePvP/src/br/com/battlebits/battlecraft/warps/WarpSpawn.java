package br.com.battlebits.battlecraft.warps;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.BaseWarp;
import br.com.battlebits.battlecraft.constructors.Warp;
import br.com.battlebits.battlecraft.events.RealMoveEvent;

public class WarpSpawn extends BaseWarp {

	public WarpSpawn(Main main) {
		super(main);
	}

	@EventHandler
	public void onReal(RealMoveEvent event) {
		Player p = event.getPlayer();
		Block above = p.getLocation().subtract(0, 0.1, 0).getBlock();
		if (above.getType() == Material.GRASS)
			if (getMain().getProtectionManager().removeProtection(p.getUniqueId())) {
				p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Proteção" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.GRAY + "Você perdeu proteção de spawn");
			}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (p.getGameMode() != GameMode.CREATIVE || !p.hasPermission("flame.build")) {
			event.setCancelled(true);
		}
	}

	@Override
	protected Warp getWarp() {
		Warp spawn = new Warp("Spawn", "Spawn do Servidor", new ItemStack(Material.NETHER_STAR), new Location(Bukkit.getWorld("spawnWarp"), 0.5, 87.5, 0.5, 180f, 0), 25.5);
		return spawn;
	}

}