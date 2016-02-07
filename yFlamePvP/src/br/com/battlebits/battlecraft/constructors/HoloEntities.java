package br.com.battlebits.battlecraft.constructors;

import net.minecraft.server.v1_7_R4.EntityHorse;
import net.minecraft.server.v1_7_R4.EntityWitherSkull;

public class HoloEntities {
	private Integer lineNumber;
	private EntityHorse horse;
	private EntityWitherSkull skull;
	
	public HoloEntities(Integer lineNumber, EntityHorse horse, EntityWitherSkull skull) {
		this.horse = horse;
		this.skull = skull;
		this.lineNumber = lineNumber;
	}

	public EntityHorse getHorse() {
		return horse;
	}

	public EntityWitherSkull getSkull() {
		return skull;
	}

	public Integer getLineNumber() {
		return lineNumber;
	}
}
