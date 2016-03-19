package br.com.battlebits.ybattlecraft.fight.gladiator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class GladiatorFightController {

	private List<UUID> playersInFight;
	private List<Block> fightsBlocks;
	
	public GladiatorFightController() {
		playersInFight = new ArrayList<>();
		fightsBlocks = new ArrayList<>();
	}
	
	public boolean isInFight(Player p) {
		return playersInFight.contains(p.getUniqueId());
	}
	
	public void removePlayerFromFight(UUID id){
		playersInFight.remove(id);
	}
	
	public void addPlayerToFights(UUID id){
		playersInFight.add(id);
	}
	
	
	public void removeBlock(Block b){
		fightsBlocks.remove(b);
	}
	
	public void addBlock(Block b){
		fightsBlocks.add(b);
	}
	
	public boolean isFightBlock(Block b){
		return fightsBlocks.contains(b);
	}

	
}
