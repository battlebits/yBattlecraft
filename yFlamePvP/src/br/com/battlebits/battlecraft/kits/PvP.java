package br.com.battlebits.battlecraft.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.Kit;
import br.com.battlebits.battlecraft.enums.KitType;
import br.com.battlebits.battlecraft.interfaces.KitInterface;

public class PvP extends KitInterface {

	public PvP(Main main) {
		super(main);
	}

	@Override
	public Kit getKit() {
		return new Kit("pvp", "Kit padrao para PVP", new ItemStack(Material.DIAMOND_SWORD), 0, KitType.NEUTRO);
	}
}
