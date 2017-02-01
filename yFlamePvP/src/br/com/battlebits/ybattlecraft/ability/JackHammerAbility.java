package br.com.battlebits.ybattlecraft.ability;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.commons.api.item.ItemBuilder;
import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseAbility;

public class JackHammerAbility extends BaseAbility {

	private HashMap<UUID, Integer> uses;
	private ItemStack axe;

	public JackHammerAbility(yBattleCraft bc) {
		super(bc);
		uses = new HashMap<>();
		axe = new ItemBuilder().amount(1).type(Material.STONE_AXE).glow().name("§7JackHammer").build();
		getItens().add(axe);
	}

	@EventHandler
	public void onBlockBreakListener(BlockBreakEvent e) {
		if (e.getPlayer().getItemInHand() != null) {
			if (e.getPlayer().getItemInHand().equals(axe)) {
				if (isUsing(e.getPlayer())) {
					if (e.getBlock().getLocation().distance(battlecraft.getWarpManager()
							.getWarpByName(battlecraft.getWarpManager().getPlayerWarp(e.getPlayer().getUniqueId())).getWarpLocation()) > 30) {
						if (!battlecraft.getCooldownManager().isOnCooldown(e.getPlayer().getUniqueId(), "jackhammerability")) {
							if (!uses.containsKey(e.getPlayer().getUniqueId())) {
								uses.put(e.getPlayer().getUniqueId(), 0);
							}
							if (uses.get(e.getPlayer().getUniqueId()) <= 5) {
								uses.put(e.getPlayer().getUniqueId(), uses.get(e.getPlayer().getUniqueId()) + 1);
							}
							if (uses.get(e.getPlayer().getUniqueId()) > 5) {
								if (!battlecraft.getCooldownManager().hasCooldown(e.getPlayer().getUniqueId(), "jackhammerability")) {
									battlecraft.getCooldownManager().setCooldown(e.getPlayer().getUniqueId(), "jackhammerability", 10);
								}
								uses.put(e.getPlayer().getUniqueId(), 0);
							}
							battlecraft.getBlockResetManager().addBlockToReset(e.getBlock().getLocation(), 7000);
							e.getBlock().setType(Material.AIR);
							double direction = e.getPlayer().getLocation().getDirection().getY();
							if(direction < 0.14){
								new BukkitRunnable() {
									Location loc = e.getBlock().getLocation().clone();
									@Override
									public void run() {
										loc.setY(loc.getY() - 1);
										if(!(loc.getBlock().getType() == Material.AIR || loc.getBlock().getType() == Material.BEDROCK)){
											new BukkitRunnable() {
												@Override
												public void run() {
													battlecraft.getBlockResetManager().addBlockToReset(loc, 7000);
													loc.getBlock().setType(Material.AIR);
												}
											}.runTask(battlecraft);
										} else {
											Bukkit.broadcastMessage("STOPED");
											cancel();
										}
									}
								}.runTaskTimerAsynchronously(battlecraft, 1L, 1L);
							} else {
								new BukkitRunnable() {
									Location loc = e.getBlock().getLocation().clone();
									@Override
									public void run() {
										loc.setY(loc.getY() + 1);
										if(!(loc.getBlock().getType() == Material.AIR || loc.getBlock().getType() == Material.BEDROCK)){
											new BukkitRunnable() {
												@Override
												public void run() {
													battlecraft.getBlockResetManager().addBlockToReset(loc, 7000);
													loc.getBlock().setType(Material.AIR);
												}
											}.runTask(battlecraft);
										} else {
											Bukkit.broadcastMessage("STOPED");
											cancel();
										}
									}
								}.runTaskTimerAsynchronously(battlecraft, 1L, 1L);
							}
//							new BukkitRunnable() {
//								Block blocku = e.getBlock().getRelative(BlockFace.UP);
//								Block blockd = e.getBlock().getRelative(BlockFace.DOWN);
//
//								public void run() {
//									if (pY < 0.14) {
//										if (blockd.getType() != Material.BEDROCK || blockd.getType() != Material.AIR) {
//											battlecraft.getBlockResetManager().addBlockToReset(blockd.getLocation(), 7000);
//											blockd.setType(Material.AIR);
//											blockd = blockd.getRelative(BlockFace.DOWN);
//										} else {
//											Bukkit.broadcastMessage("Stop");
//											cancel();
//										}
//									} else {
//										if (blocku.getType() != Material.BEDROCK || blocku.getType() != Material.AIR) {
//											battlecraft.getBlockResetManager().addBlockToReset(blocku.getLocation(), 7000);
//											blocku.setType(Material.AIR);
//											blocku = blocku.getRelative(BlockFace.UP);
//										} else {
//											Bukkit.broadcastMessage("Stop");
//											cancel();
//										}
//									}
//								}
//							}.runTaskTimer(battlecraft, 1, 1);

						} else {
							e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
							e.getPlayer().sendMessage("§5§lJackHammer §8§l>> §7Aguarde mais "
									+ battlecraft.getCooldownManager().getCooldownTimeFormated(e.getPlayer().getUniqueId(), "jackhammerability")
									+ " para utilizar sua habilidade!");
							e.setCancelled(true);
						}
					} else {
						e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
						e.getPlayer().sendMessage("§5§lJackHammer §8§l>> §7Esta area está protegida!");
						e.setCancelled(true);
					}
				}
			}
		}
	}

}
