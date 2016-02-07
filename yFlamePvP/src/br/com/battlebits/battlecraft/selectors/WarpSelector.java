package br.com.battlebits.battlecraft.selectors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.battlebits.battlecraft.constructors.Warp;
import br.com.battlebits.battlecraft.managers.WarpManager;
import br.com.battlebits.battlecraft.utils.Formatter;

public class WarpSelector extends InventoryInterface {
	private WarpManager manager;

	public WarpSelector(WarpManager manager) {
		super(ChatColor.AQUA + "" + ChatColor.BOLD + "Warps", 9);
		this.manager = manager;
		generateContents();
	}

	@Override
	public void generateContents() {
		List<Warp> warpList = new ArrayList<>();
		for (Warp warp : manager.getWarps()) {
			if(warp.getWarpName().equalsIgnoreCase("spawn"))
				continue;
			warpList.add(warp);
		}
		Collections.sort(warpList, new Comparator<Warp>() {
			public int compare(Warp o1, Warp o2) {
				return o1.getWarpName().compareTo(o2.getWarpName());
			}
		});
		for (int i = 0; i < warpList.size(); i++) {
			Warp warp = warpList.get(i);
			ItemStack item = warp.getIcon();
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + Formatter.getFormattedName(warp.getWarpName()));
			meta.setLore(wrap(warp.getDescription()));
			item.setItemMeta(meta);
			getInventory().setItem(i, item);
		}
	}

	private List<String> wrap(String string) {
		String[] split = string.split(" ");
		string = "";
		ChatColor color = ChatColor.GRAY;
		ArrayList<String> newString = new ArrayList<String>();
		for (int i = 0; i < split.length; i++) {
			if (string.length() > 20 || string.endsWith(".") || string.endsWith("!")) {
				newString.add(color + string);
				if (string.endsWith(".") || string.endsWith("!"))
					newString.add("");
				string = "";
			}
			string += (string.length() == 0 ? "" : " ") + split[i];
		}
		newString.add(color + string);
		return newString;
	}

}
