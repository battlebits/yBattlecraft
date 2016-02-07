package br.com.battlebits.battlecraft.kits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.Kit;
import br.com.battlebits.battlecraft.enums.KitType;
import br.com.battlebits.battlecraft.interfaces.KitInterface;
import br.com.battlebits.battlecraft.nms.Title;

@SuppressWarnings("deprecation")
public class HotPotato extends KitInterface {

	private HashMap<UUID, Long> tempo;
	private ArrayList<UUID> explodir;
	public static HashMap<UUID, ItemStack[]> armadura;

	public HotPotato(Main main) {
		super(main);
		explodir = new ArrayList<>();
		armadura = new HashMap<>();
		tempo = new HashMap<>();

	}

	@EventHandler
	public void onHotPotato(PlayerInteractEntityEvent event) {
		final Player p = event.getPlayer();
		if (!(event.getRightClicked() instanceof Player))
			return;
		final Player p2 = (Player) event.getRightClicked();
		if (p == null)
			return;
		if (!hasAbility(p)) {
			return;
		}
		if (p.getItemInHand().getType() != Material.TNT)
			return;
		int cooldown = 0;
		if (tempo.containsKey(p.getUniqueId()) && tempo.get(p.getUniqueId()) > System.currentTimeMillis())
			cooldown = (int) ((tempo.get(p.getUniqueId()) - System.currentTimeMillis()) / 1000);

		if (cooldown > 0) {
			p.sendMessage(ChatColor.GRAY + "HotPotato em cooldown de " + ChatColor.RESET + ChatColor.BOLD + getHourTime(cooldown) + ChatColor.GRAY + " segundos!");
			return;
		}
		if (!(event.getRightClicked() instanceof Player))
			return;

		armadura.put(p2.getUniqueId(), p2.getInventory().getArmorContents());
		p2.getInventory().setHelmet(getBatataQuente());
		p.updateInventory();
		p2.getWorld().playSound(p2.getLocation(), Sound.FIZZ, 5, 5);
		Title title = new Title("", ChatColor.RED + "Tire a bomba de sua cabeca para nao explodir!");
		title.send(p2);
		explodir.add(p2.getUniqueId());
		tempo.put(p.getUniqueId(), System.currentTimeMillis() + 30000);
		new BukkitRunnable() {

			@Override
			public void run() {
				if (explodir.contains(p2.getUniqueId())) {
					explodir.remove(p2.getUniqueId());
					devolverArmor(p);
					p2.getWorld().createExplosion(p2.getLocation(), 4F);
				}

			}
		}.runTaskLater(getMain(), 20 * 3);
	}

	private ItemStack getBatataQuente() {
		ItemStack tnt = new ItemStack(Material.TNT);
		ItemMeta tntnome = tnt.getItemMeta();
		tntnome.setDisplayName(ChatColor.DARK_PURPLE + "BATATA QUENTE");
		tnt.setItemMeta(tntnome);
		return tnt;
	}

	@EventHandler
	public void onHotPotato2(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (p == null)
			return;
		if (!hasAbility(p)) {
			return;
		}
		if (p.getItemInHand() == null)
			return;
		if (!p.getItemInHand().hasItemMeta())
			return;
		if (!p.getItemInHand().getItemMeta().hasDisplayName())
			return;
		if (!p.getItemInHand().getItemMeta().getDisplayName().contains("HOTPOTATO"))
			return;
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		int cooldown = 0;
		if (tempo.containsKey(p.getUniqueId()) && tempo.get(p.getUniqueId()) > System.currentTimeMillis())
			cooldown = (int) ((tempo.get(p.getUniqueId()) - System.currentTimeMillis()) / 1000);

		if (cooldown > 0) {
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 1.0F, 1.0F);
			p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "HotPotato " + ChatColor.GRAY + "em cooldown de " + ChatColor.RESET + ChatColor.BOLD + getHourTime(cooldown) + ChatColor.GRAY + " segundos!");
		}
		event.setCancelled(true);
		p.updateInventory();
	}

	@EventHandler
	public void onHotPotato3(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player))
			return;
		Player p = (Player) event.getWhoClicked();
		if (!explodir.contains(p.getUniqueId()))
			return;
		if (event.getCurrentItem() == null)
			return;
		if (!event.getCurrentItem().hasItemMeta())
			return;
		if (!event.getCurrentItem().getItemMeta().hasDisplayName())
			return;
		if (!event.getCurrentItem().getItemMeta().getDisplayName().contains("BATATA QUENTE"))
			return;
		event.getCurrentItem().setType(Material.AIR);
		p.updateInventory();
		explodir.remove(p.getUniqueId());
		devolverArmor(p);
		Title title = new Title("", ChatColor.GREEN + "Voce esta seguro agora");
		title.send(p);
	}

	private void devolverArmor(Player p) {
		if (armadura.containsKey(p.getUniqueId())) {
			p.getInventory().setArmorContents(armadura.remove(p.getUniqueId()));
		}
	}

	private String getHourTime(Integer i) {
		int minutes = i / 60;
		int seconds = i % 60;
		String disMinu = (minutes < 10 ? "" : "") + minutes;
		String disSec = (seconds < 10 ? "0" : "") + seconds;
		String formattedTime = disMinu + ":" + disSec;
		return formattedTime;
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		ItemStack tnt2 = new ItemStack(Material.TNT);
		ItemMeta tntnome = tnt2.getItemMeta();
		tntnome.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "HOTPOTATO");
		tnt2.setItemMeta(tntnome);
		kitItems.add(tnt2);
		return new Kit("hotpotato", "Coloque uma TNT na cabeça de seus inimigos e exploda eles todos", kitItems, new ItemStack(Material.TNT), 1000, KitType.ESTRATEGIA);
	}

}
