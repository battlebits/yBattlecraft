package br.com.battlebits.battlecraft.constructors;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class Warp {
	private String warpName;
	private String description;
	private ItemStack icon;
	private Location warpLocation;
	private double radius;
	public boolean canGetItems;

	public Warp(String name, String description, ItemStack icon, Location loc) {
		this(name, description, icon, loc, 0, true);
	}
	
	public Warp(String name, String description, ItemStack icon, Location loc,  double radius) {
		this(name, description, icon, loc, radius, true);
	}
	
	public Warp(String name, String description, ItemStack icon, Location loc, boolean canGetItems) {
		this(name, description, icon, loc, 0, canGetItems);
	}

	public Warp(String name, String description, ItemStack icon, Location loc, double radius, boolean canGetItems) {
		this.warpName = name;
		this.warpLocation = loc;
		this.radius = radius;
		this.description = description;
		this.icon = icon;
		this.canGetItems = canGetItems;
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
}
