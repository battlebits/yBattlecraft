package br.com.battlebits.ybattlecraft.ability;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseAbility;
import net.md_5.bungee.api.ChatColor;

public class MLGAbility extends BaseAbility {

	public MLGAbility(yBattleCraft yBattleCraft) {
		super(yBattleCraft);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if ((event.getRightClicked() instanceof Player))
			return;
		Player p = event.getPlayer();
		Player clicked = (Player) event.getRightClicked();
		if (battlecraft.getProtectionManager().isProtected(clicked.getUniqueId()))
			return;
		if (battlecraft.getCooldownManager().isOnCooldown(p.getUniqueId(), getAbilityName() + "ability")) {
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
			p.sendMessage(ChatColor.AQUA + "§lMLG §fAguarde §9§l" + battlecraft.getCooldownManager().getCooldownTimeFormated(p.getUniqueId(), getAbilityName() + "ability").toUpperCase() + "§f para utilizar sua habilidade!");
			return;
		}
		if (battlecraft.getProtectionManager().removeProtection(p.getUniqueId())) {
			p.sendMessage("§8§lPROTEÇÃO §FVocê §7§lPERDEU§f sua proteção de spawn");
		}
		// TODO COISAS
		
		battlecraft.getCooldownManager().setCooldown(p.getUniqueId(), getAbilityName() + "ability", 60);
	}

}
