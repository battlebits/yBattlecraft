package br.com.battlebits.ybattlecraft.config;

public enum ConfigEnum {
	EVENTOS("eventos.yml");

	private String file;

	private ConfigEnum(String file) {
		this.file = file;
	}
	
	public String getFile() {
		return this.file;
	}
}
