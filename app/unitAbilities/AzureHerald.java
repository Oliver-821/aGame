package unitAbilities;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.BetterUnit;
import structures.Board;
import structures.GameState;
import structures.HumanPlayer;
import structures.basic.EffectAnimation;
import structures.basic.Tile;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

/**
 * AzureHerald
 * when the unit summon 
 * add 3 health to avatar
 */
public class AzureHerald implements Ability{
	@Override
	public void execute(int x, int y, GameState gameState, ActorRef out) {
		Board board = gameState.getBoard();
		BetterUnit humanAvatar= board.unit1[16];
		int health = humanAvatar.getHealth() + 3;
		HumanPlayer humanPlayer= gameState.getHuman();
		Tile[][] tile = board.getTile();
		int[] pos = board.getPos(16);
		//effect animation part
		BasicCommands.addPlayer1Notification(out, "player health +3", 2);
		try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}
		EffectAnimation ef = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff);
		BasicCommands.playEffectAnimation(out, ef, tile[pos[0]][pos[1]]);
		if(health <=20) {
			humanPlayer.setHealth(health);
			humanAvatar.setHealth(health);
			BasicCommands.setPlayer1Health(out, humanPlayer);
			BasicCommands.setUnitHealth(out, humanAvatar,health);
			try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
		}else{
			humanPlayer.setHealth(20);
			humanAvatar.setHealth(20);
			BasicCommands.setPlayer1Health(out, humanPlayer);
			BasicCommands.setUnitHealth(out, humanAvatar,20);
			try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
		}
		
		
	}
	
		
}
