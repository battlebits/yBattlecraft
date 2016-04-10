package br.com.battlebits.ybattlecraft.listeners;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
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

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.enums.LoadStatus;
import br.com.battlebits.ybattlecraft.event.StatusLoadEvent;
import br.com.battlebits.ybattlecraft.hotbar.Hotbar;
import br.com.battlebits.ybattlecraft.nms.Title;
import br.com.battlebits.ybattlecraft.warps.Warp1v1;
import me.flame.utils.permissions.enums.Group;
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

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player p = event.getPlayer();
		if (!me.flame.utils.Main.getPlugin().getPermissionManager().hasGroupPermission(p, Group.LIGHT) && Bukkit.getOnlinePlayers().length >= 150) {
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
		if (me.flame.utils.Main.getPlugin().getPermissionManager().hasGroupPermission(p, Group.TRIAL))
			m.getAdminMode().setAdmin(p);
		m.getVanish().updateVanished(p);
		m.getPlayerHideManager().hideForAll(p);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onAsync(AsyncPlayerPreLoginEvent event) {
		if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
			return;
		}
		try {
			loadStatus(event.getUniqueId());
		} catch (SQLException e) {
			event.disallow(org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
					ChatColor.RED + "Nao foi possivel carregar seus status, tente novamente em breve");
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPluginReload(PluginEnableEvent event) {
		for (Player p : m.getServer().getOnlinePlayers()) {
			m.getWarpManager().teleportWarp(p, "spawn", false);
			p.setHealth(20.0);
			Hotbar.setItems(p);
			m.getProtectionManager().addProtection(p.getUniqueId());
			if (me.flame.utils.Main.getPlugin().getPermissionManager().hasGroupPermission(p, Group.TRIAL))
				m.getAdminMode().setAdmin(p);
			m.getVanish().updateVanished(p);
			try {
				loadStatus(p.getUniqueId());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onLoadStatus(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		StatusLoadEvent evente = new StatusLoadEvent(p, m.getStatusManager().getStatusByUuid(p.getUniqueId()), LoadStatus.SUCCESS);
		m.getServer().getPluginManager().callEvent(evente);
	}

	private void loadStatus(UUID uuid) throws SQLException {
		if (!m.sql)
			return;
		String sql = "SELECT * FROM `Kits` WHERE (`Uuid` = '" + uuid.toString().replace("-", "") + "');";
		if (m.mainConnection.isClosed())
			m.connect.trySQLConnection();
		PreparedStatement stmt = m.mainConnection.prepareStatement(sql);
		ResultSet result = stmt.executeQuery();
		List<String> kitList = new ArrayList<>();
		while (result.next()) {
			kitList.add(result.getString("KitName"));
		}
		sql = "SELECT * FROM `Status` WHERE (`Uuid` = '" + uuid.toString().replace("-", "") + "');";
		result.close();
		stmt.close();
		stmt = m.mainConnection.prepareStatement(sql);
		result = stmt.executeQuery();
		int kills = 0;
		int deaths = 0;
		int killstreak = 0;
		if (result.next()) {
			kills = result.getInt("Kills");
			deaths = result.getInt("Deaths");
			killstreak = result.getInt("Killstreak");
		}
		result.close();
		stmt.close();
		sql = "SELECT * FROM `KitFavorito` WHERE (`Uuid` = '" + uuid.toString().replace("-", "") + "');";
		stmt = m.mainConnection.prepareStatement(sql);
		result = stmt.executeQuery();
		List<String> kitsFavoritos = new ArrayList<>();
		if (result.next()) {
			String kits = result.getString("Kits");
			if (kits.contains(",")) {
				for (String str : kits.split(",")) {
					kitsFavoritos.add(str);
				}
			} else {
				kitsFavoritos.add(kits);
			}
		}
		m.getStatusManager().addPlayer(uuid, kills, deaths, killstreak, kitList, kitsFavoritos);
		stmt.close();
		result.close();
	}
}
