package br.com.battlebits.ybattlecraft.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.battlebits.commons.BattlebitsAPI;
import br.com.battlebits.commons.api.admin.AdminMode;
import br.com.battlebits.commons.api.tablist.TabListAPI;
import br.com.battlebits.commons.bukkit.event.update.UpdateEvent;
import br.com.battlebits.commons.bukkit.event.update.UpdateEvent.UpdateType;
import br.com.battlebits.commons.core.account.BattlePlayer;
import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.constructors.Status;
import br.com.battlebits.ybattlecraft.enums.LoadStatus;
import br.com.battlebits.ybattlecraft.event.PlayerDeathInWarpEvent;
import br.com.battlebits.ybattlecraft.event.PlayerSelectKitEvent;
import br.com.battlebits.ybattlecraft.event.StatusLoadEvent;
import br.com.battlebits.ybattlecraft.event.WarpTeleportEvent;
import br.com.battlebits.ybattlecraft.utils.Formatter;

public class TabListListener implements Listener {

	private Battlecraft m;

	public TabListListener(Battlecraft m) {
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
	public void onKill(PlayerDeathInWarpEvent e) {
		constructTabList(e.getPlayer());
		if (e.hasKiller()) {
			constructTabList(e.getKiller());
		}
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
		int players = Bukkit.getOnlinePlayers().size() - AdminMode.getInstance().playersInAdmin();
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
		headerBuilder.append(ChatColor.YELLOW + "Kit: " + ChatColor.WHITE + Formatter.getFormattedName(m.getKitManager().getCurrentKit(player.getUniqueId())));
		headerBuilder.append(traco);
		headerBuilder.append(ChatColor.YELLOW + "Warp: " + ChatColor.WHITE + Formatter.getFormattedName(m.getWarpManager().getPlayerWarp(player.getUniqueId())));
		headerBuilder.append(traco);
		headerBuilder.append(ChatColor.YELLOW + "Ping: " + ChatColor.WHITE + ping);
		headerBuilder.append(traco);
		headerBuilder.append(ChatColor.WHITE + "" + players + "/" + Bukkit.getMaxPlayers() + ChatColor.YELLOW + " players");
		BattlePlayer account = BattlebitsAPI.getAccountCommon().getBattlePlayer(player.getUniqueId());
		StringBuilder footerBuilder = new StringBuilder();
		footerBuilder.append(ChatColor.AQUA + "Nick: " + ChatColor.WHITE + player.getName());
		footerBuilder.append(traco);
		footerBuilder.append(ChatColor.AQUA + "Liga: " + ChatColor.WHITE + account.getLeague().toString());
		footerBuilder.append(traco);
		footerBuilder.append(ChatColor.AQUA + "XP: " + ChatColor.WHITE + account.getXp());
		footerBuilder.append(traco);
		footerBuilder.append(ChatColor.AQUA + "Money: " + ChatColor.WHITE + account.getMoney());
		footerBuilder.append("\n");
		footerBuilder.append(ChatColor.AQUA + "Mais informacoes em: " + ChatColor.WHITE + "battlebits.com.br");
		TabListAPI.setHeaderAndFooter(player, headerBuilder.toString(), footerBuilder.toString());
	}
}
