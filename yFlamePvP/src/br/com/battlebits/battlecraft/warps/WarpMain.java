package br.com.battlebits.battlecraft.warps;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.BaseWarp;
import br.com.battlebits.battlecraft.constructors.Warp;
import br.com.battlebits.battlecraft.events.PlayerWarpJoinEvent;

public class WarpMain extends BaseWarp {

	public WarpMain(Main main) {
		super(main);
	}

	@EventHandler
	public void onWarpJoin(PlayerWarpJoinEvent event) {
		Player p = event.getPlayer();
		if (!isOnWarp(p))
			return;
		getMain().getKitManager().giveKit(p, "pvp");
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 1));
		p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999, 0));
	}

	@Override
	protected Warp getWarp() {
		Warp main = new Warp("Main", "Treine seu PvP com poções de força e velocidade", new ItemStack(Material.POTION), null, 10.5);
		return main;
	}

}
