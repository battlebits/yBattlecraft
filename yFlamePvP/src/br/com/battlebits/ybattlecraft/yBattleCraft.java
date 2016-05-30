package br.com.battlebits.ybattlecraft;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.battlebits.ybattlecraft.config.Config;
import br.com.battlebits.ybattlecraft.constructors.Warp;
import br.com.battlebits.ybattlecraft.evento.Evento;
import br.com.battlebits.ybattlecraft.fight.gladiator.GladiatorFightController;
import br.com.battlebits.ybattlecraft.listener.MoveListener;
import br.com.battlebits.ybattlecraft.listeners.BlockListener;
import br.com.battlebits.ybattlecraft.listeners.CombatLogListener;
import br.com.battlebits.ybattlecraft.listeners.DamageListener;
import br.com.battlebits.ybattlecraft.listeners.DamagerFixer;
import br.com.battlebits.ybattlecraft.listeners.EventListener;
import br.com.battlebits.ybattlecraft.listeners.InteractListener;
import br.com.battlebits.ybattlecraft.listeners.InventoryListener;
import br.com.battlebits.ybattlecraft.listeners.ItemFrameListener;
import br.com.battlebits.ybattlecraft.listeners.JoinListener;
import br.com.battlebits.ybattlecraft.listeners.LauncherListener;
import br.com.battlebits.ybattlecraft.listeners.PlayerListener;
import br.com.battlebits.ybattlecraft.listeners.QuitListener;
import br.com.battlebits.ybattlecraft.listeners.StartgameListener;
import br.com.battlebits.ybattlecraft.listeners.TabListListener;
import br.com.battlebits.ybattlecraft.listeners.WarpScoreboardListener;
import br.com.battlebits.ybattlecraft.loader.AbilityLoader;
import br.com.battlebits.ybattlecraft.loader.CommandLoader;
import br.com.battlebits.ybattlecraft.loader.ListenerLoader;
import br.com.battlebits.ybattlecraft.loader.WarpLoader;
import br.com.battlebits.ybattlecraft.manager.AbilityManager;
import br.com.battlebits.ybattlecraft.manager.BlockResetManager;
import br.com.battlebits.ybattlecraft.manager.CooldownManager;
import br.com.battlebits.ybattlecraft.manager.KitManager;
import br.com.battlebits.ybattlecraft.manager.PlayerHideManager;
import br.com.battlebits.ybattlecraft.manager.ReportManager;
import br.com.battlebits.ybattlecraft.manager.TeleportManager;
import br.com.battlebits.ybattlecraft.manager.WarpManager;
import br.com.battlebits.ybattlecraft.managers.CombatLogManager;
import br.com.battlebits.ybattlecraft.managers.ItemManager;
import br.com.battlebits.ybattlecraft.managers.Permissions;
import br.com.battlebits.ybattlecraft.managers.ProtectionManager;
import br.com.battlebits.ybattlecraft.managers.ReflectionManager;
import br.com.battlebits.ybattlecraft.managers.StatusManager;
import br.com.battlebits.ybattlecraft.nms.barapi.BarAPI;
import br.com.battlebits.ybattlecraft.updater.WarpScoreboardUpdater;
import br.com.battlebits.ybattlecraft.util.TimeFormater;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandLoader;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.connection.backend.MySQLBackend;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public class yBattleCraft extends JavaPlugin {

	public static String site = "battlebits.com.br";
	public static String servername = "Battlecraft";
	public static String motd = "";
	private KitManager kitManager;
	private WarpManager warpManager;
	private Config config;
	private Permissions permissions;
	private ProtectionManager protection;
	private StatusManager statusManager;
	private CombatLogManager combatLog;
	public ReflectionManager reflectionManager = new ReflectionManager();
	public static Evento currentEvento;
	public static boolean IS_FULLIRON_MODE = false;

	// Controller
	private GladiatorFightController gladiatorFightController;

	// Loader
	private AbilityLoader abilityLoader;
	private WarpLoader warpLoader;

	// Manager
	private AbilityManager abilityManager;
	private CooldownManager cooldownManager;
	private ItemManager itemManager;
	private TeleportManager teleportManager;
	private BlockResetManager blockResetManager;
	private PlayerHideManager playerHideManager;
	private ReportManager reportManager;

	// Updater
	private WarpScoreboardUpdater warpScoreboardUpdater;

	// Util
	private TimeFormater timeFormater;

	// MySQL
	private MySQLBackend mysql;
	private String hostname = "localhost";
	private int port = 3306;
	private String database = "ybattlecraft";
	private String username = "root";
	private String password = "";

	private static yBattleCraft instance;

	{
		instance = this;
	}

	@Override
	public void onEnable() {
		saveDefaultConfig();
		loadConfiguration();
		mysql = new MySQLBackend(hostname, port, database, username, password);
		try {
			mysql.startConnection();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		loadTranslations();
		IS_FULLIRON_MODE = getConfig().getBoolean("FullIron");
		loadAbilities();
		loadWorlds();
		// new WarpSpawn(this);
		loadUtils();
		loadManagers();
		loadWarps();
		loadUpdaters();
		loadListeners();
		startUpdaters();
		gladiatorFightController = new GladiatorFightController();
		new CommandLoader(this).loadCommandsAndRegister();
		getLogger().info(new BukkitCommandLoader(new BukkitCommandFramework(this)).loadCommandsFromPackage("br.com.battlebits.ybattlecraft.command") + " classes de comandos foram carregadas");
		// getServer().getScheduler().runTaskTimerAsynchronously(this, new
		// PluginUpdater(this), 2L, 108000L);
	}

	@Override
	public void onDisable() {
		gladiatorFightController.stop();
		blockResetManager.stopAndResetAll();
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

	public Config getConfiguration() {
		return config;
	}

	public ProtectionManager getProtectionManager() {
		return protection;
	}

	public ReflectionManager getReflectionManager() {
		return reflectionManager;
	}

	private void loadUpdaters() {
		warpScoreboardUpdater = new WarpScoreboardUpdater(this);
	}

	private void startUpdaters() {
		warpScoreboardUpdater.start();
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
		getServer().createWorld(new WorldCreator("voidchallengeWarp")).setAutoSave(true);
		System.out.print("World 'voidchallangewarp' loaded!");
		for (World world : Bukkit.getWorlds()) {
			world.setThundering(false);
			world.setStorm(false);
			world.setWeatherDuration(1000000000);
			world.setTime(6000);
			world.setGameRuleValue("doDaylightCycle", "false");
			world.setDifficulty(Difficulty.HARD);
			world.setGameRuleValue("doMobSpawning", "false");
		}
	}

	private void loadTranslations() {
		try {
			BattlebitsAPI.debug("TRANSLATIONS > LOADING");
			PreparedStatement stmt = null;
			ResultSet result = null;
			for (Language lang : Language.values()) {
				try {
					stmt = getConnection().getConnection().prepareStatement("SELECT * FROM `translations` WHERE `language`='" + lang + "';");
					result = stmt.executeQuery();
					if (result.next()) {
						Translate.loadTranslations("ybattlecraft", lang, result.getString("json"));
						BattlebitsAPI.debug(lang.toString() + " > LOADED");
					}
					result.close();
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
					BattlebitsAPI.debug(lang.toString() + " > FAILED");
				}
			}
			result = null;
			stmt = null;
			BattlebitsAPI.debug("TRANSLATIONS > CLOSE");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void loadConfiguration() {
		hostname = getConfig().getString("database.hostname");
		port = getConfig().getInt("database.port");
		database = getConfig().getString("database.database");
		username = getConfig().getString("database.username");
		password = getConfig().getString("database.password");
	}

	private void loadAbilities() {
		abilityManager = new AbilityManager();
		abilityLoader = new AbilityLoader(this);
		abilityLoader.loadAllAbilities();
		abilityLoader.registerAllAbilities();
	}

	private void loadListeners() {
		new ListenerLoader(this).loadAndRegisterAllListeners();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new JoinListener(this), this);
		pm.registerEvents(new EventListener(this), this);
		pm.registerEvents(new DamagerFixer(), this);
		pm.registerEvents(new ItemFrameListener(this), this);
		pm.registerEvents(new BlockListener(), this);
		pm.registerEvents(new CombatLogListener(combatLog), this);
		pm.registerEvents(new DamageListener(this), this);
		pm.registerEvents(new InteractListener(this), this);
		pm.registerEvents(new MoveListener(this), this);
		pm.registerEvents(new PlayerListener(this), this);
		pm.registerEvents(new StartgameListener(), this);
		pm.registerEvents(new TabListListener(this), this);
		pm.registerEvents(new QuitListener(this), this);
		pm.registerEvents(new BarAPI(this), this);
		pm.registerEvents(new InventoryListener(this), this);
		pm.registerEvents(new LauncherListener(), this);
		pm.registerEvents(new WarpScoreboardListener(this), this);
	}

	private void loadManagers() {
		playerHideManager = new PlayerHideManager();
		warpManager = new WarpManager(this);
		kitManager = new KitManager(this);
		protection = new ProtectionManager();
		blockResetManager = new BlockResetManager(this);
		teleportManager = new TeleportManager(this);
		permissions = new Permissions(this);
		combatLog = new CombatLogManager();
		statusManager = new StatusManager();
		itemManager = new ItemManager();
		cooldownManager = new CooldownManager(this);
		reportManager = new ReportManager(this);
	}

	private void loadUtils() {
		timeFormater = new TimeFormater();
	}

	private void loadWarps() {
		warpLoader = new WarpLoader(this);
		warpLoader.initializeAllWarps();
		warpLoader.registerWarpsListeners();
		Warp simulator = new Warp("Simulator", "Utilize esta Warp para simular o HG. Pegue cogumelos, madeiras e vá para a luta", new ItemStack(Material.RED_MUSHROOM), null);
		getWarpManager().addWarp(simulator);
		Warp startgame = new Warp("StartGame", "Utilize esta Warp para treinar o pvp sem armadura e espadas de madeira", new ItemStack(Material.WOOD_SWORD), null);
		getWarpManager().addWarp(startgame);
		Warp mlg = new Warp("MLG", "Treine como cair de grandes alturas sem tomar dano de um jeito divertido", new ItemStack(Material.WATER_BUCKET), null, false);
		getWarpManager().addWarp(mlg);
		Warp texturas = new Warp("Texturas", "Utilize esta Warp para ver todos os blocos de sua textura =3", new ItemStack(Material.BAKED_POTATO), null, false);
		getWarpManager().addWarp(texturas);
	}

	public MySQLBackend getConnection() {
		return mysql;
	}

	public ItemManager getItemManager() {
		return itemManager;
	}

	public TimeFormater getTimeUtils() {
		return timeFormater;
	}

	public CooldownManager getCooldownManager() {
		return cooldownManager;
	}

	public ReportManager getReportManager() {
		return reportManager;
	}

	public AbilityManager getAbilityManager() {
		return abilityManager;
	}

	public TeleportManager getTeleportManager() {
		return teleportManager;
	}

	public BlockResetManager getBlockResetManager() {
		return blockResetManager;
	}

	public GladiatorFightController getGladiatorFightController() {
		return gladiatorFightController;
	}

	public PlayerHideManager getPlayerHideManager() {
		return playerHideManager;
	}

	public static yBattleCraft getInstance() {
		return instance;
	}

}
