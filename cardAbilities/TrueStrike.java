package cardAbilities;

import akka.actor.ActorRef;
import commands.AdvancedCommands;
import commands.BasicCommands;
import structures.BetterUnit;
import structures.Board;
import structures.GameState;
import structures.HumanPlayer;
import structures.basic.EffectAnimation;
import structures.basic.Tile;
import structures.basic.UnitAnimationType;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;


/**
 * TrueStrike  
 * select the tile and 2 damage to the unit
 */
public class TrueStrike implements cardAbility {
	
	@Override
	public void execute(int handPosition, GameState gameState, ActorRef out) {
		HumanPlayer human = gameState.getHuman();
		// determine whether Mana is enough
		int mana;
		mana = human.getMana();
		if(mana<1) {
			BasicCommands.addPlayer1Notification(out, "no enough mana", 2);
			return;	
		}else {
			highlight(handPosition, gameState, out);
		}
		
	}
	
	/**
	 * high light the tile could use spell card 
	 * 
	 * @param handPosition : on hand card position 
	 * @param gameState : GameState
	 * @param out : ActorRef
	 */
	public void highlight(int handPosition, GameState gameState, ActorRef out ) {
		Board board = gameState.getBoard();
		Tile[][] tiles = board.getTile();
		int count = 0;
		for(int i = 0; i < 9 ; i++) {
			for(int j = 0; j < 5; j++) {
				if(tiles[i][j].getUnit() != null && tiles[i][j].getUnit().getId()>= 30 &&  tiles[i][j].getUnit().getId()!=46) {
					BasicCommands.drawTile(out, tiles[i][j], 2);
					tiles[i][j].setTileStatus(2);
					try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
					count ++;
				}
				
			}
		}
		if(count != 0) {
			BasicCommands.addPlayer1Notification(out, "you could attack unit", 2);
		} else {
			BasicCommands.addPlayer1Notification(out, "no unit for you to attack", 2);
		}
		gameState.setGameAction("clickCard");
		gameState.setHandPosition(handPosition);
	}
	
	
	/**
	 * use true trike on the selected tile
	 * 
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef
	 */
	public static void useTrueStrike(int x, int y, GameState gameState, ActorRef out) {
		int handPosition = gameState.getHandPosition();
		Board board = gameState.getBoard();
		HumanPlayer human = gameState.getHuman();
		Tile[][] tiles = board.getTile();
		int mana = human.getMana();
		if(tiles[x][y].getTileStatus() == 2) {
			mana = human.getMana();
			BasicCommands.addPlayer1Notification(out, "Turestrike", 2);
			// update mana
			mana -= 1;
			human.setMana(mana);
			BasicCommands.setPlayer1Mana(out,human);
			// get unit
			BetterUnit unit = tiles[x][y].getUnit();		
			AdvancedCommands.clearHighlight(gameState, out);
			// play animation  
			EffectAnimation ef = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_inmolation);
			BasicCommands.playEffectAnimation(out, ef, tiles[x][y]);
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
			// get the avatar2's health
			int health = unit.getHealth();
			// determine if the AI dies after the spell is cast
			if(health <=2 ) {
				// set health to 0
				unit.setHealth(0);
				// play death
				BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.death);
				BasicCommands.deleteUnit(out, unit);
				tiles[x][y].setUnit(null);
				try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}	
			}else {
				// subtract 2
				unit.setHealth(health-2);
				BasicCommands.setUnitHealth(out, unit, unit.getHealth());
				try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
			}
			// delete card
			human.getOnhandCard().remove(gameState.getHandPosition());
			int onhandCardNum = human.getOnhandCardNum();
			onhandCardNum--;
			human.setOnhandCardNum(onhandCardNum);
			BasicCommands.deleteCard(out, handPosition);
			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
			gameState.setHandPosition(-1);
			gameState.setGameAction("pending");
		}else {
			BasicCommands.addPlayer1Notification(out, "you couldn't put here", 2);
			AdvancedCommands.clearHighlight(gameState, out);
			gameState.setHandPosition(-1);
		}

	}
}