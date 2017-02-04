package br.com.battlebits.ybattlecraft.manager;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.util.TimeFormater;

public class CooldownManager {

	private Table<UUID, String, Long> cooldowns;
	private TimeFormater time;
	private Battlecraft battleCraft;

	public CooldownManager(Battlecraft plugin) {
		cooldowns = HashBasedTable.create();
		time = plugin.getTimeUtils();
		battleCraft = plugin;
		 checkCooldowns();
	}

	public void checkCooldowns() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Map<UUID, Long> map : cooldowns.columnMap().values()) {
					for (Entry<UUID, Long> entry : map.entrySet()) {
						if (System.currentTimeMillis() >= entry.getValue()) {
							map.remove(entry.getKey());
							Player t = Bukkit.getPlayer(entry.getKey());
							if (t != null) {
								t.playSound(t.getLocation(), Sound.LEVEL_UP, 0.5F, 1.0F);
							}
						}
					}
				}

			}
		}.runTaskTimerAsynchronously(battleCraft, 20L, 20L);
	}

	public void removeAllCooldowns(UUID id) {
		cooldowns.row(id).clear();
	}

	public boolean hasCooldown(UUID id, String key) {
		if (cooldowns.containsRow(id) && cooldowns.row(id).containsKey(key)) {
			return true;
		}
		return false;
	}

	public void removeCooldown(UUID id, String key) {
		if (hasCooldown(id, key)) {
			cooldowns.remove(id, key);
		}
	}

	public void setCooldown(UUID id, String key, int seconds) {

		cooldowns.put(id, key, System.currentTimeMillis() + (seconds * 1000));
	}

	public boolean isOnCooldown(UUID id, String key) {
		if (hasCooldown(id, key)) {
			if (System.currentTimeMillis() - getLongTime(id, key) >= 0) {
				removeCooldown(id, key);
				return false;
			}
			return true;
		}
		return false;
	}

	public String getCooldownTimeFormated(UUID id, String key) {
		if (isOnCooldown(id, key)) {
			return time.formatToMinutesAndSeconds((int) ((getLongTime(id, key) - System.currentTimeMillis()) / 1000));
		} else {
			return "1 segundo";
		}
	}

	private Long getLongTime(UUID id, String key) {
		if (hasCooldown(id, key)) {
			return cooldowns.row(id).get(key);
		} else {
			return System.currentTimeMillis();
		}
	}

}
