package br.com.battlebits.ybattlecraft.warps;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.commons.BattlebitsAPI;
import br.com.battlebits.commons.bukkit.event.update.UpdateEvent;
import br.com.battlebits.commons.bukkit.event.update.UpdateEvent.UpdateType;
import br.com.battlebits.commons.core.account.BattlePlayer;
import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseWarp;
import br.com.battlebits.ybattlecraft.constructors.Warp;
import br.com.battlebits.ybattlecraft.constructors.WarpScoreboard;
import br.com.battlebits.ybattlecraft.event.PlayerDamagePlayerEvent;
import br.com.battlebits.ybattlecraft.event.PlayerWarpJoinEvent;
import br.com.battlebits.ybattlecraft.event.RealMoveEvent;

public class WarpVoidChallenge extends BaseWarp {

	private WarpScoreboard scoreboard;
	private HashMap<UUID, Long> time;

	public WarpVoidChallenge(yBattleCraft yBattleCraft) {
		super(yBattleCraft);
		time = new HashMap<>();
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
	public void onQuit(PlayerQuitEvent e) {
		e.getPlayer().setLevel(-10);
		time.remove(e.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onUpdate(UpdateEvent e) {
		if (e.getType() == UpdateType.SECOND) {
			new BukkitRunnable() {
				@Override
				public void run() {
					for (Player p : getPlayersInWarp()) {
						if (time.containsKey(p.getUniqueId())) {
							String tempo = yBattleCraft.getTimeUtils().formatToMinutesAndSeconds(
									(int) ((System.currentTimeMillis() - time.get(p.getUniqueId())) / 1000));
							String p1 = tempo;
							String p2 = "";
							if (p1.length() > 14) {
								p2 = p1.substring(14, p1.length());
								p1 = p1.substring(0, 14);
							}
							scoreboard.updateScore(p, "tempo", "§a" + p1, "§a" + p2);
							p.setLevel((int) ((System.currentTimeMillis() - time.get(p.getUniqueId())) / 1000));
						}
					}

				}
			}.runTaskAsynchronously(yBattleCraft);
		}
	}

	@EventHandler
	public void onTeleport(PlayerWarpJoinEvent event) {
		if (isOnWarp(event.getPlayer())) {
			Player p = event.getPlayer();
			getMain().getProtectionManager().removeProtection(p.getUniqueId());
			p.getInventory().setItem(14, new ItemStack(Material.RED_MUSHROOM, 64));
			p.getInventory().setItem(15, new ItemStack(Material.BOWL, 64));
			p.getInventory().setItem(16, new ItemStack(Material.BROWN_MUSHROOM, 64));
			for (ItemStack i : p.getInventory().getContents()) {
				if (i == null) {
					p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));
				}
			}
			if (time.containsKey(p.getUniqueId())) {
				p.sendMessage("§5§lVOID CHALLENGE §fVocê sobreviveu por §9§l" + yBattleCraft.getTimeUtils()
						.formatToMinutesAndSeconds(
								(int) ((System.currentTimeMillis() - time.get(p.getUniqueId())) / 1000))
						.toUpperCase() + "§f!");
				time.remove(p.getUniqueId());
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamage(EntityDamageEvent e) {
		if (e.getCause() == DamageCause.FALL) {
			if (e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				if (isOnWarp(p)) {
					e.setCancelled(true);
					p.teleport(new Location(Bukkit.getWorld("voidchallengeWarp"), 0, 54, -18));
					p.sendMessage("§5§lVOID CHALLENGE §fVocê deve pular no §9§lVOID §fpara iniciar o desafio.");
				}
			}
		}
	}

	@EventHandler
	public void onRealMove(RealMoveEvent e) {
		if (e.getTo().getBlockY() < 0) {
			if (isOnWarp(e.getPlayer())) {
				if (!time.containsKey(e.getPlayerUUID())) {
					time.put(e.getPlayerUUID(), System.currentTimeMillis());
					scoreboard.updateScore(e.getPlayer(), "sobreviveu", "§7Sobreviveu:", "");
					scoreboard.updateScore(e.getPlayer(), "tempo", "§a1 segundo", "");
				}
			}
		}
	}

	@Override
	protected Warp getWarp(yBattleCraft battleCraft) {
		scoreboard = new WarpScoreboard("void") {
			@Override
			public void createScores(Player p) {
				BattlePlayer a = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
				createScore(p, "b3", "", "", 8);
				createScore(p, "xp", "§7XP: ", "§b" + a.getXp(), 7);
				createScore(p, "liga", "§7Liga: ", a.getLeague().getSymbol() + " " + a.getLeague().toString(), 6);
				createScore(p, "b2", "", "", 5);
				createScore(p, "sobreviveu", "§7Para §ainiciar", "§7 o desafio", 4);
				createScore(p, "tempo", "§7desafio pule", "§7 no §5void§7!", 3);
				createScore(p, "b1", "", "", 2);
				createScore(p, "site", "§6www.battle", "§6bits.com.br", 1);
			}
		};
		Warp w = new Warp("Void Challenge", "Treine seus refils e seus recrafts com um dano maior",
				new ItemStack(Material.ENDER_PORTAL_FRAME), new Location(Bukkit.getWorld("voidchallengeWarp"), 0, 54, -18),
				false, scoreboard, false, false);
		return w;
	}
}
