package br.com.battlebits.battlecraft.kits;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.Kit;
import br.com.battlebits.battlecraft.enums.KitType;
import br.com.battlebits.battlecraft.interfaces.KitInterface;

public class Kangaroo extends KitInterface {
	private ArrayList<Player> kangaroodj;
	private ArrayList<Player> cooldown;

	public Kangaroo(Main main) {
		super(main);
		kangaroodj = new ArrayList<>();
		cooldown = new ArrayList<>();
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				Iterator<Player> doubleJump = kangaroodj.iterator();
				while (doubleJump.hasNext()) {
					Player p = doubleJump.next();
					if (!p.isOnGround())
						continue;
					doubleJump.remove();
				}
				Iterator<Player> cooldownIt = cooldown.iterator();
				while (cooldownIt.hasNext()) {
					Player kangaroo = cooldownIt.next();
					boolean canRemove = false;
					for (Entity e : kangaroo.getNearbyEntities(15, 15, 15)) {
						if (!(e instanceof Player))
							continue;
						canRemove = true;
						break;
					}
					if (canRemove)
						cooldownIt.remove();
				}
			}
		}.runTaskTimer(getMain(), 20, 2);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		final Player p = event.getPlayer();
		Action a = event.getAction();
		final ItemStack item = p.getItemInHand();
		if (!a.name().contains("RIGHT") && !a.name().contains("LEFT"))
			return;
		if (!hasAbility(p))
			return;
		if (item == null)
			return;
		if (item.getType() != Material.FIREWORK)
			return;
		event.setCancelled(true);
		if (cooldown.contains(p)) {
			p.sendMessage(ChatColor.RED + "Você não pode usar enquanto esta em pvp.");
			return;
		}
		if (p.isOnGround()) {
			if (!p.isSneaking()) {
				Vector vector = p.getEyeLocation().getDirection();
				vector.multiply(0.3F);
				vector.setY(0.9F);
				p.setVelocity(vector);
				if (kangaroodj.contains(p)) {
					kangaroodj.remove(p);
				}
			} else {
				Vector vector = p.getEyeLocation().getDirection();
				vector.multiply(0.3F);
				vector.setY(0.65F);
				p.setVelocity(vector);
				if (kangaroodj.contains(p)) {
					kangaroodj.remove(p);
				}
			}
		} else {
			if (!kangaroodj.contains(p)) {
				if (!p.isSneaking()) {
					Vector vector = p.getEyeLocation().getDirection();
					vector.multiply(0.3F);
					vector.setY(0.85F);
					p.setVelocity(vector);
					kangaroodj.add(p);
				} else {
					Vector vector = p.getEyeLocation().getDirection();
					vector.multiply(1F);
					vector.setY(0.65F);
					p.setVelocity(vector);
					kangaroodj.add(p);
				}
			}
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (kangaroodj.contains(event.getPlayer()))
			kangaroodj.remove(event.getPlayer());
	}

	@EventHandler
	public void onDamageTest(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		Player p = (Player) event.getEntity();
		double dano = event.getDamage();
		DamageCause cause = event.getCause();
		if (cause != null && cause == DamageCause.FALL) {
			if (hasAbility(p)) {
				if (!event.isCancelled() && dano > 7) {
					event.setCancelled(true);
					p.damage(7);
				}
			}
		}
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		if (!(event.getDamager() instanceof Player))
			return;
		final Player damager = (Player) event.getDamager();
		final Player p = (Player) event.getEntity();
		if (hasAbility(damager))
			if (!cooldown.contains(damager))
				cooldown.add(damager);
		if (hasAbility(p))
			if (!cooldown.contains(p))
				cooldown.add(p);
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kititems = new ArrayList<ItemStack>();
		kititems.add(createItem(Material.FIREWORK, ChatColor.GOLD + "Kangaroo Boost"));
		return new Kit("kangaroo", "Utilize seu foguete para ser lançado para onde você desejar", kititems, new ItemStack(Material.FIREWORK), 0, KitType.MOBILIDADE);
	}

}