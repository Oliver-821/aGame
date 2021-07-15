package commands;

import java.util.ArrayList;



import akka.actor.ActorRef;
import structures.Board;
import structures.GameState;
import structures.HumanPlayer;
import structures.AIPlayer;
import structures.BetterUnit;
import structures.basic.Card;
import structures.basic.EffectAnimation;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import unitAbilities.Provoke;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

/**
 * the class is to write: 
 * the logic of unit attack, unit move, unit MoveAttack 
 * attack highlight, move highlight, moveAttack highlight , summon highlight , clear highlight
 * summon card 
 * 
 * the class is the pack of BasicCommands. it could be used for AI and human. 
 * AI and human has different logic to play game. AI and human could invoke the method in AdvancedCommands directly 
 *
 */
public class AdvancedCommands {

	/**
	 * move highlight(bright white)
	 * if there is no unit on the tile
	 * store the unit, tile, posX and posY
	 * set tile status to 1
	 * 
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef
	 * 
	 */
	@SuppressWarnings({ "deprecation" })
	public static void moveHighlight(int x, int y, GameState gameState, ActorRef out) {
		Tile[][] tile = gameState.getBoard().getTile();
		for (int i = x - 2; i <= x + 2; i++) {
			for (int j = y - 2; j <= y + 2; j++) {
				if (i >= 0 && j >= 0 && i < 9 && j < 5) {
					if (i == x - 2 || i == x + 2 || j == y - 2 || j == y + 2) {
						if (tile[i][y].getUnit() == null) {
							BasicCommands.drawTile(out, tile[i][y], 1);
							tile[i][y].setTileStatus(1);
							try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
						}
						if (tile[x][j].getUnit() == null) {
							BasicCommands.drawTile(out, tile[x][j], 1);
							tile[x][j].setTileStatus(1);
							try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
						}		
					} else {
						if (tile[i][j].getUnit() == null) {
							BasicCommands.drawTile(out, tile[i][j], 1);
							tile[i][j].setTileStatus(1);
							try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
						} else {
							BasicCommands.drawTile(out, tile[i][j], 0);
							tile[i][j].setTileStatus(0);
							try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
						}
					}
				}
			}
		}
		try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
		gameState.setTempTile(tile[x][y]);
		gameState.setUnit(tile[x][y].getUnit());
		gameState.setPosX(x);
		gameState.setPosY(y);
	}
	
	/**
	 *  attack high light (red)
	 *  check unit around selected unit
	 *  if human: id >= 30 red ,set tile status to 2
	 *  if AI: id < 30 red ,set tile status to 2
	 *  store the unit, tile, posX and posY
	 *  
	 * @param x :tile position x
	 * @param y :tile position y
	 * @param gameState : GameState
	 * @param out : ActorRef  
	 */
	public static void attackHighlight(int x, int y, GameState gameState, ActorRef out) {
		Tile[][] tile = gameState.getBoard().getTile();
		int count = 0;
		Boolean isHuman = gameState.getIsHumanTurn();
		Boolean hasProvoke = Provoke.hasProvoke(x, y, gameState, out);
		if(hasProvoke) {
			Provoke.drawProvoke(x, y, gameState, out);
		} else {
			for (int i = x - 1; i <= x + 1; i++) {
				for (int j = y - 1; j <= y + 1; j++) {
					if (i >= 0 && j >= 0 && i < 9 && j < 5) {
						if (tile[i][j].getUnit() != null){
							if(isHuman) {
								if (tile[i][j].getUnit().getId() >= 30) {
									BasicCommands.drawTile(out, tile[i][j], 2);
									tile[i][j].setTileStatus(2);
									count ++;
									try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
									
								} 
							}else {
								if (tile[i][j].getUnit().getId() < 30) {
									BasicCommands.drawTile(out, tile[i][j], 2);
									tile[i][j].setTileStatus(2);
									count ++;
									try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
								}
								
							}

						}
					}
				}
			}
		}

		if(count == 0 && isHuman && !hasProvoke) {
			gameState.setGameAction("pending");
			BasicCommands.addPlayer1Notification(out, "no unit could attack", 2);
		}else 
			gameState.setGameAction("clickUnit");
		
		gameState.setTempTile(tile[x][y]);
		gameState.setUnit(tile[x][y].getUnit());
		gameState.setPosX(x);
		gameState.setPosY(y);
		try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
	}


	/**
	 * clear all the highlight
	 * 
	 * @param gameState : gameState
	 * @param out : ActorRef
	 */
	@SuppressWarnings({ "deprecation" })
	public static void clearHighlight(GameState gameState, ActorRef out) {
		Tile[][] tile = gameState.getBoard().getTile();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 5; j++) {
				BasicCommands.drawTile(out, tile[i][j], 0);
				tile[i][j].setTileStatus(0);
				try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
	}
	
	/**
	 * Unit move
	 * load unit stored before, once clicked the tile, unit will move here
	 * 
	 * @param x :tile position x
	 * @param y :tile position y
	 * @param gameState : GameState
	 * @param out : ActorRef  
	 */
	@SuppressWarnings({ "deprecation" })
	public static void unitMove(int x, int y, GameState gameState, ActorRef out1) {
		Tile tile = gameState.getBoard().tile[x][y];
		Tile tile1 = gameState.getBoard().tile[gameState.getPosX()][gameState.getPosY()];
		BetterUnit unit = tile1.getUnit();
		Boolean isHumanTurn = gameState.getIsHumanTurn();
		BasicCommands.playUnitAnimation(out1, unit, UnitAnimationType.move);
		try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.moveUnitToTile(out1, unit, tile);
		unit.setPositionByTile(tile);
		gameState.getBoard().tile[gameState.getPosX()][gameState.getPosY()].setUnit(null);
		int i = unit.getId();
		BetterUnit unit1 = null;
		if(isHumanTurn) {
			unit1 = gameState.getBoard().unit1[i];
		}else  unit1 = gameState.getBoard().unit2[i - 30];
		
		gameState.getBoard().tile[x][y].setUnit(unit1);
		gameState.setTempTile(null);
		gameState.setUnit(null);
		int j = unit1.getMoveNum() + 1;
		unit1.setMoveNum(j);
		try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
	}

	/**
	 * unit ranged attack if attack > health, delete unit else set unit health and
	 * counter attack
	 * 
	 * @param x :tile position x
	 * @param y :tile position y
	 * @param gameState : GameState
	 * @param out : ActorRef  
	 */
	@SuppressWarnings({ "deprecation" })
	public static void rangeAttack(int x, int y, GameState gameState, ActorRef out) {
		Board board = gameState.getBoard();
		Tile tile = board.tile[x][y];
		Tile selectedTile = board.tile[gameState.getPosX()][gameState.getPosY()];
		BetterUnit selectedUnit = selectedTile.getUnit();
		BetterUnit unit = tile.getUnit();
		AIPlayer AI = gameState.getAI();
		Boolean isHumanTurn = gameState.getIsHumanTurn();
		addPlayerNotification(out, "range attack", 2,isHumanTurn);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		EffectAnimation projectile = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_projectiles);
		BasicCommands.playProjectileAnimation(out, projectile, 0, selectedTile, tile);
		selectedUnit.setAttackNum(selectedUnit.getAttackNum() + 1);
		int health = unit.getHealth() - selectedUnit.getAttack();
		unit.setHealth(health);
		if (health<=0) {
			BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.death);
			BasicCommands.deleteUnit(out, unit);
			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
			if(unit.getId() == 37 || unit.getId() == 45) AI.darwCard(out);
			tile.setUnit(null);
		} else {
			BasicCommands.setUnitHealth(out, unit, health);
			counterAttack(x, y, gameState, out);
			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		}
		gameState.setTempTile(null);
		gameState.setUnit(null);
	}

	/**
	 * adjacent attack and unit die
	 * 
	 * @param x :tile position x
	 * @param y :tile position y
	 * @param gameState : GameState
	 * @param out : ActorRef  
	 */
	public static void adjacentAttack(int x, int y, GameState gameState, ActorRef out) {
		Board board = gameState.getBoard();
		Tile tile = board.tile[x][y];
		Tile selectedTile = board.tile[gameState.getPosX()][gameState.getPosY()];
		BetterUnit selectedUnit = selectedTile.getUnit();
		BetterUnit unit = tile.getUnit();
		AIPlayer AI = gameState.getAI();
		Boolean isHumanTurn = gameState.getIsHumanTurn();
		addPlayerNotification(out, "adjacent attack", 2, isHumanTurn);
		BasicCommands.playUnitAnimation(out, selectedUnit, UnitAnimationType.attack);
		selectedUnit.setAttackNum(selectedUnit.getAttackNum() + 1);
		int health = unit.getHealth() - selectedUnit.getAttack();
		unit.setHealth(health);
		if (health<=0) {
			BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.death);
			BasicCommands.deleteUnit(out, unit);
			try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
			if(unit.getId() == 37 || unit.getId() == 45) {
				addPlayerNotification(out, "WindShrike die", 2, isHumanTurn);
				addPlayerNotification(out, "draw one card", 2, isHumanTurn);
				AI.darwCard(out);
			}
			tile.setUnit(null);
		} else if (health>0) {
			BasicCommands.setUnitHealth(out, unit, health);
			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
			counterAttack(x, y, gameState, out);
		}
		gameState.setTempTile(null);
		gameState.setUnit(null);
		try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
	}

	/**
	 * Counter Attack and unit die
	 * 
	 * @param x :tile position x
	 * @param y :tile position y
	 * @param gameState : GameState
	 * @param out : ActorRef  
	 */
	public static void counterAttack(int x, int y, GameState gameState, ActorRef out) {
		Board board = gameState.getBoard();
		Tile tile = board.tile[x][y];
		Tile selectedTile = board.tile[gameState.getPosX()][gameState.getPosY()];
		BetterUnit selectedUnit = selectedTile.getUnit();
		BetterUnit unit = tile.getUnit();
		AIPlayer AI = gameState.getAI();
		int health = selectedUnit.getHealth() - unit.getAttack();
		if (unit.getId() == 3 || unit.getId() == 11) {
			EffectAnimation projectile = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_projectiles);
			BasicCommands.playProjectileAnimation(out, projectile, 0, tile, selectedTile);
		} else {
			BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.attack);
		}
		selectedUnit.setHealth(health);
		if (health > 0) {
			BasicCommands.setUnitHealth(out, selectedUnit, health);
			try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
		} else {
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
			BasicCommands.playUnitAnimation(out, selectedUnit, UnitAnimationType.death);
			BasicCommands.deleteUnit(out, selectedUnit);
			try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
			if(unit.getId() == 37 || unit.getId() == 45) AI.darwCard(out);
			selectedTile.setUnit(null);
		}
	}

	/**
	 * set avatar health as human or AI health if human avatar health <= 0 , AI win
	 * if AI avatar health <= 0 , huamn win
	 * 
	 * @param x :tile position x
	 * @param y :tile position y
	 * @param gameState : GameState
	 * @param out : ActorRef  
	 * 
	 */
	public static void avatarAction(int x, int y, GameState gameState, ActorRef out) {
		BetterUnit avatar = gameState.getBoard().unit1[16];
		BetterUnit avatar2 = gameState.getBoard().unit2[16];
		int health = avatar.getHealth();
		HumanPlayer human = gameState.getHuman();
		AIPlayer AI = gameState.getAI();
		int health2 = avatar2.getHealth();
		if (health != human.getHealth()) {
			gameState.getHuman().setHealth(health);
			BasicCommands.addPlayer1Notification(out, "update health", 2);
			//********************Silverguard Knight***********************************
			Tile[][] tile1 = gameState.getBoard().getTile();
			for(int i = 0; i < 9 ; i++) {
				for(int j = 0; j < 5; j++) {
					if(tile1[i][j].getUnit() != null  ) {
						if(tile1[i][j].getUnit().getId() == 7 || tile1[i][j].getUnit().getId() == 15) {
							BasicCommands.addPlayer1Notification(out, "Silverguard Knight:attack +2", 2);
							EffectAnimation ef = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff);
							BasicCommands.playEffectAnimation(out, ef,tile1[i][j]);
							int newAttack =tile1[i][j].getUnit().getAttack()+2;
							tile1[i][j].getUnit().setAttack(newAttack);  
							BasicCommands.setUnitAttack(out, tile1[i][j].getUnit(), newAttack);
							try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
						}
					}
				}
			}
			//************************Silverguard Knight*********************************
			BasicCommands.setPlayer1Health(out, gameState.getHuman());
			try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
		}
		if (health <= 0) {
			BasicCommands.addPlayer1Notification(out, "Sorry, AI win", 2);
			gameState.setGameState("AI win");
		}
		try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
		if (health2 != AI.getHealth()) {
			gameState.getAI().setHealth(health2);
			BasicCommands.addPlayer2Notification(out, "update health", 2);
			BasicCommands.setPlayer2Health(out, gameState.getAI());
			try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
		}
		if (health2 <= 0) {
			BasicCommands.addPlayer1Notification(out, "Congratulation, you win", 2);
			gameState.setGameState("human win");
		}
		try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
	}

	/**
	 * board highlighting : summon unit
	 * 
	 * @param handPosition : on hand card position(from 0 to 5)
	 * @param gameState : GameState
	 * @param out : ActorRef  
	 */

	public static void summonHighlight(int handPosition, GameState gameState, ActorRef out) {
		gameState.setHandPosition(handPosition);
		Board board = gameState.getBoard();
		Boolean isHuman = gameState.getIsHumanTurn();
		addPlayerNotification(out, "summon highlight", 2,isHuman);
		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 5; j++) {
				Tile tile = board.tile[i][j];
				Unit unit = tile.getUnit();
				if( unit != null) {
					if(isHuman &&tile.getUnit().getId()<30) {						
						for (int x = i - 1; x <= i + 1; x++) {
							for (int y = j - 1; y <= j + 1; y++) {
								if (x >= 0 && y >= 0 && x < 9 && y < 5) {
									if (board.tile[x][y].getUnit() == null) {
										BasicCommands.drawTile(out, board.tile[x][y], 1);
										board.tile[x][y].setTileStatus(1);
										try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
									}
								}
							}
						}	
					}else if(!isHuman &&tile.getUnit().getId()>=30) {
						for (int x = i - 1; x <= i + 1; x++) {
							for (int y = j - 1; y <= j + 1; y++) {
								if (x >= 0 && y >= 0 && x < 9 && y < 5) {
									if (board.tile[x][y].getUnit() == null) {
										BasicCommands.drawTile(out, board.tile[x][y], 1);
										board.tile[x][y].setTileStatus(1);
										try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
									}
								}
							}
						}	
					}
					
				}
			}
		}
		try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	/**
	 * summon unit to the selected tile
	 * 
	 * @param x :tile position x
	 * @param y :tile position y
	 * @param gameState : GameState
	 * @param out : ActorRef  
	 */
	public static void summonUnit(int x, int y, GameState gameState, ActorRef out) {
		Player	player = gameState.getCurrPlayer();
		Card selectedCard = player.getOnhandCard().get(gameState.getHandPosition());
		Board board = gameState.getBoard();
		Tile[][] tile = board.getTile();
		ArrayList<Card> onhandCard = player.getOnhandCard();
		int onhandCardNum = player.getOnhandCardNum();
		Boolean isHuman = gameState.getIsHumanTurn();
		BetterUnit[] unit = isHuman?  board.getUnit1() :  board.getUnit2();
		int cardID = isHuman?selectedCard.getId():selectedCard.getId()-30;
		// record unit to tile
		tile[x][y].setUnit(unit[cardID]);
		// delete card
		if(isHuman) {
			addPlayerNotification(out, "delete card", 2, isHuman);
			BasicCommands.deleteCard(out, gameState.getHandPosition());
			try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
		}
		onhandCard.remove(gameState.getHandPosition());
		onhandCardNum--;
		player.setOnhandCardNum(onhandCardNum);
		// drawUnit
		addPlayerNotification(out, "summon unit", 2, isHuman);
		EffectAnimation ef = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon);
		BasicCommands.playEffectAnimation(out, ef, gameState.getBoard().tile[x][y]);
		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		unit[cardID].setPositionByTile(tile[x][y]);
		BasicCommands.drawUnit(out, unit[cardID], tile[x][y]);
		try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
		// setUnitAttack
		BasicCommands.setUnitAttack(out, unit[cardID], unit[cardID].getAttack());
		try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
		// setUnitHealth
		BasicCommands.setUnitHealth(out, unit[cardID], unit[cardID].getHealth());
		try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
		gameState.setHandPosition(-1);
	
	}

	/**
	 * move attack highlight. unit move and then could attack it will highlight
	 * 
	 * @param x :tile position x
	 * @param y :tile position y
	 * @param gameState : GameState
	 * @param out : ActorRef  
	 */
	public static void moveAttackHighlight(int x, int y, GameState gameState, ActorRef out) {
		Tile[][] tile = gameState.getBoard().getTile();
		int count = 0;
		Boolean isHumanTurn = gameState.getIsHumanTurn();
		for (int a = x - 2; a <= x + 2; a++) {
			for (int b = y - 2; b <= y + 2; b++) {
				if (a >= 0 && b >= 0 && a < 9 && b < 5) {
					if(tile[a][b].getTileStatus() == 1 ) {
						Boolean hasProvoke = Provoke.hasProvoke(a, b, gameState, out);
						if(hasProvoke) {
							Provoke.drawProvoke(a, b, gameState, out);
						} else {
							for (int i = a - 1; i <= a + 1; i++) {
								for (int j = b - 1; j <= b + 1; j++) {
									if (i >= 0 && j >= 0 && i < 9 && j < 5) {
										if (tile[i][j].getUnit() != null){
											if(isHumanTurn) {
												if (tile[i][j].getUnit().getId() >= 30) {
													BasicCommands.drawTile(out, tile[i][j], 2);
													tile[i][j].setTileStatus(2);
													count ++;
													try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
												} 
											}else {
												if (tile[i][j].getUnit().getId() < 30) {
													BasicCommands.drawTile(out, tile[i][j], 2);
													tile[i][j].setTileStatus(2);
													try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
												}
												
											}

										}
									}
								}
							}
						}
					}
				}
			}
		}
		if(count != 0 && isHumanTurn) 
			BasicCommands.addPlayer1Notification(out, "move and attack", 2);
		// store status to gameState for using 
		gameState.setTempTile(tile[x][y]);
		gameState.setUnit(tile[x][y].getUnit());
		gameState.setPosX(x);
		gameState.setPosY(y);
		try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	/**
	 * check isHumanTurn or not, and add Notification 
	 * 
	 * @param out : ActorRef 
	 * @param test : notification of player
	 * @param displayTimeSeconds : display time seconds 
	 * @param isHuman : is human turn or not 
	 */
	public static void addPlayerNotification(ActorRef out, String text, int displayTimeSeconds, Boolean isHuman) {
		if(isHuman) {
			BasicCommands.addPlayer1Notification(out, text, displayTimeSeconds);
		}else 
			BasicCommands.addPlayer2Notification(out, text, displayTimeSeconds);
	}
	
	/**
	 * check on hand card of human and AI. decide the game results 
	 * 
	 * @param gameState : gameState
	 * @param out : ActorRef
	 */
	
	public static void checkGameStatus(GameState gameState, ActorRef out) {
		Boolean isHumanTurn = gameState.getIsHumanTurn();
		Tile[][] tile = gameState.getBoard().getTile();
		int humanCheck = 0;
		int AICheck = 0;
		Boolean isHumanWin = false;
		Boolean isAIWin = false;
		if(isHumanTurn) {
			HumanPlayer human = gameState.getHuman();
			int onhandCardNum = human.getOnhandCardNum();
			Boolean isRemainCard = human.getIsRemainCards();
			if(!isRemainCard && onhandCardNum == 0) {
				for(int i = 0; i < 9 ; i++) {
					for(int j = 0; j < 5; j++) {
						if(tile[i][j].getUnit() != null && tile[i][j].getUnit().getId() < 30 && tile[i][j].getUnit().getId() != 16) {
							humanCheck ++;
						}
					}
				}
				if(humanCheck > 0) {
					isAIWin = false;
				}else
					isAIWin = true;
			}

		} else {
			AIPlayer AI = gameState.getAI();
			int onhandCardNum = AI.getOnhandCardNum();
			Boolean isRemainCard = AI.getIsRemainCards();
			if(!isRemainCard && onhandCardNum == 0) {
				for(int i = 0; i < 9 ; i++) {
					for(int j = 0; j < 5; j++) {
						if(tile[i][j].getUnit() != null && tile[i][j].getUnit().getId() < 30 && tile[i][j].getUnit().getId() != 16) {
							AICheck ++;
						}
					}
				}
				
				if(AICheck > 0) {
					isHumanWin = false;
				}else
					isHumanWin = true;
			}
		}
		if(isHumanTurn && isAIWin) {
			BasicCommands.addPlayer1Notification(out, "Sorry, AI win", 2);
			gameState.setGameState("AI win");
		} else if(!isHumanTurn && isHumanWin) {
			BasicCommands.addPlayer1Notification(out, "Congratulation, you win", 2);
			gameState.setGameState("human win");
		}
	}
}
