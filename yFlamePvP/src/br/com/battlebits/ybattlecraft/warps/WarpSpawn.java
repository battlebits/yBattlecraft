package br.com.battlebits.ybattlecraft.warps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.constructors.BaseWarp;
import br.com.battlebits.ybattlecraft.constructors.Status;
import br.com.battlebits.ybattlecraft.constructors.Warp;
import br.com.battlebits.ybattlecraft.constructors.WarpScoreboard;
import br.com.battlebits.ybattlecraft.enums.KitType;
import br.com.battlebits.ybattlecraft.event.PlayerDeathInWarpEvent;
import br.com.battlebits.ybattlecraft.event.PlayerRemoveKitEvent;
import br.com.battlebits.ybattlecraft.event.PlayerSelectKitEvent;
import br.com.battlebits.ybattlecraft.event.PlayerWarpJoinEvent;
import br.com.battlebits.ybattlecraft.event.RealMoveEvent;
import br.com.battlebits.ybattlecraft.kit.Kit;
import net.md_5.bungee.api.ChatColor;

public class WarpSpawn extends BaseWarp {

	private Warp warp;
	private WarpScoreboard scoreboard;
	private UUID topKsUUID;

	public WarpSpawn(yBattleCraft yBattleCraft) {
		super(yBattleCraft);
	}

	@EventHandler
	public void onReal(RealMoveEvent event) {
		Player p = event.getPlayer();
		Block above = p.getLocation().subtract(0, 0.1, 0).getBlock();
		if (above.getType() == Material.GRASS)
			if (getMain().getProtectionManager().removeProtection(p.getUniqueId())) {
				p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Prote��o" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> "
						+ ChatColor.GRAY + "Voc� perdeu prote��o de spawn");
			}
	}

	@EventHandler
	public void onPlayerJoin(PlayerWarpJoinEvent e) {
		if (e.getWarp().getWarpName().equalsIgnoreCase(warp.getWarpName())) {
			new BukkitRunnable() {
				@Override
				public void run() {
					setTopKS(e.getPlayer());
				}
			}.runTaskLaterAsynchronously(yBattleCraft, 20L);
		} else {
			if (e.getPlayer().getUniqueId() == topKsUUID) {
				updateTopKS();
			}
		}
	}

	@EventHandler
	public void onPlayerQuitListener(PlayerQuitEvent e) {
		if (topKsUUID != null) {
			if (topKsUUID.toString().equalsIgnoreCase(e.getPlayer().getUniqueId().toString())) {
				updateTopKS();
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.PHYSICAL && !(e.getPlayer().getGameMode() == GameMode.CREATIVE)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerSelectKitListener(PlayerSelectKitEvent e) {
		if (isOnWarp(e.getPlayer())) {
			scoreboard.updateScoreValue(e.getPlayer(), "kit", "�e" + warp.getKit(e.getKitName()).getName());
		}
	}

	@EventHandler
	public void onPlayerRemoveKitListener(PlayerRemoveKitEvent e) {
		if (isOnWarp(e.getPlayer())) {
			scoreboard.updateScoreValue(e.getPlayer(), "kit", "�eNenhum");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDeathInWarpListener(PlayerDeathInWarpEvent e) {
		if (isOnWarp(e.getPlayer())) {
			scoreboard.updateScoreValue(e.getPlayer(), "deaths", "�b" + getMain().getStatusManager().getStatusByUuid(e.getPlayerUUID()).getDeaths());
			scoreboard.updateScoreValue(e.getPlayer(), "ks", "�b0");
			if (e.hasKiller()) {
				scoreboard.updateScoreValue(e.getKiller(), "kills",
						"�b" + getMain().getStatusManager().getStatusByUuid(e.getKillerUUID()).getKills());
				scoreboard.updateScoreValue(e.getKiller(), "ks",
						"�b" + getMain().getStatusManager().getStatusByUuid(e.getKillerUUID()).getKillstreak());
			}
			updateTopKS();
		}
	}

	public void updateTopKS() {
		new BukkitRunnable() {
			@Override
			public void run() {
				int ks = 0;
				UUID topks = null;
				for (UUID id : yBattleCraft.getWarpManager().getPlayersInWarp(warp.getWarpName())) {
					Status s = yBattleCraft.getStatusManager().getStatusByUuid(id);
					if (s.getKillstreak() > ks) {
						ks = s.getKillstreak();
						topks = id;
					}
				}
				topKsUUID = topks;
				for (UUID id : yBattleCraft.getWarpManager().getPlayersInWarp(warp.getWarpName())) {
					Player p = Bukkit.getPlayer(id);
					setTopKS(p);
				}
			}
		}.runTaskAsynchronously(yBattleCraft);
	}

	public void setTopKS(Player p) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (topKsUUID != null) {
					Player t = Bukkit.getPlayer(topKsUUID);
					if (t != null && t.isOnline()) {
						String name1 = "";
						String name2 = "";
						name1 = t.getName();
						if (t.getName().length() > 14) {
							name1 = t.getName().substring(0, 14);
							name2 = t.getName().substring(14, t.getName().length());
						}
						if (p != null && p.isOnline()) {
							scoreboard.updateScoreName(p, "topksplayer", "�b" + name1);
							scoreboard.updateScoreValue(p, "topksplayer",
									"�b" + name2 + " - " + yBattleCraft.getStatusManager().getStatusByUuid(topKsUUID).getKillstreak());
						}
						return;
					}
				}
				if (p != null && p.isOnline()) {
					scoreboard.updateScoreName(p, "topksplayer", "�bNinguem");
					scoreboard.updateScoreValue(p, "topksplayer", "�b - 0");
				}
			}
		}.runTaskAsynchronously(yBattleCraft);
	}

	@Override
	protected Warp getWarp(yBattleCraft battleCraft) {
		scoreboard = new WarpScoreboard("spawn") {
			@Override
			public void createScores(Player p) {
				createScore(p, "b4", "", "", 11);
				createScore(p, "kills", "�7Kills: ", "�b" + getMain().getStatusManager().getStatusByUuid(p.getUniqueId()).getKills(), 10);
				createScore(p, "deaths", "�7Deaths: ", "�b" + getMain().getStatusManager().getStatusByUuid(p.getUniqueId()).getDeaths(), 9);
				createScore(p, "ks", "�7KillStreak: ", "�b0", 8);
				createScore(p, "b3", "", "", 7);
				createScore(p, "topks", "�7Top Kill", "�7Streak:", 6);
				createScore(p, "topksplayer", "�bNinguem", "�b - 0", 5);
				createScore(p, "b2", "", "", 4);
				createScore(p, "kit", "�7Kit: ", "�e" + getMain().getKitManager().getCurrentKit(p.getUniqueId()), 3);
				createScore(p, "b1", "", "", 2);
				createScore(p, "site", "�6www.battle", "�6bits.com.br", 1);
			}
		};
		warp = new Warp("Spawn", "Spawn do Servidor", new ItemStack(Material.NETHER_STAR),
				new Location(Bukkit.getWorld("spawnWarp"), 0.5, 87.5, 0.5, 180f, 0), 25.5, scoreboard, true, true);
		// warp.addKit(new Kit(battleCraft, "Deshzin", "Aperte espa�o para dar
		// um double jump.", new ArrayList<ItemStack>(),
		// new ItemStack(Material.FIREWORK), 1000, KitType.MOBILIDADE,
		// Arrays.asList("deshzin")));
		warp.addKit(new Kit(battleCraft, "Grappler", "Movimente mais rapido sua corda!", new ArrayList<>(), new ItemStack(Material.LEASH), 1000,
				KitType.MOBILIDADE, Arrays.asList("grappler")));
		warp.addKit(new Kit(battleCraft, "Ninja", "Aperte SHIFT para teleportar-se para o utltimo jogador hitado.", new ArrayList<ItemStack>(),
				new ItemStack(Material.EMERALD), 1000, KitType.ESTRATEGIA, Arrays.asList("ninja")));
		warp.addKit(new Kit(battleCraft, "PvP", "Kit padrao para PVP", new ArrayList<ItemStack>(), new ItemStack(Material.DIAMOND_SWORD), 0,
				KitType.NEUTRO, new ArrayList<>()));
		warp.addKit(new Kit(battleCraft, "Snail", "Deixe seus inimigos mais lerdos", new ArrayList<ItemStack>(),
				new ItemStack(Material.FERMENTED_SPIDER_EYE), 1000, KitType.FORCA, Arrays.asList("snail")));
		warp.addKit(new Kit(battleCraft, "Stomper", "Esmague seus inimigos", new ArrayList<ItemStack>(), new ItemStack(Material.IRON_BOOTS), 2000,
				KitType.FORCA, Arrays.asList("stomper")));
		warp.addKit(new Kit(battleCraft, "Viper", "Deixe seus inimigos envenenados", new ArrayList<ItemStack>(), new ItemStack(Material.SPIDER_EYE),
				1000, KitType.FORCA, Arrays.asList("viper")));
		warp.addKit(new Kit(battleCraft, "Switcher", "Troque de lugar com suas snowballs.", new ArrayList<ItemStack>(),
				new ItemStack(Material.SNOW_BALL), 1000, KitType.ESTRATEGIA, Arrays.asList("switcher")));
		warp.addKit(new Kit(battleCraft, "HotPotato", "Coloque uma TNT na cabe�a de seus inimigos e exploda eles todos", new ArrayList<ItemStack>(),
				new ItemStack(Material.TNT), 1000, KitType.ESTRATEGIA, Arrays.asList("hotpotato")));
		warp.addKit(new Kit(battleCraft, "Kangaroo", "Utilize seu foguete para ser lan�ado para onde voc� desejar", new ArrayList<ItemStack>(),
				new ItemStack(Material.FIREWORK), 0, KitType.MOBILIDADE, Arrays.asList("kangaroo")));
		warp.addKit(new Kit(battleCraft, "Lifesteal", "Ao atacar um jogador tem chances de aumentar meio cora��o.", new ArrayList<ItemStack>(),
				new ItemStack(Material.TORCH), 1000, KitType.FORCA, Arrays.asList("lifesteal")));
		warp.addKit(new Kit(battleCraft, "Magneto", "Sugue seus inimigos por 5 segundos", new ArrayList<ItemStack>(),
				new ItemStack(Material.IRON_INGOT), 1000, KitType.ESTRATEGIA, Arrays.asList("magneto")));
		warp.addKit(new Kit(battleCraft, "Anchor", "Nao leve e nao de knockback.", new ArrayList<ItemStack>(), new ItemStack(Material.ANVIL), 1000,
				KitType.MOBILIDADE, Arrays.asList("anchor")));
		warp.addKit(new Kit(battleCraft, "Gladiator", "Puxe um jogador clicando com o direito nele para uma luta nos ceus.",
				new ArrayList<ItemStack>(), new ItemStack(Material.IRON_FENCE), 1000, KitType.ESTRATEGIA, Arrays.asList("gladiator")));
		// warp.addKit(new Kit(battleCraft, "JackHammer", "Quebre um bloco que
		// todos serao quebrados.", new ArrayList<ItemStack>(),
		// new ItemStack(Material.STONE_AXE), 1000, KitType.ESTRATEGIA,
		// Arrays.asList("jackhammer")));
		return warp;
	}

}