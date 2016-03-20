package br.com.battlebits.ybattlecraft.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.evento.enums.EventState;
import br.com.battlebits.ybattlecraft.warps.Warp1v1;
import me.flame.utils.event.UpdateEvent;
import me.flame.utils.permissions.enums.Group;

public class PlayerListener implements Listener {

	private yBattleCraft m;

	public PlayerListener(yBattleCraft m) {
		this.m = m;
	}

	@EventHandler
	public void onStarve(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onPreCommand(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().startsWith("/kill"))
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
		if (!me.flame.utils.Main.getPlugin().getPermissionManager().hasGroupPermission(p, Group.TRIAL) && (event.getMessage().toLowerCase().contains("hack") || event.getMessage().toLowerCase().contains("autosoup") || event.getMessage().toLowerCase().contains("forcefield"))) {
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
		if(m.getProtectionManager().isProtected(event.getPlayer().getUniqueId())){
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
			String kitName = m.getKitManager().getCurrentKit(p.getUniqueId()).toLowerCase();
			//TODO: UNDROP KIT ITENS
//			if (m.getKitManager().getKitByName(kitName) != null)
//				for (ItemStack i : m.getKitManager().getKitByName(kitName).getItens()) {
//					if (item.getType() == i.getType()) {
//						isItemKit = true;
//						break;
//					}
//				}
		}
		if (isItemKit) {
			event.setCancelled(true);
		}
	}

}
