package br.com.battlebits.battlecraft.kits;

import java.util.ArrayList;

import me.flame.utils.event.UpdateEvent;
import me.flame.utils.event.UpdateEvent.UpdateType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.Kit;
import br.com.battlebits.battlecraft.enums.KitType;
import br.com.battlebits.battlecraft.events.PlayerRemoveKitEvent;
import br.com.battlebits.battlecraft.interfaces.KitInterface;
import br.com.battlebits.battlecraft.utils.Cooldown;

public class Deshzin extends KitInterface {

	public Deshzin(Main main) {
		super(main);
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		if (!(event.getDamager() instanceof Player))
			return;
		final Player damager = (Player) event.getDamager();
		final Player p = (Player) event.getEntity();
		if (hasAbility(damager, "deshzin")) {
			if (!Cooldown.isInCooldown(damager.getUniqueId(), "deshzin")) {
				damager.sendMessage(ChatColor.RED + "Voce esta em cooldown de 10 segundos por hitar um player!");
			}
			Cooldown c = new Cooldown(damager.getUniqueId(), "deshzin", 10);
			c.start();
			if (damager.isFlying())
				damager.setFlying(false);
			damager.setAllowFlight(false);
		}
		if (hasAbility(p, "deshzin")) {
			if (!Cooldown.isInCooldown(p.getUniqueId(), "deshzin")) {
				p.sendMessage(ChatColor.RED + "Voce esta em cooldown de 10 segundos por receber dano de um player!");
			}
			Cooldown c = new Cooldown(p.getUniqueId(), "deshzin", 10);
			c.start();
			if (p.isFlying())
				p.setFlying(false);
			p.setAllowFlight(false);
		}
	}

	@EventHandler
	public void onToogleSpace(PlayerToggleFlightEvent event) {
		final Player player = event.getPlayer();
		if (!hasAbility(player))
			return;
		event.setCancelled(true);
		player.setFlying(false);
		if (Cooldown.isInCooldown(player.getUniqueId(), "deshzin")) {
			player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 1.0F, 1.0F);
			int timeleft = Cooldown.getTimeLeft(player.getUniqueId(), "deshzin");
			player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Deshzin " + ChatColor.GRAY + "em cooldown de " + ChatColor.RESET + ChatColor.BOLD + timeleft + ChatColor.GRAY + " segundos!");
			return;
		}
		player.setAllowFlight(false);
		player.setVelocity(player.getLocation().getDirection().multiply(1.0D).setY(0.6));
		if (!Cooldown.isInCooldown(player.getUniqueId(), "deshzin")) {
			Cooldown c = new Cooldown(player.getUniqueId(), "deshzin", 3);
			c.start();
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(getMain(), new Runnable() {
			public void run() {
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
			}
		}, 60L);
	}

	@EventHandler
	public void onRemoveKit(PlayerRemoveKitEvent event) {
		if(event.getKit() == null)
			return;
		if (event.getKit().getName() != getKitName())
			return;
		Player player = event.getPlayer();
		if (player.isFlying())
			player.setFlying(false);
		player.setAllowFlight(false);
	}

	@EventHandler
	public void onAdd(UpdateEvent event) {
		if (event.getType() != UpdateType.TICK)
			return;
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (!hasAbility(player))
				continue;
			if (Cooldown.isInCooldown(player.getUniqueId(), "deshzin")) {
				if (player.isFlying())
					player.setFlying(false);
				player.setAllowFlight(false);
				continue;
			}
			if (player.getGameMode() != GameMode.CREATIVE && player.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() != Material.AIR) {
				player.setAllowFlight(true);
			}
		}
	}

	@Override
	public Kit getKit() {
		return new Kit("deshzin", "Aperte espaço para dar um double jump.", new ArrayList<ItemStack>(), new ItemStack(Material.FIREWORK), 1000, KitType.MOBILIDADE);
	}

}
