package br.com.battlebits.ybattlecraft.kit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.ability.BaseAbility;
import br.com.battlebits.ybattlecraft.enums.KitType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class Kit {

	private String name;
	private ArrayList<ItemStack> itens;
	private String info;
	private ItemStack icon;
	private int price;
	private KitType type;
	private ArrayList<String> abilities;
	private TextComponent clickToSelectMessage;
	private TextComponent clickToBuyMessage;

	public Kit(yBattleCraft battleCraft, String kitName, String kitInfo, List<ItemStack> kitItens, ItemStack icon, int price, KitType kitType,
			List<String> kitAbilities) {
		this.name = kitName;
		this.info = kitInfo;
		this.itens = new ArrayList<>(kitItens);
		this.icon = icon;
		this.price = price;
		this.type = kitType;
		this.abilities = new ArrayList<>(kitAbilities);
		for (String str : abilities) {
			str = str.toLowerCase();
			BaseAbility ability = battleCraft.getAbilityManager().getAbility(str);
			if (ability != null) {
				for (ItemStack i : ability.getItens()) {
					itens.add(i);
				}
			}
		}
		clickToSelectMessage = new TextComponent("§7" + kitName);
		clickToSelectMessage
				.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new TextComponent[] { new TextComponent("§bClique aqui para selecionar esse Kit") }));
		clickToSelectMessage.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/kit " + kitName.toLowerCase()));
		clickToBuyMessage = new TextComponent("§7" + kitName);
		clickToBuyMessage
				.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new TextComponent[] { new TextComponent("§eClique para obter mais informacoes") }));
		clickToBuyMessage.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/kit " + kitName.toLowerCase()));
	}

	public KitType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public ArrayList<ItemStack> getItens() {
		return itens;
	}

	public String getInfo() {
		return info;
	}

	public ItemStack getIcon() {
		return icon;
	}

	public int getPrice() {
		return price;
	}

	public ArrayList<String> getAbilities() {
		return abilities;
	}

	public TextComponent getClickToSelectMessage() {
		return clickToSelectMessage;
	}

	public TextComponent getClickToBuyMessage() {
		return clickToBuyMessage;
	}

}
