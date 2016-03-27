package br.com.battlebits.ybattlecraft.warps;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseWarp;
import br.com.battlebits.ybattlecraft.constructors.Warp;
import br.com.battlebits.ybattlecraft.event.PlayerDamagePlayerEvent;
import br.com.battlebits.ybattlecraft.event.PlayerWarpJoinEvent;

public class WarpLavaChallenge extends BaseWarp {

	public WarpLavaChallenge(yBattleCraft yBattleCraft) {
		super(yBattleCraft);
	}

	@EventHandler
	public void onDamage(PlayerDamagePlayerEvent event) {
		Player p1 = event.getDamager();
		Player p2 = event.getDamaged();
		if (isOnWarp(p1) || isOnWarp(p2)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onVelocity(PlayerVelocityEvent e){
		if(isOnWarp(e.getPlayer())){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onTeleport(PlayerWarpJoinEvent event) {
		if (!event.getWarp().getWarpName().toLowerCase().contains("lava"))
			return;
		Player p = event.getPlayer();
		getMain().getProtectionManager().removeProtection(p.getUniqueId());
		p.getInventory().setItem(0, new ItemStack(Material.STONE_SWORD));
		p.getInventory().setItem(14, new ItemStack(Material.RED_MUSHROOM, 64));
		p.getInventory().setItem(15, new ItemStack(Material.BOWL, 64));
		p.getInventory().setItem(16, new ItemStack(Material.BROWN_MUSHROOM, 64));
		for (ItemStack i : p.getInventory().getContents()) {
			if (i == null)
				p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));
		}
		//TODO: GIVE KIT
//		getMain().getKitManager().setForcedKit(p, "Lava Challenge");
	}
	
	@Override
	protected Warp getWarp(yBattleCraft battleCraft) {
		Warp lavachallenge = new Warp("Lava Challenge", "Treine seus refils e seus recrafts para ser o melhor no pvp",
				new ItemStack(Material.LAVA_BUCKET), new Location(Bukkit.getWorld("lavachallengeWarp"), 0.5, 66.5, 0.5, 90 * 2f, 0f), false);
		return lavachallenge;
	}

}