package br.com.battlebits.ybattlecraft.managers;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import br.com.battlebits.commons.BattlebitsAPI;
import br.com.battlebits.commons.core.account.BattlePlayer;
import br.com.battlebits.ybattlecraft.constructors.Status;

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

	public void addPlayer(UUID uuid, int kills, int deaths, int killstreak, List<String> kitList,
			List<String> kitsFavoritos) {
		statusList.put(uuid, new Status(uuid, kills, deaths, killstreak, kitList, kitsFavoritos, true));
	}

	public void removePlayer(Player p) {
		if (statusList.containsKey(p.getUniqueId()))
			statusList.remove(p.getUniqueId());
	}

	public void updateStatus(Player killer, Player killed) {
		BattlePlayer killedAccount = BattlebitsAPI.getAccountCommon().getBattlePlayer(killed.getUniqueId());
		Status killedStatus = getStatusByUuid(killed.getUniqueId());
		if (killer != null) {
			if (!killer.getUniqueId().equals(killed.getUniqueId())) {
				Status killerStatus = getStatusByUuid(killer.getUniqueId());
				killerStatus.addKills();
				killerStatus.addKill(killed.getUniqueId());
				BattlePlayer killerAccount = BattlebitsAPI.getAccountCommon().getBattlePlayer(killer.getUniqueId());
				int money = 1;
				if (killerStatus.getKillstreak() >= 20) {
					++money;
				}
				int xp = calculatePlayerKill(killerAccount, killedAccount);
				killer.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "KILL " + ChatColor.WHITE + "Voce matou "
						+ ChatColor.YELLOW + ChatColor.BOLD + killed.getName());
				killer.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "MONEY" + ChatColor.WHITE + " Voce recebeu "
						+ ChatColor.GOLD + ChatColor.BOLD + killerAccount.addMoney(money) + " MOEDAS");
				killer.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "XP " + ChatColor.WHITE + " Voce recebeu "
						+ ChatColor.BLUE + ChatColor.BOLD + killerAccount.addXp(xp) + " XPs");
				if (killerStatus.getKillstreak() % 5 == 0 && killerStatus.getKillstreak() > 5) {
					killerAccount.addXp(1);
					Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "KILLSTREAK " + ChatColor.RED
							+ ChatColor.BOLD + killer.getName() + ChatColor.WHITE + " conseguiu um " + ChatColor.GOLD
							+ ChatColor.BOLD + "KILLSTREAK DE " + killerStatus.getKillstreak());
				}
				if (killedStatus.getKillstreak() >= 10) {
					Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "KILLSTREAK "
							+ ChatColor.DARK_BLUE + ChatColor.BOLD + killed.getName() + ChatColor.WHITE + " perdeu seu "
							+ ChatColor.GOLD + ChatColor.BOLD + "KILLSTREAK DE " + killedStatus.getKillstreak()
							+ ChatColor.WHITE + " para " + ChatColor.RED + ChatColor.BOLD + killer.getName());
				}
			}
		}
		killedStatus.addDeaths();
		killedAccount.removeMoney(1);
		String para = "";
		if (killer != null)
			para = " para " + ChatColor.YELLOW + ChatColor.BOLD + killer.getName();
		killed.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "MORTE " + ChatColor.WHITE + "Voce morreu" + para);
		killedStatus.death();
	}

	private int calculatePlayerKill(BattlePlayer killer, BattlePlayer killed) {
		double xpValue = 10;

		int ligaDifference = killed.getLeague().ordinal() - killer.getLeague().ordinal();
		Status killedStatus = getStatusByUuid(killed.getUniqueId());
		Status killerStatus = getStatusByUuid(killer.getUniqueId());
		double killedKd = killedStatus.getKills() / (killedStatus.getDeaths() > 0 ? killedStatus.getDeaths() : 1);

		xpValue += ligaDifference;
		xpValue += killedStatus.getKillstreak() / 2;

		xpValue += (killedStatus.getTotalDamageTaken() - 20) / 50;
		xpValue -= 10 * ((100 - killedStatus.getPorcentagemTaken(killer.getUniqueId())) / 100);
		xpValue -= 5 * ((100 - killerStatus.getPorcentagemKilled(killed.getUniqueId())) / 100);
		if (killedKd < 1)
			xpValue *= killedKd;
		int i = (int) xpValue;
		if (i < 1)
			i = 1;
		if (i > 50)
			i = 50;
		return i;
	}

}
