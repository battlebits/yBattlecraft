package br.com.battlebits.ybattlecraft.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.base.BaseCommandWithTab;
import br.com.battlebits.ybattlecraft.constructors.Warp;
import br.com.battlebits.ybattlecraft.kit.Kit;
import net.md_5.bungee.api.chat.TextComponent;

public class KitCommand extends BaseCommandWithTab {

	private yBattleCraft battleCraft;
	private TextComponent spaceText;
	private TextComponent ownedKitsText;
	private TextComponent otherKitsText;
	private TextComponent noOwnedKitsText;
	private TextComponent youHaveAllKitsText;

	public KitCommand(yBattleCraft plugin) {
		super(plugin);
		this.description = "Utilize este comando para escolher um Kit";
		this.battleCraft = plugin;
		this.spaceText = new TextComponent("§F, ");
		this.ownedKitsText = new TextComponent("§b§lSEUS KITS ");
		this.otherKitsText = new TextComponent("§5§lOUTROS KITS ");
		this.noOwnedKitsText = new TextComponent("§fVoce nao possui nenhum kit, tente relogar.");
		this.youHaveAllKitsText = new TextComponent("§fVoce possui todos os kits!");
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
								p.sendMessage("§3§lESCOLHA UM KIT §fClique no chat ou use /kit [nome]");
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
								p.sendMessage("§6§lMAIS KITS §fCompre em §e§lhttp://www.battlebits.com.br");
							} else {
								if (w.getKit(args[0].toLowerCase()) != null) {
									if (battleCraft.getKitManager().canUseKit(p, args[0].toLowerCase())) {
										battleCraft.getKitManager().giveKit(p, w.getKit(args[0].toLowerCase()), true);
									} else {
										p.sendMessage("§b§lKITS §fVocê não §3§lPOSSUI §feste kit!");
									}
								} else {
									p.sendMessage("§B§lKITS §fO kit §3§l" + args[0] + "§F não existe.");
								}
							}
						} else {
							p.sendMessage("§B§lKITS §fVocê já está §3§lUTILIZANDO§F um kit.");
						}
					} else {
						p.sendMessage("§B§lKITS §fNão ha kits §3§lDISPONIVEIS§F nessa warp.");
					}
				} else {
					p.sendMessage("§B§lKITS §fOs kits estao §3§lDESATIVADOS§F nessa warp.");
				}
			} else {
				p.sendMessage("§B§lKITS §fVoce precisa estar em uma warp para usar os kits.");
			}
		} else {
			sender.sendMessage("§B§lKITS §fComando disponivel apenas para jogadores.");
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