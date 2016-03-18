package br.com.battlebits.ybattlecraft.ability.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.ability.BaseAbility;
import br.com.battlebits.ybattlecraft.builder.ItemBuilder;

public class SwitcherAbility extends BaseAbility {

	private ItemStack balls;

	public SwitcherAbility(yBattleCraft yBattleCraft) {
		super(yBattleCraft);
		balls = new ItemBuilder().type(Material.SNOW_BALL).amount(16).name("�b�lSwitcher Balls").glow().build();
		getItens().add(balls);
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDamageByEntityListener(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getDamager() instanceof Snowball) {
				Snowball ball = (Snowball) e.getDamager();
				if (ball.getShooter() instanceof Player) {
					Player shooter = (Player) ball.getShooter();
					if (isUsing(shooter)) {
						Player hit = (Player) e.getEntity();
						if (battlecraft.getProtectionManager().removeProtection(shooter.getUniqueId())) {
							shooter.sendMessage("�7�lProte��o �8�l>> �7Voc� perdeu prote��o de spawn");
						}
						Location loc = shooter.getLocation();
						shooter.teleport(hit.getLocation());
						hit.teleport(loc);
						shooter.sendMessage("�5�lSwitcher �8�l>> �7Voce trocou de lugar com " + hit.getName());
					}
				}
			}
		}
	}

}
