package br.com.battlebits.ybattlecraft.manager;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ybattlecraft.Battlecraft;

public class BlockResetManager {

	private ArrayList<Location> blocks;
	private Battlecraft battleCraft;

	public BlockResetManager(Battlecraft Battlecraft) {
		battleCraft = Battlecraft;
		blocks = new ArrayList<>();
		startBlockReseter();
	}

	public void startBlockReseter() {
		new BukkitRunnable() {
			@SuppressWarnings({ "deprecation", "unchecked" })
			@Override
			public void run() {
				for (Location loc : (ArrayList<Location>)blocks.clone()) {
					if (System.currentTimeMillis() >= (long) loc.getBlock().getMetadata("resettime").get(0).value()) {
						new BukkitRunnable() {
							@Override
							public void run() {
								loc.getBlock().setType(Material.getMaterial((int) loc.getBlock().getMetadata("type").get(0).value()));
								loc.getBlock().setData((byte) loc.getBlock().getMetadata("data").get(0).value());
								loc.getBlock().removeMetadata("type", battleCraft);
								loc.getBlock().removeMetadata("data", battleCraft);
								loc.getBlock().removeMetadata("resettime", battleCraft);
							}
						}.runTask(battleCraft);
						blocks.remove(loc);
					}
				}
			}
		}.runTaskTimerAsynchronously(battleCraft, 1L, 1L);
	}

	@SuppressWarnings("deprecation")
	public void addBlockToReset(Location loc, int time) {
		loc.getBlock().setMetadata("type", new FixedMetadataValue(battleCraft, loc.getBlock().getType().getId()));
		loc.getBlock().setMetadata("data", new FixedMetadataValue(battleCraft, loc.getBlock().getData()));
		loc.getBlock().setMetadata("resettime", new FixedMetadataValue(battleCraft, System.currentTimeMillis() + time));
		blocks.add(loc);
	}

	@SuppressWarnings("deprecation")
	public void stopAndResetAll() {
		for (Location l : blocks) {
			l.getBlock().setType(Material.getMaterial((int) l.getBlock().getMetadata("type").get(0).value()));
			l.getBlock().setData((byte) l.getBlock().getMetadata("data").get(0).value());
			l.getBlock().removeMetadata("type", battleCraft);
			l.getBlock().removeMetadata("data", battleCraft);
			l.getBlock().removeMetadata("resettime", battleCraft);
		}
		blocks.clear();
	}

}
