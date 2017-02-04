package br.com.battlebits.ybattlecraft.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.commons.BattlebitsAPI;
import br.com.battlebits.commons.api.admin.AdminMode;
import br.com.battlebits.commons.api.title.TitleAPI;
import br.com.battlebits.commons.core.permission.Group;
import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.constructors.Status;
import br.com.battlebits.ybattlecraft.data.DataStatus;
import br.com.battlebits.ybattlecraft.enums.LoadStatus;
import br.com.battlebits.ybattlecraft.event.StatusLoadEvent;
import br.com.battlebits.ybattlecraft.hotbar.Hotbar;
import br.com.battlebits.ybattlecraft.warps.Warp1v1;
import net.md_5.bungee.api.ChatColor;

public class JoinListener implements Listener {

	private Battlecraft m;
	private List<String> novidades;

	public JoinListener(Battlecraft m) {
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
		if (!BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).hasGroupPermission(Group.LIGHT) && Bukkit
				.getOnlinePlayers().size() >= Bukkit.getMaxPlayers() + AdminMode.getInstance().playersInAdmin()) {
			event.disallow(PlayerLoginEvent.Result.KICK_FULL,
					"Lotado! Compre VIP em " + Battlecraft.site + " e tenha sempre um Slot reservado");
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
		p.setGameMode(GameMode.ADVENTURE);
		Hotbar.setItems(p);
		TitleAPI.setTitle(p, ChatColor.GOLD + "BATTLECRAFT", "Versão 2.0", 30, 20, 0, true);
		m.getProtectionManager().addProtection(p.getUniqueId());
		for (int i = 0; i < novidades.size(); i++) {
			String message = novidades.get(i);
			new BukkitRunnable() {
				@Override
				public void run() {
					TitleAPI.setTitle(p, ChatColor.GOLD + "BATTLECRAFT", message, 0, 30, 0, true);
				}
			}.runTaskLaterAsynchronously(m, (i + 1) * 30);
		}
		if (BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).hasGroupPermission(Group.TRIAL))
			AdminMode.getInstance().setAdmin(p);
		m.getPlayerHideManager().hideForAll(p);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onAsync(AsyncPlayerPreLoginEvent event) {
		Status status = DataStatus.createIfNotExistMongo(event.getUniqueId());
		m.getStatusManager().addPlayer(event.getUniqueId(), status);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onLogin(PlayerLoginEvent event) {
		if(event.getResult() != Result.ALLOWED)
			m.getStatusManager().removePlayer(event.getPlayer());
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
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onLoadStatus(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		StatusLoadEvent evente = new StatusLoadEvent(p, m.getStatusManager().getStatusByUuid(p.getUniqueId()),
				LoadStatus.SUCCESS);
		m.getServer().getPluginManager().callEvent(evente);
	}

}
