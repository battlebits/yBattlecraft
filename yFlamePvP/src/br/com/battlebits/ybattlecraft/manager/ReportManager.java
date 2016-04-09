package br.com.battlebits.ybattlecraft.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.builder.ItemBuilder;
import br.com.battlebits.ybattlecraft.constructors.Report;
import br.com.battlebits.ybattlecraft.enums.ReportStatus;
import br.com.battlebits.ybattlecraft.nms.barapi.BarAPI;
import me.flame.utils.event.UpdateEvent;
import me.flame.utils.event.UpdateEvent.UpdateType;
import me.flame.utils.utils.UUIDFetcher;

public class ReportManager {

	private yBattleCraft battleCraft;
	private HashMap<Integer, Inventory> pages;
	private HashMap<UUID, Report> reports;
	private boolean ordering;
	private ItemBuilder builder;

	public ReportManager(yBattleCraft bc) {
		battleCraft = bc;
		pages = new HashMap<>();
		reports = new HashMap<UUID, Report>();
		ordering = false;
		builder = new ItemBuilder();
		Bukkit.getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void onUpdate(UpdateEvent e) {
				if (e.getType() == UpdateType.MINUTE) {
					checkExpires();
				}
			}

			@EventHandler
			public void onInventoryClick(InventoryClickEvent e) {
				if (e.getInventory() != null) {
					if (e.getInventory().getType() == InventoryType.CHEST) {
						if (e.getInventory().getTitle().startsWith("Reports - Página")) {
							e.setCancelled(true);
							if (e.getClickedInventory() == e.getInventory()) {
								new BukkitRunnable() {

									@Override
									public void run() {
										Player p = (Player) e.getWhoClicked();
										if (e.getSlot() >= 9 && e.getSlot() <= 44) {
											if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
												String name = e.getCurrentItem().getItemMeta().getDisplayName().split("§7")[1];
												UUID id = null;
												Player t = Bukkit.getPlayer(name);
												if (t != null) {
													id = t.getUniqueId();
												} else {
													try {
														id = UUIDFetcher.getUUIDOf(name);
													} catch (Exception ex) {
													}
												}
												if (id != null) {
													if (reports.containsKey(id)) {
														Report report = reports.get(id);
														if (report.getStatus() == ReportStatus.OFFLINE) {
															removeReports(id);
															e.getInventory().setItem(e.getSlot(), null);
															p.sendMessage("§9§lREPORT §fReport §3§lremovido§f!");
														} else if (report.getStatus() == ReportStatus.BANNED) {
															removeReports(id);
															e.getInventory().setItem(e.getSlot(), null);
															p.sendMessage("§9§lREPORT §fReport §3§lremovido§f!");
														} else if (report.getStatus() == ReportStatus.OPEN) {
															p.teleport(t.getLocation());
															report.setStatus(ReportStatus.CHECKING);
															p.closeInventory();
															orderPages();
															p.sendMessage("§9§LREPORT §FTeleportado para §3§L" + t.getName() + "§f!");
														} else if (report.getStatus() == ReportStatus.CHECKING) {
															if (e.getAction() == InventoryAction.PICKUP_HALF) {
																removeReports(id);
																e.getInventory().setItem(e.getSlot(), null);
																p.sendMessage("§9§lREPORT §fReport §3§lremovido§f!");
															} else {
																p.teleport(t.getLocation());
																p.closeInventory();
																orderPages();
																p.sendMessage("§9§LREPORT §FTeleportado para §3§L" + t.getName() + "§f!");
															}
														}
													} else {
														p.sendMessage("§9§lREPORT §fJogador não §3§lencontrado§7!");
													}
												} else {
													p.sendMessage("§9§lREPORT §fJogador não §3§lencontrado§7!");
												}
											}
										} else if (e.getSlot() == 0) {
											if (e.getCurrentItem().getType() == Material.INK_SACK) {
												int page = Integer.valueOf(e.getInventory().getTitle().split(" ")[3]);
												if (pages.containsKey(page - 1)) {
													p.openInventory(pages.get(page - 1));
												}
											}
										} else if (e.getSlot() == 8) {
											if (e.getCurrentItem().getType() == Material.INK_SACK) {
												int page = Integer.valueOf(e.getInventory().getTitle().split(" ")[3]);
												if (pages.containsKey(page + 1)) {
													p.openInventory(pages.get(page + 1));
												}
											}
										}
									}
								}.runTaskAsynchronously(battleCraft);
							}
						}
					}
				}
			}
		}, battleCraft);

	}

	@SuppressWarnings("deprecation")
	public void report(Player reporter, Player reported, String reason) {
		if (!reports.containsKey(reported.getUniqueId())) {
			reports.put(reported.getUniqueId(), new Report(reported.getUniqueId(), reported.getName()));
		}
		Report r = reports.get(reported.getUniqueId());
		if (!r.getReasons().contains(reason)) {
			r.getReasons().add(reason);
		}
		if (!r.getReporters().contains(reporter.getName())) {
			r.getReporters().add(reporter.getName());
		}
		r.updateExpire();
		orderPages();
		for (Player staff : Bukkit.getOnlinePlayers()) {
			if (battleCraft.getPermissions().isTrial(staff)) {
				BarAPI.setMessage("§fNovo §9§LREPORT §frecebido!", 2);
				staff.playSound(staff.getLocation(), Sound.ITEM_BREAK, 0.25F, 1F);
			}
		}
	}

	public void orderPages() {
		if (!ordering) {
			ordering = true;
			new BukkitRunnable() {
				@Override
				public void run() {
					int page = 1;
					int current = 9;
					int total = 0;
					for (Report report : reports.values()) {
						if (!pages.containsKey(page)) {
							Inventory inv = Bukkit.createInventory(null, 54, "Reports - Página " + page);
							ItemStack blank = builder.type(Material.STAINED_GLASS_PANE).durability(15).name("§0").build();
							if (page > 1) {
								inv.setItem(0, builder.type(Material.INK_SACK).durability(10).name("§7« §aPágina Anteiror").build());
							} else {
								inv.setItem(0, blank);
							}
							inv.setItem(1, blank);
							inv.setItem(2, blank);
							inv.setItem(3, blank);
							inv.setItem(4, builder.type(Material.SKULL_ITEM).amount(1).name("§9§lReports")
									.lore("§3§l" + reports.size() + "§7 reports no total!").build());
							inv.setItem(5, blank);
							inv.setItem(6, blank);
							inv.setItem(7, blank);
							if (Math.ceil(reports.size() / 36) + 1 > page) {
								inv.setItem(8, builder.type(Material.INK_SACK).durability(10).name("§aPróxima Página §f»").build());
							} else {
								inv.setItem(8, blank);
							}
							inv.setItem(45, blank);
							inv.setItem(46, blank);
							inv.setItem(47, blank);
							inv.setItem(48, blank);
							inv.setItem(49, blank);
							inv.setItem(50, blank);
							inv.setItem(51, blank);
							inv.setItem(52, blank);
							inv.setItem(53, blank);
							pages.put(page, inv);
						}
						Inventory inv = pages.get(page);
						String prefix = "";
						String reason = "";
						for (String s : report.getReasons()) {
							if (!reason.isEmpty()) {
								reason += ", ";
							}
							reason += s;
						}
						String reporters = "";
						for (String s : report.getReporters()) {
							if (!reporters.isEmpty()) {
								reporters += ", ";
							}
							reporters += s;
						}
						switch (report.getStatus()) {
						case BANNED:
							prefix = "§c§l»";
							builder.type(Material.REDSTONE_BLOCK);
							builder.lore("§7Este jogador se encontra §c§lBANIDO §7clique para apagar este report.");
							break;
						case OFFLINE:
							prefix = "§9§l»";
							builder.type(Material.LAPIS_BLOCK);
							builder.lore("§7Este jogador se encontra §9§lOFFLINE §7clique para apagar este report.");
							break;
						case CHECKING:
							prefix = "§e§l»";
							builder.type(Material.GOLD_BLOCK);
							builder.lore("§0\\n§7Razões: §b" + reason + "\\n§7Reportado por: §b" + reporters
									+ "\\n§0\\n§7Clique esquerdo para §a§lverificar§7 este jogador.\\n§7Clique direito para §e§lapagar§7 este report.");
							break;
						case OPEN:
							prefix = "§a§l»";
							builder.type(Material.EMERALD_BLOCK);
							builder.lore("§0\\n§7Razões: §b" + reason + "\\n§7Reportado por: §b" + reporters
									+ "\\n§0\\n§7Clique para §a§lverificar§7 este jogador.");
							break;
						}
						builder.amount(report.getReasons().size());
						builder.name(prefix + " §7" + report.getReportedName());
						inv.setItem(current, builder.build());
						current++;
						total++;
						if (current > 44) {
							current = 9;
							page++;
						}
						if (total == reports.size()) {
							while (current <= 44) {
								inv.setItem(current, null);
								current++;
							}
						}
					}
					ordering = false;
				}
			}.runTaskAsynchronously(battleCraft);
		}
	}

	public void checkExpires() {
		new BukkitRunnable() {
			@Override
			public void run() {
				Iterator<Entry<UUID, Report>> i = reports.entrySet().iterator();
				while (i.hasNext()) {
					Entry<UUID, Report> entry = i.next();
					try {
						// if
						// (Main.getPlugin().getBanManager().isBanned(entry.getValue().getReported()))
						// {
						// entry.getValue().setStatus(ReportStatus.BANNED);
						// } else {
						if (Bukkit.getPlayer(entry.getValue().getReported()) == null) {
							entry.getValue().setStatus(ReportStatus.OFFLINE);
						} else {
							if (entry.getValue().getStatus() == ReportStatus.OFFLINE) {
								entry.getValue().setStatus(ReportStatus.OPEN);
							} else if (entry.getValue().getExpire() < System.currentTimeMillis()) {
								i.remove();
							}
						}
						// }
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				orderPages();
			}
		}.runTaskAsynchronously(battleCraft);
	}

	public HashMap<Integer, Inventory> getPages() {
		return pages;
	}

	public HashMap<UUID, Report> getReports() {
		return reports;
	}

	public void open(Player p) {
		if (reports.size() > 0 && pages.containsKey(1)) {
			if (!battleCraft.getAdminMode().isAdmin(p)) {
				battleCraft.getAdminMode().setAdmin(p);
			}
			p.openInventory(pages.get(1));
		} else {
			p.sendMessage("§9§lREPORT §fNenhum report no momento!");
		}
	}

	public void removeReports(UUID id) {
		reports.remove(id);
		orderPages();
	}

}
