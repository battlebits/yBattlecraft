package br.com.battlebits.ybattlecraft.constructors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.commons.util.string.StringScroller;
import br.com.battlebits.ybattlecraft.kit.Kit;

public class Warp {

	private String warpName;
	private String description;
	private ItemStack icon;
	private Location warpLocation;
	private double radius;
	private boolean canGetItems;
	private WarpScoreboard scoreboard;
	private boolean affectMainStatus;
	private HashMap<String, Kit> warpKits;
	private boolean canUseKits;
	private ArrayList<Kit> kits;
	private StringScroller scroller;
	private boolean pvpEnabled;

	public Warp(String name, String description, ItemStack icon, Location loc) {
		this(name, description, icon, loc, 0, true);
	}

	public Warp(String name, String description, ItemStack icon, Location loc, double radius) {
		this(name, description, icon, loc, radius, true);
	}

	public Warp(String name, String description, ItemStack icon, Location loc, double radius, WarpScoreboard scoreboard) {
		this(name, description, icon, loc, radius, true, scoreboard, true, false);
	}

	public Warp(String name, String description, ItemStack icon, Location loc, double radius, WarpScoreboard scoreboard, boolean affectStatus) {
		this(name, description, icon, loc, radius, true, scoreboard, affectStatus, false);
	}

	public Warp(String name, String description, ItemStack icon, Location loc, double radius, WarpScoreboard scoreboard, boolean affectStatus,
			boolean kits) {
		this(name, description, icon, loc, radius, true, scoreboard, affectStatus, kits);
	}

	public Warp(String name, String description, ItemStack icon, Location loc, boolean canGetItems) {
		this(name, description, icon, loc, 0, canGetItems);
	}

	public Warp(String name, String description, ItemStack icon, Location loc, boolean canGetItems, WarpScoreboard scoreboard) {
		this(name, description, icon, loc, 0, canGetItems, scoreboard, true, false);
	}

	public Warp(String name, String description, ItemStack icon, Location loc, boolean canGetItems, WarpScoreboard scoreboard, boolean affectStatus) {
		this(name, description, icon, loc, 0, canGetItems, scoreboard, affectStatus, false);
	}

	public Warp(String name, String description, ItemStack icon, Location loc, boolean canGetItems, WarpScoreboard scoreboard, boolean affectStatus,
			boolean kits) {
		this(name, description, icon, loc, 0, canGetItems, scoreboard, affectStatus, kits);
	}

	public Warp(String name, String description, ItemStack icon, Location loc, double radius, boolean canGetItems) {
		this(name, description, icon, loc, radius, canGetItems, null, true, false);
	}

	public Warp(String name, String description, ItemStack icon, Location loc, double radius, boolean canGetItems, boolean kits) {
		this(name, description, icon, loc, radius, canGetItems, null, true, kits);
	}

	public Warp(String name, String description, ItemStack icon, Location loc, double radius, boolean canGetItems, WarpScoreboard warpScoreboard,
			boolean affectStatus, boolean kits) {
		this.warpName = name;
		this.warpLocation = loc;
		this.radius = radius;
		this.description = description;
		this.icon = icon;
		this.canGetItems = canGetItems;
		this.scoreboard = warpScoreboard;
		this.affectMainStatus = affectStatus;
		this.warpKits = new HashMap<>();
		this.canUseKits = kits;
		this.kits = new ArrayList<>();
		this.scroller = new StringScroller("BattleCraft - " + name + " -", 14, 1);
	}

	public String getWarpName() {
		return warpName;
	}

	public String getDescription() {
		return description;
	}

	public ItemStack getIcon() {
		return icon;
	}

	public Location getWarpLocation() {
		return warpLocation;
	}

	public boolean canGetItems() {
		return canGetItems;
	}

	public double getRadius() {
		return radius;
	}

	public WarpScoreboard getScoreboard() {
		return scoreboard;
	}

	public boolean isAffectMainStatus() {
		return affectMainStatus;
	}

	public boolean hasScoreboard() {
		return scoreboard != null;
	}

	public void addKit(Kit kit) {
		warpKits.put(kit.getName().toLowerCase(), kit);
		kits.add(kit);
		Collections.sort(kits, new Comparator<Kit>() {
			@Override
			public int compare(Kit o1, Kit o2) {
				return String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName());
			}
		});
	}

	public boolean isKit(String name) {
		return warpKits.containsKey(name.toLowerCase());
	}

	public Kit getKit(String name) {
		return warpKits.get(name.toLowerCase());
	}

	public ArrayList<Kit> getKits() {
		return kits;
	}

	public boolean canUseKits() {
		return canUseKits;
	}

	public StringScroller getScroller() {
		return scroller;
	}
	
	public boolean isPvpEnabled() {
		return pvpEnabled;
	}
}
