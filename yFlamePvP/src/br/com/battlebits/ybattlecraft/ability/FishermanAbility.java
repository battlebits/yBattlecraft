package br.com.battlebits.ybattlecraft.ability;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseAbility;
import br.com.battlebits.ybattlecraft.builder.ItemBuilder;

public class FishermanAbility extends BaseAbility {

	private ItemStack item;

	public FishermanAbility(yBattleCraft yBattleCraft) {
		super(yBattleCraft);
		item = new ItemBuilder().amount(1).type(Material.FISHING_ROD).name("§bFisherman").build();
		getItens().add(item);
	}

	@EventHandler
	public void onPlayerFishListener(PlayerFishEvent e) {
		if (e.getState() == State.CAUGHT_ENTITY) {
			if (isUsing(e.getPlayer())) {
				if (battlecraft.getProtectionManager().removeProtection(e.getPlayer().getUniqueId())) {
					e.getPlayer().sendMessage("§7§lPROTEÇÃO §FVocê §8§lPERDEU§f sua proteção de spawn");
				}
				Entity c = (Entity) e.getCaught();
				if (c instanceof Player) {
					Player p = (Player) c;
					if (battlecraft.getProtectionManager().isProtected(p.getUniqueId())) {
						e.getPlayer().sendMessage("§7§lPROTEÇÃO §fEste jogador §8§lPOSSUI §7proteção de spawn");
						e.setCancelled(true);
						return;
					}
				}
				World w = e.getPlayer().getLocation().getWorld();
				double x = e.getPlayer().getLocation().getBlockX() + 0.5D;
				double y = e.getPlayer().getLocation().getBlockY();
				double z = e.getPlayer().getLocation().getBlockZ() + 0.5D;
				float yaw = c.getLocation().getYaw();
				float pitch = c.getLocation().getPitch();
				Location loc = new Location(w, x, y, z, yaw, pitch);
				c.teleport(loc);
				e.getPlayer().updateInventory();
			}
		}
	}

}
