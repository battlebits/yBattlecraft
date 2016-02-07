package br.com.battlebits.battlecraft.listeners;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.managers.ProtectionManager;
import br.com.battlebits.battlecraft.warps.Warp1v1;

public class DamageListener implements Listener {

	private Main m;
	private ProtectionManager manager;

	public DamageListener(Main m) {
		this.m = m;
		manager = this.m.getProtectionManager();
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		Player player = (Player) event.getEntity();
		if (m.getProtectionManager().isProtected(player.getUniqueId()))
			event.setCancelled(true);
	}

	@EventHandler
	public void onProtectionLose(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		Player p = (Player) event.getEntity();
		if (Warp1v1.isIn1v1(p))
			return;
		if (m.getWarpManager().isInWarp(p, "lavachallenge")) {
			event.setCancelled(true);
			return;
		}
		if (m.getWarpManager().isInWarp(p, "1v1")) {
			event.setCancelled(true);
			return;
		}
		if (manager.isProtected(p.getUniqueId()))
			event.setCancelled(true);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onWarpDamage(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player))
			return;
		Player p = (Player) event.getDamager();
		ItemStack item = p.getItemInHand();
		if (item.toString().contains("SWORD") || item.toString().contains("AXE")) {
			item.setDurability((byte) 0);
			p.updateInventory();
		}
		if (Warp1v1.isIn1v1(p))
			return;
		if (m.getWarpManager().isInWarp(p, "lavachallenge")) {
			event.setCancelled(true);
			return;
		}
		if (m.getWarpManager().isInWarp(p, "1v1")) {
			event.setCancelled(true);
			return;
		}
		if (!(event.getEntity() instanceof Player))
			return;
		if (manager.isProtected(p.getUniqueId())) {
			if (!manager.isProtected(((Player) event.getEntity()).getUniqueId())) {
				if (m.getKitManager().hasKit(p)) {
					manager.removeProtection(p.getUniqueId());
					p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Proteção" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.GRAY + "Você perdeu proteção de spawn");
				} else
					event.setCancelled(true);
			}
		}
	}
}
