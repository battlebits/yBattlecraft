package br.com.battlebits.ybattlecraft.ability;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
		item = new ItemBuilder().amount(1).type(Material.FISHING_ROD).name("§bFisherman").glow().build();
		getItens().add(item);
	}

	@EventHandler
	public void onPlayerFishListener(PlayerFishEvent e) {
		if (e.getState() == State.CAUGHT_ENTITY) {
			if (isUsing(e.getPlayer())) {
				if (battlecraft.getProtectionManager().removeProtection(e.getPlayer().getUniqueId())) {
					e.getPlayer().sendMessage("§8§lPROTEÇÃO §FVocê §7§lPERDEU§f sua proteção de spawn");
				}
				if (e.getCaught() instanceof Player) {
					Player c = (Player) e.getCaught();
					World w = e.getPlayer().getLocation().getWorld();
					double x = e.getPlayer().getLocation().getBlockX() + 0.5D;
					double y = e.getPlayer().getLocation().getBlockY();
					double z = e.getPlayer().getLocation().getBlockZ() + 0.5D;
					float yaw = c.getLocation().getYaw();
					float pitch = c.getLocation().getPitch();
					Location loc = new Location(w, x, y, z, yaw, pitch);
					c.sendMessage("§5§lFISHERMAN §9§l" + e.getPlayer().getName() + "§f puxou você!");
					c.teleport(loc);
					e.getPlayer().getItemInHand().setDurability((short) -e.getPlayer().getItemInHand().getType().getMaxDurability());
					e.getPlayer().updateInventory();
				}
			}
		}
	}

}
