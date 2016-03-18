package br.com.battlebits.ybattlecraft.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.constructors.Warp;
import br.com.battlebits.ybattlecraft.kit.Kit;
import net.md_5.bungee.api.chat.TextComponent;

public class KitCommand implements CommandExecutor, TabExecutor {

	private yBattleCraft battleCraft;
	private TextComponent spaceText;
	private TextComponent ownedKitsText;
	private TextComponent otherKitsText;
	private TextComponent noOwnedKitsText;
	private TextComponent youHaveAllKitsText;

	public KitCommand(yBattleCraft plugin) {
		this.battleCraft = plugin;
		this.spaceText = new TextComponent("§7, ");
		this.ownedKitsText = new TextComponent("§b§lSeus Kits §8§l>> ");
		this.otherKitsText = new TextComponent("§5§lOutros Kits §8§l>> ");
		this.noOwnedKitsText = new TextComponent("§7Voce nao possui nenhum kit, tente relogar.");
		this.youHaveAllKitsText = new TextComponent("§7Voce possui todos os kits!");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (battleCraft.getWarpManager().hasWarp(p)) {
				Warp w = battleCraft.getWarpManager().getWarpByName(battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId()));
				if (w.canUseKits()) {
					if (w.getKits().size() > 0) {
						if (!battleCraft.getKitManager().hasCurrentKit(p.getUniqueId())) {
							if (args.length == 0) {
								p.sendMessage("§3§lEscolha um kit §8§l>> §7Clique no chat ou use /kit [nome]");
								ArrayList<TextComponent> ownedKits = new ArrayList<>();
								ArrayList<TextComponent> otherKits = new ArrayList<>();
								ownedKits.add(ownedKitsText);
								otherKits.add(otherKitsText);
								for (Kit kit : w.getKits()) {
									if (battleCraft.getKitManager().canUseKit(p, kit.getName().toLowerCase())) {
										if (ownedKits.size() > 1) {
											ownedKits.add(spaceText);
										}
										ownedKits.add(kit.getClickToSelectMessage());
									} else {
										if (otherKits.size() > 1) {
											otherKits.add(spaceText);
										}
										otherKits.add(kit.getClickToBuyMessage());
									}
								}
								if (ownedKits.size() == 1) {
									ownedKits.add(noOwnedKitsText);
								}
								if (otherKits.size() == 1) {
									otherKits.add(youHaveAllKitsText);
								}
								p.spigot().sendMessage(ownedKits.toArray(new TextComponent[ownedKits.size()]));
								p.spigot().sendMessage(otherKits.toArray(new TextComponent[otherKits.size()]));
								p.sendMessage("§6§lMais kits §8§l>> §7Compre em §ehttp://www.battlebits.com.br");
							} else {
								if (w.getKit(args[0].toLowerCase()) != null) {
									battleCraft.getKitManager().giveKit(p, w.getKit(args[0].toLowerCase()), true);
								} else {
									p.sendMessage("§b§lKits §8§l>> §7O kit " + args[0] + " não existe!");
								}
							}
						} else {
							p.sendMessage("§b§lKits §8§l>> §7Voce ja esta usando um kit!");
						}
					} else {
						p.sendMessage("§b§lKits §8§l>> §7Nao ha kits disponiveis nessa warp.");
					}
				} else {
					p.sendMessage("§b§lKits §8§l>> §7Os kits estao desativados nessa warp.");
				}
			} else {
				p.sendMessage("§b§lKits §8§l>> §7Voce precisa estar em uma warp para usar os kits.");
			}
		} else {
			sender.sendMessage("Comando apenas para jogadores.");
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (battleCraft.getWarpManager().hasWarp(p)) {
				Warp w = battleCraft.getWarpManager().getWarpByName(battleCraft.getWarpManager().getPlayerWarp(p.getUniqueId()));
				if (w.canUseKits()) {
					if (!battleCraft.getKitManager().hasCurrentKit(p.getUniqueId())) {
						if (args.length <= 1) {
							ArrayList<String> r = new ArrayList<>();
							for (Kit k : w.getKits()) {
								if (args.length == 0 || k.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
									r.add(k.getName());
								}
							}
							return r;
						}
					}
				}
			}
		}
		return null;
	}
}