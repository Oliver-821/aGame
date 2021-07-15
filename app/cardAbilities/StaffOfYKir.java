package cardAbilities;

import akka.actor.ActorRef;
import commands.AdvancedCommands;
import commands.BasicCommands;
import structures.AIPlayer;
import structures.BetterUnit;
import structures.Board;
import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Tile;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;


/**
 * StaffOfYKir
 * add teo attack to AI
 * if AI has no card to summon or AI is risk. 
 * AI will add two attack 
 * 
 * PurebladeEnforcer
 * once use the spell card
 * add attack and health
 * 
 */
public class StaffOfYKir implements cardAbility{
	 
	@Override
	public void execute(int handPosition, GameState gameState, ActorRef out) {
		Board board = gameState.getBoard();
		AIPlayer ai = gameState.getAI();
		BetterUnit avatar2 = board.unit2[16];
		int[]pos = new int[2];
		pos = board.getPos(46);
		Tile tile = board.tile[pos[0]][pos[1]];
		int attack = avatar2.getAttack() +2;
		avatar2.setAttack(attack);
		AdvancedCommands.clearHighlight(gameState, out);
		EffectAnimation ef = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff);
		BasicCommands.playEffectAnimation(out, ef, tile);
		BasicCommands.setUnitAttack(out, avatar2, attack);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		//Detects the presence or absence of PurebladeEnforcer, and if so, uses PurebladeEnforcer for +1 attack and health
		Tile[][] tile1 = gameState.getBoard().getTile();
		for(int i = 0; i < 9 ; i++) {
			for(int j = 0; j < 5; j++) {
				if(tile1[i][j].getUnit() != null) {
					if(tile1[i][j].getUnit().getId() == 6 || tile1[i][j].getUnit().getId() == 14) {
						BasicCommands.addPlayer1Notification(out, "Pureblade Enforcer", 2);
						int newhealth = tile1[i][j].getUnit().getHealth()+1;
						int newAttack =tile1[i][j].getUnit().getAttack()+1;
						BasicCommands.addPlayer1Notification(out, "health/attack +1", 2);
						EffectAnimation ef2 = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff);
						BasicCommands.playEffectAnimation(out,ef2, tile1[i][j]);
						tile1[i][j].getUnit().setHealth(newhealth);
						BasicCommands.setUnitHealth(out, tile1[i][j].getUnit(), newhealth);
						try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
						tile1[i][j].getUnit().setAttack(newAttack);  
						BasicCommands.setUnitAttack(out, tile1[i][j].getUnit(), newAttack);
						try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
					}
				}
			}
		}
		
		

		int mana = ai.getMana() - 2;
		ai.setMana(mana);
		BasicCommands.setPlayer2Mana(out,ai);

	}

}
