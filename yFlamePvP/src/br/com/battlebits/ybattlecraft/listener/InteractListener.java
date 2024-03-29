package br.com.battlebits.ybattlecraft.listener;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.commons.api.admin.AdminMode;
import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.selectors.KitSelector;
import br.com.battlebits.ybattlecraft.selectors.WarpSelector;

public class InteractListener implements Listener {

	private Battlecraft m;
	private WarpSelector warpSelector;

	public InteractListener(Battlecraft m) {
		this.m = m;
		warpSelector = new WarpSelector(this.m.getWarpManager());
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		ItemStack item = p.getItemInHand();
		Action a = event.getAction();
		if (!a.toString().contains("RIGHT_CLICK"))
			return;
		if (item == null)
			return;
		if (!item.hasItemMeta())
			return;
		if (!item.getItemMeta().hasDisplayName())
			return;
		String displayName = item.getItemMeta().getDisplayName();
		if (displayName.contains("Kits")) {
			// TODO: KIT SELECTOR
			new KitSelector(p, m).open();
			event.setCancelled(true);
			return;
		}
		if (displayName.contains("Warps")) {
			warpSelector.open(p);
			event.setCancelled(true);
			return;
		}
		if (displayName.contains("Kits - StartGame")) {
			event.setCancelled(true);
			return;
		}
		if (displayName.contains("BattleCraft")) {
			event.setCancelled(true);
			return;
		}
		if (displayName.contains("Shop")) {
			event.setCancelled(true);
			return;
		}
		if (displayName.contains("Eventos")) {
			event.setCancelled(true);
			p.chat("/evento");
		}
	}

	@EventHandler
	public void onSoup(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		ItemStack item = p.getItemInHand();
		Action a = event.getAction();
		if (item == null)
			return;
		if (item.getType() != Material.MUSHROOM_SOUP)
			return;
		if (!a.toString().contains("RIGHT_CLICK"))
			return;
		if (((Damageable) p).getHealth() < 20 || p.getFoodLevel() < 19) {
			event.setCancelled(true);
			if (((Damageable) p).getHealth() < 20)
				if (((Damageable) p).getHealth() + 7 <= 20)
					p.setHealth(((Damageable) p).getHealth() + 7);
				else
					p.setHealth(20);
			else if (p.getFoodLevel() < 20)
				if (p.getFoodLevel() + 7 <= 20)
					p.setFoodLevel(p.getFoodLevel() + 7);
				else
					p.setFoodLevel(20);
			p.setItemInHand(new ItemStack(Material.BOWL));
		}
	}

	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent event) {
		Player p = event.getPlayer();
		Entity e = event.getRightClicked();
		if (!AdminMode.getInstance().isAdmin(p)) {
			return;
		}
		if (e instanceof Player) {
			Player clicado = (Player) e;
			p.openInventory(clicado.getInventory());
		}
	}
}
