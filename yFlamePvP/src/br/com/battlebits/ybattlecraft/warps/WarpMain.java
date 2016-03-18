package br.com.battlebits.ybattlecraft.warps;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.constructors.BaseWarp;
import br.com.battlebits.ybattlecraft.constructors.Warp;
import br.com.battlebits.ybattlecraft.event.PlayerWarpJoinEvent;

public class WarpMain extends BaseWarp {

	public WarpMain(yBattleCraft yBattleCraft) {
		super(yBattleCraft);
	}

	@EventHandler
	public void onWarpJoin(PlayerWarpJoinEvent event) {
		Player p = event.getPlayer();
		if (!isOnWarp(p))
			return;
		//TODO: GIVE KIT
//		getMain().getKitManager().giveKit(p, "pvp", false);
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 1));
		p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999, 0));
	}

	@Override
	protected Warp getWarp(yBattleCraft battleCraft) {
		Warp main = new Warp("Main", "Treine seu PvP com poções de força e velocidade", new ItemStack(Material.POTION), null, 10.5);
		return main;
	}

}
