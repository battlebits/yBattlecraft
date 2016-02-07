package br.com.battlebits.battlecraft.listeners;

import me.flame.utils.event.UpdateEvent;
import me.flame.utils.event.UpdateEvent.UpdateType;
import me.flame.utils.ranking.constructors.Account;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.Status;
import br.com.battlebits.battlecraft.enums.LoadStatus;
import br.com.battlebits.battlecraft.events.PlayerSelectKitEvent;
import br.com.battlebits.battlecraft.events.StatusLoadEvent;
import br.com.battlebits.battlecraft.events.WarpTeleportEvent;
import br.com.battlebits.battlecraft.nms.TabListManager;
import br.com.battlebits.battlecraft.utils.Formatter;

public class TabListListener implements Listener {

	private Main m;

	public TabListListener(Main m) {
		this.m = m;

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent event) {
		for (Player p : m.getServer().getOnlinePlayers()) {
			constructTabList(p);
		}
	}

	@EventHandler
	public void onStatusLoad(StatusLoadEvent event) {
		if (event.getLoadStatus() == LoadStatus.FAILED) {
			event.getPlayer().sendMessage(ChatColor.RED + "Não foi possivel carregar seus Status");
			return;
		}
		constructTabList(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onKill(PlayerDeathEvent event) {
		Player p = event.getEntity();
		Player killer = p.getKiller();
		if (killer != null)
			constructTabList(killer);
		constructTabList(p);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		for (Player p : m.getServer().getOnlinePlayers()) {
			if (p.getUniqueId() == event.getPlayer().getUniqueId())
				continue;
			constructTabList(p);
		}
	}

	@EventHandler
	public void onTick(UpdateEvent event) {
		if (event.getType() != UpdateType.SECOND)
			return;
		for (Player p : m.getServer().getOnlinePlayers()) {
			constructTabList(p);
		}
	}

	@EventHandler
	public void onWarpTeleport(WarpTeleportEvent event) {
		constructTabList(event.getPlayer());
	}

	@EventHandler
	public void onSelectKit(PlayerSelectKitEvent event) {
		constructTabList(event.getPlayer());
	}

	private void constructTabList(Player player) {
		int ping = 0;
		ping = ((CraftPlayer) player).getHandle().ping;
		int players = Bukkit.getOnlinePlayers().length - m.getAdminMode().admin.size();
		int maxPlayer = 200;
		Status status = m.getStatusManager().getStatusByUuid(player.getUniqueId());
		String traco = ChatColor.DARK_BLUE + " - ";
		StringBuilder headerBuilder = new StringBuilder();
		headerBuilder.append(ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + ">> " + ChatColor.GOLD + ChatColor.BOLD + "Battlecraft" + ChatColor.DARK_GRAY + ChatColor.BOLD + " <<");
		headerBuilder.append("\n        ");
		headerBuilder.append(ChatColor.YELLOW + "Kills: " + ChatColor.WHITE + status.getKills());
		headerBuilder.append(traco);
		headerBuilder.append(ChatColor.YELLOW + "Deaths: " + ChatColor.WHITE + status.getDeaths());
		headerBuilder.append(traco);
		headerBuilder.append(ChatColor.YELLOW + "Killstreak: " + ChatColor.WHITE + status.getKillstreak());
		headerBuilder.append("\n");
		headerBuilder.append(ChatColor.YELLOW + "Kit: " + ChatColor.WHITE + Formatter.getFormattedName(m.getKitManager().getPlayerKit(player)));
		headerBuilder.append(traco);
		headerBuilder.append(ChatColor.YELLOW + "Warp: " + ChatColor.WHITE + Formatter.getFormattedName(m.getWarpManager().getPlayerWarp(player)));
		headerBuilder.append(traco);
		headerBuilder.append(ChatColor.YELLOW + "Ping: " + ChatColor.WHITE + ping);
		headerBuilder.append(traco);
		headerBuilder.append(ChatColor.WHITE + "" + players + "/" + maxPlayer + ChatColor.YELLOW + " players");
		Account account = me.flame.utils.Main.getPlugin().getRankingManager().getAccount(player);
		StringBuilder footerBuilder = new StringBuilder();
		footerBuilder.append(ChatColor.AQUA + "Nick: " + ChatColor.WHITE + player.getName());
		footerBuilder.append(traco);
		footerBuilder.append(ChatColor.AQUA + "Liga: " + ChatColor.WHITE + account.getLiga().toString());
		footerBuilder.append(traco);
		footerBuilder.append(ChatColor.AQUA + "XP: " + ChatColor.WHITE + account.getXp());
		footerBuilder.append(traco);
		footerBuilder.append(ChatColor.AQUA + "Money: " + ChatColor.WHITE + account.getMoney());
		footerBuilder.append("\n");
		footerBuilder.append(ChatColor.AQUA + "Mais informacoes em: " + ChatColor.WHITE + "battlebits.com.br");
		TabListManager.setHeaderAndFooter(player, headerBuilder.toString(), footerBuilder.toString());
	}
}
