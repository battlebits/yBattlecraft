package br.com.battlebits.ybattlecraft.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.event.PlayerWarpJoinEvent;
import br.com.battlebits.ybattlecraft.evento.enums.EventState;
import br.com.battlebits.ybattlecraft.warps.Warp1v1;
import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.event.ram.RamOutOfLimitEvent;
import br.com.battlebits.ycommon.bukkit.event.update.UpdateEvent;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.permissions.enums.Group;

public class PlayerListener implements Listener {

	private yBattleCraft m;

	public PlayerListener(yBattleCraft m) {
		this.m = m;
	}

	@EventHandler
	public void onRam(RamOutOfLimitEvent event) {
		for (Player p : m.getServer().getOnlinePlayers()) {
			if (m.getProtectionManager().isProtected(p.getUniqueId()))
				if (p.getGameMode() != GameMode.CREATIVE)
					yBattleCraft.sendNextServer(p);
		}
	}

	@EventHandler
	public void onJoin(PlayerLoginEvent event) {
		if (BukkitMain.isMemoryRamRestart())
			if (BattlebitsAPI.getAccountCommon().getBattlePlayer(event.getPlayer().getUniqueId()).hasGroupPermission(Group.TRIAL)) {
				event.disallow(Result.KICK_OTHER, ChatColor.RED + "O servidor está se preparando para reiniciar e você foi kickado do servidor.");
			}
	}

	@EventHandler
	public void onWarp(PlayerWarpJoinEvent event) {
		if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
			if (BukkitMain.isMemoryRamRestart())
				yBattleCraft.sendNextServer(event.getPlayer());
	}

	@EventHandler
	public void onStarve(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onPreCommand(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().toLowerCase().startsWith("/kill "))
			event.setCancelled(true);
		if (event.getMessage().equalsIgnoreCase("/kill"))
			event.setCancelled(true);
	}

	@EventHandler
	public void onLeaves(LeavesDecayEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		if (!BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).hasGroupPermission(Group.TRIAL) && (event.getMessage().toLowerCase().contains("hack") || event.getMessage().toLowerCase().contains("autosoup") || event.getMessage().toLowerCase().contains("forcefield"))) {
			/*
			 * BarAPI.setMessage(event.getPlayer(), ChatColor.RED +
			 * "Use /report para reportar um player!", 5);
			 * p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
			 * Title title = new Title(ChatColor.RED + "Use /report");
			 * title.setSubtitle(ChatColor.WHITE + "para reportar um player");
			 * title.setFadeOutTime(20); title.setTimingsToTicks();
			 * title.setStayTime(60); title.send(p);
			 */
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemPickUp(PlayerPickupItemEvent event) {
		ItemStack item = event.getItem().getItemStack();
		if (m.getProtectionManager().isProtected(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
			return;
		}
		if (item.getItemMeta().hasDisplayName()) {
			event.setCancelled(true);
			return;
		}
		if (item.getType().toString().contains("SWORD") || item.getType().toString().contains("AXE")) {
			event.setCancelled(true);
			return;
		}
		if (item.getType().toString().contains("HELMET") || item.getType().toString().contains("CHESTPLATE") || item.getType().toString().contains("LEGGING") || item.getType().toString().contains("BOOTS")) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onUpdate(UpdateEvent event) {
		for (World world : Bukkit.getServer().getWorlds()) {
			for (Entity e : world.getEntitiesByClass(Item.class)) {
				if (!(e instanceof Item))
					continue;
				if (e.getTicksLived() >= 200) {
					e.remove();
				}
			}
		}
	}

	// TODO Refazer mensagem no Inicio
	@EventHandler
	public void onPing(ServerListPingEvent event) {
		if (yBattleCraft.currentEvento != null && yBattleCraft.currentEvento.getState() == EventState.WAITING) {
			String motd = ChatColor.GOLD + "BattleCraft";
			if (!yBattleCraft.motd.isEmpty())
				motd = motd + " - " + yBattleCraft.motd;
			event.setMotd(motd + "\n" + ChatColor.DARK_PURPLE + "ENTRE! EVENTO " + yBattleCraft.currentEvento.getType().toString().toUpperCase() + " COMECA EM " + yBattleCraft.currentEvento.getTime() + "s");
		} else {
			String motd = ChatColor.GOLD + "BattleCraft";
			if (!yBattleCraft.motd.isEmpty())
				motd = motd + " - " + yBattleCraft.motd;
			motd = motd + "\n" + ChatColor.BOLD + ChatColor.AQUA + "Entre para treinar seu PvP";
			event.setMotd(motd);
		}
		event.setMaxPlayers(150);
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		final Item drop = event.getItemDrop();
		ItemStack item = drop.getItemStack();
		if (item.hasItemMeta()) {
			event.setCancelled(true);
			return;
		} else if (item.toString().contains("SWORD") || item.toString().contains("AXE")) {
			event.setCancelled(true);
			return;
		}
		if (Warp1v1.isIn1v1(event.getPlayer())) {
			drop.remove();
			return;
		}
		Player p = event.getPlayer();
		if (item == null || item.getType() == Material.AIR)
			return;
		boolean isItemKit = false;
		if (m.getKitManager().hasCurrentKit(p.getUniqueId())) {
			// String kitName =
			// m.getKitManager().getCurrentKit(p.getUniqueId()).toLowerCase();
			// TODO: UNDROP KIT ITENS
			// if (m.getKitManager().getKitByName(kitName) != null)
			// for (ItemStack i :
			// m.getKitManager().getKitByName(kitName).getItens()) {
			// if (item.getType() == i.getType()) {
			// isItemKit = true;
			// break;
			// }
			// }
		}
		if (isItemKit) {
			event.setCancelled(true);
		}
	}

}
