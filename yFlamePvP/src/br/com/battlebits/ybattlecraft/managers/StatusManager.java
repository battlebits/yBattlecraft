package br.com.battlebits.ybattlecraft.managers;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import br.com.battlebits.ybattlecraft.constructors.Status;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;

public class StatusManager {
	private HashMap<UUID, Status> statusList;

	public StatusManager() {
		statusList = new HashMap<>();
	}

	public Status getStatusByUuid(UUID uuid) {
		if (statusList.containsKey(uuid))
			return statusList.get(uuid);
		return new Status(uuid);
	}

	public void addPlayer(UUID uuid, Status status) {
		statusList.put(uuid, status);
	}

	public void addPlayer(UUID uuid, int kills, int deaths, int killstreak, List<String> kitList, List<String> kitsFavoritos) {
		statusList.put(uuid, new Status(uuid, kills, deaths, killstreak, kitList, kitsFavoritos, true));
	}

	public void removePlayer(Player p) {
		if (statusList.containsKey(p.getUniqueId()))
			statusList.remove(p.getUniqueId());
	}

	public void updateStatus(Player killer, Player killed) {
		Status killedStatus = getStatusByUuid(killed.getUniqueId());
		if (killer != null) {
			Status killerStatus = getStatusByUuid(killer.getUniqueId());
			killerStatus.addKills();
			BattlePlayer killerAccount = BattlebitsAPI.getAccountCommon().getBattlePlayer(killer.getUniqueId());
			int money = 1;
			if (killerStatus.getKillstreak() >= 20) {
				++money;
			}
			killer.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "KILL " + ChatColor.WHITE + "Voce matou " + ChatColor.YELLOW + ChatColor.BOLD + killed.getName());
			killer.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "MONEY" + ChatColor.WHITE + " Voce recebeu " + ChatColor.GOLD + ChatColor.BOLD + killerAccount.addMoney(money) + " MOEDAS");
			// killer.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "XP " +
			// ChatColor.WHITE + " ");
			if (killerStatus.getKillstreak() % 5 == 0 && killerStatus.getKillstreak() > 5) {
				Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "KILLSTREAK " + ChatColor.RED + ChatColor.BOLD + killer.getName() + ChatColor.WHITE + " conseguiu um " + ChatColor.GOLD + ChatColor.BOLD + "KILLSTREAK DE " + killerStatus.getKillstreak());
			}
			if (killedStatus.getKillstreak() >= 10) {
				Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "KILLSTREAK " + ChatColor.DARK_BLUE + ChatColor.BOLD + killed.getName() + ChatColor.WHITE + " perdeu seu " + ChatColor.GOLD + ChatColor.BOLD + "KILLSTREAK DE " + killedStatus.getKillstreak() + ChatColor.WHITE + " para " + ChatColor.RED + ChatColor.BOLD + killer.getName());
			}
		}
		killedStatus.addDeaths();
		BattlePlayer killedAccount = BattlebitsAPI.getAccountCommon().getBattlePlayer(killed.getUniqueId());
		killedAccount.removeMoney(1);
		String para = "";
		if (killer != null)
			para = " para " + ChatColor.YELLOW + ChatColor.BOLD + killer.getName();
		killed.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "MORTE " + ChatColor.WHITE + "Voce morreu" + para);
		killed.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "MONEY" + ChatColor.WHITE + " Voce perdeu " + ChatColor.DARK_RED + ChatColor.BOLD + "1 MOEDA");
	}

}
