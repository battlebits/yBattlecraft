package br.com.battlebits.battlecraft.nms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.server.v1_7_R4.EntityHorse;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EntityWitherSkull;
import net.minecraft.server.v1_7_R4.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_7_R4.WorldServer;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.battlecraft.Main;
import br.com.battlebits.battlecraft.constructors.HoloEntities;

public class Hologram {

	private static final double distance = 0.23;
	private List<String> lines = new ArrayList<String>();
	private HashMap<Integer, HoloEntities> entities = new HashMap<>();
	private boolean cancel = false;
	private Player player;
	private EntityPlayer nmsPlayer;
	private int plus;
	private boolean ret = false;
	private Main main;

	public Hologram(Main main, Player p, int plus, boolean bugar, String... lines) {
		this.player = p;
		this.plus = plus;
		this.nmsPlayer = ((CraftPlayer) p).getHandle();
		this.main = main;
		changeText(lines);
	}

	public Player getPlayer() {
		return player;
	}

	public void change(String... lines) {
		this.lines = Arrays.asList(lines);
	}

	public void cancelTask() {
		if (ret)
			return;
		cancel = true;
		for (HoloEntities e : entities.values()) {
			PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(e.getSkull().getId(), e.getHorse().getId());
			nmsPlayer.playerConnection.sendPacket(destroy);
		}
	}

	public void changeText(String... text) {
		if (ret)
			return;
		lines = Arrays.asList(text);
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			HoloEntities e = entities.get(i);
			if (e == null)
				spawnEntity(i);
			else {
				if (!e.getHorse().getCustomName().equals(line)) {
					e.getHorse().setCustomName(line);
					PacketPlayOutEntityMetadata data = new PacketPlayOutEntityMetadata(e.getHorse().getId(), e.getHorse().getDataWatcher(), true);
					nmsPlayer.playerConnection.sendPacket(data);
				}
			}
		}
		for (HoloEntities e : entities.values()) {
			if (e.getLineNumber() >= lines.size()) {
				PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(e.getSkull().getId(), e.getHorse().getId());
				nmsPlayer.playerConnection.sendPacket(destroy);
			}
		}
	}

	public void run() {
		if (ret)
			return;
		cancel = false;
		new BukkitRunnable() {
			public void run() {
				if (cancel) {
					cancel();
				}
				for (HoloEntities e : entities.values()) {
					Location loc = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(3)).subtract(0, 0.5, 0);
					Location first = loc.clone().add(0, ((Hologram.this.lines.size() / 2) * distance) - (e.getLineNumber() * distance), 0);
					e.getSkull().setLocation(first.getX(), first.getY() + 57.5 + plus, first.getZ(), 0, 0);
					e.getHorse().setLocation(first.getX(), first.getY() + 55.5 + plus, first.getZ(), 0, 0);
					PacketPlayOutEntityTeleport teleportSkull = new PacketPlayOutEntityTeleport(e.getSkull());
					PacketPlayOutEntityTeleport teleportHorse = new PacketPlayOutEntityTeleport(e.getHorse());
					nmsPlayer.playerConnection.sendPacket(teleportSkull);
					nmsPlayer.playerConnection.sendPacket(teleportHorse);
				}
			}
		}.runTaskTimer(main, 2, 1);
	}

	private void spawnEntity(int line) {
		if (ret)
			return;
		String text = this.lines.get(line);
		Location loc = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(3)).subtract(0, 0.5, 0);
		WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
		final EntityWitherSkull skull = new EntityWitherSkull(world);
		skull.setLocation(loc.getX(), loc.getY() + 56.5 + plus, loc.getZ(), 0, 0);
		((CraftWorld) loc.getWorld()).getHandle().addEntity(skull);
		final EntityHorse horse = new EntityHorse(world);
		horse.setLocation(loc.getX(), loc.getY() + 55.5 + plus, loc.getZ(), 0, 0);
		horse.setAge(-1700000);
		horse.setCustomName(text);
		horse.setCustomNameVisible(true);
		PacketPlayOutSpawnEntityLiving spawn = new PacketPlayOutSpawnEntityLiving(horse);
		EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
		nmsPlayer.playerConnection.sendPacket(spawn);
		PacketPlayOutAttachEntity pa = new PacketPlayOutAttachEntity(0, horse, skull);
		nmsPlayer.playerConnection.sendPacket(pa);
		entities.put(line, new HoloEntities(line, horse, skull));
	}
}
