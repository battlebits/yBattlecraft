package br.com.battlebits.ybattlecraft.ability;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.commons.api.item.ItemBuilder;
import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.base.BaseAbility;

public class SwitcherAbility extends BaseAbility {

	private ItemStack balls;

	public SwitcherAbility(Battlecraft Battlecraft) {
		super(Battlecraft);
		balls = new ItemBuilder().type(Material.SNOW_BALL).amount(16).name("§b§lSwitcher Balls").glow().build();
		getItens().add(balls);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDamageByEntityListener(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getDamager() instanceof Snowball) {
				Snowball ball = (Snowball) e.getDamager();
				if (ball.getShooter() instanceof Player) {
					Player shooter = (Player) ball.getShooter();
					if (isUsing(shooter)) {
						Player hit = (Player) e.getEntity();
						if (!battlecraft.getGladiatorFightController().isInFight(shooter)) {
							if (battlecraft.getProtectionManager().removeProtection(shooter.getUniqueId())) {
								shooter.sendMessage("§8§lPROTEÇÃO §FVocê §7§lPERDEU§f sua proteção de spawn");
							}
							Location loc = shooter.getLocation();
							shooter.teleport(hit.getLocation());
							hit.teleport(loc);
							shooter.sendMessage("§5§LSWITCHER §fVoce trocou de lugar com §9§l" + hit.getName());
						}
					}
				}
			}
		}
	}

}
