package br.com.battlebits.ybattlecraft.ability;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseAbility;
import br.com.battlebits.ybattlecraft.builder.ItemBuilder;
import br.com.battlebits.ybattlecraft.event.PlayerDeathInWarpEvent;
import br.com.battlebits.ybattlecraft.nms.Title;
import br.com.battlebits.ycommon.bukkit.api.admin.AdminMode;
import br.com.battlebits.ycommon.bukkit.event.update.UpdateEvent;
import de.inventivegames.holograms.HologramAPI;

public class HotPotatoAbility extends BaseAbility {

	private HashMap<UUID, Long> playersWithHotPotato;
	private HashMap<UUID, ItemStack> playerHelmet;
	private Title removeTitle;
	private Title secureTitle;
	private ItemStack abilityItem;
	private ItemStack tntItem;

	public HotPotatoAbility(yBattleCraft yBattleCraft) {
		super(yBattleCraft);
		playersWithHotPotato = new HashMap<>();
		playerHelmet = new HashMap<>();
		removeTitle = new Title("", "§c§lRemova a batata quente da sua cabeca!");
		secureTitle = new Title("", "§9§lAgora voce esta seguro!");
		ItemBuilder builder = new ItemBuilder();
		abilityItem = builder.type(Material.BAKED_POTATO).amount(1).glow().name("§c§lHot Potato").lore("§7Coloque uma batata quente na cabeca do seu inimigo").build();
		tntItem = builder.type(Material.TNT).amount(1).name("§5§lHot Potato").lore("§7Remova da sua cabeca o mais rapido possivel!").build();
		getItens().add(abilityItem);
	}

	
	@EventHandler
	public void onUpdateListener(UpdateEvent e) {
		for (Entry<UUID, Long> entry : playersWithHotPotato.entrySet()) {
			Player p = Bukkit.getPlayer(entry.getKey());
			if (p != null && p.isOnline()) {
				if (System.currentTimeMillis() >= entry.getValue()) {
					playersWithHotPotato.remove(entry.getKey());
					if (playerHelmet.containsKey(entry.getKey())) {
						p.getInventory().setHelmet(playerHelmet.get(entry.getKey()));
						playerHelmet.remove(entry.getKey());
					} else {
						p.getInventory().setHelmet(null);
					}
					p.getWorld().createExplosion(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), 4F, false, false);
				}
			} else {
				playersWithHotPotato.remove(entry.getKey());
			}
		}
	}

	@EventHandler
	public void onPlayerQuitListener(PlayerQuitEvent e) {
		if (playersWithHotPotato.containsKey(e.getPlayer().getUniqueId())) {
			playersWithHotPotato.remove(e.getPlayer().getUniqueId());
		}
		if (playerHelmet.containsKey(e.getPlayer().getUniqueId())) {
			playerHelmet.remove(e.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void onPlayerDeathInWarpListener(PlayerDeathInWarpEvent e) {
		if (playersWithHotPotato.containsKey(e.getPlayer().getUniqueId())) {
			playersWithHotPotato.remove(e.getPlayer().getUniqueId());
		}
		if (playerHelmet.containsKey(e.getPlayer().getUniqueId())) {
			playerHelmet.remove(e.getPlayer().getUniqueId());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerInteractEntityListener(PlayerInteractEntityEvent e) {
		if (isUsing(e.getPlayer())) {
			if (e.getRightClicked() instanceof Player) {
				if (e.getPlayer().getItemInHand() != null) {
					if (e.getPlayer().getItemInHand().equals(abilityItem)) {
						if (!battlecraft.getCooldownManager().isOnCooldown(e.getPlayer().getUniqueId(), "hotpotatoability")) {
							Player target = (Player) e.getRightClicked();
							if (!AdminMode.getInstance().isAdmin(target)) {
								if (!battlecraft.getProtectionManager().isProtected(target.getUniqueId())) {
									if (target.getInventory().getHelmet() == null || !target.getInventory().getHelmet().equals(tntItem)) {
										if (battlecraft.getProtectionManager().removeProtection(e.getPlayer().getUniqueId())) {
											e.getPlayer().sendMessage("§7§lPROTEÇÃO §FVocê §8§lPERDEU§f sua proteção de spawn");
										}
										if (target.getInventory().getHelmet() != null) {
											if (target.getInventory().getHelmet().getType() != Material.AIR) {
												playerHelmet.put(target.getUniqueId(), target.getInventory().getHelmet());
											}
										}
										target.playSound(target.getLocation(), Sound.FIZZ, 5, 5);
										target.getInventory().setHelmet(tntItem);
										target.updateInventory();
										playersWithHotPotato.put(target.getUniqueId(), System.currentTimeMillis() + 3000);
										battlecraft.getCooldownManager().setCooldown(e.getPlayer().getUniqueId(), "hotpotatoability", 30);
										if (((CraftPlayer) target).getHandle().playerConnection.networkManager.getVersion() >= 47) {
											removeTitle.send(target);
										} else {
											HologramAPI.createRunningHologram(target, "§c§lRemova a batata quente da sua cabeca!", 0).spawn(40);
										}
									} else {
										e.getPlayer().sendMessage("§5§lHOTPOTATO §fEste jogador nao pode receber a batata quente!");
									}
								} else {
									e.getPlayer().sendMessage("§5§lHOTPOTATO §fEste jogador nao pode receber a batata quente!");
								}
							}
						} else {
							e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
							e.getPlayer().sendMessage("§5§lHOTPOTATO §fAguarde §9§l" + battlecraft.getCooldownManager().getCooldownTimeFormated(e.getPlayer().getUniqueId(), "hotpotatoability").toUpperCase() + "§f para utilizar sua habilidade!");
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerInteractListener(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (isUsing(e.getPlayer())) {
				if (e.getPlayer().getItemInHand() != null) {
					if (e.getPlayer().getItemInHand().equals(abilityItem)) {
						if (battlecraft.getCooldownManager().isOnCooldown(e.getPlayer().getUniqueId(), "hotpotatoability")) {
							e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
							e.getPlayer().sendMessage("§5§lHOTPOTATO §fAguarde §9§l" + battlecraft.getCooldownManager().getCooldownTimeFormated(e.getPlayer().getUniqueId(), "hotpotatoability").toUpperCase() + "§f para utilizar sua habilidade!");
						}
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClickListener(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			if (e.getInventory().getType() == InventoryType.CRAFTING) {
				if (e.getSlot() == 39) {
					if (e.getCurrentItem() != null) {
						if (e.getCurrentItem().equals(tntItem)) {
							Player p = (Player) e.getWhoClicked();
							e.setCancelled(true);
							if (playersWithHotPotato.containsKey(p.getUniqueId())) {
								playersWithHotPotato.remove(p.getUniqueId());
								if (((CraftPlayer) p).getHandle().playerConnection.networkManager.getVersion() >= 47) {
									secureTitle.send(p);
								} else {
									HologramAPI.createRunningHologram(p, "§9§lAgora voce esta seguro!", 0).spawn(40);
								}
							}
							if (playerHelmet.containsKey(p.getUniqueId())) {
								p.getInventory().setHelmet(playerHelmet.get(p.getUniqueId()));
							} else {
								p.getInventory().setHelmet(null);
							}
							p.updateInventory();
						}
					}
				}
			}
		}
	}

}
