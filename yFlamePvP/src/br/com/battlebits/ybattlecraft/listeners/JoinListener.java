package br.com.battlebits.ybattlecraft.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.constructors.Status;
import br.com.battlebits.ybattlecraft.enums.LoadStatus;
import br.com.battlebits.ybattlecraft.event.StatusLoadEvent;
import br.com.battlebits.ybattlecraft.hotbar.Hotbar;
import br.com.battlebits.ybattlecraft.nms.Title;
import br.com.battlebits.ybattlecraft.warps.Warp1v1;
import br.com.battlebits.ycommon.bukkit.api.admin.AdminMode;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.account.game.GameType;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import net.md_5.bungee.api.ChatColor;

public class JoinListener implements Listener {

	private yBattleCraft m;
	private List<String> novidades;

	public JoinListener(yBattleCraft m) {
		this.m = m;
		novidades = new ArrayList<>();
		novidades.add("Grande atualização");
		novidades.add("Novos plugins");
		novidades.add("Novo mapa");
		novidades.add("Seja bem-vindo");
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player p = event.getPlayer();
		if (!BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).hasGroupPermission(Group.LIGHT) && Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
			event.disallow(PlayerLoginEvent.Result.KICK_FULL, "Lotado! Compre VIP em " + yBattleCraft.site + " e tenha sempre um Slot reservado");
		} else if (!Bukkit.hasWhitelist() || Bukkit.getWhitelistedPlayers().contains(p)) {
			event.allow();
		} else {
			event.disallow(Result.KICK_WHITELIST, "O servidor esta em manutencao!");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		Player p = event.getPlayer();
		for (Player pl : Warp1v1.playersIn1v1) {
			pl.hidePlayer(p);
		}
		m.getWarpManager().teleportWarp(p, "spawn", false);
		p.setMaxHealth(20.0);
		p.setHealth(20.0);
		p.setFoodLevel(20);
		p.setGameMode(GameMode.SURVIVAL);
		Hotbar.setItems(p);
		Title title = new Title(ChatColor.GOLD + "BATTLECRAFT");
		title.setSubtitle("Versão 2.0");
		title.setTimingsToTicks();
		title.setFadeOutTime(20);
		title.setFadeInTime(30);
		title.setStayTime(20);
		title.send(p);
		m.getProtectionManager().addProtection(p.getUniqueId());
		for (int i = 0; i < novidades.size(); i++) {
			String message = novidades.get(i);
			new BukkitRunnable() {
				@Override
				public void run() {
					Title title = new Title(ChatColor.GOLD + "BATTLECRAFT");
					title.setSubtitle(message);
					title.setTimingsToTicks();
					title.setFadeOutTime(10);
					title.setFadeInTime(0);
					title.setStayTime(30);
					title.send(p);
				}
			}.runTaskLaterAsynchronously(m, (i + 1) * 30);
		}
		if (BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).hasGroupPermission(Group.TRIAL))
			AdminMode.getInstance().setAdmin(p);
		m.getPlayerHideManager().hideForAll(p);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onAsync(PlayerLoginEvent event) {
		if (event.getResult() != Result.ALLOWED) {
			return;
		}
		loadStatus(event.getPlayer());
	}

	@EventHandler
	public void onPluginReload(PluginEnableEvent event) {
		for (Player p : m.getServer().getOnlinePlayers()) {
			m.getWarpManager().teleportWarp(p, "spawn", false);
			p.setHealth(20.0);
			Hotbar.setItems(p);
			m.getProtectionManager().addProtection(p.getUniqueId());
			if (BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).hasGroupPermission(Group.TRIAL))
				AdminMode.getInstance().setAdmin(p);
			loadStatus(p.getUniqueId());
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onLoadStatus(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		StatusLoadEvent evente = new StatusLoadEvent(p, m.getStatusManager().getStatusByUuid(p.getUniqueId()), LoadStatus.SUCCESS);
		m.getServer().getPluginManager().callEvent(evente);
	}

	private void loadStatus(Player p) {
		loadStatus(p.getUniqueId());
	}

	private void loadStatus(UUID uuid) {
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid);
		Status status = player.getGameStatus().getMinigame(GameType.BATTLECRAFT_PVP_STATUS, Status.class);
		if (status == null)
			status = new Status(uuid);
		status.setUuid(uuid);
		m.getStatusManager().addPlayer(uuid, status);
	}
}
