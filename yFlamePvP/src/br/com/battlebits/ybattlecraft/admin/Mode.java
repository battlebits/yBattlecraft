package br.com.battlebits.ybattlecraft.admin;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import br.com.battlebits.ybattlecraft.yBattleCraft;

public class Mode {
	public ArrayList<Player> admin = new ArrayList<Player>();
	private yBattleCraft m;

	public Mode(yBattleCraft m) {
		this.m = m;
	}

	public void setAdmin(Player p) {
		if (!admin.contains(p) || admin.isEmpty()) {
			admin.add(p);
		}
		p.setGameMode(GameMode.CREATIVE);
		m.getVanish().makeVanished(p);
		m.getVanish().updateVanished();
		p.sendMessage(ChatColor.RED + "Modo Admin ATIVADO");
		p.sendMessage(ChatColor.GRAY + "Voce esta invisivel para " + getInvisible(p) + " e abaixo!");
	}

	public void setPlayer(Player p) {
		if (admin.contains(p)) {
			p.sendMessage(ChatColor.GREEN + "Modo Admin DESATIVADO");
		}
		p.sendMessage(ChatColor.GRAY + "Voce esta visivel para todos os players");
		admin.remove(p);
		p.setGameMode(GameMode.SURVIVAL);
		m.getVanish().makeVisible(p);
		m.getVanish().updateVanished();
	}

	public boolean isAdmin(Player p) {
		return admin.contains(p);
	}

	public void removeAdmin(Player p) {
		admin.remove(p);
	}

	private String getInvisible(Player p) {
		if (m.getVanish().getPlayerLevel(p) == VLevel.DONO)
			return VLevel.ADMIN.name();
		if (m.getVanish().getPlayerLevel(p) == VLevel.ADMIN)
			return VLevel.MODPLUS.name();
		if (m.getVanish().getPlayerLevel(p) == VLevel.MODPLUS)
			return VLevel.MOD.name();
		if (m.getVanish().getPlayerLevel(p) == VLevel.MOD)
			return VLevel.YOUTUBER.name();
		return VLevel.PLAYER.name();
	}
}