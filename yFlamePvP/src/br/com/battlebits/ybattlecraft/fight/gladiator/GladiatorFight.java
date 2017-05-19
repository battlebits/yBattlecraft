package br.com.battlebits.ybattlecraft.fight.gladiator;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.event.PlayerDamagePlayerEvent;
import br.com.battlebits.ybattlecraft.event.PlayerDeathInWarpEvent;
import br.com.battlebits.ybattlecraft.event.PlayerWarpJoinEvent;

public class GladiatorFight {

	private Battlecraft battleCraft;
	private Player gladiator;
	private Player target;
	private Location tpLocGladiator;
	private Location tpLocTarget;
	private BukkitRunnable witherEffect;
	private BukkitRunnable teleportBack;
	private List<Block> blocksToRemove;
	private Listener listener;
	private boolean ended;

	public GladiatorFight(final Player gladiator, final Player target, Battlecraft bc) {
		battleCraft = bc;
		this.gladiator = gladiator;
		this.target = target;
		this.blocksToRemove = new ArrayList<Block>();
		send1v1();
		ended = false;
		listener = new Listener() {

			@EventHandler
			public void onEntityDamage(PlayerDamagePlayerEvent e) {
				if ((isIn1v1(e.getDamaged()) || isIn1v1(e.getDamager())) && (!(isIn1v1(e.getDamaged()) && isIn1v1(e.getDamager())))) {
					e.setCancelled(true);
				}
			}

			@EventHandler(priority = EventPriority.MONITOR)
			public void onEntityDamage(PlayerWarpJoinEvent e) {
				if (isIn1v1(e.getPlayer())) {
					if (!ended) {
						ended = true;
						if (e.getPlayer() == gladiator) {
							// target winner
							target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
							battleCraft.getItemManager().dropItems(e.getPlayer(), tpLocGladiator);
							battleCraft.getStatusManager().updateStatus(target, gladiator);
							teleportBack(target, gladiator);
							return;
						}
						// gladiator winner
						gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
						battleCraft.getItemManager().dropItems(e.getPlayer(), tpLocTarget);
						battleCraft.getStatusManager().updateStatus(gladiator, target);
						teleportBack(gladiator, target);
					}
				}
			}

			@EventHandler(priority = EventPriority.LOWEST)
			public void onDeath(PlayerDeathInWarpEvent e) {
				if (isIn1v1(e.getPlayer())) {
					if (!ended) {
						ended = true;
						if (e.getPlayer() == gladiator) {
							// target winner
							target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
							battleCraft.getItemManager().dropItems(e.getPlayer(), tpLocGladiator);
							teleportBack(target, gladiator);
							return;
						}
						// gladiator winner
						gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
						battleCraft.getItemManager().dropItems(e.getPlayer(), tpLocTarget);
						teleportBack(gladiator, target);
					}
				}
			}

			@EventHandler
			public void onQuit(PlayerQuitEvent e) {
				Player p = e.getPlayer();
				if (!isIn1v1(p))
					return;
				if (e.getPlayer().isDead())
					return;
				if (!ended) {
					ended = true;
					if (p == gladiator) {
						// target winner
						target.sendMessage("§5§LGLADIATOR §f" + gladiator.getName() + " deslogou! Voce §9§lGANHOU§f a batalha!");
						target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
						battleCraft.getItemManager().dropItems(p, tpLocGladiator);
						battleCraft.getStatusManager().updateStatus(target, gladiator);
						teleportBack(target, gladiator);
						return;
					}
					// gladiator winner
					battleCraft.getStatusManager().updateStatus(gladiator, target);
					gladiator.sendMessage("§5§LGLADIATOR §f" + target.getName() + " deslogou! Voce §9§lGANHOU§f a batalha!");
					gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
					battleCraft.getItemManager().dropItems(p, tpLocTarget);
					teleportBack(gladiator, target);
				}
			}

			@EventHandler
			public void onKick(PlayerKickEvent event) {
				Player p = event.getPlayer();
				if (!isIn1v1(p))
					return;
				if (event.getPlayer().isDead())
					return;
				if (!ended) {
					ended = true;
					if (p == gladiator) {
						// target winner
						target.sendMessage("§5§LGLADIATOR §f" + gladiator.getName() + " deslogou! Voce §9§lGANHOU§f a batalha!");
						target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
						battleCraft.getItemManager().dropItems(p, tpLocGladiator);
						teleportBack(target, gladiator);
						battleCraft.getStatusManager().updateStatus(target, gladiator);
						return;
					}
					// gladiator winner
					battleCraft.getStatusManager().updateStatus(gladiator, target);
					gladiator.sendMessage("§5§LGLADIATOR §f" + target.getName() + " deslogou! Voce §9§lGANHOU§f a batalha!");
					gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
					battleCraft.getItemManager().dropItems(p, tpLocTarget);
					teleportBack(gladiator, target);
				}
			}
		};
		battleCraft.getServer().getPluginManager().registerEvents(listener, battleCraft);
	}

	public boolean isIn1v1(Player player) {
		return player == gladiator || player == target;
	}

	public void destroy() {
		HandlerList.unregisterAll(listener);
	}

	public void send1v1() {
		Location loc = gladiator.getLocation();
		boolean hasGladi = true;
		while (hasGladi) {
			hasGladi = false;
			boolean stop = false;
			for (double x = -8; x <= 8; x++) {
				for (double z = -8; z <= 8; z++) {
					for (double y = 0; y <= 10; y++) {
						Location l = new Location(loc.getWorld(), loc.getX() + x, 120 + y, loc.getZ() + z);
						if (l.getBlock().getType() != Material.AIR) {
							hasGladi = true;
							loc = new Location(loc.getWorld(), loc.getX() + 20, loc.getY(), loc.getZ());
							stop = true;
						}
						if (stop)
							break;
					}
					if (stop)
						break;
				}
				if (stop)
					break;
			}
		}
		Block mainBlock = loc.getBlock();
		generateBlocks(mainBlock);
		tpLocGladiator = gladiator.getLocation().clone();
		tpLocTarget = target.getLocation().clone();
		gladiator.sendMessage("§5§lGLADIATOR §fVoce desafiou §9§l" + target.getName() + " §fpara para uma batalha!");
		gladiator.sendMessage("§5§lGLADIATOR §fVoce tem  §9§l5 SEGUNDOS §fde invencibilidade.");
		target.sendMessage("§5§lGLADIATOR §fVoce foi desafiado por §9§l" + gladiator.getName() + " §fpara uma batalha!");
		target.sendMessage("§5§lGLADIATOR §fVoce tem  §9§l5 SEGUNDOS §fde invencibilidade.");
		Location l1 = new Location(mainBlock.getWorld(), mainBlock.getX() + 6.5, 121, mainBlock.getZ() + 6.5);
		l1.setYaw((float) (90.0 * 1.5));
		Location l2 = new Location(mainBlock.getWorld(), mainBlock.getX() - 5.5, 121, mainBlock.getZ() - 5.5);
		l2.setYaw((float) (90.0 * 3.5));
		target.teleport(l1);
		gladiator.teleport(l2);
		battleCraft.getGladiatorFightController().addPlayerToFights(gladiator.getUniqueId());
		battleCraft.getGladiatorFightController().addPlayerToFights(target.getUniqueId());
		gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
		target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
		witherEffect = new BukkitRunnable() {
			@Override
			public void run() {
				gladiator.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 60, 5));
				target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 60, 5));
			}
		};
		witherEffect.runTaskLater(battleCraft, 20 * 60 * 2);
		teleportBack = new BukkitRunnable() {
			@Override
			public void run() {
				teleportBack(tpLocGladiator, tpLocTarget);
			}
		};
		teleportBack.runTaskLater(battleCraft, 20 * 60 * 3);
	}

	public void teleportBack(Location loc, Location loc1) {
		battleCraft.getGladiatorFightController().removePlayerFromFight(gladiator.getUniqueId());
		battleCraft.getGladiatorFightController().removePlayerFromFight(target.getUniqueId());
		gladiator.teleport(loc);
		target.teleport(loc1);
		removeBlocks();
		gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
		target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
		gladiator.removePotionEffect(PotionEffectType.WITHER);
		target.removePotionEffect(PotionEffectType.WITHER);
		stop();
		destroy();
	}

	public void teleportBack(Player winner, Player loser) {
		battleCraft.getGladiatorFightController().removePlayerFromFight(winner.getUniqueId());
		battleCraft.getGladiatorFightController().removePlayerFromFight(loser.getUniqueId());
		winner.teleport(tpLocGladiator);
		removeBlocks();
		winner.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 100000));
		winner.removePotionEffect(PotionEffectType.WITHER);
		stop();
		destroy();
	}

	public void generateBlocks(Block mainBlock) {
		for (double x = -8; x <= 8; x++) {
			for (double z = -8; z <= 8; z++) {
				for (double y = 0; y <= 9; y++) {
					Location l = new Location(mainBlock.getWorld(), mainBlock.getX() + x, 120 + y, mainBlock.getZ() + z);
					l.getBlock().setType(Material.GLASS);
					battleCraft.getGladiatorFightController().addBlock(l.getBlock());
					blocksToRemove.add(l.getBlock());
				}
			}
		}
		for (double x = -7; x <= 7; x++) {
			for (double z = -7; z <= 7; z++) {
				for (double y = 1; y <= 8; y++) {
					Location l = new Location(mainBlock.getWorld(), mainBlock.getX() + x, 120 + y, mainBlock.getZ() + z);
					l.getBlock().setType(Material.AIR);
				}
			}
		}
	}

	public void removeBlocks() {
		for (Block b : blocksToRemove) {
			if (b.getType() != null && b.getType() != Material.AIR)
				b.setType(Material.AIR);
			battleCraft.getGladiatorFightController().removeBlock(b);
		}
		blocksToRemove.clear();
	}

	public void stop() {
		witherEffect.cancel();
		teleportBack.cancel();
	}
}
