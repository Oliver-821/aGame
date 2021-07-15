package unitAbilities;

import akka.actor.ActorRef;
import commands.AdvancedCommands;
import commands.BasicCommands;
import structures.Board;
import structures.GameState;
import structures.BetterUnit;
import structures.basic.Tile;


/**
 * AzuriteLion
 * could attack twice 
 * move twice
 */
public class AzuriteLion implements Ability {

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
				if (x < gameState.getPosX() - 1 || y < gameState.getPosY() - 1 || x > gameState.getPosX() + 1
						|| y > gameState.getPosY() + 1) {
					moveAttack(x, y, gameState, out);
				}else if(unit.getId()>=30) {
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
	 * unit move range and attack range high light 
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
		if (unit != null) {
			if (unit.getTurnNum() == 0 ||  unit.getAttackNum() ==2 ) {
				BasicCommands.addPlayer1Notification(out, "wait for next turn", 2);
			} else {
				
				if(unit.getMoveNum()== 2 && unit.getTurnNum() != 0) {	
					AdvancedCommands.attackHighlight(x, y, gameState, out);
				}else {
					AdvancedCommands.moveHighlight(x, y, gameState, out);	
					AdvancedCommands.attackHighlight(x, y, gameState, out);
					if ((unit.getAttackNum() == 0 || unit.getMoveNum() == 0) || (unit.getAttackNum() == 1 && unit.getMoveNum() == 1)) {
						AdvancedCommands.moveAttackHighlight(x, y, gameState, out);	
					}
					gameState.setGameAction("clickUnit");
				}	
			}
		}
	}
	
	/**
	 * unit adjacent attack
	 * 
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef
	 */
	public void unitAttack(int x, int y, GameState gameState, ActorRef out) {
		Board board = gameState.getBoard();
		Tile selectedTile = board.tile[gameState.getPosX()][gameState.getPosY()];
		BetterUnit selectedUnit = selectedTile.getUnit();
		Tile tile = board.tile[x][y];
		if (selectedUnit != null ) {
			if (selectedUnit.getAttackNum() == 0 || selectedUnit.getAttackNum() == 1) {
				if (tile.getTileStatus() != 2) {
					BasicCommands.addPlayer1Notification(out, "you can't attack this ", 2);
					gameState.setTempTile(null);
					gameState.setUnit(null);
				} else {
					AdvancedCommands.adjacentAttack(x, y, gameState, out);
					try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
					AdvancedCommands.avatarAction(x, y, gameState, out);
					try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
				} 
			} else {
				BasicCommands.addPlayer1Notification(out, "unit can't move or attack", 2);
			}
		}
		AdvancedCommands.clearHighlight(gameState, out);
		try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	/**
	 *Unit move to the tile 
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
		if (selectedUnit.getAttackNum() == 0 || selectedUnit.getAttackNum() ==1) {
			if (unit == null) {
				if (selectedUnit.getMoveNum() == 0 || selectedUnit.getMoveNum() == 1) {
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
		try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	
	/**
	 *move and attack atomically 
	 *
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef
	 *
	 */
	public void moveAttack(int x, int y, GameState gameState, ActorRef out) {
		Board board = gameState.getBoard();
		Tile selectedTile = board.tile[gameState.getPosX()][gameState.getPosY()];
		Tile tile = board.tile[x][y];
		BetterUnit selectedUnit = selectedTile.getUnit();
		if (tile.getTileStatus() == 2 && (x < gameState.getPosX() - 1 || y < gameState.getPosY() - 1 || x > gameState.getPosX() + 1
				|| y > gameState.getPosY() + 1)) {
			for (int i = x - 1; i <= x; i++) {
				for (int j = y - 1; j <= y + 1; j++) {
					if (i >= 0 && j >= 0 && i < 9 && j < 5) {
						Tile tile2 = board.tile[i][j];
						if (tile2.getTileStatus() == 1 && tile2.getUnit() == null) {
							unitMove(i, j, gameState, out);
							gameState.setPosX(i);
							gameState.setPosY(j);
							gameState.setUnit(selectedUnit);
							gameState.setTempTile(tile2);
							tile.setTileStatus(2);
							try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
							unitAttack(x, y, gameState, out);
							break;
						}
					}
				}
			}

		}
	}
}

/**
import java.util.HashSet;
import akka.actor.ActorRef;
import structures.GameState;

public class UnitAttackingTwice  {
	protected int attackCounter=0;
	protected int moveCounter=0;
	protected final int TIMES=2; 
	public void move(ActorRef out, GameState gs, NewTile targetTile) {
		super.move(out, gs, targetTile);
		this.moveCounter++;
		if(moveCounter<TIMES) {//if move counter is under TIMES, the unit can still move
			this.setCanMove(true);
		}
		if(moveCounter-attackCounter>1) {//if the unit moved twice without attacking between two moves, it loses an opportunity to attack
			this.attackCounter++;
		}
	}
	public void attack(ActorRef out, GameState gs, NewTile targetTile, GeneralPlayer enemyPlayer, HashSet<NewTile> moveRange) {
		super.attack(out, gs, targetTile, enemyPlayer, moveRange);
		this.moveCounter++;
		this.attackCounter++;
		if(attackCounter<TIMES) {//if attack counter is under TIMES, the unit can still move and attack
			this.setCanMove(true);
			this.setCanAttack(true);
		}
	}

 * This is called when a new turn starts to clear count.

	public void clearCounter() {
		this.attackCounter=0;
		this.moveCounter=0;
	}

 */



