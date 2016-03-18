package br.com.battlebits.ybattlecraft.managers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.constructors.Status;
import me.flame.utils.ranking.constructors.Account;

public class StatusManager {
	private HashMap<UUID, Status> statusList;

	private yBattleCraft m;

	public StatusManager(yBattleCraft m) {
		this.m = m;
		statusList = new HashMap<>();
	}

	public Status getStatusByUuid(UUID uuid) {
		if (!statusList.containsKey(uuid))
			statusList.put(uuid, new Status(this, uuid));
		return statusList.get(uuid);
	}

	public void addPlayer(UUID uuid, int kills, int deaths, int killstreak, List<String> kitList, List<String> kitsFavoritos) {
		statusList.put(uuid, new Status(this, uuid, kills, deaths, killstreak, kitList, kitsFavoritos));
	}

	public void removePlayer(Player p) {
		if (statusList.containsKey(p.getUniqueId()))
			statusList.remove(p.getUniqueId());
	}

	public void saveStatus(Player p) {
		UUID uuid = p.getUniqueId();
		saveStatus(uuid);
	}

	public void addFavoriteKit(final UUID uuid, final String kitName) {
		m.connect.SQLQuery("INSERT INTO `KitFavorito`(`Uuid`, `Kits`) VALUES ('" + uuid.toString().replace("-", "") + "','" + kitName + "')", m.mainConnection);
	}

	public void removeFavoriteKit(final UUID uuid, final String kitName) {
		m.connect.SQLQuery("DELETE INTO `KitFavorito`(`Uuid`, `Kits`) VALUES ('" + uuid.toString().replace("-", "") + "','" + kitName + "')", m.mainConnection);
	}

	public void saveStatus(final UUID uuid) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (statusList.containsKey(uuid)) {
					Status status = getStatusByUuid(uuid);
					int kills = status.getKills();
					int deaths = status.getDeaths();
					int killstreak = status.getKillstreak();
					String sql = "SELECT * FROM `Status` WHERE (`Uuid` = '" + uuid.toString().replace("-", "") + "');";
					try {
						if (m.mainConnection.isClosed())
							m.connect.trySQLConnection();
						PreparedStatement stmt = m.mainConnection.prepareStatement(sql);
						ResultSet result = stmt.executeQuery();
						if (result.next()) {
							stmt.execute("UPDATE `Status` SET `Kills`=" + kills + ",`Deaths`=" + deaths + ",`Killstreak`=" + killstreak + " WHERE (`Uuid`='" + uuid.toString().replace("-", "") + "');");
						} else {
							stmt.execute("INSERT INTO `Status`(`Uuid`, `Kills`, `Deaths`, `Killstreak`) VALUES ('" + uuid.toString().replace("-", "") + "'," + kills + "," + deaths + "," + killstreak + ")");
						}
						stmt.close();
						result.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}.runTaskAsynchronously(m);
	}

	public void updateStatus(Player killer, Player killed) {
		Status killedStatus = getStatusByUuid(killed.getUniqueId());
		if (killer != null) {
			Status killerStatus = getStatusByUuid(killer.getUniqueId());
			killerStatus.addKills();
			Account killerAccount = me.flame.utils.Main.getPlugin().getRankingManager().getAccount(killer);
			int money = 1;
			if (killerStatus.getKillstreak() >= 20) {
				++money;
			}
			killer.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "KILL " + ChatColor.WHITE + "Voce matou " + ChatColor.YELLOW + ChatColor.BOLD + killed.getName());
			killer.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "MONEY" + ChatColor.WHITE + " Voce recebeu " + ChatColor.GOLD + ChatColor.BOLD + killerAccount.addMoney(money) + " MOEDAS");
			//killer.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "XP " + ChatColor.WHITE + " ");
			if (killerStatus.getKillstreak() % 5 == 0 && killerStatus.getKillstreak() > 5) {
				Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "KILLSTREAK " + ChatColor.RED + ChatColor.BOLD + killer.getName() + ChatColor.WHITE + " conseguiu um " + ChatColor.GOLD + ChatColor.BOLD + "KILLSTREAK DE " + killerStatus.getKillstreak());
			}
			if (killedStatus.getKillstreak() >= 10) {
				Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "KILLSTREAK " + ChatColor.DARK_BLUE + ChatColor.BOLD + killed.getName() + ChatColor.WHITE + " perdeu seu " + ChatColor.GOLD + ChatColor.BOLD + "KILLSTREAK DE " + killedStatus.getKillstreak() + ChatColor.WHITE + " para " + ChatColor.RED + ChatColor.BOLD + killer.getName());
			}
		}
		killedStatus.addDeaths();
		Account killedAccount = me.flame.utils.Main.getPlugin().getRankingManager().getAccount(killed);
		killedAccount.removeMoney(1);
		String para = "";
		if(killer != null)
			para = " para " + ChatColor.YELLOW + ChatColor.BOLD + killer.getName();
		killed.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "MORTE " + ChatColor.WHITE + "Voce morreu" + para);
		killed.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "MONEY" + ChatColor.WHITE + " Voce perdeu " + ChatColor.DARK_RED + ChatColor.BOLD + "1 MOEDA");
	}

}
