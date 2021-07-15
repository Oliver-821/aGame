package cardAbilities;

import java.util.ArrayList;
import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.AIPlayer;
import structures.BetterUnit;
import structures.Board;
import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Tile;
import structures.basic.UnitAnimationType;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;


/**
 * EntropicDecay
 * reduce one unit health to 0 
 * give value to different unit
 * AI will select the unit with high attack and high health
 * if there is provoke, attack firstly 
 * 
 * PurebladeEnforcer
 * once use the spell card
 * add attack and health
 */

public class EntropicDecay implements cardAbility{

	@Override
	public void execute(int handPosition, GameState gameState, ActorRef out) {
		// TODO Auto-generated method stub
		Board board = gameState.getBoard();
		AIPlayer ai = gameState.getAI();
		int[]pos = new int[2];
		Tile[][] tile = board.getTile();
		ai.OnboardUnit(gameState);
		int id = 0;
		ArrayList<BetterUnit> onboardUnit = ai.getAttackModeUnit();
		int value = 0;
		int prevalue = 0;
		if(onboardUnit.size() >0) {
			for(BetterUnit unit : onboardUnit) {
				if(unit.getId()<30 && unit.getId() != 16) {
					if(unit.getId() == 5 || unit.getId() ==13) {
						value = 300;
					} else if(unit.getId() == 7 || unit.getId() == 15) {
						value = 200;
					}else 
						value = unit.getHealth() + unit.getAttack();
					
					if(value >prevalue) {
						prevalue = value;
						id =unit.getId();
					}
				}
			}
			
			if(id != 16) {
				pos = board.getPos(id);
				EffectAnimation ef = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_martyrdom);
				BasicCommands.playEffectAnimation(out, ef, tile[pos[0]][pos[1]]);
				try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
				BasicCommands.playUnitAnimation(out, tile[pos[0]][pos[1]].getUnit(), UnitAnimationType.death);
				BasicCommands.deleteUnit(out, tile[pos[0]][pos[1]].getUnit());
				tile[pos[0]][pos[1]].setUnit(null);
				int mana = ai.getMana() - 5;
				ai.setMana(mana);
				BasicCommands.setPlayer2Mana(out,ai);
			}

			
			
			
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
		}	
	}
	
}
