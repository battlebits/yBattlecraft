package br.com.battlebits.battlecraft.evento.enums;

public enum EventType {
	RDM("Rei da Mesa"), MDR("Mae da Rua");
	
	private String nome;
	
	private EventType(String nome){
		this.nome = nome;
	}
	
	@Override
	public String toString() {
		return nome;
	}
}
