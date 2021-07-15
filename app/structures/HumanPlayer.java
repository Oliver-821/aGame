package structures;

import commands.AdvancedCommands;
import commands.BasicCommands;
import structures.basic.Card;
import structures.basic.Player;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;
import utils.deckFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import akka.actor.ActorRef;
import cardAbilities.SundropElixir;
import cardAbilities.TrueStrike;
import commands.BasicCommands;
import structures.basic.Card;
import structures.basic.EffectAnimation;
import structures.basic.Tile;


public class HumanPlayer extends Player {
	/**
	 * @humanDeck the arraylist of human storing all cards
	 * @cardNum the index of humanDeck
	 * @onhandCardNum index of human on hand card
	 * @onhandCard human on hand card
	 */
	private ArrayList<Card> humanDeck;
	private int cardNum = 0;
	private int onhandCardNum = 0;
	public ArrayList<Card> onhandCard;
	private Boolean isRemainCards = true;
	/*
	 * getter and setter
	 */
	public ArrayList<Card> getOnhandCard() {
		return onhandCard;
	}

	public void setOnhandCard(ArrayList<Card> onhandCard) {
		this.onhandCard = onhandCard;
	}

	public int getOnhandCardNum() {
		return onhandCardNum;
	}

	public void setOnhandCardNum(int onhandCard) {
		this.onhandCardNum = onhandCard;
	}
	
	public Boolean getIsRemainCards() {
		return isRemainCards;
	}

	public void setIsRemainCards(Boolean isRemainCards) {
		this.isRemainCards = isRemainCards;
	}
	
	public int getCardNum() {
		return cardNum;
	}

	public void setCardNum(int cardNum) {
		this.cardNum = cardNum;
	}

	/**
	 * create deck and shuffle cards set health and mana for human
	 * 
	 * @param out : ActorRef
	 * @param gameState : GameState 
	 */

	public HumanPlayer(ActorRef out, GameState gameState) {
		//set health to 20 and mana to 0
		setHealth(20);
		setMana(0);
		// create deck and shuffle deck
		humanDeck = new ArrayList<Card>();
		humanDeck = deckFactory.makeDeck("human");
		Collections.shuffle(humanDeck);
		onhandCard = new ArrayList<Card>();
		// set health and mana for human for round 1
		setMana(0);
		BasicCommands.addPlayer1Notification(out, "set human", 2);
		BasicCommands.setPlayer1Health(out, this);
		try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setPlayer1Mana(out, this);
		try {Thread.sleep(1500);} catch (InterruptedException e) {e.printStackTrace();}

	}


	/**
	 * At the start of the game I should be able to draw three cards 
	 * store the card number of list in gamestate for future use
	 * 
	 * @param out : ActorRef
	 */
	public void firstDraw(ActorRef out) {
		BasicCommands.addPlayer1Notification(out, "draw three Card", 2);
		for (int i = 0; i < 3; i++) {
			onhandCard.add(humanDeck.get(i));
			BasicCommands.drawCard(out, humanDeck.get(i), i, 0);
			cardNum++;
			onhandCardNum = 2;
			try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}

		}
		try {Thread.sleep(1500);} catch (InterruptedException e) {e.printStackTrace();}
	}

	/**
	 *  then draw an extra card at the end of each turn 
	 *  if card num is more than 6, drop card
	 *  
	 * @param out : ActorRef
	 */
	public void darwCard(ActorRef out) {
		onhandCardNum++;
		cardNum++;
		isRemainCards = true;
		if(cardNum<20) {
			if (onhandCardNum > 5) {
				onhandCardNum = 5;
			} else {
				BasicCommands.drawCard(out, humanDeck.get(cardNum), onhandCardNum, 0);
				try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
				onhandCard.add(humanDeck.get(cardNum));
			}
		}else {
			BasicCommands.addPlayer1Notification(out, "no more cards", 2);
		    isRemainCards = false;
		    
		}
	}
	
	/**
	 * draw the cards on hand 
	 * 
	 * @param out : ActorRef
	 */
	public void drawAllCard(ActorRef out) {
		for (int i = 0; i <= onhandCardNum; i++) {
			BasicCommands.drawCard(out, onhandCard.get(i), i, 0);
			try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
		}	
	}

	/**
	 *  gain Mana at the beginning of each turn and make it less than 9
	 *  
	 * @param gameState : GameState
	 * @param out : ActorRef
	 */
	public void gainMana(GameState gameState, ActorRef out) {
		int mana = gameState.getCurrTurn() + 1;
		if (mana > 9) {
			mana = 9;
		}
		setMana(mana);
		BasicCommands.setPlayer1Mana(out, this);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Drain Mana at the end of each Turn
	 * 
	 * @param out : ActorRef
	 */

	public void drainMana(ActorRef out) {
		setMana(0);
		BasicCommands.setPlayer1Mana(out, this);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * draw Avatar in board and make Avater health equals to human health
	 * 
	 * @param gameState : GameState
	 * @param out : ActorRef
	 */
	public void drawAvatar(GameState gameState, ActorRef out) {
		BasicCommands.addPlayer1Notification(out, "draw Human Avatar", 2);
		BetterUnit avatar = gameState.getBoard().unit1[16];
		avatar.setPositionByTile(gameState.getBoard().tile[1][2]);
		EffectAnimation ef = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon);
		BasicCommands.playEffectAnimation(out, ef, gameState.getBoard().tile[1][2]);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.drawUnit(out, avatar, gameState.getBoard().tile[1][2]);
		gameState.getBoard().tile[1][2].setUnit(avatar);
		try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
		avatar.setHealth(getHealth());
		int health = avatar.getHealth();
		BasicCommands.setUnitHealth(out, avatar, health);
		try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, avatar, avatar.getAttack());
		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}	
	}

	/**
	 * Board Highlighting if it is human turn and the card didn't attack before it
	 * could run
	 * 
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef
	 */
	public void highlight(int x, int y, GameState gameState, ActorRef out) {
		Tile[][] tile = gameState.getBoard().getTile();
		BetterUnit unit = tile[x][y].getUnit();
		AdvancedCommands.clearHighlight(gameState, out);			
		if (unit != null) {
			if (unit.getId() < 30) {
				if (unit.getTurnNum() == 0 ||  unit.getAttackNum() == 1) {
					BasicCommands.addPlayer1Notification(out, "wait for next turn", 2);
					gameState.setGameAction("pending");
				} else {
					if(unit.getMoveNum()!=0 && unit.getTurnNum() != 0) {
						AdvancedCommands.attackHighlight(x, y, gameState, out);
				}else {
					AdvancedCommands.moveHighlight(x, y, gameState, out);	
					AdvancedCommands.attackHighlight(x, y, gameState, out);
					if (unit.getAttackNum() == 0 && unit.getMoveNum() == 0) 
						AdvancedCommands.moveAttackHighlight(x, y, gameState, out);
					}
					gameState.setGameAction("clickUnit");
				}
			} else 
				BasicCommands.addPlayer1Notification(out, "not your unit", 2);
		}
	}
	


	/**
	 * if unit already attack, unit couldn't move clear highlight tiles if unit
	 * already move
	 * 
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef
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
			BasicCommands.addPlayer1Notification(out, "unit couldn't move or attack", 2);
		AdvancedCommands.clearHighlight(gameState, out);
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * run unit attack. if tile didn't highlight before, it couldn't attack. if unit
	 * have attacked, it couldn't attack attack way depends on the tile status
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
			if (selectedUnit.getAttackNum() == 0) {
					if (tile.getTileStatus() != 2) {
						BasicCommands.addPlayer1Notification(out, "you couldn't attack this ", 2);
						gameState.setTempTile(null);
						gameState.setUnit(null);
					} else {
						AdvancedCommands.adjacentAttack(x, y, gameState, out);
						try {
							Thread.sleep(30);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						AdvancedCommands.avatarAction(x, y, gameState, out);
						try {
							Thread.sleep(30);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			} else 
				BasicCommands.addPlayer1Notification(out, "unit couldn't move or attack", 2);
		AdvancedCommands.clearHighlight(gameState, out);
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * summon unit on the board and use spell card 
	 * 
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef
	 */
	public void summonUnit(int x, int y, GameState gameState, ActorRef out) {
		Board board = gameState.getBoard();
		Tile tile = board.tile[x][y];
		Card selectedCard = onhandCard.get(gameState.getHandPosition());
		if (gameState.getGameAction().equalsIgnoreCase("clickCard")) {
			if(selectedCard.getId() == 17 || selectedCard.getId() == 19) {		
				TrueStrike.useTrueStrike(x, y, gameState, out);
				drawAllCard(out);
				BasicCommands.deleteCard(out, onhandCardNum + 1);
			} else if(selectedCard.getId() == 16 || selectedCard.getId() == 18) {
				SundropElixir.useSundropElixir(x, y, gameState, out);
				drawAllCard(out);
				BasicCommands.deleteCard(out, onhandCardNum + 1);
			}else {
				if (tile.getTileStatus() == 1) {
					if (gameState.getHandPosition() != -1) {
						Card card = onhandCard.get(gameState.getHandPosition());
						if (card.getManacost() <= getMana()) {
							AdvancedCommands.clearHighlight(gameState, out);
							AdvancedCommands.summonUnit(x, y, gameState, out);
							setMana(getMana() - card.getManacost());
							BasicCommands.setPlayer1Mana(out, this);
							if(selectedCard.getId() == 0 || selectedCard.getId() == 8) {
								BetterUnit unit = board.unit1[selectedCard.getId()];
								unit.executeAbility(x, y, gameState, out);
							}
							BasicCommands.deleteCard(out, onhandCardNum + 1);
							drawAllCard(out);
						} else {
							BasicCommands.addPlayer1Notification(out, "your mana not enough", 2);
							AdvancedCommands.clearHighlight(gameState, out);
							gameState.setHandPosition(-1);
						}
					}
				} else {
					BasicCommands.addPlayer1Notification(out, "you couldn't put here", 2);
					AdvancedCommands.clearHighlight(gameState, out);
					gameState.setHandPosition(-1);
				}
			}
		}
		gameState.setGameAction("pending");
	}

	/**
	 * summon highlight and spell card highlight
	 * 
	 *@param handPosition : on hand card position
	 *@param gameState : GameState
	 *@param out : ActorRef
	 */
	public void summonHighlight(int handPosition, GameState gameState, ActorRef out) {
		if(onhandCard.size() >0) {
			AdvancedCommands.clearHighlight(gameState, out);
			Card card = onhandCard.get(handPosition);
			int id = card.getId();
			highlightCards(handPosition,gameState,out);
			if(id == 5 || id == 13) {
				card.executeAbility(handPosition, gameState, out);
			}else if (id == 17 || id == 19 || id == 16 || id == 18) {
				card.executeAbility(handPosition, gameState, out);
			} else {
				if(card.getManacost()<= getMana()) {
					AdvancedCommands.summonHighlight(handPosition, gameState, out);
					gameState.setGameAction("clickCard");
					gameState.setHandPosition(handPosition);
				} else 
					BasicCommands.addPlayer1Notification(out, "no enough mana", 2);

			}
		}

	}
	
	/**
	 * move and attack. Unit will move place and then attack
	 * 
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef
	 */
	public void moveAttack(int x, int y, GameState gameState, ActorRef out) {
		Board board = gameState.getBoard();
		Tile selectedTile = board.tile[gameState.getPosX()][gameState.getPosY()];
		Tile tile = board.tile[x][y];
		BetterUnit selectedUnit = selectedTile.getUnit();
		ArrayList<int[]> movePlace = new ArrayList<int[]>();
		Random random;
		if (tile.getTileStatus() == 2 && (x < gameState.getPosX() - 1 || y < gameState.getPosY() - 1 || x > gameState.getPosX() + 1
				|| y > gameState.getPosY() + 1)) {
			for (int i = x - 1; i <= x+1; i++) {
				for (int j = y - 1; j <= y + 1; j++) {
					if (i >= 0 && j >= 0 && i < 9 && j < 5) {
						Tile tile2 = board.tile[i][j];
						if (tile2.getTileStatus() == 1 && tile2.getUnit() == null) {
							int[] place = new int[2];
							place[0] = i;
							place[1] = j;
							movePlace.add(place);
						}
					}
				}
			}
			int[] updatePlace = new int[2];
			if(movePlace.size()> 0) {
				random = new Random();
				int index = random.nextInt(movePlace.size());
				updatePlace = movePlace.get(index);
			} 
			unitMove(updatePlace[0], updatePlace[1], gameState, out);
			gameState.setPosX(updatePlace[0]);
			gameState.setPosY(updatePlace[1]);
			gameState.setUnit(selectedUnit);
			gameState.setTempTile(board.tile[updatePlace[0]][updatePlace[1]]);
			tile.setTileStatus(2);
			try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
			unitAttack(x, y, gameState, out);
		}
	}
	
	/**
	 * normal tile click action including summon card , unit move , unit attack
	 * 
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef
	 */
	public void normalTileClick(int x, int y, GameState gameState, ActorRef out) {
		Board board = gameState.getBoard();
		Tile tile = board.tile[x][y];
		BetterUnit unit = tile.getUnit();
		Card selectedCard = null;
		clearhighlightCards(gameState, out);
		if (gameState.getHandPosition() != -1) {
			selectedCard = onhandCard.get(gameState.getHandPosition());
		} else 
			selectedCard = null;
		if(gameState.getGameAction().equals("clickCard")) {
			summonUnit(x, y, gameState, out);
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
			}else if(tile.getTileStatus() == 1) 
				unitMove(x, y, gameState, out);
			gameState.setGameAction("pending");
		}
		
		AdvancedCommands.checkGameStatus(gameState, out);
	}
	
	/**
	 * Once click the card, if mana is enough other card will highlight
	 * 
	 *@param handPosition :on hand card position
	 *@param gameState : GameState
	 *@param out : ActorRef
	 */
	
	public void highlightCards(int handPosition, GameState gameState, ActorRef out) {
		clearhighlightCards(gameState,out);
		for(int i =0; i < onhandCard.size(); i++) {
			if(onhandCard.get(i).getManacost()<= getMana()) {
				BasicCommands.drawCard(out, onhandCard.get(i), i, 1);
				try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
	}
	
	/**
	 * clear all highlight card 
	 */
	
	
	public void clearhighlightCards( GameState gameState, ActorRef out) {
		for(int i =0; i < onhandCard.size(); i++) {
			BasicCommands.drawCard(out, onhandCard.get(i), i, 0);	
			try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	

	/**
	 * final human play, including unit ability
	 * 
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef
	 */
	public void humanPlay(int x, int y, GameState gameState, ActorRef out) {
		Board board = gameState.getBoard();
		BetterUnit selectedUnit = gameState.getUnit();
		Tile tile = board.tile[x][y];
		BetterUnit unit = tile.getUnit();
		int unitID =-1 ;
		int selectedUnitID = -1;
		if(unit != null) 
			unitID = unit.getId();
		if(selectedUnit != null) 
			selectedUnitID = selectedUnit.getId();
		
			if((unitID == 1 || unitID == 9  ) && (gameState.getGameAction().equals("pending") ||gameState.getGameAction().equals("clickCard"))) {
				unit.executeAbility(x, y, gameState, out);
				clearhighlightCards(gameState,out);
			}else if((selectedUnitID == 1 || selectedUnitID == 9  ) &&gameState.getGameAction().equals("clickUnit")) {
				selectedUnit.executeAbility(x, y, gameState, out);	
			}else if((unitID == 3 || unitID == 11 ) && (gameState.getGameAction().equals("pending") ||gameState.getGameAction().equals("clickCard"))) {
				unit.executeAbility(x, y, gameState, out);
				clearhighlightCards(gameState,out);
			}else if((selectedUnitID == 3 || selectedUnitID == 11  ) &&gameState.getGameAction().equals("clickUnit")) {
				selectedUnit.executeAbility(x, y, gameState, out);
			}else
				normalTileClick(x, y, gameState, out);
	}
	

}
