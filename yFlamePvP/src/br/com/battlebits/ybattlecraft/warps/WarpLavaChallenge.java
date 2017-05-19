package br.com.battlebits.ybattlecraft.warps;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.commons.BattlebitsAPI;
import br.com.battlebits.commons.core.account.BattlePlayer;
import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.base.BaseWarp;
import br.com.battlebits.ybattlecraft.constructors.Warp;
import br.com.battlebits.ybattlecraft.constructors.WarpScoreboard;
import br.com.battlebits.ybattlecraft.event.PlayerDamagePlayerEvent;
import br.com.battlebits.ybattlecraft.event.PlayerWarpJoinEvent;

public class WarpLavaChallenge extends BaseWarp {

	public WarpLavaChallenge(Battlecraft Battlecraft) {
		super(Battlecraft);
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
	public void onVelocity(PlayerVelocityEvent e) {
		if (isOnWarp(e.getPlayer())) {
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
		// TODO: GIVE KIT
		// getMain().getKitManager().setForcedKit(p, "Lava Challenge");
	}

	@Override
	protected Warp getWarp(Battlecraft battleCraft) {
		Warp lavachallenge = new Warp("Lava Challenge", "Treine seus refils e seus recrafts para ser o melhor no pvp",
				new ItemStack(Material.LAVA_BUCKET),
				new Location(Bukkit.getWorld("lavachallengeWarp"), 0.5, 66.5, 0.5, 90 * 2f, 0f), false,
				new WarpScoreboard("lava") {
					@Override
					public void createScores(Player p) {
						BattlePlayer a = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
						createScore(p, "b2", "", "", 5);
						createScore(p, "xp", "§7XP: ", "§b" + a.getXp(), 4);
						createScore(p, "liga", "§7Liga: ", a.getLeague().getSymbol() + " " + a.getLeague().toString(),
								3);
						createScore(p, "b1", "", "", 2);
						createScore(p, "site", "§6www.battle", "§6bits.com.br", 1);
					}
				}, false, false);
		return lavachallenge;
	}

}