package br.com.battlebits.ybattlecraft.ability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import br.com.battlebits.commons.api.item.ItemBuilder;
import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.base.BaseAbility;
import br.com.battlebits.ybattlecraft.util.NovaDirection;

public class SupernovaAbility extends BaseAbility {

	private ArrayList<NovaDirection> directions;
	private HashMap<Arrow, Vector> arrows;
	private ItemStack superNova;

	public SupernovaAbility(Battlecraft bc) {
		super(bc);
		directions = new ArrayList<>();
		ArrayList<Double> pitchs = new ArrayList<>();
		pitchs.add(0.0);
		pitchs.add(22.5);
		pitchs.add(45.0);
		pitchs.add(67.5);
		pitchs.add(90.0);
		pitchs.add(112.5);
		pitchs.add(135.0);
		pitchs.add(157.5);
		pitchs.add(180.0);
		pitchs.add(202.5);
		pitchs.add(225.0);
		pitchs.add(247.5);
		pitchs.add(270.0);
		pitchs.add(292.5);
		pitchs.add(315.0);
		pitchs.add(337.5);
		for (double i : pitchs) {
			directions.add(new NovaDirection(i, 67.5));
			directions.add(new NovaDirection(i, 45.0));
			directions.add(new NovaDirection(i, 22.5));
			directions.add(new NovaDirection(i, 0.0));
			directions.add(new NovaDirection(i, -22.5));
			directions.add(new NovaDirection(i, -45));
			directions.add(new NovaDirection(i, -67.5));
		}
		directions.add(new NovaDirection(90.0, 0.0));
		directions.add(new NovaDirection(-90.0, 0.0));
		directions.add(new NovaDirection(0.0, 90.0));
		directions.add(new NovaDirection(0.0, -90.0));
		pitchs.clear();
		pitchs = null;
		arrows = new HashMap<>();
		new BukkitRunnable() {
			@Override
			public void run() {
				Iterator<Entry<Arrow, Vector>> entrys = arrows.entrySet().iterator();
				while (entrys.hasNext()) {
					Entry<Arrow, Vector> entry = entrys.next();
					Arrow arrow = entry.getKey();
					Vector vec = entry.getValue();
					if (!arrow.isDead()) {
						arrow.setVelocity(vec.normalize().multiply(vec.lengthSquared() / 4));
						if (arrow.isOnGround() || arrow.getTicksLived() >= 100) {
							arrow.remove();
						}
					} else {
						entrys.remove();
					}
				}
			}
		}.runTaskTimerAsynchronously(battlecraft, 1L, 1L);
		superNova = new ItemBuilder().amount(1).type(Material.NETHER_STAR).name("§5§lSupernova Explosion").build();
		getItens().add(superNova);
	}

	@EventHandler
	public void onPlayerInteractListener(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getItem() != null && e.getItem().equals(superNova)) {
				if (isUsing(e.getPlayer())) {
					if (!battlecraft.getCooldownManager().isOnCooldown(e.getPlayer().getUniqueId(), getAbilityName() + "ability")) {
						if (battlecraft.getProtectionManager().removeProtection(e.getPlayer().getUniqueId())) {
							e.getPlayer().sendMessage("§8§lPROTEÇÃO §FVocê §7§lPERDEU§f sua proteção de spawn");
						}
						Location loc = e.getPlayer().getLocation();
						for (NovaDirection d : directions) {
							final Arrow arrow = loc.getWorld().spawn(loc.clone().add(0, 1, 0), Arrow.class);
							double pitch = ((d.getPitch() + 90) * Math.PI) / 180;
							double yaw = ((d.getYaw() + 90) * Math.PI) / 180;
							double x = Math.sin(pitch) * Math.cos(yaw);
							double y = Math.sin(pitch) * Math.sin(yaw);
							double z = Math.cos(pitch);
							Vector vec = new Vector(x, z, y);
							arrow.setShooter(e.getPlayer());
							arrow.setVelocity(vec.multiply(2));
							arrow.setMetadata("Supernova", new FixedMetadataValue(battlecraft, e.getPlayer().getUniqueId()));
							arrows.put(arrow, vec);
						}
						e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.SHOOT_ARROW, 0.5F, 1.0F);
						battlecraft.getCooldownManager().setCooldown(e.getPlayer().getUniqueId(), getAbilityName() + "ability", 30);
					} else {
						e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
						e.getPlayer()
								.sendMessage("§5§lSUPERNOVA §fAguarde §9§l"
										+ battlecraft.getCooldownManager()
												.getCooldownTimeFormated(e.getPlayer().getUniqueId(), getAbilityName() + "ability").toUpperCase()
										+ "§f para utilizar sua habilidade!");
					}
				}
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager().hasMetadata("Supernova")) {
			if (e.getDamager() instanceof Arrow) {
				if (e.getEntity() instanceof Player) {
					Player p = (Player) e.getEntity();
					Arrow arrow = (Arrow) e.getDamager();
					try {
						if (arrow.getShooter() instanceof Player) {
							Player s = (Player) arrow.getShooter();
							if(s.getUniqueId() == p.getUniqueId()){
								e.setCancelled(true);
								return;
							}
						}
					} catch (Exception ex) {
					}
				}
				e.setDamage(6.0);
			}
		}
	}

}
