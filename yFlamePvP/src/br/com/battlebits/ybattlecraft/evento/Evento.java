package br.com.battlebits.ybattlecraft.evento;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.config.ConfigEnum;
import br.com.battlebits.ybattlecraft.evento.constructors.Cabine;
import br.com.battlebits.ybattlecraft.evento.enums.EventState;
import br.com.battlebits.ybattlecraft.evento.enums.EventType;
import br.com.battlebits.ybattlecraft.utils.API;

public class Evento {
	private EventType type;
	private EventState state;
	private static Battlecraft m;
	private int totalPlayers = 0;
	private int tempo;
	private Integer runnable = null;
	private List<Player> participantes = new ArrayList<>();
	private List<Player> organizadores = new ArrayList<>();
	private List<Player> espectadores = new ArrayList<>();
	private Location mdrSpawn;
	private Location rdmSpawn;
	private boolean chatOn;
	// public Player rdm;
	// public Player desafiante;
	// public Location loc1;
	// public Location loc2;
	private List<Cabine> locations = new ArrayList<>();

	public Evento(final Battlecraft battle, Player organizador, final EventType type) {
		this.type = type;
		this.state = EventState.WAITING;
		m = battle;
		chatOn = false;
		if (type == EventType.RDM) {
			if (m.getConfiguration().getConfig(ConfigEnum.EVENTOS).getConfigurationSection("eventos.rdm") == null) {
				organizador.sendMessage(ChatColor.RED + "Nao ha spawns setados para o evento!");
				Battlecraft.currentEvento = null;
				return;
			}
			for (String loc : m.getConfiguration().getConfig(ConfigEnum.EVENTOS).getConfigurationSection("eventos.rdm.locations.").getKeys(false)) {
				if (loc.equals("spawn"))
					continue;
				String locString = m.getConfiguration().getConfig(ConfigEnum.EVENTOS).getString("eventos.rdm.locations." + loc);
				String[] coords = locString.split(",");
				String worldString = coords[0];
				World world = Bukkit.getWorld(worldString);
				double x = Double.valueOf(coords[1]);
				double y = Double.valueOf(coords[2]);
				double z = Double.valueOf(coords[3]);
				float yaw = Float.valueOf(coords[4]);
				float pitch = Float.valueOf(coords[5]);
				Location location = new Location(world, x, y, z, yaw, pitch);
				Cabine cabine = new Cabine(null, location);
				locations.add(cabine);
			}
			String locString = m.getConfiguration().getConfig(ConfigEnum.EVENTOS).getString("eventos.rdm.spawn");
			if (locString.isEmpty()) {
				organizador.sendMessage(ChatColor.RED + "Nao ha spawn setado!");
				Battlecraft.currentEvento = null;
				return;
			}
			String[] coords = locString.split(",");
			String worldString = coords[0];
			World world = Bukkit.getWorld(worldString);
			double x = Double.valueOf(coords[1]);
			double y = Double.valueOf(coords[2]);
			double z = Double.valueOf(coords[3]);
			float yaw = Float.valueOf(coords[4]);
			float pitch = Float.valueOf(coords[5]);
			Location location = new Location(world, x, y, z, yaw, pitch);
			rdmSpawn = location;
		}
		if (type == EventType.MDR) {
			String locString = m.getConfiguration().getConfig(ConfigEnum.EVENTOS).getString("eventos.mdr.spawn");
			if (locString == null) {
				organizador.sendMessage(ChatColor.RED + "Nao ha spawn setado!");
				Battlecraft.currentEvento = null;
				return;
			}
			String[] coords = locString.split(",");
			String worldString = coords[0];
			World world = Bukkit.getWorld(worldString);
			double x = Double.valueOf(coords[1]);
			double y = Double.valueOf(coords[2]);
			double z = Double.valueOf(coords[3]);
			float yaw = Float.valueOf(coords[4]);
			float pitch = Float.valueOf(coords[5]);
			Location location = new Location(world, x, y, z, yaw, pitch);
			mdrSpawn = location;
		}
		organizador.sendMessage(ChatColor.AQUA + "Evento " + type.toString() + " criado com sucesso");
		addAdm(organizador);
		tempo = 180;
		runnable = Bukkit.getScheduler().scheduleSyncRepeatingTask(m, new Runnable() {
			@Override
			public void run() {
				int i = tempo;
				if (i % 60 == 0 || i == 45 || i == 30 || i == 15 || i == 10 || i <= 5 && i >= 0) {
					m.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "[EVENTO] " + ChatColor.BLUE + "Evento " + type.toString() + " comeca em " + i + " segundos");
					m.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "[EVENTO] " + ChatColor.BLUE + "Use /evento para entrar no evento!");
				} else if (i <= 0) {
					startEvento();
				}
				tempo--;
			}
		}, 0, 20);
	}

	public void aplicarMDR(Player player) {
		if (!player.hasPermission("flame.evento")) {
			player.sendMessage(ChatColor.RED + "Voce nao possui permissao");
			return;
		}
		if (type != EventType.MDR) {
			player.sendMessage(ChatColor.RED + "Nao esta ocorrendo Evento Mae da Rua");
			return;
		}
		for (Player p : participantes) {
			giveMDR(p);
		}
	}

	public boolean isChatEnabled() {
		return chatOn;
	}

	public boolean teleportPlayer(Player p) {
		if (this.type == EventType.MDR) {
			p.teleport(mdrSpawn);
			p.teleport(mdrSpawn);
			return true;
		}
		if (this.type == EventType.RDM) {
			for (Cabine c : locations) {
				if (c.getPlayer() == null) {
					c.setPlayer(p);
					p.teleport(c.getLocation());
					p.teleport(c.getLocation());
					p.teleport(c.getLocation());
					return true;
				}
			}
		}
		p.sendMessage(ChatColor.RED + "Nenhum lugar encontrado!");
		return false;
	}

	public void startEvento() {
		this.state = EventState.STARTED;
		if (runnable != null) {
			m.getServer().getScheduler().cancelTask(runnable);
			runnable = null;
		}
		totalPlayers = participantes.size();
		m.getServer().broadcastMessage(ChatColor.RED + "O evento iniciou");
	}

	public EventState getState() {
		return state;
	}

	public EventType getType() {
		return type;
	}

	public int getTime() {
		return tempo;
	}

	public void addPlayer(Player p) {
		if (organizadores.contains(p)) {
			p.sendMessage(ChatColor.RED + "Ja esta administrando o evento");
			return;
		}
		if (isInCombat(p)) {
			p.sendMessage(ChatColor.RED + "Voce esta em combate");
			return;
		}
		if (state != EventState.WAITING) {
			p.sendMessage(ChatColor.RED + "O evento ja iniciou");
			return;
		}
		if (isOnEvent(p)) {
			p.sendMessage(ChatColor.RED + "Voce ja esta no evento");
			return;
		}
		if (this.type == EventType.RDM && participantes.size() >= locations.size()) {
			p.sendMessage(ChatColor.RED + "Evento Lotado");
			return;
		}
		if (teleportPlayer(p)) {
			participantes.add(p);
			m.getKitManager().removeKit(p);
			m.getProtectionManager().addProtection(p.getUniqueId());
			m.getWarpManager().removeWarp(p);
			API.clearInventory(p);
//			if (this.type == EventType.RDM)
//				//TODO: GIVE KIT
////				m.getKitManager().giveKit(p, "pvp", false);
//			else
//				giveMDR(p);
		}
	}

	public void addEspectador(Player p) {
		if (organizadores.contains(p)) {
			p.sendMessage(ChatColor.RED + "Ja esta administrando o evento");
			return;
		}
		if (isInCombat(p)) {
			p.sendMessage(ChatColor.RED + "Voce esta em combate");
			return;
		}
		if (state == EventState.WAITING) {
			p.sendMessage(ChatColor.RED + "O evento ainda nao iniciou");
			return;
		}
		if (isOnEvent(p)) {
			p.sendMessage(ChatColor.RED + "Voce esta no evento");
			return;
		}
		if (this.type != EventType.RDM) {
			p.sendMessage(ChatColor.RED + "Evento Nao e Rei da Mesa");
			return;
		}
		espectadores.add(p);
		m.getKitManager().removeKit(p);
		API.clearInventory(p);
		m.getWarpManager().removeWarp(p);
	}

	private boolean isInCombat(Player p) {
		// TODO
		/*
		 * CombatLog log = Main.instance.getCombatLogManager().getCombatLog(p);
		 * if (log != null) { if (log.getDamager() != null) { if (log.getTime()
		 * > System.currentTimeMillis()) { return true; } } }
		 */
		return false;
	}

	public void addAdm(Player p) {
		if (!p.hasPermission("flame.evento")) {
			p.sendMessage(ChatColor.RED + p.getName() + " nao pode fazer eventos");
			return;
		}
		if (isOnEvent(p)) {
			p.sendMessage(ChatColor.RED + "Voce ja esta no evento");
			return;
		}
		if (organizadores.contains(p)) {
			p.sendMessage(ChatColor.RED + "Ja esta administrando o evento");
			return;
		}
		organizadores.add(p);
		m.getKitManager().removeKit(p);
		m.getWarpManager().removeWarp(p);
		API.clearInventory(p);
		//TODO: GIVE KIT
//		m.getKitManager().giveKit(p, "fisherman", false);
		m.getProtectionManager().removeProtection(p.getUniqueId());
		if (type == EventType.MDR) {
			p.teleport(mdrSpawn);
		}
		if (type == EventType.RDM) {
			p.teleport(rdmSpawn);
		}
	}

	public void removePlayer(Player p) {
		if (!isOnEvent(p))
			return;
		participantes.remove(p);
		if (state != EventState.WAITING)
			broadcastMessage(ChatColor.RED + p.getName() + " foi eliminado do evento!");

		for (Cabine c : locations) {
			if (c.getPlayer() == null)
				continue;
			if (c.getPlayer().getName().equals(p.getName())) {
				c.setPlayer(null);
			}
		}
		if (!p.isDead())
			m.getWarpManager().teleportWarp(p, "spawn", false);
	}

	public void win(Player p) {
		Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "[EVENTO] " + p.getName() + " foi o grande vencedor!");
		Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "[EVENTO] " + totalPlayers + " participaram do evento, e o " + p.getName() + " recebe 50x Numero de participantes");
		Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "[EVENTO] " + p.getName() + " recebeu " + (totalPlayers * 50) + " de money!");
		Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "[EVENTO] " + "Parabens!");
		// TODO Main.instance.getStatusManager().addMoney(p, totalPlayers * 50);
		stopEvento();
	}

	public void stopEvento() {
		for (Player p : participantes) {
			m.getWarpManager().teleportWarp(p, "spawn", false);
		}
		for (Player p : organizadores) {
			m.getWarpManager().teleportWarp(p, "spawn", false);
		}
		if (runnable != null) {
			m.getServer().getScheduler().cancelTask(runnable);
			runnable = null;
		}
		Bukkit.broadcastMessage(ChatColor.DARK_RED + "O evento acabou!");
		organizadores.clear();
		participantes.clear();
		Battlecraft.currentEvento = null;
	}

	public void broadcastMessage(String message) {
		m.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "[EVENTO] " + message);
	}

	public boolean isOnEvent(Player p) {
		return participantes.contains(p);
	}

	public static void giveMDR(Player p) {
		PlayerInventory inv = p.getInventory();
		for (int i = 0; i < 18; i++) {
			inv.addItem(new ItemStack(Material.MUSHROOM_SOUP));
		}
		inv.addItem(new ItemStack(Material.COOKED_BEEF, 32));
	}

}
