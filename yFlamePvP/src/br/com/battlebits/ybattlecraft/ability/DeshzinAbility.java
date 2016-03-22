package br.com.battlebits.ybattlecraft.ability;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseAbility;
import br.com.battlebits.ybattlecraft.event.PlayerDamagePlayerEvent;
import me.flame.utils.event.UpdateEvent;
import me.flame.utils.event.UpdateEvent.UpdateType;

public class DeshzinAbility extends BaseAbility {

	public DeshzinAbility(yBattleCraft yBattleCraft) {
		super(yBattleCraft);
	}

	@EventHandler
	public void onPlayerDamagePlayerListener(PlayerDamagePlayerEvent e) {
		if (isUsing(e.getDamager())) {
			if (!battlecraft.getCooldownManager().isOnCooldown(e.getDamager().getUniqueId(), getAbilityName() + "ability")) {
				e.getDamager().sendMessage("§5§lDESHZIN §fVoce entrou em cooldown por entrar em §9§LCOMBATE§F!");
			}
			battlecraft.getCooldownManager().setCooldown(e.getDamager().getUniqueId(), getAbilityName() + "ability", 10);
			if (e.getDamager().isFlying()) {
				e.getDamager().setFlying(false);
			}
			if (e.getDamager().getAllowFlight()) {
				e.getDamager().setAllowFlight(false);
			}
		}
		if (isUsing(e.getDamaged())) {
			if (!battlecraft.getCooldownManager().isOnCooldown(e.getDamaged().getUniqueId(), getAbilityName() + "ability")) {
				e.getDamaged().sendMessage("§5§lDESHZIN §fVoce entrou em cooldown por entrar em §9§LCOMBATE§F!");
			}
			battlecraft.getCooldownManager().setCooldown(e.getDamaged().getUniqueId(), getAbilityName() + "ability", 10);
			if (e.getDamaged().isFlying()) {
				e.getDamaged().setFlying(false);
			}
			if (e.getDamaged().getAllowFlight()) {
				e.getDamaged().setAllowFlight(false);
			}
		}
	}

	@EventHandler
	public void onPlayerToggleFlightListener(PlayerToggleFlightEvent e) {
		if (isUsing(e.getPlayer())) {
			e.setCancelled(true);
			if (e.getPlayer().isFlying()) {
				e.getPlayer().setFlying(false);
			}
			if (!battlecraft.getCooldownManager().isOnCooldown(e.getPlayer().getUniqueId(), getAbilityName() + "ability")) {
				if (e.getPlayer().getAllowFlight()) {
					e.getPlayer().setAllowFlight(false);
				}
				e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(1.0D).setY(0.6));
				e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.FIREWORK_LAUNCH, 0.5F, 1.0F);
				battlecraft.getCooldownManager().setCooldown(e.getPlayer().getUniqueId(), getAbilityName() + "ability", 3);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onUpdateListener(UpdateEvent e) {
		if (e.getType() == UpdateType.SECOND) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (isUsing(p)) {
					if (battlecraft.getCooldownManager().isOnCooldown(p.getUniqueId(), getAbilityName() + "ability")) {
						if (p.isFlying()) {
							p.setFlying(false);
						}
						if (p.getAllowFlight()) {
							p.setAllowFlight(false);
						}
					} else {
						if (p.getGameMode() != GameMode.CREATIVE && p.getLocation().subtract(0, 1.0D, 0).getBlock().getType() != Material.AIR) {
							if (!p.getAllowFlight()) {
								p.setAllowFlight(true);
							}
						}
					}
				}
			}
		}
	}

}
