package br.com.battlebits.ybattlecraft.enums;

public enum KitCategory {
	FREE((short) 5), OWNED((short) 4), STORE, FAVORITE((short) 14), ALL((short) 11);

	private short id;

	private KitCategory() {
		this.id = 0;
	}

	private KitCategory(short id) {
		this.id = id;
	}

	public short getId() {
		return id;
	}
}
