package br.com.battlebits.ybattlecraft.command;

import java.io.File;
import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import br.com.battlebits.ybattlecraft.yBattleCraft;
import br.com.battlebits.ybattlecraft.config.ConfigEnum;
import br.com.battlebits.ybattlecraft.evento.enums.EventType;

public class Evento implements CommandExecutor {

	private yBattleCraft m;

	public Evento(yBattleCraft m) {
		this.m = m;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("evento")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Voce nao e um player");
				return true;
			}
			Player p = (Player) sender;
			if (args.length == 1) {
				String arg1 = args[0];
				if (arg1.equalsIgnoreCase("entrar")) {
					if (yBattleCraft.currentEvento == null) {
						p.sendMessage(ChatColor.RED + "Nenhum evento esta rodando no momento!");
						return true;
					}
					yBattleCraft.currentEvento.addPlayer(p);
					return true;
				}
				if (arg1.equalsIgnoreCase("stop")) {
					if (p.hasPermission("flame.evento")) {
						if (yBattleCraft.currentEvento == null) {
							p.sendMessage(ChatColor.RED + "Nenhum evento esta rodando no momento!");
							return true;
						}
						yBattleCraft.currentEvento.stopEvento();
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("iniciar")) {
					if (p.hasPermission("flame.evento")) {
						p.sendMessage(ChatColor.RED + "Eventos: [RDM] ou [MDR]");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("teleport")) {
					if (p.hasPermission("flame.evento")) {
						p.teleport(new Location(Bukkit.getWorld("rdm"), 0, 100, 0));
						return true;
					}
				}
			} else if (args.length == 2) {
				if (p.hasPermission("flame.evento")) {
					if (args[0].equalsIgnoreCase("iniciar")) {
						String evento = args[1];
						if (evento.equalsIgnoreCase("rdm")) {
							yBattleCraft.currentEvento = new br.com.battlebits.ybattlecraft.evento.Evento(m, p, EventType.RDM);
							return true;
						} else if (evento.equalsIgnoreCase("mdr")) {
							yBattleCraft.currentEvento = new br.com.battlebits.ybattlecraft.evento.Evento(m, p, EventType.MDR);
							return true;
						}
						p.sendMessage(ChatColor.RED + "Eventos: [RDM] ou [MDR]");
						return true;
					}
					if (args[0].equalsIgnoreCase("vencedor")) {
						if (yBattleCraft.currentEvento == null) {
							p.sendMessage(ChatColor.RED + "Nenhum evento esta rodando no momento!");
							return true;
						}
						String vencedor = args[1];
						Player target = Bukkit.getPlayer(vencedor);
						if (target == null) {
							p.sendMessage(ChatColor.RED + "Player nao existe");
							return true;
						}
						yBattleCraft.currentEvento.win(target);
					}
					if (args[0].equalsIgnoreCase("adicionar")) {
						if (yBattleCraft.currentEvento == null) {
							p.sendMessage(ChatColor.RED + "Nenhum evento esta rodando no momento!");
							return true;
						}
						String vencedor = args[1];
						Player target = Bukkit.getPlayer(vencedor);
						if (target == null) {
							p.sendMessage(ChatColor.RED + "Player nao existe");
							return true;
						}
						yBattleCraft.currentEvento.addAdm(target);
					}
					if (args[0].equalsIgnoreCase("rdm")) {
						if (args[1].equalsIgnoreCase("setspawn")) {
							setRDMSpawn(p);
						}
						if (args[1].equalsIgnoreCase("addspawn")) {
							addRDMSpawn(p);
						}
						if (args[1].equalsIgnoreCase("setspawn1")) {
							setRDMLocation(p, 1);
						}
						if (args[1].equalsIgnoreCase("setspawn2")) {
							setRDMLocation(p, 2);
						}
					}
					if (args[0].equalsIgnoreCase("mdr")) {
						if (args[1].equalsIgnoreCase("setspawn")) {
							setMDRSpawn(p);
						}
						if (args[1].equalsIgnoreCase("aplicarkit")) {
							if (yBattleCraft.currentEvento == null) {
								p.sendMessage(ChatColor.RED + "Nenhum evento esta rodando no momento!");
								return true;
							}
							yBattleCraft.currentEvento.aplicarMDR(p);
						}
					}
					return true;
				}
			}
			if (yBattleCraft.currentEvento == null) {
				p.sendMessage(ChatColor.RED + "Nenhum evento esta rodando no momento!");
				return true;
			}
			yBattleCraft.currentEvento.addPlayer(p);
			return true;
		}
		return false;
	}

	private void addRDMSpawn(Player p) {
		if (!p.hasPermission("flame.evento")) {
			p.sendMessage(ChatColor.RED + "Voce nao possui permissao");
			return;
		}
		Location loc = p.getLocation();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		float yaw = loc.getYaw();
		float pitch = loc.getPitch();
		DecimalFormat dm = new DecimalFormat("##.##");
		String locString = loc.getWorld().getName() + "," + dm.format(x).replace(",", ".") + "," + y + "," + dm.format(z).replace(",", ".") + "," + dm.format(yaw).replace(",", ".") + "," + dm.format(pitch).replace(",", ".");
		FileConfiguration file = m.getConfiguration().getConfig(ConfigEnum.EVENTOS);
		file.set("eventos.rdm." + getLocations(), locString);
		saveConfig(file, ConfigEnum.EVENTOS);
		p.sendMessage(ChatColor.RED + "RDMSpawn adicionado com sucesso!");
	}

	private void setRDMLocation(Player p, int locationId) {
		if (!p.hasPermission("flame.evento")) {
			p.sendMessage(ChatColor.RED + "Voce nao possui permissao");
			return;
		}
		Location loc = p.getLocation();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		float yaw = loc.getYaw();
		float pitch = loc.getPitch();
		DecimalFormat dm = new DecimalFormat("##.##");
		String locString = loc.getWorld().getName() + "," + dm.format(x).replace(",", ".") + "," + y + "," + dm.format(z).replace(",", ".") + "," + dm.format(yaw).replace(",", ".") + "," + dm.format(pitch).replace(",", ".");
		FileConfiguration file = m.getConfiguration().getConfig(ConfigEnum.EVENTOS);
		file.set("eventos.rdm.location" + locationId, locString);
		saveConfig(file, ConfigEnum.EVENTOS);
		p.sendMessage(ChatColor.RED + "Localizacao " + locationId + " de 1v1 adicionado com sucesso!");
	}

	private void setRDMSpawn(Player p) {
		if (!p.hasPermission("flame.evento")) {
			p.sendMessage(ChatColor.RED + "Voce nao possui permissao");
			return;
		}
		Location loc = p.getLocation();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		float yaw = loc.getYaw();
		float pitch = loc.getPitch();
		DecimalFormat dm = new DecimalFormat("##.##");
		String locString = loc.getWorld().getName() + "," + dm.format(x).replace(",", ".") + "," + y + "," + dm.format(z).replace(",", ".") + "," + dm.format(yaw).replace(",", ".") + "," + dm.format(pitch).replace(",", ".");
		FileConfiguration file = m.getConfiguration().getConfig(ConfigEnum.EVENTOS);
		file.set("eventos.rdm.spawn", locString);
		saveConfig(file, ConfigEnum.EVENTOS);
		p.sendMessage(ChatColor.RED + "Spawn setado com sucesso!");
	}

	private int getLocations() {
		int i = 0;
		if (m.getConfiguration().getConfig(ConfigEnum.EVENTOS).getConfigurationSection("eventos.rdm") == null) {
			return i;
		}
		for (String loc : m.getConfiguration().getConfig(ConfigEnum.EVENTOS).getConfigurationSection("eventos.rdm").getKeys(false)) {
			if (loc.equals("spawn"))
				continue;
			i++;
		}
		return i;
	}

	private void setMDRSpawn(Player p) {
		if (!p.hasPermission("flame.evento")) {
			p.sendMessage(ChatColor.RED + "Voce nao possui permissao");
			return;
		}
		Location loc = p.getLocation();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		float yaw = loc.getYaw();
		float pitch = loc.getPitch();
		DecimalFormat dm = new DecimalFormat("##.##");
		String locString = loc.getWorld().getName() + "," + dm.format(x).replace(",", ".") + "," + y + "," + dm.format(z).replace(",", ".") + "," + dm.format(yaw).replace(",", ".") + "," + dm.format(pitch).replace(",", ".");
		FileConfiguration file = m.getConfiguration().getConfig(ConfigEnum.EVENTOS);
		file.set("eventos.mdr.spawn", locString);
		saveConfig(file, ConfigEnum.EVENTOS);
		p.sendMessage(ChatColor.RED + "Spawn setado com sucesso!");
	}

	private void saveConfig(FileConfiguration config, ConfigEnum enume) {
		File evento = new File(m.getDataFolder(), enume.getFile());
		try {
			config.save(evento);
		} catch (Exception e) {
		}
	}
}
