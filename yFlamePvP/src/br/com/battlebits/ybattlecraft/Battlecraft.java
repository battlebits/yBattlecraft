package br.com.battlebits.ybattlecraft;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import br.com.battlebits.commons.BattlebitsAPI;
import br.com.battlebits.commons.bukkit.BukkitMain;
import br.com.battlebits.commons.bukkit.command.BukkitCommandFramework;
import br.com.battlebits.commons.core.backend.mongodb.MongoBackend;
import br.com.battlebits.commons.core.translate.Language;
import br.com.battlebits.commons.core.translate.Translate;
import br.com.battlebits.commons.util.updater.AutoUpdater;
import br.com.battlebits.ybattlecraft.config.Config;
import br.com.battlebits.ybattlecraft.constructors.Warp;
import br.com.battlebits.ybattlecraft.evento.Evento;
import br.com.battlebits.ybattlecraft.fight.gladiator.GladiatorFightController;
import br.com.battlebits.ybattlecraft.listener.BlockListener;
import br.com.battlebits.ybattlecraft.listener.CombatLogListener;
import br.com.battlebits.ybattlecraft.listener.DamageListener;
import br.com.battlebits.ybattlecraft.listener.DamagerFixer;
import br.com.battlebits.ybattlecraft.listener.EventListener;
import br.com.battlebits.ybattlecraft.listener.InteractListener;
import br.com.battlebits.ybattlecraft.listener.InventoryListener;
import br.com.battlebits.ybattlecraft.listener.ItemFrameListener;
import br.com.battlebits.ybattlecraft.listener.JoinListener;
import br.com.battlebits.ybattlecraft.listener.LauncherListener;
import br.com.battlebits.ybattlecraft.listener.MoveListener;
import br.com.battlebits.ybattlecraft.listener.PlayerListener;
import br.com.battlebits.ybattlecraft.listener.QuitListener;
import br.com.battlebits.ybattlecraft.listener.StartgameListener;
import br.com.battlebits.ybattlecraft.listener.TabListListener;
import br.com.battlebits.ybattlecraft.listener.WarpScoreboardListener;
import br.com.battlebits.ybattlecraft.loader.AbilityLoader;
import br.com.battlebits.ybattlecraft.loader.ListenerLoader;
import br.com.battlebits.ybattlecraft.loader.WarpLoader;
import br.com.battlebits.ybattlecraft.manager.AbilityManager;
import br.com.battlebits.ybattlecraft.manager.BlockResetManager;
import br.com.battlebits.ybattlecraft.manager.CombatLogManager;
import br.com.battlebits.ybattlecraft.manager.CooldownManager;
import br.com.battlebits.ybattlecraft.manager.ItemManager;
import br.com.battlebits.ybattlecraft.manager.KitManager;
import br.com.battlebits.ybattlecraft.manager.PlayerHideManager;
import br.com.battlebits.ybattlecraft.manager.ProtectionManager;
import br.com.battlebits.ybattlecraft.manager.ReflectionManager;
import br.com.battlebits.ybattlecraft.manager.StatusManager;
import br.com.battlebits.ybattlecraft.manager.TeleportManager;
import br.com.battlebits.ybattlecraft.manager.WarpManager;
import br.com.battlebits.ybattlecraft.updater.WarpScoreboardUpdater;
import br.com.battlebits.ybattlecraft.util.TimeFormater;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

public class Battlecraft extends JavaPlugin {

	public static String site = "battlebits.com.br";
	public static String servername = "Battlecraft";
	public static String motd = "";
	private KitManager kitManager;
	private WarpManager warpManager;
	private Config config;
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
	@Getter
	private PlayerHideManager playerHideManager;
	// Updater
	private WarpScoreboardUpdater warpScoreboardUpdater;

	// Util
	private TimeFormater timeFormater;

	// Mongo
	@Getter
	private MongoBackend mongo;
	private String database = "battlecraft";
	private String username = "battlecraft";
	private String password = "";

	private static Battlecraft instance;

	{
		instance = this;
	}

	@Override
	public void onLoad() {
		new AutoUpdater(this, "`'kfHE?F2Qd7_~5`").run();
	}

	@Override
	public void onEnable() {
		saveDefaultConfig();
		loadConfiguration();

		if (username != null && !username.isEmpty() && database != null && !database.isEmpty() && password != null
				&& !password.isEmpty())
			BattlebitsAPI.getMongo().getClient().getCredentialsList()
					.add(MongoCredential.createMongoCRCredential(username, database, password.toCharArray()));

		for (Language lang : Language.values()) {
			Translate.loadTranslations("Battlecraft", lang, loadTranslation(lang));
		}
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
		getLogger().info(new br.com.battlebits.commons.core.command.CommandLoader(new BukkitCommandFramework(this))
				.loadCommandsFromPackage("br.com.battlebits.ybattlecraft.command")
				+ " classes de comandos foram carregadas");
	}

	@Override
	public void onDisable() {
		gladiatorFightController.stop();
		blockResetManager.stopAndResetAll();
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

	private void loadConfiguration() {
		database = getConfig().getString("mongo.database");
		username = getConfig().getString("mongo.username");
		password = getConfig().getString("mongo.password");
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> loadTranslation(Language language) {
		MongoDatabase database = BattlebitsAPI.getMongo().getClient().getDatabase("battlecraft");
		MongoCollection<Document> collection = database.getCollection("translation");
		Document found = collection.find(Filters.eq("language", language.toString())).first();
		if (found != null) {
			return (Map<String, String>) found.get("map");
		}
		collection.insertOne(new Document("language", language.toString()).append("map", new HashMap<>()));
		return new HashMap<>();
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
		combatLog = new CombatLogManager();
		statusManager = new StatusManager();
		itemManager = new ItemManager();
		cooldownManager = new CooldownManager(this);
	}

	private void loadUtils() {
		timeFormater = new TimeFormater();
	}

	private void loadWarps() {
		warpLoader = new WarpLoader(this);
		warpLoader.initializeAllWarps();
		warpLoader.registerWarpsListeners();
		Warp simulator = new Warp("Simulator",
				"Utilize esta Warp para simular o HG. Pegue cogumelos, madeiras e vá para a luta",
				new ItemStack(Material.RED_MUSHROOM), null);
		getWarpManager().addWarp(simulator);
		Warp startgame = new Warp("StartGame", "Utilize esta Warp para treinar o pvp sem armadura e espadas de madeira",
				new ItemStack(Material.WOOD_SWORD), null);
		getWarpManager().addWarp(startgame);
		Warp mlg = new Warp("MLG", "Treine como cair de grandes alturas sem tomar dano de um jeito divertido",
				new ItemStack(Material.WATER_BUCKET), null, false);
		getWarpManager().addWarp(mlg);
		Warp texturas = new Warp("Texturas", "Utilize esta Warp para ver todos os blocos de sua textura =3",
				new ItemStack(Material.BAKED_POTATO), null, false);
		getWarpManager().addWarp(texturas);
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

	public static void sendNextServer(Player p) {
		String command = IS_FULLIRON_MODE ? "PVPFulliron" : "PVPSimulator";
		ByteArrayDataOutput outp = ByteStreams.newDataOutput();
		outp.writeUTF(command);
		p.sendPluginMessage(BukkitMain.getPlugin(), "BungeeCord", outp.toByteArray());
		p.kickPlayer(ChatColor.RED + "O servidor está se preparando para reiniciar e você foi kickado do servidor.");
	}

	public static Battlecraft getInstance() {
		return instance;
	}

}
