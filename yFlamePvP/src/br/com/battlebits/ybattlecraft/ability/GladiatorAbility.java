package br.com.battlebits.ybattlecraft.ability;

import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.commons.api.item.ItemBuilder;
import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseAbility;
import br.com.battlebits.ybattlecraft.fight.gladiator.GladiatorFight;

public class GladiatorAbility extends BaseAbility {

	private ItemStack startBattle;

	public GladiatorAbility(yBattleCraft bc) {
		super(bc);
		startBattle = new ItemBuilder().amount(1).type(Material.IRON_FENCE).name("§c§lIniciar Batalha").glow().build();
		getItens().add(startBattle);
	}

	@EventHandler
	public void onPlayerInteractEntityListener(PlayerInteractEntityEvent e) {
		if (e.getPlayer().getItemInHand() != null) {
			if (e.getPlayer().getItemInHand().equals(startBattle)) {
				if (isUsing(e.getPlayer())) {
					if (e.getRightClicked() instanceof Player) {
						Player t = (Player) e.getRightClicked();
						if (!battlecraft.getProtectionManager().isProtected(t.getUniqueId())) {
							if(!battlecraft.getGladiatorFightController().isInFight(e.getPlayer())){
								if(!battlecraft.getGladiatorFightController().isInFight(t)){
									new GladiatorFight(e.getPlayer(), t, battlecraft);
								} else {
									e.getPlayer().sendMessage("§5§lGLADIATOR §fEste jogador já está em §9§lBATALHA.");
								}
							} else {
								e.getPlayer().sendMessage("§5§lGLADIATOR §fVocê já esta em §9§lBATALHA.");
							}
						} else {
							e.getPlayer().sendMessage("§5§lGLADIATOR §fEste jogador está com proteção de §9§lSPAWN§f.");
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerInteractListener(PlayerInteractEvent e) {
		if (e.getAction() != Action.PHYSICAL && isUsing(e.getPlayer()) && e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().equals(startBattle)) {
			e.getPlayer().updateInventory();
			e.setCancelled(true);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlock(BlockDamageEvent event) {
		if (battlecraft.getGladiatorFightController().isFightBlock(event.getBlock())) {
			final Block b = event.getBlock();
			final Player p = event.getPlayer();
			p.sendBlockChange(b.getLocation(), Material.BEDROCK, (byte) 0);
		}
	}

	@EventHandler
	public void onExplode(EntityExplodeEvent event) {
		Iterator<Block> blockIt = event.blockList().iterator();
		while (blockIt.hasNext()) {
			Block b = blockIt.next();
			if (battlecraft.getGladiatorFightController().isFightBlock(b)) {
				blockIt.remove();
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (battlecraft.getGladiatorFightController().isFightBlock(event.getBlock())) {
			event.setCancelled(true);
		}
	}

}
