package br.com.battlebits.battlecraft;

import java.sql.Connection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import br.com.battlebits.battlecraft.admin.Mode;
import br.com.battlebits.battlecraft.admin.Vanish;
import br.com.battlebits.battlecraft.config.Config;
import br.com.battlebits.battlecraft.constructors.Warp;
import br.com.battlebits.battlecraft.evento.Evento;
import br.com.battlebits.battlecraft.hotbar.Hotbar;
import br.com.battlebits.battlecraft.listeners.BlockListener;
import br.com.battlebits.battlecraft.listeners.CombatLogListener;
import br.com.battlebits.battlecraft.listeners.DamageListener;
import br.com.battlebits.battlecraft.listeners.DamagerFixer;
import br.com.battlebits.battlecraft.listeners.DeathListener;
import br.com.battlebits.battlecraft.listeners.EventListener;
import br.com.battlebits.battlecraft.listeners.InteractListener;
import br.com.battlebits.battlecraft.listeners.InventoryListener;
import br.com.battlebits.battlecraft.listeners.ItemFrameListener;
import br.com.battlebits.battlecraft.listeners.JoinListener;
import br.com.battlebits.battlecraft.listeners.LauncherListener;
import br.com.battlebits.battlecraft.listeners.MoveListener;
import br.com.battlebits.battlecraft.listeners.PlayerDamageByPlayerListener;
import br.com.battlebits.battlecraft.listeners.PlayerListener;
import br.com.battlebits.battlecraft.listeners.QuitListener;
import br.com.battlebits.battlecraft.listeners.ScoreboardListener;
import br.com.battlebits.battlecraft.listeners.StartgameListener;
import br.com.battlebits.battlecraft.listeners.TabListListener;
import br.com.battlebits.battlecraft.managers.CombatLogManager;
import br.com.battlebits.battlecraft.managers.CommandManager;
import br.com.battlebits.battlecraft.managers.KitLoader;
import br.com.battlebits.battlecraft.managers.KitManager;
import br.com.battlebits.battlecraft.managers.Permissions;
import br.com.battlebits.battlecraft.managers.ProtectionManager;
import br.com.battlebits.battlecraft.managers.ReflectionManager;
import br.com.battlebits.battlecraft.managers.StatusManager;
import br.com.battlebits.battlecraft.managers.WarpLoader;
import br.com.battlebits.battlecraft.managers.WarpManager;
import br.com.battlebits.battlecraft.mysql.Connect;
import br.com.battlebits.battlecraft.nms.barapi.BarAPI;

public class Main extends JavaPlugin {
	public static String site = "battlebits.com.br";
	public static String servername = "Battlecraft";
	public static String motd = "";
	private KitManager kitManager;
	private WarpManager warpManager;
	private Mode mode = new Mode(this);
	private Vanish vanish = new Vanish(this);
	private Config config;
	private Permissions permissions;
	private ProtectionManager protection;
	private StatusManager statusManager;
	private CombatLogManager combatLog;
	public ReflectionManager reflectionManager = new ReflectionManager();
	public static Evento currentEvento;
	public static boolean IS_FULLIRON_MODE = false;

	/**
	 * 
	 * MySQL
	 * 
	 */
	public boolean sql = true;
	public String host = "localhost";
	public String password = "saobestanime";
	public String user = "root";
	public String port = "3306";
	public Connect connect;
	public Connection mainConnection;

	@Override
	public void onEnable() {
		connect = new Connect(this);
		connect.trySQLConnection();
		connect.prepareSQL(mainConnection);
		loadWorlds();
		loadManagers();
		loadWarps();
		new KitLoader(this).registerAbilityListeners();
		new CommandManager(this);
		loadListeners();
	}

	public Permissions getPermissions() {
		return permissions;
	}

	public KitManager getKitManager() {
		return kitManager;
	}

	public WarpManager getWarpManager() {
		return warpManager;
	}

	public StatusManager getStatusManager() {
		return statusManager;
	}

	public Mode getAdminMode() {
		return mode;
	}

	public Vanish getVanish() {
		return vanish;
	}

	public Config getConfiguration() {
		return config;
	}

	public ProtectionManager getProtectionManager() {
		return protection;
	}

	public ReflectionManager getReflectionManager() {
		return reflectionManager;
	}

	public void teleportSpawn(Player p) {
		getWarpManager().removeWarp(p);
		getKitManager().removeKit(p);
		getWarpManager().teleportWarp(p, "spawn", false);
		Hotbar.setItems(p);
		for (PotionEffect potion : p.getActivePotionEffects()) {
			p.removePotionEffect(potion.getType());
		}
		if (getProtectionManager().addProtection(p.getUniqueId())) {
			p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Proteção" + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " >> " + ChatColor.GRAY + "Você recebeu proteção de spawn");
		}
	}

	private void loadWorlds() {
		getServer().createWorld(new WorldCreator("spawnWarp")).setAutoSave(false);
		System.out.print("World 'spawn' loaded!");
		getServer().createWorld(new WorldCreator("1v1spawn")).setAutoSave(true);
		System.out.print("World '1v1pawn' loaded!");
		getServer().createWorld(new WorldCreator("main")).setAutoSave(true);
		System.out.print("World 'main' loaded!");
		getServer().createWorld(new WorldCreator("fpsWarp")).setAutoSave(false);
		System.out.print("World 'fps' loaded!");
		getServer().createWorld(new WorldCreator("startgame")).setAutoSave(true);
		System.out.print("World 'startgame' loaded!");
		getServer().createWorld(new WorldCreator("rdm")).setAutoSave(true);
		System.out.print("World 'rdm' loaded!");
		getServer().createWorld(new WorldCreator("lavachallengeWarp")).setAutoSave(true);
		System.out.print("World 'lavachallange' loaded!");
		for (World world : Bukkit.getWorlds()) {
			world.setThundering(false);
			world.setStorm(false);
			world.setWeatherDuration(1000000000);
			world.setTime(0);
		}
	}

	private void loadListeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new JoinListener(this), this);
		pm.registerEvents(new EventListener(this), this);
		pm.registerEvents(new DamagerFixer(), this);
		pm.registerEvents(new ItemFrameListener(this), this);
		pm.registerEvents(new BlockListener(), this);
		pm.registerEvents(new CombatLogListener(combatLog), this);
		pm.registerEvents(new DamageListener(this), this);
		pm.registerEvents(new DeathListener(this), this);
		pm.registerEvents(new InteractListener(this), this);
		pm.registerEvents(new MoveListener(this), this);
		pm.registerEvents(new PlayerDamageByPlayerListener(), this);
		pm.registerEvents(new PlayerListener(this), this);
		pm.registerEvents(new StartgameListener(), this);
		pm.registerEvents(new TabListListener(this), this);
		pm.registerEvents(new ScoreboardListener(this), this);
		pm.registerEvents(new QuitListener(this), this);
		pm.registerEvents(new BarAPI(this), this);
		pm.registerEvents(new InventoryListener(this), this);
		pm.registerEvents(new LauncherListener(), this);
		}

	private void loadManagers() {
		warpManager = new WarpManager(this);
		kitManager = new KitManager(this);
		protection = new ProtectionManager();
		permissions = new Permissions(this);
		combatLog = new CombatLogManager();
		statusManager = new StatusManager(this);
	}

	private void loadWarps() {
		new WarpLoader(this).registerAbilityListeners();
		Warp simulator = new Warp("Simulator", "Utilize esta Warp para simular o HG. Pegue cogumelos, madeiras e vá para a luta", new ItemStack(Material.RED_MUSHROOM), null);
		getWarpManager().addWarp(simulator);
		Warp startgame = new Warp("StartGame", "Utilize esta Warp para treinar o pvp sem armadura e espadas de madeira", new ItemStack(Material.WOOD_SWORD), null);
		getWarpManager().addWarp(startgame);
		Warp mlg = new Warp("MLG", "Treine como cair de grandes alturas sem tomar dano de um jeito divertido", new ItemStack(Material.WATER_BUCKET), null, false);
		getWarpManager().addWarp(mlg);
		Warp texturas = new Warp("Texturas", "Utilize esta Warp para ver todos os blocos de sua textura =3", new ItemStack(Material.BAKED_POTATO), null, false);
		getWarpManager().addWarp(texturas);
		Warp voidchallenge = new Warp("Void Challenge", "Treine seus refils e seus recrafts com um dano maior", new ItemStack(Material.ENDER_PORTAL), null, false);
		getWarpManager().addWarp(voidchallenge);
	}
}
