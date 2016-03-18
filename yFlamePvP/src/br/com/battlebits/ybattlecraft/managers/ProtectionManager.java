package br.com.battlebits.ybattlecraft.managers;

import java.util.ArrayList;
import java.util.UUID;

public class ProtectionManager {
	private ArrayList<UUID> protection;

	public ProtectionManager() {
		protection = new ArrayList<>();
	}

	public boolean isProtected(UUID uuid) {
		return protection.contains(uuid);
	}

	public boolean addProtection(UUID uuid) {
		if (protection.contains(uuid))
			return false;
		protection.add(uuid);
		return true;
	}

	public boolean removeProtection(UUID uuid) {
		if (!protection.contains(uuid))
			return false;
		protection.remove(uuid);
		return true;
	}

}
