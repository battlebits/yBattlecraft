package br.com.battlebits.battlecraft.kits;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.Kit;
import br.com.battlebits.battlecraft.enums.KitType;
import br.com.battlebits.battlecraft.interfaces.KitInterface;

public class Ninja extends KitInterface {

	private HashMap<String, NinjaHit> ninjados;

	public Ninja(Main main) {
		super(main);
		ninjados = new HashMap<>();
	}

	@EventHandler
	public void onNinjaHit(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			final Player damager = (Player) event.getDamager();
			Player damaged = (Player) event.getEntity();
			if (hasAbility(damager, "ninja")) {
				NinjaHit ninjaHit = ninjados.get(damager.getName());
				if (ninjaHit == null)
					ninjaHit = new NinjaHit(damaged);
				else
					ninjaHit.setTarget(damaged);
				ninjados.put(damager.getName(), ninjaHit);
			}
		}
	}

	@EventHandler
	public void onShift(PlayerToggleSneakEvent event) {
		Player p = event.getPlayer();
		if (!event.isSneaking())
			return;
		if (!hasAbility(p))
			return;
		if (!ninjados.containsKey(p.getName()))
			return;
		NinjaHit ninjaHit = ninjados.get(p.getName());
		Player target = ninjaHit.getTarget();
		if (target.isDead())
			return;
		if (p.getLocation().distance(target.getLocation()) > 50)
			return;
		if (p.getLocation().getY() - target.getLocation().getY() > 20)
			return;
		if (ninjaHit.getTargetExpires() < System.currentTimeMillis())
			return;
		if (ninjaHit.getCooldown() > System.currentTimeMillis()) {
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 1.0F, 1.0F);
			p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Ninja " + ChatColor.GRAY + "em cooldown!");
			return;
		}
		p.teleport(target.getLocation());
		p.sendMessage(ChatColor.GREEN + "Teleportado");
		ninjaHit.teleport();
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();
		if (!ninjados.containsKey(p.getName()))
			return;
		ninjados.remove(p.getName());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (!ninjados.containsKey(p.getName()))
			return;
		ninjados.remove(p.getName());
	}

	@Override
	public Kit getKit() {
		ArrayList<ItemStack> kititems = new ArrayList<>();
		return new Kit("ninja", "Aperte SHIFT para teleportar-se para o utltimo jogador hitado.", kititems, new ItemStack(Material.EMERALD), 1000, KitType.ESTRATEGIA);
	}

	private static class NinjaHit {
		private Player target;
		private long cooldown;
		private long targetExpires;

		public NinjaHit(Player target) {
			this.target = target;
			this.cooldown = 0;
			this.targetExpires = System.currentTimeMillis() + 15000;
		}

		public Player getTarget() {
			return target;
		}

		public long getCooldown() {
			return cooldown;
		}

		public long getTargetExpires() {
			return targetExpires;
		}

		public void teleport() {
			cooldown = System.currentTimeMillis() + 7000;
		}

		public void setTarget(Player player) {
			this.target = player;
			this.targetExpires = System.currentTimeMillis() + 20000;
		}

	}

}
