package br.com.battlebits.ybattlecraft.ability;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import br.com.battlebits.commons.api.item.ItemBuilder;
import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseAbility;

public class KangarooAbility extends BaseAbility {

	private ArrayList<UUID> doubleJump;
	private ItemStack abilityItem;

	public KangarooAbility(yBattleCraft yBattleCraft) {
		super(yBattleCraft);
		doubleJump = new ArrayList<>();
		abilityItem = new ItemBuilder().amount(1).type(Material.FIREWORK).glow().lore("§7Utilize seu foguete para ser lancado para onde voce desejar").name("§6§lKangaroo Boost").build();
		getItens().add(abilityItem);
		new BukkitRunnable() {
			@SuppressWarnings({ "deprecation", "unchecked" })
			@Override
			public void run() {
				for (UUID id : (ArrayList<UUID>) doubleJump.clone()) {
					Player p = Bukkit.getPlayer(id);
					if (p != null && p.isOnline()) {
						if (p.isOnGround()) {
							doubleJump.remove(id);
						}
					}
				}
			}
		}.runTaskTimerAsynchronously(battlecraft, 20, 20);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteractListener(PlayerInteractEvent e) {
		if (isUsing(e.getPlayer())) {
			if (e.getPlayer().getItemInHand() != null) {
				if (e.getPlayer().getItemInHand().equals(abilityItem)) {
					if (e.getAction() != Action.PHYSICAL) {
						if (e.getPlayer().isOnGround()) {
							Vector vector = e.getPlayer().getEyeLocation().getDirection();
							if (!e.getPlayer().isSneaking()) {
								vector.multiply(0.3F);
								vector.setY(0.9F);
							} else {
								vector.multiply(0.3F);
								vector.setY(0.65F);
							}
							e.getPlayer().setVelocity(vector);
							if (doubleJump.contains(e.getPlayer().getUniqueId())) {
								doubleJump.remove(e.getPlayer().getUniqueId());
							}
						} else {
							if (!doubleJump.contains(e.getPlayer().getUniqueId())) {
								Vector vector = e.getPlayer().getEyeLocation().getDirection();
								if (!e.getPlayer().isSneaking()) {
									vector.multiply(0.3F);
									vector.setY(0.85F);
									e.getPlayer().setVelocity(vector);
									doubleJump.add(e.getPlayer().getUniqueId());
								} else {
									vector.multiply(1F);
									vector.setY(0.65F);
									e.getPlayer().setVelocity(vector);
									doubleJump.add(e.getPlayer().getUniqueId());
								}
							}
						}
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerQuitListener(PlayerQuitEvent e) {
		if (doubleJump.contains(e.getPlayer().getUniqueId())) {
			doubleJump.remove(e.getPlayer().getUniqueId());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDamageListener(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getCause() == DamageCause.FALL) {
				if (isUsing(p)) {
					if (e.getDamage() > 7) {
						e.setDamage(7.0);
					}
				}
			}
		}
	}

}