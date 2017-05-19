package br.com.battlebits.ybattlecraft.warps;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.commons.BattlebitsAPI;
import br.com.battlebits.commons.api.admin.AdminMode;
import br.com.battlebits.commons.core.account.BattlePlayer;
import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.base.BaseWarp;
import br.com.battlebits.ybattlecraft.constructors.Status;
import br.com.battlebits.ybattlecraft.constructors.Warp;
import br.com.battlebits.ybattlecraft.constructors.WarpScoreboard;
import br.com.battlebits.ybattlecraft.enums.KitType;
import br.com.battlebits.ybattlecraft.event.PlayerDeathInWarpEvent;
import br.com.battlebits.ybattlecraft.event.PlayerResetKDEvent;
import br.com.battlebits.ybattlecraft.event.PlayerWarpJoinEvent;
import br.com.battlebits.ybattlecraft.kit.Kit;

public class WarpFps extends BaseWarp {

	private Warp warp;
	private WarpScoreboard scoreboard;
	private Kit kit;
	private UUID topKsUUID;

	public WarpFps(Battlecraft Battlecraft) {
		super(Battlecraft);
		kit = new Kit(Battlecraft, "PvP", "Kit Padrao", new ArrayList<ItemStack>(),
				new ItemStack(Material.DIAMOND_SWORD), 0, KitType.NEUTRO, new ArrayList<>());
	}

	@EventHandler
	public void onPlayerJoin(PlayerWarpJoinEvent e) {
		if (topKsUUID != null && e.getPlayer().getUniqueId() == topKsUUID) {
			updateTopKS();
		}
		if (e.getWarp().getWarpName().equalsIgnoreCase(warp.getWarpName())) {
			Battlecraft.getPlayerHideManager().hidePlayer(e.getPlayer());
			Battlecraft.getKitManager().giveKit(e.getPlayer(), kit, false);
			getMain().getProtectionManager().addProtection(e.getPlayer().getUniqueId());
			new BukkitRunnable() {
				@Override
				public void run() {
					setTopKS(e.getPlayer());
				}
			}.runTaskLaterAsynchronously(Battlecraft, 20L);
		}
	}

	@EventHandler
	public void onPlayerQuitListener(PlayerQuitEvent e) {
		if (topKsUUID != null) {
			if (topKsUUID.toString().equalsIgnoreCase(e.getPlayer().getUniqueId().toString())) {
				updateTopKS();
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDeathInWarpListener(PlayerDeathInWarpEvent e) {
		if (isOnWarp(e.getPlayer())) {
			scoreboard.updateScoreValue(e.getPlayer(), "deaths",
					"§b" + getMain().getStatusManager().getStatusByUuid(e.getPlayerUUID()).getDeaths());
			scoreboard.updateScoreValue(e.getPlayer(), "ks", "§b0");
			if (e.hasKiller()) {
				scoreboard.updateScoreValue(e.getKiller(), "kills",
						"§b" + getMain().getStatusManager().getStatusByUuid(e.getKillerUUID()).getKills());
				scoreboard.updateScoreValue(e.getKiller(), "ks",
						"§b" + getMain().getStatusManager().getStatusByUuid(e.getKillerUUID()).getKillstreak());
			}
			updateTopKS();
		}
	}

	@EventHandler
	public void onResetKD(PlayerResetKDEvent e) {
		if (isOnWarp(e.getPlayer())) {
			scoreboard.updateScoreValue(e.getPlayer(), "deaths",
					"§b" + getMain().getStatusManager().getStatusByUuid(e.getPlayer().getUniqueId()).getDeaths());
			scoreboard.updateScoreValue(e.getPlayer(), "kills",
					"§b" + getMain().getStatusManager().getStatusByUuid(e.getPlayer().getUniqueId()).getKills());
			scoreboard.updateScoreValue(e.getPlayer(), "ks",
					"§b" + getMain().getStatusManager().getStatusByUuid(e.getPlayer().getUniqueId()).getKillstreak());
			updateTopKS();
		}
	}

	public void updateTopKS() {
		new BukkitRunnable() {
			@Override
			public void run() {
				int ks = 0;
				UUID topks = null;
				for (UUID id : Battlecraft.getWarpManager().getPlayersInWarp(warp.getWarpName())) {
					if (!AdminMode.getInstance().isAdmin(Bukkit.getPlayer(id))) {
						if (!Battlecraft.getProtectionManager().isProtected(id)) {
							Status s = Battlecraft.getStatusManager().getStatusByUuid(id);
							if (s.getKillstreak() > ks) {
								ks = s.getKillstreak();
								topks = id;
							}
						}
					}
				}
				topKsUUID = topks;
				for (UUID id : Battlecraft.getWarpManager().getPlayersInWarp(warp.getWarpName())) {
					Player p = Bukkit.getPlayer(id);
					setTopKS(p);
				}
			}
		}.runTaskAsynchronously(Battlecraft);
	}

	public void setTopKS(Player p) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (topKsUUID != null) {
					Player t = Bukkit.getPlayer(topKsUUID);
					if (t != null && t.isOnline()) {
						String name1 = "";
						String name2 = "";
						name1 = t.getName();
						if (t.getName().length() > 14) {
							name1 = t.getName().substring(0, 14);
							name2 = t.getName().substring(14, t.getName().length());
						}
						if (p != null && p.isOnline()) {
							scoreboard.updateScoreName(p, "topksplayer", "§3" + name1);
							scoreboard.updateScoreValue(p, "topksplayer", "§3" + name2 + " - "
									+ Battlecraft.getStatusManager().getStatusByUuid(topKsUUID).getKillstreak());
						}
						return;
					}
				}
				if (p != null && p.isOnline()) {
					scoreboard.updateScoreName(p, "topksplayer", "§3Ninguem");
					scoreboard.updateScoreValue(p, "topksplayer", "§3 - 0");
				}
			}
		}.runTaskAsynchronously(Battlecraft);
	}

	@Override
	protected Warp getWarp(Battlecraft battleCraft) {
		scoreboard = new WarpScoreboard("fps") {
			@Override
			public void createScores(Player p) {
				Status s = battleCraft.getStatusManager().getStatusByUuid(p.getUniqueId());
				BattlePlayer a = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
				createScore(p, "b3", "", "", 11);
				createScore(p, "kills", "§7Kills: ", "§b" + s.getKills(), 10);
				createScore(p, "deaths", "§7Deaths: ", "§b" + s.getDeaths(), 9);
				createScore(p, "ks", "§7KillStreak: ", "§b" + s.getKillstreak(), 8);
				createScore(p, "xp", "§7XP: ", "§b" + a.getXp(), 7);
				createScore(p, "liga", "§7Liga: ", a.getLeague().getSymbol() + " " + a.getLeague().toString(), 6);
				createScore(p, "b2", "", "", 5);
				createScore(p, "topks", "§7Top Kill", "§7Streak:", 4);
				createScore(p, "topksplayer", "§3Ninguem", "§3 - 0", 3);
				createScore(p, "b1", "", "", 2);
				createScore(p, "site", "§6www.battle", "§6bits.com.br", 1);
			}
		};
		warp = new Warp("FPS", "Utilize esta Warp feita com um mapa mais leve para aumentar seus FPSs",
				new ItemStack(Material.GLASS), new Location(Bukkit.getWorld("fpsWarp"), 0.5, 65.5, 0.5, 0, 0), 3.5,
				scoreboard, true);
		return warp;
	}
}
