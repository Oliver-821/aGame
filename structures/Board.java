package structures;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.basic.Tile;
import utils.BasicObjectBuilders;
import utils.UnitCreate;

public class Board {
	/**
	 * @tile Array to contain all tile 
	 * @unit1 all Unit for human 
	 * @unit2 all Unit for AI 
	 */
	public Tile[][] tile;
	public BetterUnit[] unit1;
	public BetterUnit[] unit2;
	
	
	public BetterUnit[] getUnit1() {
		return unit1;
	}


	public void setUnit1(BetterUnit[] unit1) {
		this.unit1 = unit1;
	}


	public BetterUnit[] getUnit2() {
		return unit2;
	}


	public void setUnit2(BetterUnit[] unit2) {
		this.unit2 = unit2;
	}

	public Tile[][] getTile() {
		return tile;
	}

	public void setTile(Tile[][] tile) {
		this.tile = tile;
	}
	
	/**
	 * create unit for human and AI 
	 * Draw all the tiles
	 * 
	 * @param out : ActorRef
	 */
	public Board(ActorRef out) {
		// new tile for future use 
		unit1 = UnitCreate.unitCreate("human");
		unit2 = UnitCreate.unitCreate("AI");
		tile = new Tile[9][5];
		for(int i = 0; i < 9 ; i++) {
			for(int j = 0; j < 5; j++ ) {
				tile[i][j] = BasicObjectBuilders.loadTile(i,j);
				BasicCommands.drawTile(out, tile[i][j], 0);
				tile[i][j].setUnit(null);
				try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
	}

	/**
	 * reset all unit information like attack times, move times
	 */
	public void resetUnit() {
		for(int i = 0 ; i < unit1.length ; i++ ) {
			unit1[i].clearAllNum();
		}
		
		for(int i = 0 ; i < unit2.length ; i++ ) {
			unit2[i].clearAllNum();
		}
	}
	
	/**
	 * calculate current unit turn make turn + 1
	 */
	public void countUnitTurn() {
		for(int i = 0; i < 9 ; i++) {
			for(int j = 0; j < 5; j++ ) {
				if(tile[i][j].getUnit() != null) {
					BetterUnit unit = tile[i][j].getUnit();
					unit.setTurnNum(unit.getTurnNum()+1);
				} 
			}
		}
	}
	
	/**
	 * get pos of unit based on different id 
	 * 
	 * @param id : the id of the unit 
	 */
	public int[] getPos(int id) {
		BetterUnit unit = null;
		if(id < 30) {
			unit = unit1[id];
		} else 
			unit = unit2[id - 30];
		int[]pos = new int[2];
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 5; j++) {
				if(tile[i][j].getUnit() != null) {
					if(tile[i][j].getUnit().getId() == unit.getId()) {
						pos[0] = i;
						pos[1] = j;
					}
				}

			}
		}
		return pos;
	}
	
	
	



	
}
