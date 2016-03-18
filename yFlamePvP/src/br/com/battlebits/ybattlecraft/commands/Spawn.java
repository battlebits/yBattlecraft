package br.com.battlebits.ybattlecraft.commands;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.managers.ProtectionManager;
import br.com.battlebits.ybattlecraft.warps.Warp1v1;

public class Spawn implements CommandExecutor {
	private HashMap<Player, Location> cooldown;
	private yBattleCraft m;

	public Spawn(yBattleCraft m) {
		this.m = m;
		this.cooldown = new HashMap<>();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Voce nao e um player");
			return true;
		}
		Player p = (Player) sender;
		if (label.equalsIgnoreCase("spawn")) {
			if (yBattleCraft.currentEvento != null && yBattleCraft.currentEvento.isOnEvent(p)) {
				p.sendMessage(ChatColor.RED + "Voce nao pode fazer isso enquanto esta em um evento!\nSe deseja sair do evento, saia do servidor e volte!");
				return true;
			}
			if (cooldown.containsKey(p)) {
				p.sendMessage(ChatColor.RED + "Voce esta em precesso de teleporte.");
				return true;
			}
			// TODO INVENCIVEL NA STARTGAME
			// TODO LUTA DE GLADIATOR
			if (Warp1v1.isIn1v1(p)) {
				p.sendMessage(ChatColor.RED + "Voce nao pode usar esse comando enquanto esta em uma luta no 1v1");
				return true;
			}
			if (p.getFallDistance() > 0) {
				p.sendMessage(ChatColor.RED + "Voce nao pode se teleportar enquanto esta caindo!");
				return true;
			}
			boolean canTeleport = true;
			ProtectionManager man = m.getProtectionManager();
			if (!m.getWarpManager().isInWarp(p, "lava challenge"))
				if (!man.isProtected(p.getUniqueId()))
					for (Player online : m.getServer().getOnlinePlayers()) {
						if (online.getUniqueId() == p.getUniqueId())
							continue;
						if (online.getWorld() != p.getWorld())
							continue;
						if (online.getLocation().distance(p.getLocation()) > 10)
							continue;
						if (!m.getProtectionManager().isProtected(online.getUniqueId())) {
							canTeleport = false;
							break;
						}
					}
			if (canTeleport) {
				m.teleportSpawn(p);
			} else {
				p.sendMessage(ChatColor.RED + "Ha alguem perto de voce! Espere 5 segundos para teleportar.");
				cooldown.put(p, p.getLocation().clone());
				new BukkitRunnable() {
					@Override
					public void run() {
						Location lastLoc = cooldown.get(p);
						double lastX = lastLoc.getX();
						double lastY = lastLoc.getY();
						double lastZ = lastLoc.getZ();
						Location actual = p.getLocation();
						double locX = actual.getX();
						double locY = actual.getY();
						double locZ = actual.getZ();
						if (lastX == locX && lastY == locY && lastZ == locZ) {
							m.teleportSpawn(p);
						} else {
							p.sendMessage(ChatColor.RED + "Você se mexeu e o processo de teleporte foi cancelado");
						}
						cooldown.remove(p);
					}
				}.runTaskLater(m, 100);
			}
			return true;
		}
		return false;
	}
}
