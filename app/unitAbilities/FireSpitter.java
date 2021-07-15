package unitAbilities;

import akka.actor.ActorRef;
import commands.AdvancedCommands;
import commands.BasicCommands;
import structures.BetterUnit;
import structures.Board;
import structures.GameState;
import structures.basic.Tile;


/**
 * FireSpitter
 * range attack 
 */
public class FireSpitter implements Ability{

	@Override
	public void execute(int x, int y, GameState gameState, ActorRef out) {
		Board board = gameState.getBoard();
		Tile tile = board.tile[x][y];
		BetterUnit unit = tile.getUnit();
		if(gameState.getGameAction().equals("clickCard")) {
			BasicCommands.addPlayer1Notification(out, "you can't put here", 2);
			AdvancedCommands.clearHighlight(gameState, out);
			highlight(x,y,gameState,out);
			gameState.setUnit(null);
		}else if(gameState.getGameAction().equals("pending")){
				highlight(x, y, gameState, out);
		}else if(gameState.getGameAction().equals("clickUnit")) {
			if (tile.getTileStatus() == 2) {
				 if(unit.getId()>=30) {
					unitAttack(x, y, gameState, out);
				} else 
					highlight(x, y, gameState, out);
			}else if(tile.getTileStatus() == 1) {
				unitMove(x, y, gameState, out);
			}
			gameState.setGameAction("pending");
		}

	}
	
	/**
	 * unit range attack high light
	 * 
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef
	 *
	 */
	public void attackhighLight(int x, int y, GameState gameState, ActorRef out) {
		Board board = gameState.getBoard();
		Tile[][] tile = board.getTile();
		for(int i = 0; i < 9 ; i++) {
			for(int j = 0; j < 5; j++) {
				if(tile[i][j].getUnit() != null) {
					if(tile[i][j].getUnit().getId() >=30) {
						BasicCommands.drawTile(out, tile[i][j], 2);
						tile[i][j].setTileStatus(2);
						try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
					}
				}
			}
		}
		gameState.setGameAction("clickUnit");
		gameState.setTempTile(tile[x][y]);
		gameState.setUnit(tile[x][y].getUnit());
		gameState.setPosX(x);
		gameState.setPosY(y);
	}
	
	
	/**
	 * unit move and range attack high light
	 * 
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef
	 *
	 */
	
	public void highlight(int x, int y, GameState gameState, ActorRef out) {
		Tile[][] tile = gameState.getBoard().getTile();
		BetterUnit unit = tile[x][y].getUnit();
		AdvancedCommands.clearHighlight(gameState, out);	
		if (unit.getTurnNum() == 0 ||  unit.getAttackNum() == 1) {
			BasicCommands.addPlayer1Notification(out, "wait for next turn", 2);
			gameState.setGameAction("pending");
		} else {
			if(unit.getMoveNum()!=0 && unit.getTurnNum() != 0) {
				attackhighLight(x, y, gameState, out);
		}else {
			AdvancedCommands.moveHighlight(x, y, gameState, out);
			attackhighLight(x, y, gameState, out);
			}
			gameState.setGameAction("clickUnit");
		}
	}
	
	
	/**
	 * unit move 
	 * 
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef
	 *
	 */
	public void unitMove(int x, int y, GameState gameState, ActorRef out) {
		Board board = gameState.getBoard();
		Tile selectedTile = board.tile[gameState.getPosX()][gameState.getPosY()];
		BetterUnit selectedUnit = selectedTile.getUnit();
		Tile tile = board.tile[x][y];
		BetterUnit unit = tile.getUnit();	
		if (selectedUnit.getAttackNum() == 0) {
			if (unit == null) {
				if (selectedUnit.getMoveNum() == 0) {
					if (selectedUnit.getTurnNum() == 0)
						BasicCommands.addPlayer1Notification(out, "wait for next turn", 2);
					else 
						AdvancedCommands.unitMove(x, y, gameState, out);
				} else
					BasicCommands.addPlayer1Notification(out, "unit already move", 2);
			}
		} else 
			BasicCommands.addPlayer1Notification(out, "unit can't move or attack", 2);
		AdvancedCommands.clearHighlight(gameState, out);
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * unit range attack
	 * 
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef
	 *
	 */
	public void unitAttack(int x, int y, GameState gameState, ActorRef out) {
		Board board = gameState.getBoard();
		Tile selectedTile = board.tile[gameState.getPosX()][gameState.getPosY()];
		BetterUnit selectedUnit = selectedTile.getUnit();
		Tile tile = board.tile[x][y];
		if (selectedUnit != null ) {
			if (selectedUnit.getAttackNum() == 0) {
					if (tile.getTileStatus() != 2) {
						BasicCommands.addPlayer1Notification(out, "you can't attack this ", 2);
						gameState.setTempTile(null);
						gameState.setUnit(null);
					} else {
						AdvancedCommands.rangeAttack(x, y, gameState, out);
						try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
						AdvancedCommands.avatarAction(x, y, gameState, out);
						try {
							Thread.sleep(30);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				BasicCommands.addPlayer1Notification(out, "unit can't move or attack", 2);
			}
		AdvancedCommands.clearHighlight(gameState, out);
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
